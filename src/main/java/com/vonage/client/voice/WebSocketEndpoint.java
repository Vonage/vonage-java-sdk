/*
 *   Copyright 2024 Vonage
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Map;

public class WebSocketEndpoint extends JsonableBaseObject implements Endpoint {
    private String uri, contentType;
    @JsonProperty("headers") private Map<String, Object> headers;

    protected WebSocketEndpoint() {
    }

    public WebSocketEndpoint(String uri, String contentType, Map<String, Object> headers) {
        this.uri = uri;
        this.contentType = contentType;
        this.headers = headers;
    }

    @Override
    public String getType() {
        return EndpointType.WEBSOCKET.toString();
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
     * @return The content type.
     */
    @JsonProperty("content-type")
    public String getContentType() {
        return contentType;
    }

    @JsonProperty("headers")
    public Map<String, ?> getHeadersMap() {
        return headers;
    }
}
