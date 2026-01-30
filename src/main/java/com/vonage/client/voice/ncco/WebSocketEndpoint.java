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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.users.channels.Websocket;
import com.vonage.client.voice.Authorization;
import com.vonage.client.voice.EndpointType;
import java.net.URI;
import java.util.Map;

/**
 * Represents a web socket endpoint used in a {@link ConnectAction}. See
 * <a href=https://developer.vonage.com/voice/voice-api/ncco-reference#websocket-endpoint>the documentation</a>
 * for an example.
 */
public class WebSocketEndpoint extends JsonableBaseObject implements ConnectEndpoint {
    private final URI uri;
    private final String contentType;
    private final Map<String, ?> headers;
    private final Authorization authorization;

    private WebSocketEndpoint(Builder builder) {
        uri = builder.uri;
        contentType = builder.contentType;
        headers = builder.headers;
        authorization = builder.authorization;
    }

    @Override
    public EndpointType getType() {
        return EndpointType.WEBSOCKET;
    }

    /**
     * URI to the websocket you are streaming to.
     *
     * @return The websocket URI.
     */
    @JsonProperty("uri")
    public URI getUri() {
        return uri;
    }

    /**
     * The internet media type for the audio you are streaming.
     *
     * @return The content type as a string.
     */
    @JsonProperty("content-type")
    public String getContentType() {
        return contentType;
    }

    /**
     * Additional headers to be sent with the WebSocket request.
     *
     * @return The headers data as a map, or {@code null} if unspecified.
     */
    @JsonProperty("headers")
    public Map<String, ?> getHeaders() {
        return headers;
    }

    /**
     * Optional configuration defining how the Authorization HTTP header is set
     * in the WebSocket opening handshake.
     *
     * @return The authorization configuration, or {@code null} if unspecified.
     * @since 9.1.0
     */
    @JsonProperty("authorization")
    public Authorization getAuthorization() {
        return authorization;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param uri The websocket URI.
     * @param contentType The content type as a string.
     *
     * @return A new Builder.
     */
    public static Builder builder(String uri, String contentType) {
        return new Builder(uri, contentType);
    }

    /**
     * Builder for specifying properties of a WebSocket endpoint.
     */
    public static class Builder {
        private URI uri;
        private String contentType;
        private Map<String, ?> headers;
        private Authorization authorization;

        Builder(String uri, String contentType) {
            this.uri = URI.create(uri);
            this.contentType = contentType;
        }

        /**
         * URI to the websocket you are streaming to.
         *
         * @param uri The websocket URI.
         *
         * @return This builder.
         */
        public Builder uri(URI uri) {
            this.uri = uri;
            return this;
        }

        /**
         * URI to the websocket you are streaming to.
         *
         * @param uri The websocket URI as a string.
         *
         * @return This builder.
         */
        public Builder uri(String uri) {
            return uri(URI.create(uri));
        }

        /**
         * The internet media type for the audio you are streaming. Possible values are:
         * {@code audio/l16;rate=8000}, {@code audio/l16;rate=16000}, or {@code audio/l16;rate=24000}.
         *
         * @param contentType The content type as a string.
         *
         * @return This builder.
         */
        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * The internet media type for the audio you are streaming.
         *
         * @param contentType The content type as an enum.
         *
         * @return This builder.
         * @since 8.17.0
         */
        public Builder contentType(Websocket.ContentType contentType) {
            return contentType(contentType.toString());
        }

        /**
         * Additional headers to be sent with the WebSocket request.
         *
         * @param headers The headers data as a map.
         *
         * @return This builder.
         */
        public Builder headers(Map<String, ?> headers) {
            this.headers = headers;
            return this;
        }

        /**
         * Optional configuration defining how the Authorization HTTP header is set
         * in the WebSocket opening handshake.
         *
         * @param authorization The authorization configuration.
         *
         * @return This builder.
         * @since 9.1.0
         */
        public Builder authorization(Authorization authorization) {
            this.authorization = authorization;
            return this;
        }

        /**
         * Builds the WebSocketEndpoint with this builder's properties.
         *
         * @return A new WebSocketEndpoint instance.
         */
        public WebSocketEndpoint build() {
            return new WebSocketEndpoint(this);
        }
    }
}
