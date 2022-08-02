/*
 *   Copyright 2022 Vonage
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
import java.net.URI;
import java.util.Map;

/**
 * Represents a SIP endpoint used in a {@link ConnectAction}. See
 * <a href=https://developer.vonage.com/voice/voice-api/ncco-reference#sip-endpoint>the documentation</a>
 * for an example.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SipEndpoint implements Endpoint {
    private static final String TYPE = "sip";

    private final URI uri;
    private final Map<String, ?> headers;

    private SipEndpoint(Builder builder) {
        this.uri = builder.uri;
        this.headers = builder.headers;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public URI getUri() {
        return uri;
    }

    public Map<String, ?> getHeaders() {
        return headers;
    }

    public static Builder builder(String uri) {
        return builder(URI.create(uri));
    }

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
