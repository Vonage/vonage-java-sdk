/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
package com.nexmo.client;

import com.nexmo.client.auth.AuthCollection;
import com.nexmo.client.auth.AuthMethod;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.nio.charset.Charset;

/**
 * Internal class that holds available authentication methods and a shared HttpClient.
 */
public class HttpWrapper {
    private AuthCollection authCollection;
    private HttpClient httpClient = null;

    public HttpWrapper(AuthCollection authCollection) {
        this.authCollection = authCollection;
    }

    public HttpWrapper(AuthMethod... authMethods) {
        this(new AuthCollection());
        for (AuthMethod authMethod : authMethods) {
            authCollection.add(authMethod);
        }

    }

    public HttpClient getHttpClient() {
        if (this.httpClient == null) {
            this.httpClient = createHttpClient();
        }
        return this.httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public AuthCollection getAuthCollection() {
        return authCollection;
    }

    public void setAuthCollection(AuthCollection authCollection) {
        this.authCollection = authCollection;
    }

    protected HttpClient createHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(200);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultConnectionConfig(
                ConnectionConfig.custom().setCharset(Charset.forName("UTF-8")).build());
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().setTcpNoDelay(true).build());

        // Need to work out a good value for the following:
        // threadSafeClientConnManager.setValidateAfterInactivity();

        RequestConfig requestConfig = RequestConfig.custom().build();

        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setUserAgent("nexmo-java/3.2.0")
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}
