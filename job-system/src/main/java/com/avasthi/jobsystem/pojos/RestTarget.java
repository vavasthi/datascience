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

import java.util.Map;

public class RestTarget extends Target {

  public enum METHOD {
    GET,
    POST,
    DELETE
  }
  public RestTarget() {
  }

  public RestTarget(String url, METHOD method, String contentType, Map<String, String> headers) {
    this.url = url;
    this.method = method;
    this.contentType = contentType;
    this.headers = headers;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public METHOD getMethod() {
    return method;
  }

  public void setMethod(METHOD method) {
    this.method = method;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  private String url;
  private METHOD method;
  private String contentType;
  private Map<String, String> headers;
}
