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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.users.channels.Websocket;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

public class WebSocketEndpoint extends JsonableBaseObject implements CallEndpoint {
    private URI uri;
    private Websocket.ContentType contentType;
    @JsonProperty("headers") private Map<String, Object> headers;
    @JsonProperty("authorization") private Authorization authorization;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    protected WebSocketEndpoint() {
    }

    /**
     * Create a new WebSocket Endpoint
     *
     * @param uri URI to the websocket, starting with {@code ws://} or {@code wss://}.
     * @param contentType The audio MIME type.
     * @param headers Additional headers to be sent with the request.
     */
    public WebSocketEndpoint(String uri, String contentType, Map<String, Object> headers) {
        this(
            URI.create(uri),
            contentType != null ? Websocket.ContentType.fromString(contentType) : null,
            headers,
            null
        );
    }

    /**
     * Create a new WebSocket Endpoint
     *
     * @param uri URI to the websocket, starting with {@code ws://} or {@code wss://}.
     * @param contentType The audio MIME type.
     * @param headers Additional headers to be sent with the request.
     *
     * @since 8.17.0
     */
    public WebSocketEndpoint(URI uri, Websocket.ContentType contentType, Map<String, Object> headers) {
        this(uri, contentType, headers, null);
    }

    /**
     * Create a new WebSocket Endpoint
     *
     * @param uri URI to the websocket, starting with {@code ws://} or {@code wss://}.
     * @param contentType The audio MIME type as a string.
     * @param headers Additional headers to be sent with the request.
     * @param authorization Optional authorization configuration for the WebSocket handshake.
     *
     * @since 9.1.0
     */
    public WebSocketEndpoint(String uri, String contentType, 
                           Map<String, Object> headers, Authorization authorization) {
        this(
            URI.create(uri),
            contentType != null ? Websocket.ContentType.fromString(contentType) : null,
            headers,
            authorization
        );
    }

    /**
     * Create a new WebSocket Endpoint
     *
     * @param uri URI to the websocket, starting with {@code ws://} or {@code wss://}.
     * @param contentType The audio MIME type.
     * @param headers Additional headers to be sent with the request.
     * @param authorization Optional authorization configuration for the WebSocket handshake.
     *
     * @since 9.1.0
     */
    public WebSocketEndpoint(URI uri, Websocket.ContentType contentType, 
                           Map<String, Object> headers, Authorization authorization) {
        this.uri = Objects.requireNonNull(uri, "URI is required.");
        this.contentType = contentType;
        this.headers = headers;
        this.authorization = authorization;
    }

    @Override
    public EndpointType getType() {
        return EndpointType.WEBSOCKET;
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
    public URI getUri() {
        return uri;
    }

    /**
     * Content type of the audio stream; either {@code audio/l16;rate=16000} or {@code audio/l16;rate=8000}.
     *
     * @return The content type.
     */
    @JsonProperty("content-type")
    public Websocket.ContentType getContentType() {
        return contentType;
    }

    /**
     * Additional headers to be sent with the request.
     *
     * @return A map of headers to be sent in the payload.
     */
    @JsonProperty("headers")
    public Map<String, Object> getHeadersMap() {
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
}
