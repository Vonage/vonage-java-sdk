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

public class HttpConfig {
    private static final String
            DEFAULT_API_BASE_URI = "https://api.nexmo.com",
            DEFAULT_REST_BASE_URI = "https://rest.nexmo.com",
            DEFAULT_API_EU_BASE_URI = "https://api-eu.vonage.com",
            DEFAULT_VIDEO_BASE_URI = "https://video.api.vonage.com";

    private final int timeoutMillis;
    private final String apiBaseUri, restBaseUri, apiEuBaseUri, videoBaseUri;

    private HttpConfig(Builder builder) {
        if ((timeoutMillis = builder.timeoutMillis) < 10) {
            throw new IllegalArgumentException("Timeout must be greater than 10ms.");
        }
        apiBaseUri = builder.apiBaseUri;
        restBaseUri = builder.restBaseUri;
        videoBaseUri = builder.videoBaseUri;
        apiEuBaseUri = builder.apiEuBaseUri;
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

    public String getApiBaseUri() {
        return apiBaseUri;
    }

    public String getRestBaseUri() {
        return restBaseUri;
    }

    public String getVideoBaseUri() {
        return videoBaseUri;
    }

    public String getApiEuBaseUri() {
        return apiEuBaseUri;
    }

    public boolean isDefaultApiBaseUri() {
        return DEFAULT_API_BASE_URI.equals(apiBaseUri);
    }

    public boolean isDefaultRestBaseUri() {
        return DEFAULT_REST_BASE_URI.equals(restBaseUri);
    }

    public boolean isDefaultApiEuBaseUri() {
        return DEFAULT_API_EU_BASE_URI.equals(apiEuBaseUri);
    }

    public boolean isDefaultVideoBaseUri() {
        return DEFAULT_VIDEO_BASE_URI.equals(videoBaseUri);
    }

    public String getVersionedApiBaseUri(String version) {
        return appendVersionToUri(apiBaseUri, version);
    }

    public String getVersionedRestBaseUri(String version) {
        return appendVersionToUri(restBaseUri, version);
    }

    public String getVersionedApiEuBaseUri(String version) {
        return appendVersionToUri(apiEuBaseUri, version);
    }

    public String getVersionedVideoBaseUri(String version) {
        return appendVersionToUri(videoBaseUri, version);
    }

    private String appendVersionToUri(String uri, String version) {
        return uri + "/" + version;
    }

    /**
     * @return an HttpConfig object with sensible defaults.
     */
    public static HttpConfig defaultConfig() {
        return new Builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int timeoutMillis = 60_000;
        private String
                apiBaseUri = DEFAULT_API_BASE_URI,
                restBaseUri = DEFAULT_REST_BASE_URI,
                apiEuBaseUri = DEFAULT_API_EU_BASE_URI,
                videoBaseUri = DEFAULT_VIDEO_BASE_URI;

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
         * @param apiBaseUri The base uri to use in place of {@link HttpConfig#DEFAULT_API_BASE_URI}.
         *
         * @return This builder.
         */
        public Builder apiBaseUri(String apiBaseUri) {
            this.apiBaseUri = sanitizeUri(apiBaseUri);
            return this;
        }

        /**
         * @param restBaseUri The base uri to use in place of {@link HttpConfig#DEFAULT_REST_BASE_URI}.
         *
         * @return This builder.
         */
        public Builder restBaseUri(String restBaseUri) {
            this.restBaseUri = sanitizeUri(restBaseUri);
            return this;
        }

        /**
         * @param apiEuBaseUri The base uri to use in place of {@link HttpConfig#DEFAULT_API_EU_BASE_URI}
         *
         * @return This builder.
         */
        public Builder apiEuBaseUri(String apiEuBaseUri) {
            this.apiEuBaseUri = sanitizeUri(apiEuBaseUri);
            return this;
        }

        /**
         * @param videoBaseUri The base uri to use in place of {@link HttpConfig#DEFAULT_VIDEO_BASE_URI}.
         *
         * @return This builder.
         */
        public Builder videoBaseUri(String videoBaseUri) {
            this.videoBaseUri = sanitizeUri(videoBaseUri);
            return this;
        }

        /**
         * @param baseUri The base uri to use in place of {@link HttpConfig#DEFAULT_REST_BASE_URI},
         * {@link HttpConfig#DEFAULT_API_BASE_URI}, {@link HttpConfig#DEFAULT_API_EU_BASE_URI} and
         * {@link HttpConfig#DEFAULT_VIDEO_BASE_URI}.
         *
         * @return This builder.
         */
        public Builder baseUri(String baseUri) {
            String sanitizedUri = sanitizeUri(baseUri);
            apiBaseUri = sanitizedUri;
            restBaseUri = sanitizedUri;
            apiEuBaseUri = sanitizedUri;
            videoBaseUri = sanitizedUri;
            return this;
        }

        /**
         * @return A new {@link HttpConfig} object from the stored builder options.
         */
        public HttpConfig build() {
            return new HttpConfig(this);
        }

        private String sanitizeUri(String uri) {
            if (uri != null && uri.endsWith("/")) {
                return uri.substring(0, uri.length() - 1);
            }
            return uri;
        }
    }
}
