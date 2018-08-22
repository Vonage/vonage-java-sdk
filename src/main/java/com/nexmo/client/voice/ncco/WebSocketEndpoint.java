/*
 * Copyright (c) 2011-2018 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.voice.ncco;

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
        this.uri = builder.uri;
        this.contentType = builder.contentType;
        this.headers = builder.headers;
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
