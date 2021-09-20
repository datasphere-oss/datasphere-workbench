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


import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.projection.ProjectionFactory;

import com.datasphere.server.common.GlobalObjectMapper;
import com.datasphere.server.common.KeepAsJsonDeserialzier;
import com.datasphere.server.common.domain.AbstractHistoryEntity;
import com.datasphere.server.common.domain.DSSDomain;
import com.datasphere.datasource.DataSource;
import com.datasphere.datasource.connections.jdbc.JdbcConnectInformation;
import com.datasphere.server.domain.workbench.Workbench;
import com.datasphere.server.domain.workspace.Workspace;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Table(name = "dataconnection")
public class DataConnection extends AbstractHistoryEntity implements DSSDomain<String>, JdbcConnectInformation {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "id")
  protected String id;

  @Column(name = "dc_implementor")
  protected String implementor;

  @Column(name = "dc_name")
  @NotBlank
  @Size(max = 150)
  protected String name;

  @Column(name = "dc_desc")
  @Size(max = 900)
  protected String description;
  // 数据源连接类型
  @Column(name = "dc_type")
  @Enumerated(EnumType.STRING)
  protected SourceType type;
  // 数据库类型
  @Column(name = "dc_typename")
  protected String typename;
  
  
@Column(name = "dc_url")
  protected String url;
  // 选项
  @Column(name = "dc_options")
  protected String options;

  @Column(name = "dc_published")
  protected Boolean published;
  // 主机名
  @Column(name = "dc_hostname")
  protected String hostname;
  
  // IP地址
  @Column(name = "dc_ip")
  protected String ip;
  
  // 端口
  @Column(name = "dc_port")
  protected Integer port;
  // 数据库
  @Column(name = "dc_database")
  protected String database;

  @Column(name = "dc_catalog")
  protected String catalog;

  @Column(name = "dc_sid")
  protected String sid;
  
  @Column(name = "dc_batchsize")
  protected Integer batchsize;
  
//  private String schema;
  
  @Column(name = "dc_properties", length = 65535, columnDefinition = "TEXT")
  @JsonRawValue
  @JsonDeserialize(using = KeepAsJsonDeserialzier.class)
  private String properties;

  @Column(name = "dc_username")
  protected String username;

  @Column(name = "dc_password")
  protected String password;
  

	
  // 认证类型
  @Column(name = "dc_authentication_type")
  @Enumerated(EnumType.STRING)
  protected AuthenticationType authenticationType;

  /**
   * Connected workspaces
   * 与之关联的工作空间
   */
  @Column(name = "dc_linked_workspaces")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  protected Integer linkedWorkspaces = 0;

  @OneToMany(mappedBy = "connection",
      cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
  @JsonBackReference
  protected Set<DataSource> dataSources;

  @OneToMany(mappedBy = "dataConnection", cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
  protected Set<Workbench> workbenches;

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(name = "dataconnection_workspace",
      joinColumns = @JoinColumn(name = "dc_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "ws_id", referencedColumnName = "id"))
  protected Set<Workspace> workspaces;

  public DataConnection() {
    // Empty Constructor
  }

  public DataConnection(String implementor) {
    super();
    this.setImplementor(implementor);
  }

  /**
   * In case of the object included in Entity, Projection does not work properly for each type.
   */
  public Object getDataConnectionProjection(ProjectionFactory projectionFactory, Class projection) {
    return projectionFactory.createProjection(projection, this);
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getImplementor() {
    return implementor;
  }

  public void setImplementor(String implementor) {
    this.implementor = implementor;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public SourceType getType() {
    return type;
  }

  public void setType(SourceType type) {
    this.type = type;
  }

  @Override
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String getOptions() {
    return options;
  }

  public void setOptions(String options) {
    this.options = options;
  }

  public Boolean getPublished() {
    return published;
  }

  public void setPublished(Boolean published) {
    this.published = published;
  }

  @Override
  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  @Override
  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  @Override
  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  @Override
  public String getCatalog() {
    return catalog;
  }

  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  @Override
  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  @Override
  public String getProperties() {
    return properties;
  }

  public void setProperties(String properties) {
    this.properties = properties;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public AuthenticationType getAuthenticationType() {
    return authenticationType;
  }

  public void setAuthenticationType(AuthenticationType authenticationType) {
    this.authenticationType = authenticationType;
  }

  public Integer getLinkedWorkspaces() {
    return linkedWorkspaces;
  }

  public void setLinkedWorkspaces(Integer linkedWorkspaces) {
    this.linkedWorkspaces = linkedWorkspaces;
  }

  public Set<DataSource> getDataSources() {
    return dataSources;
  }

  public void setDataSources(Set<DataSource> dataSources) {
    this.dataSources = dataSources;
  }

  public Set<Workbench> getWorkbenches() {
    return workbenches;
  }

  public void setWorkbenches(Set<Workbench> workbenches) {
    this.workbenches = workbenches;
  }

  public Set<Workspace> getWorkspaces() {
    return workspaces;
  }

  public void setWorkspaces(Set<Workspace> workspaces) {
    this.workspaces = workspaces;
  }

  @JsonIgnore
  public Map<String, String> getPropertiesMap(){
    return GlobalObjectMapper.readValue(this.properties, Map.class);
  }
  
  public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getBatchsize() {
		return batchsize;
	}

	public void setBatchsize(Integer batchsize) {
		this.batchsize = batchsize;
	}


  @Override
  public String toString() {
    return "DataConnection{" +
        "id='" + id + '\'' +
        ", implementor='" + implementor + '\'' +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", type=" + type +
        ", typename=" + typename + '\'' +
        ", ip=" + ip + '\'' +
        ", url='" + url + '\'' +
        ", options='" + options + '\'' +
        ", published=" + published +
        ", hostname='" + hostname + '\'' +
        ", port=" + port +
        ", database='" + database + '\'' +
        ", catalog='" + catalog + '\'' +
        ", sid='" + sid + '\'' +
        ", properties='" + properties + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", batchsize=" + batchsize + '\'' +
        ", authenticationType=" + authenticationType +
        '}';
  }

  @Override
  public void prePersist() {
    super.prePersist();

    //Authentication Type userinfo, dialog not persist username/password
    if(this.getAuthenticationType() != null){
      switch(this.getAuthenticationType()){
        case USERINFO:
        case DIALOG:
          this.setUsername(null);
          this.setPassword(null);
          break;
      }
    }
  }

  @Override
  public void preUpdate() {
    super.preUpdate();

    //Authentication Type userinfo, dialog not persist username/password
    if(this.getAuthenticationType() != null){
      switch(this.getAuthenticationType()){
        case USERINFO:
        case DIALOG:
          this.setUsername(null);
          this.setPassword(null);
          break;
      }
    }
  }

  public enum SourceType {
    FILE, JDBC //, VIEW
  }
}
