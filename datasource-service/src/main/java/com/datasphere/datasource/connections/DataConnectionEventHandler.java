/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datasphere.datasource.connections;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeLinkDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeLinkSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

import com.datasphere.server.domain.activities.ActivityStreamService;
import com.datasphere.server.domain.activities.spec.ActivityGenerator;
import com.datasphere.server.domain.activities.spec.ActivityObject;
import com.datasphere.server.domain.activities.spec.ActivityStreamV2;
import com.datasphere.server.domain.workspace.Workspace;

@RepositoryEventHandler(DataConnection.class)
public class DataConnectionEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataConnectionEventHandler.class);

  @Autowired
  DataConnectionRepository connectionRepository;

  @Autowired
  ActivityStreamService activityStreamService;	// 活动流服务
  // 在创建连接前处理
  @HandleBeforeCreate
  public void handleBeforeCreate(DataConnection dataConnection) {
    if(BooleanUtils.isNotTrue(dataConnection.getPublished()) && CollectionUtils.isNotEmpty(dataConnection.getWorkspaces())) {
      dataConnection.setLinkedWorkspaces(dataConnection.getWorkspaces().size());
    }
  }
  //在关联保存前处理
  @HandleBeforeLinkSave
  public void handleBeforeLinkSave(DataConnection dataConnection, Object linked) {

    // Count connected workspaces.
    // a value is injected to linked object when PATCH,
    // but not injected when PUT request so doesn't check linked object.

    if(BooleanUtils.isNotTrue(dataConnection.getPublished())) {
      dataConnection.setLinkedWorkspaces(dataConnection.getWorkspaces().size());

      // Insert ActivityStream for saving grant history.
      if(!CollectionUtils.sizeIsEmpty(linked) && CollectionUtils.get(linked, 0) instanceof Workspace) {
        for (int i = 0; i < CollectionUtils.size(linked); i++) {
          Workspace linkedWorkspace = (Workspace) CollectionUtils.get(linked, i);
          if (!linkedWorkspace.getDataConnections().contains(dataConnection)) {
            activityStreamService.addActivity(new ActivityStreamV2(
                null, null, "Accept", null, null
                , new ActivityObject(dataConnection.getId(),"DATACONNECTION")
                , new ActivityObject(linkedWorkspace.getId(), "WORKSPACE"),
                new ActivityGenerator("WEBAPP",""), DateTime.now()));

            LOGGER.debug("[Activity] Accept workspace ({}) to dataconnection ({})", linkedWorkspace.getId(), dataConnection.getId());
          }
        }
      }
    }
  }
  //在连接删除前处理
  @HandleBeforeLinkDelete
  @PreAuthorize("hasAuthority('PERM_SYSTEM_MANAGE_DATASOURCE')")
  public void handleBeforeLinkDelete(DataConnection dataConnection, Object linked) {
    // Count connected workspaces.
    Set<Workspace> preWorkspaces = connectionRepository.findWorkspacesInDataConnection(dataConnection.id);
    // Not a public workspace and linked entity type is Workspace.
    if(BooleanUtils.isNotTrue(dataConnection.getPublished()) &&
        !CollectionUtils.sizeIsEmpty(preWorkspaces)) {
      dataConnection.setLinkedWorkspaces(dataConnection.getWorkspaces().size());
      LOGGER.debug("DELETED: Set linked workspace in dataconnection({}) : {}", dataConnection.getId(), dataConnection.getLinkedWorkspaces());

      for (Workspace workspace : preWorkspaces) {
        if(!dataConnection.getWorkspaces().contains(workspace)) {
          activityStreamService.addActivity(new ActivityStreamV2(
              null, null, "Block", null, null,
              new ActivityObject(dataConnection.getId(), "DATACONNECTION"),
              new ActivityObject(workspace.getId(), "WORKSPACE"),
              new ActivityGenerator("WEBAPP", ""), DateTime.now()));

          LOGGER.debug("[Activity] Block workspace ({}) from dataconnection ({})", workspace.getId(), dataConnection.getId());
        }
      }

    }

  }

}
