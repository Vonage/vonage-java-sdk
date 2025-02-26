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

public class WebSocketEndpoint extends JsonableBaseObject implements Endpoint {
    // TODO: Use stronger typing
    private String uri, contentType;
    @JsonProperty("headers") private Map<String, Object> headers;

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
            headers
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
        this.uri = Objects.requireNonNull(uri, "URI is required.").toString();
        this.contentType = contentType != null ? contentType.toString() : null;
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
     * Content type of the audio stream; either {@code audio/l16;rate=16000} or {@code audio/l16;rate=8000}.
     *
     * @return The content type.
     */
    @JsonProperty("content-type")
    public String getContentType() {
        return contentType;
    }

    /**
     * Additional headers to be sent with the request.
     *
     * @return A map of headers to be sent in the payload.
     */
    @JsonProperty("headers")
    public Map<String, ?> getHeadersMap() {
        return headers;
    }
}
