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

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class HttpConfig {
    private static final String
            DEFAULT_API_BASE_URI = "https://api.nexmo.com",
            DEFAULT_REST_BASE_URI = "https://rest.nexmo.com",
            DEFAULT_API_EU_BASE_URI = "https://api-eu.vonage.com",
            DEFAULT_VIDEO_BASE_URI = "https://video.api.vonage.com";

    private final int timeoutMillis;
    private final String customUserAgent, apiBaseUri, restBaseUri, apiEuBaseUri, videoBaseUri;
    private final Function<ApiRegion, String> regionalUriGetter;
    private final URI proxy;
    private final Map<String, String> customHeaders;

    private HttpConfig(Builder builder) {
        if ((timeoutMillis = builder.timeoutMillis) < 10) {
            throw new IllegalArgumentException("Timeout must be greater than 10ms.");
        }
        proxy = builder.proxy;
        apiBaseUri = builder.apiBaseUri;
        restBaseUri = builder.restBaseUri;
        videoBaseUri = builder.videoBaseUri;
        apiEuBaseUri = builder.apiEuBaseUri;
        regionalUriGetter = builder.regionalUriGetter;
        customUserAgent = builder.customUserAgent;
        customHeaders = builder.customHeaders;
    }

    /**
     * Gets the timeout setting for the underlying HTTP client configuration.
     *
     * @return The request timeout in milliseconds.
     * @since 7.8.0
     */
    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    /**
     * Returns the base URI for the "api" endpoints.
     *
     * @return The base URI for the "api" endpoints.
     */
    public String getApiBaseUri() {
        return apiBaseUri;
    }

    /**
     * Returns the base URI for the "rest" endpoints.
     *
     * @return The base URI for the "rest" endpoints.
     */
    public String getRestBaseUri() {
        return restBaseUri;
    }

    /**
     * Returns the base URI for the "video" endpoints.
     *
     * @return The base URI for the "video" endpoints.
     * @since 8.0.0
     */
    public String getVideoBaseUri() {
        return videoBaseUri;
    }

    /**
     * Returns the base URI for the "api-eu" endpoints.
     *
     * @return The base URI for the "api-eu" endpoints.
     */
    public String getApiEuBaseUri() {
        return apiEuBaseUri;
    }

    /**
     * Returns the base URI for the specified region.
     *
     * @param region The region as an enum.
     * @return The base URI for the given region.
     * @since 8.11.0
     */
    public URI getRegionalBaseUri(ApiRegion region) {
        return URI.create(regionalUriGetter.apply(region));
    }

    /**
     * Returns the custom user agent string that will be appended to the default one, if set.
     *
     * @return The custom user agent string to append, or {@code null} if not set.
     * @since 8.11.0
     */
    public String getCustomUserAgent() {
        return customUserAgent;
    }

    /**
     * Returns the additional headers to be included in all requests.
     *
     * @return A map of custom headers (may be empty if none are set).
     * @since 9.2.0
     */
    public Map<String, String> getCustomHeaders() {
        return customHeaders;
    }

    /**
     * Returns the proxy URL to use for the underlying HTTP client configuration.
     *
     * @return The proxy URI, or {@code null} if not set.
     * @since 8.15.0
     */
    public URI getProxy() {
        return proxy;
    }

    /**
     * Creates a standard HttpConfig.
     *
     * @return an HttpConfig object with sensible defaults.
     */
    public static HttpConfig defaultConfig() {
        return builder().build();
    }

    /**
     * Entrypoint for creating a custom HttpConfig.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for configuring the base URI and timeout of the client.
     */
    public static final class Builder {
        private int timeoutMillis = 60_000;
        private URI proxy;
        private Map<String, String> customHeaders = new LinkedHashMap<>(4);
        private Function<ApiRegion, String> regionalUriGetter = region -> "https://"+region+".vonage.com";
        private String customUserAgent,
                apiBaseUri = DEFAULT_API_BASE_URI,
                restBaseUri = DEFAULT_REST_BASE_URI,
                apiEuBaseUri = DEFAULT_API_EU_BASE_URI,
                videoBaseUri = DEFAULT_VIDEO_BASE_URI;

        /**
         * Constructor.
         */
        private Builder() {}

        private String sanitizeUri(String uri) {
            String sanitized = Objects.requireNonNull(uri, "URI must not be null");
            if (sanitized.endsWith("/")) {
                sanitized = sanitized.substring(0, sanitized.length() - 1);
            }
            return URI.create(sanitized).toString();
        }

        /**
         * Sets the socket timeout for requests. By default, this is one minute (60000 ms).
         * <br>
         * Note that this timeout applies to both the connection and socket; therefore, it defines
         * the maximum time for each stage of the request. For example, if set to 30 seconds, then
         * establishing a connection may take 29 seconds and receiving a response may take 29 seconds
         * without timing out (therefore a total of 58 seconds for the request).
         *
         * @param timeoutMillis The timeout in milliseconds.
         *
         * @return This builder.
         * @since 7.8.0
         */
        public Builder timeoutMillis(int timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
            return this;
        }

        /**
         * Sets the proxy to use for requests. This will route requests through the specified URL.
         *
         * @param proxy The proxy URI to use as a string.
         * @return This builder.
         * @since 8.15.0
         * @throws IllegalArgumentException If the proxy URI is invalid.
         */
        public Builder proxy(String proxy) {
            return proxy(URI.create(proxy));
        }

        /**
         * Sets the proxy to use for requests. This will route requests through the specified URL.
         *
         * @param proxy The proxy URI to use.
         * @return This builder.
         * @since 8.15.0
         */
        public Builder proxy(URI proxy) {
            this.proxy = proxy;
            return this;
        }

        /**
         * Add a custom HTTP header to all requests made with this client.
         *
         * @param name The header name.
         * @param value The header value.
         *
         * @return This builder.
         * @since 9.2.0
         */
        public Builder addRequestHeader(String name, String value) {
            customHeaders.put(name, value);
            return this;
        }

        /**
         * Replaces the URI used in "api" endpoints.
         *
         * @param apiBaseUri The base uri to use.
         * @return This builder.
         */
        public Builder apiBaseUri(String apiBaseUri) {
            this.apiBaseUri = sanitizeUri(apiBaseUri);
            return this;
        }

        /**
         * Replaces the base URI used in "rest" endpoints.
         *
         * @param restBaseUri The base uri to use.
         * @return This builder.
         */
        public Builder restBaseUri(String restBaseUri) {
            this.restBaseUri = sanitizeUri(restBaseUri);
            return this;
        }

        /**
         * Replaces the base URI used in "api-eu" endpoints.
         *
         * @param apiEuBaseUri The base URI to use.
         * @return This builder.
         */
        public Builder apiEuBaseUri(String apiEuBaseUri) {
            this.apiEuBaseUri = sanitizeUri(apiEuBaseUri);
            return this;
        }

        /**
         * Replaces the base URI used in "video" endpoints.
         *
         * @param videoBaseUri The base URI to use.
         * @return This builder.
         * @since 8.0.0
         */
        public Builder videoBaseUri(String videoBaseUri) {
            this.videoBaseUri = sanitizeUri(videoBaseUri);
            return this;
        }

        /**
         * Replaces the base URI used in all requests with the specified parameter.
         *
         * @param baseUri The base URI to use.
         * @return This builder.
         */
        public Builder baseUri(String baseUri) {
            String sanitizedUri = sanitizeUri(baseUri);
            apiBaseUri = sanitizedUri;
            restBaseUri = sanitizedUri;
            apiEuBaseUri = sanitizedUri;
            videoBaseUri = sanitizedUri;
            return regionalUriGetter(region -> sanitizedUri.replace("://", "://" + region + '.'));
        }

        /**
         * Replaces the base URI used in all requests with the specified parameter.
         *
         * @param baseUri The base URI to use.
         * @return This builder.
         * @since 8.9.0
         */
        public Builder baseUri(URI baseUri) {
            return baseUri(baseUri.toString());
        }

        /**
         * Sets a function to get the base URI for a given region.
         *
         * @param uriGetter The function which takes as input a region and returns a base URI as a string.
         * @return This builder.
         * @since 8.11.0
         */
        public Builder regionalUriGetter(Function<ApiRegion, String> uriGetter) {
            this.regionalUriGetter = Objects.requireNonNull(uriGetter);
            return this;
        }

        /**
         * Appends a custom string to the default {@code User-Agent} header. This is mainly used for
         * derivatives of the SDK, or to distinguish particular users / use cases.
         *
         * @param userAgent The user agent string to append to the existing one. Must be less than 128 characters.
         * @return This builder.
         * @since 8.11.0
         */
        public Builder appendUserAgent(String userAgent) {
            if ((this.customUserAgent = Objects.requireNonNull(userAgent).trim()).length() > 127) {
                throw new IllegalArgumentException("User agent string must be less than 128 characters in length.");
            }
            if (customUserAgent.isEmpty()) {
                throw new IllegalArgumentException("Custom user agent string cannot be blank.");
            }
            return this;
        }

        /**
         * Builds the HttpConfig.
         *
         * @return A new HttpConfig object from the stored builder options.
         */
        public HttpConfig build() {
            return new HttpConfig(this);
        }
    }
}
