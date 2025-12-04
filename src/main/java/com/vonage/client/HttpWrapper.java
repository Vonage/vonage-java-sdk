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
package com.vonage.client;

import com.vonage.client.auth.ApiKeyAuthMethod;
import com.vonage.client.auth.AuthCollection;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Internal class that holds available authentication methods and a shared HttpClient.
 */
public class HttpWrapper {
    private static final String
            CLIENT_NAME = "vonage-java-sdk",
            CLIENT_VERSION = "9.5.0",
            JAVA_VERSION = System.getProperty("java.version"),
            USER_AGENT = String.format("%s/%s java/%s", CLIENT_NAME, CLIENT_VERSION, JAVA_VERSION);

    private CloseableHttpClient httpClient;
    private HttpConfig httpConfig;
    private final AuthCollection authCollection;

    /**
     * Creates a new instance of the HttpWrapper class.
     *
     * @param httpConfig The HTTP configuration settings to use.
     * @param authCollection The authentication settings to use.
     * @param httpClient The HTTP client to use.
     *
     * @since 9.0.0
     */
    public HttpWrapper(HttpConfig httpConfig, AuthCollection authCollection, HttpClient httpClient) {
        this.authCollection = authCollection;
        this.httpConfig = httpConfig;
        this.httpClient = httpClient instanceof CloseableHttpClient ?
                (CloseableHttpClient) httpClient : createHttpClient();
    }

    /**
     * Creates a new instance of the HttpWrapper class.
     *
     * @param httpConfig The HTTP configuration settings to use.
     * @param authCollection The authentication settings to use.
     */
    public HttpWrapper(HttpConfig httpConfig, AuthCollection authCollection) {
        this(httpConfig, authCollection, null);
    }

    /**
     * Creates a new instance of the HttpWrapper class.
     *
     * @param authCollection The authentication settings to use.
     */
    public HttpWrapper(AuthCollection authCollection) {
        this(HttpConfig.builder().build(), authCollection);
    }

    /**
     * Creates a new instance of the HttpWrapper class.
     *
     * @param authMethods The authentication methods to use.
     */
    public HttpWrapper(AuthMethod... authMethods) {
        this(HttpConfig.builder().build(), authMethods);
    }

    /**
     * Creates a new instance of the HttpWrapper class.
     *
     * @param httpConfig The HTTP configuration settings to use.
     * @param authMethods The authentication methods to use.
     */
    public HttpWrapper(HttpConfig httpConfig, AuthMethod... authMethods) {
        this(httpConfig, new AuthCollection(authMethods));
    }

    /**
     * Sets the HTTP client to be used by the SDK.
     *
     * @param httpClient The custom HTTP client instance to use.
     * @deprecated Please use {@linkplain HttpConfig} options where possible.
     */
    @Deprecated
    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Sets the HTTP configuration options for the client.
     *
     * @param httpConfig The HTTP configuration settings to use.
     */
    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }

    /**
     * Gets the underlying {@link HttpClient} instance used by the SDK.
     *
     * @return The Apache HTTP client instance.
     */
    public CloseableHttpClient getHttpClient() {
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

    /**
     * Gets the authentication settings used by the client.
     *
     * @return The available authentication methods object.
     */
    public AuthCollection getAuthCollection() {
        return authCollection;
    }

    protected CloseableHttpClient createHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(200);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultConnectionConfig(
            ConnectionConfig.custom().setCharset(StandardCharsets.UTF_8).build()
        );
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().setTcpNoDelay(true).build());

        // Need to work out a good value for the following:
        // threadSafeClientConnManager.setValidateAfterInactivity();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(httpConfig.getTimeoutMillis())
                .setConnectionRequestTimeout(httpConfig.getTimeoutMillis())
                .setSocketTimeout(httpConfig.getTimeoutMillis())
                .build();

        HttpClientBuilder clientBuilder = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setUserAgent(getUserAgent())
                .setDefaultRequestConfig(requestConfig)
                .useSystemProperties().disableRedirectHandling();

        URI proxy = httpConfig.getProxy();
        if (proxy != null) {
            clientBuilder.setProxy(new HttpHost(proxy.getHost(), proxy.getPort(), proxy.getScheme()));
        }

        return clientBuilder.build();
    }

    /**
     * Gets the HTTP configuration settings for the client.
     *
     * @return The request configuration settings object.
     */
    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    /**
     * Gets the {@code User-Agent} header to be used in HTTP requests.
     * This includes the Java runtime and SDK version, as well as the custom user agent string, if present.
     *
     * @return The user agent string.
     */
    public String getUserAgent() {
        String ua = USER_AGENT, custom = httpConfig.getCustomUserAgent();
        if (custom != null) {
            ua += " " + httpConfig.getCustomUserAgent();
        }
        return ua;
    }

    /**
     * Gets the SDK version.
     *
     * @return The SDK version as a string.
     * @since 8.11.0
     */
    public String getClientVersion() {
        return CLIENT_VERSION;
    }
}
