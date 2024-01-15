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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.Map;

/**
 * Represents a SIP endpoint used in a {@link ConnectAction}. See
 * <a href=https://developer.vonage.com/voice/voice-api/ncco-reference#sip-endpoint>the documentation</a>
 * for an example.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SipEndpoint extends JsonableBaseObject implements Endpoint {
    private static final String TYPE = "sip";

    private final URI uri;
    private final Map<String, ?> headers;

    private SipEndpoint(Builder builder) {
        this.uri = builder.uri;
        this.headers = builder.headers;
    }

    @JsonProperty("type")
    @Override
    public String getType() {
        return TYPE;
    }

    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    @JsonProperty("headers")
    public Map<String, ?> getHeaders() {
        return headers;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param uri The URI as a string.
     *
     * @return A new Builder.
     */
    public static Builder builder(String uri) {
        return builder(URI.create(uri));
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param uri The URI object.
     *
     * @return A new Builder.
     */
    public static Builder builder(URI uri) {
        return new Builder(uri);
    }

    public static class Builder {
        private URI uri;
        private Map<String, ?> headers;

        Builder(URI uri) {
            this.uri = uri;
        }

        public Builder uri(URI uri) {
            this.uri = uri;
            return this;
        }

        public Builder uri(String uri) {
            return uri(URI.create(uri));
        }

        public Builder headers(Map<String, ?> headers) {
            this.headers = headers;
            return this;
        }

        public SipEndpoint build() {
            return new SipEndpoint(this);
        }
    }
}
