/*
 *   Copyright 2025 Vonage
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Defines properties used for Audio Connector.
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
     * WebSocket parameters.
     *
     * @return The websocket properties.
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
        String uri;
        Collection<String> streams;
        Map<String, String> headers;
        Websocket.AudioRate audioRate;

        private Builder() {
        }

        /**
         * (REQUIRED)
         * A valid Vonage Video token for the Audio Connector connection to the Vonage Video Session.
         * You can add additional data to the JWT to identify that the connection is the Audio Connector
         * endpoint or for any other identifying data.
         *
         * @param token The Base64-encoded JWT as a string.
         *
         * @return This builder.
         */
        @Override
        public Builder token(String token) {
            return super.token(token);
        }

        /**
         * The Vonage Video session ID that includes the Vonage Video streams you want to include in the
         * WebSocket stream. The Audio Connector feature is only supported in routed sessions.
         *
         * @param sessionId The session ID as a string.
         *
         * @return This builder.
         */
        @Override
        public Builder sessionId(String sessionId) {
            return super.sessionId(sessionId);
        }

        /**
         * (REQUIRED)
         * A publicly reachable WebSocket URI to be used for the destination of the audio stream.
         * This must start with the {@code ws://} or {@code wss://} protocol.
         *
         * @param uri The WebSocket URI as a string.
         *
         * @return This builder.
         */
        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        /**
         * (OPTIONAL)
         * An array of stream IDs for the Vonage Video streams you want to include in the WebSocket audio.
         * If you omit this property, all streams in the session will be included.
         *
         * @param streams The stream IDs to include in the audio.
         *
         * @return This builder.
         */
        public Builder streams(String... streams) {
            return streams(Arrays.asList(streams));
        }

        /**
         * A collection of stream IDs for the Vonage Video streams you want to include in the WebSocket audio.
         * If you omit this property, all streams in the session will be included.
         *
         * @param streams The stream IDs to include in the audio connection.
         *
         * @return This builder.
         */
        public Builder streams(Collection<String> streams) {
            this.streams = streams;
            return this;
        }

        /**
         * Key-value pairs of headers to be sent to your WebSocket server with each message,
         * with a maximum length of 512 bytes.
         *
         * @param headers The custom request headers as a Map.
         *
         * @return This builder.
         */
        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        /**
         * A number representing the audio sampling rate in Hz.
         *
         * @param audioRate The sampling rate as an enum.
         *
         * @return This builder.
         */
        public Builder audioRate(Websocket.AudioRate audioRate) {
            this.audioRate = audioRate;
            return this;
        }

        /**
         * Builds the ConnectRequest object.
         *
         * @return The ConnectRequest object with this builder's settings.
         */
        @Override
        public ConnectRequest build() {
            return new ConnectRequest(this);
        }
    }
}
