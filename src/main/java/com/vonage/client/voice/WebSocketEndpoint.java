/*
 *   Copyright 2023 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketEndpoint implements Endpoint {
    private static final String TYPE = "websocket";
    private String uri, contentType;
    @JsonProperty("headers") private Map<String, Object> headers;

    protected WebSocketEndpoint() {
    }

    public WebSocketEndpoint(String uri, String contentType, Map<String, Object> headers) {
        this.uri = uri;
        this.contentType = contentType;
        this.headers = headers;
    }

    @Deprecated
    public WebSocketEndpoint(String uri, String contentType, List<NameValuePair> headers) {
        this.uri = uri;
        this.contentType = contentType;
        setHeaders(headers);
    }

    @JsonProperty("type")
    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String toLog() {
        return "uri=" + uri + " content-type=" + contentType;
    }

    /**
     * The URI to the websocket you are streaming to.
     *
     * @return The URI as a string.
     */
    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    /**
     *
     * @return Thge content type.
     */
    @JsonProperty("content-type")
    public String getContentType() {
        return contentType;
    }

    @Deprecated
    @JsonIgnore
    public List<NameValuePair> getHeaders() {
        if (headers == null) return null;
        return headers.entrySet().stream()
                .map(e -> new BasicNameValuePair(e.getKey(), Objects.toString(e.getValue())))
                .collect(Collectors.toList());
    }

    @JsonProperty("headers")
    public Map<String, ?> getHeadersMap() {
        return headers;
    }

    @Deprecated
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Deprecated
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonIgnore
    @Deprecated
    public void setHeaders(List<NameValuePair> headers) {
        this.headers = headers.stream().collect(Collectors.toMap(
                NameValuePair::getName, NameValuePair::getValue
        ));
    }
}
