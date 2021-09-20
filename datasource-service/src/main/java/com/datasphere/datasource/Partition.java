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

package com.datasphere.datasource;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by aladin on 2019. 7. 3..
 */
@Embeddable
public class Partition implements Serializable {

  @Column(name = "partition_seq")
  Long seq;

  @Column(name = "partition_name")
  String name;

  public Partition() {
  }

  public Partition(String name) {
    this.name = name;
  }

  public Partition(Long seq, String name) {
    this.name = name;
    this.seq = seq;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getSeq() {
    return seq;
  }

  public void setSeq(Long seq) {
    this.seq = seq;
  }
}
