/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.http.NameValuePair;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WebSocketEndpoint implements Endpoint {
    private String uri;
    private String type = "websocket";
    private String contentType;
    private List<NameValuePair> headers;

    public WebSocketEndpoint(String uri, String contentType, List<NameValuePair> headers) {
        this.uri = uri;
        this.contentType = contentType;
        this.headers = headers;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toLog() {
        return "uri=" + uri + " content-type=" + contentType;
    }

    public String getUri() {
        return uri;
    }

    @JsonProperty("content-type")
    public String getContentType() {
        return contentType;
    }

    public List<NameValuePair> getHeaders() {
        return headers;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setHeaders(List<NameValuePair> headers) {
        this.headers = headers;
    }
}
