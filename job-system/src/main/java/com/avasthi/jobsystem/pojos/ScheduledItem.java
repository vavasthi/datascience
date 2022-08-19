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

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Builder
@Data
public class ScheduledItem implements Serializable {

  private UUID id;
  private UUID responseId;
  private UUID requestId;
  private String repeatSpecification;
  private String key;
  private Date timestamp;
  private int count;
  private MessageTarget messageTarget;
  private RestTarget restTarget;
  private MessageTarget messageCallback;
  private RestTarget restCallback;
  private RetrySpecification retry;
  private Date triedAt;
  private String body;
  private String responseBody;


}
