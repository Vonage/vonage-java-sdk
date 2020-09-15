/*
 *   Copyright 2020 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.http.NameValuePair;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WebSocketEndpoint implements Endpoint {
    private String uri;
    private String type = "websocket";
    private String contentType;
    private List<NameValuePair> headers;

    public WebSocketEndpoint(String uri, String contentType, List<NameValuePair> headers) {
        this.uri = uri;
        this.contentType = contentType;
        this.headers = headers;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toLog() {
        return "uri=" + uri + " content-type=" + contentType;
    }

    public String getUri() {
        return uri;
    }

    @JsonProperty("content-type")
    public String getContentType() {
        return contentType;
    }

    public List<NameValuePair> getHeaders() {
        return headers;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setHeaders(List<NameValuePair> headers) {
        this.headers = headers;
    }
}
