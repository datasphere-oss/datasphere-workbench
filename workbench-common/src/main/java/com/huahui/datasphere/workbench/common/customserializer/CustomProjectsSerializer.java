/*
 * Apache License
 * 
 * Copyright (c) 2020 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huahui.datasphere.workbench.common.customserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.huahui.datasphere.workbench.common.vo.Identifier;
import com.huahui.datasphere.workbench.common.vo.Project;

/**
 * CustomProjectsSerializer will used while serializing classes having bidirectional relationship with Project class.
 *
 */
public class CustomProjectsSerializer extends StdSerializer<List<Project>>{

	private static final long serialVersionUID = -4320442707927808746L;

	public CustomProjectsSerializer(Class<List<Project>> t) {
		super(t);
	}

	public CustomProjectsSerializer() {
		this(null);
	}
	
	@Override
	public void serialize(List<Project> projects, JsonGenerator generator,
			SerializerProvider provider) throws IOException, JsonProcessingException {
		List<Identifier> ids = new ArrayList<Identifier>();
        for (Project project : projects) {
            ids.add(project.getProjectId());
        }
        generator.writeObject(ids);
		
	}

}
