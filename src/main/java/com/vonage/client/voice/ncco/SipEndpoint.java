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

/**
 * Represents a SIP endpoint used in a {@link ConnectAction}
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SipEndpoint implements Endpoint {
    private static final String TYPE = "sip";

    private String uri;

    private SipEndpoint(Builder builder) {
        this.uri = builder.uri;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public String getUri() {
        return uri;
    }

    public static Builder builder(String uri) {
        return new Builder(uri);
    }

    public static class Builder {
        private String uri;

        public Builder(String uri) {
            this.uri = uri;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public SipEndpoint build() {
            return new SipEndpoint(this);
        }
    }
}
