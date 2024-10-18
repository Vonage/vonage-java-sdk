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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.voice.EndpointType;
import java.net.URI;
import java.util.Map;

/**
 * Represents a web socket endpoint used in a {@link ConnectAction}. See
 * <a href=https://developer.vonage.com/voice/voice-api/ncco-reference#websocket-endpoint>the documentation</a>
 * for an example.
 */
public class WebSocketEndpoint extends JsonableBaseObject implements Endpoint {
    private final URI uri;
    private final String contentType;
    private final Map<String, ?> headers;

    private WebSocketEndpoint(Builder builder) {
        uri = builder.uri;
        contentType = builder.contentType;
        headers = builder.headers;
    }

    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    @Override
    public String getType() {
        return EndpointType.WEBSOCKET.toString();
    }

    @JsonProperty("content-type")
    public String getContentType() {
        return contentType;
    }

    @JsonProperty("headers")
    public Map<String, ?> getHeaders() {
        return headers;
    }

    public static Builder builder(String uri, String contentType) {
        return new Builder(uri, contentType);
    }

    public static class Builder {
        private URI uri;
        private String contentType;
        private Map<String, ?> headers;

        Builder(String uri, String contentType) {
            this.uri = URI.create(uri);
            this.contentType = contentType;
        }

        public Builder uri(URI uri) {
            this.uri = uri;
            return this;
        }

        public Builder uri(String uri) {
            return uri(URI.create(uri));
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder headers(Map<String, ?> headers) {
            this.headers = headers;
            return this;
        }

        public WebSocketEndpoint build() {
            return new WebSocketEndpoint(this);
        }
    }
}
