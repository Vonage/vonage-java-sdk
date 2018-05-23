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

package com.nexmo.client.test;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.auth.AuthCollection;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.NexmoUnacceptableAuthException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Set;

public class HttpWrapperStub extends HttpWrapper {
    private HttpUriRequest uriRequest;
    private HttpResponse presetResponse;

    public HttpWrapperStub() {
        super();
        this.presetResponse = new BasicHttpResponse(new ProtocolVersion("1.1", 1, 1), 200, "OK");
    }

    public HttpUriRequest getUriRequest() {
        return uriRequest;
    }

    public void setPresetResponse(HttpResponse presetResponse) {
        this.presetResponse = presetResponse;
    }

    @Override
    public AuthCollection getAuthCollection() {
        return new AuthCollection() {
            @Override
            public AuthMethod getAcceptableAuthMethod(Set<Class> acceptableAuthMethodClasses) throws NexmoUnacceptableAuthException {
                return new AuthMethod() {
                    @Override
                    public RequestBuilder apply(RequestBuilder request) {
                        return request;
                    }

                    @Override
                    public int getSortKey() {
                        return 0;
                    }

                    @Override
                    public int compareTo(AuthMethod o) {
                        return 0;
                    }
                };
            }
        };
    }

    @Override
    public HttpClient getHttpClient() {
        return new HttpClient() {
            @Override
            public HttpParams getParams() {
                return null;
            }

            @Override
            public ClientConnectionManager getConnectionManager() {
                return null;
            }

            @Override
            public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
                uriRequest = request;
                return presetResponse;
            }

            @Override
            public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
                uriRequest = request;
                return presetResponse;
            }

            @Override
            public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
                throw new UnsupportedOperationException();

            }

            @Override
            public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
                throw new UnsupportedOperationException();
            }

            @Override
            public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
                uriRequest = request;
                return null;
            }

            @Override
            public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
                uriRequest = request;
                return null;
            }

            @Override
            public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
                throw new UnsupportedOperationException();
            }

            @Override
            public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
                throw new UnsupportedOperationException();
            }
        };
    }
}