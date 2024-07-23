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
package com.vonage.client;

import com.vonage.client.auth.ApiKeyAuthMethod;
import com.vonage.client.auth.AuthCollection;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Internal class that holds available authentication methods and a shared HttpClient.
 */
public class HttpWrapper {
    private static final String
            CLIENT_NAME = "vonage-java-sdk",
            CLIENT_VERSION = "8.9.3",
            JAVA_VERSION = System.getProperty("java.version"),
            USER_AGENT = String.format("%s/%s java/%s", CLIENT_NAME, CLIENT_VERSION, JAVA_VERSION);

    private AuthCollection authCollection;
    private HttpClient httpClient;
    private HttpConfig httpConfig;

    public HttpWrapper(HttpConfig httpConfig, AuthCollection authCollection) {
        this.authCollection = authCollection;
        this.httpConfig = httpConfig;
    }

    public HttpWrapper(AuthCollection authCollection) {
        this(HttpConfig.builder().build(), authCollection);
    }

    public HttpWrapper(AuthMethod... authMethods) {
        this(HttpConfig.builder().build(), authMethods);
    }

    public HttpWrapper(HttpConfig httpConfig, AuthMethod... authMethods) {
        this(httpConfig, new AuthCollection(authMethods));
    }

    public HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = createHttpClient();
        }
        return httpClient;
    }

    /**
     * Returns the application ID if it was set when creating the client.
     *
     * @return The application ID, or {@code null} if unavailable.
     * @since 8.9.0
     */
    public UUID getApplicationId() {
        try {
            return UUID.fromString(getAuthCollection().getAuth(JWTAuthMethod.class).getApplicationId());
        }
        catch (RuntimeException ex) {
            return null;
        }
    }

    /**
     * Returns the API key if it was set when creating the client.
     *
     * @return The API key, or {@code null} if unavailable.
     * @since 8.9.0
     */
    public String getApiKey() {
        try {
            return getAuthCollection().getAuth(ApiKeyAuthMethod.class).getApiKey();
        }
        catch (RuntimeException ex) {
            return null;
        }
    }

    @Deprecated
    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Deprecated
    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }

    public AuthCollection getAuthCollection() {
        return authCollection;
    }

    @Deprecated
    public void setAuthCollection(AuthCollection authCollection) {
        this.authCollection = authCollection;
    }

    protected HttpClient createHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(200);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultConnectionConfig(
            ConnectionConfig.custom()
                .setCharset(StandardCharsets.UTF_8)
                .build()
        );
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().setTcpNoDelay(true).build());

        // Need to work out a good value for the following:
        // threadSafeClientConnManager.setValidateAfterInactivity();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(httpConfig.getTimeoutMillis())
                .setConnectionRequestTimeout(httpConfig.getTimeoutMillis())
                .setSocketTimeout(httpConfig.getTimeoutMillis())
                .build();

        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setUserAgent(USER_AGENT)
                .setDefaultRequestConfig(requestConfig)
                .useSystemProperties()
                .disableRedirectHandling()
                .build();
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    public String getUserAgent() {
        return USER_AGENT;
    }
}
