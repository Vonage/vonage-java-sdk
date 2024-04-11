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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @since 8.5.0
 */
public final class ConnectRequest extends AbstractSessionTokenRequest {
    private Websocket websocket;

    private ConnectRequest() {}

    private ConnectRequest(Builder builder) {
        super(builder);
        websocket = new Websocket(builder);
    }

    /**
     * @return The websocket, or {@code null} if absent / unknown / not applicable.
     */
    @JsonProperty("websocket")
    public Websocket getWebsocket() {
        return websocket;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for defining the fields in a ConnectRequest object.
     */
    public static final class Builder extends AbstractSessionTokenRequest.Builder<ConnectRequest, Builder> {
        URI uri;
        List<String> streams;
        Map<String, String> headers;
        Websocket.AudioRate audioRate;

        private Builder() {
        }

        /**
         * TODO description
         *
         * @param uri The TODO.
         * @return This builder.
         */
        public Builder uri(String uri) {
            this.uri = URI.create(uri);
            return this;
        }

        /**
         * TODO description
         *
         * @param streams The TODO.
         * @return This builder.
         */
        public Builder streams(String... streams) {
            return streams(Arrays.asList(streams));
        }

        /**
         * TODO description
         *
         * @param streams The TODO.
         * @return This builder.
         */
        public Builder streams(List<String> streams) {
            this.streams = streams;
            return this;
        }

        /**
         * TODO description
         *
         * @param headers The TODO.
         * @return This builder.
         */
        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        /**
         * TODO description
         *
         * @param audioRate The TODO.
         * @return This builder.
         */
        public Builder audioRate(Websocket.AudioRate audioRate) {
            this.audioRate = audioRate;
            return this;
        }

        /**
         * Builds the StartCaptionsRequest object.
         *
         * @return The StartCaptionsRequest object with this builder's settings.
         */
        @Override
        public ConnectRequest build() {
            return new ConnectRequest(this);
        }
    }
}
