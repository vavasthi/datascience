/*
 * Copyright (c) 2018 Vinay Avasthi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.avasthi.jobsystem.pojos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageTarget extends Target {

  public MessageTarget() {

  }
  public MessageTarget(String topic, int partition, List<String> servers, Map<String, String> properties) {
    this.topic = topic;
    this.partition = partition;
    this.servers = servers;
    this.properties = properties;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public List<String> getServers() {
    return servers;
  }

  public void setServers(List<String> servers) {
    this.servers = servers;
  }

  public int getPartition() {
    return partition;
  }

  public void setPartition(int partition) {
    this.partition = partition;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  private String topic;
  private int partition;
  private List<String> servers;
  private Map<String, String> properties = new HashMap<>();
}
