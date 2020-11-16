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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a web socket endpoint used in a {@link ConnectAction}
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WebSocketEndpoint implements Endpoint {
    private static final String TYPE = "websocket";

    private String uri;
    private String contentType;
    private Map<String, String> headers;

    private WebSocketEndpoint(Builder builder) {
        uri = builder.uri;
        contentType = builder.contentType;
        headers = builder.headers;
    }

    public String getUri() {
        return uri;
    }

    @JsonProperty("content-type")
    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public static Builder builder(String uri, String contentType) {
        return new Builder(uri, contentType);
    }

    public static class Builder {
        private String uri;
        private String contentType;
        private Map<String, String> headers;

        public Builder(String uri, String contentType) {
            this.uri = uri;
            this.contentType = contentType;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder headers(String... entries) {
            // TODO: Replace with Map.of when we target Java 9
            if (entries.length % 2 != 0) {
                throw new IllegalArgumentException("Entries must be key, value and every key must have a value.");
            }

            Map<String, String> headers = new HashMap<>();
            for (int i = 0; i < entries.length - 1; i += 2) {
                headers.put(entries[i], entries[i + 1]);
            }

            return headers(headers);
        }

        public WebSocketEndpoint build() {
            return new WebSocketEndpoint(this);
        }
    }
}
