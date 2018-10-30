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
package com.nexmo.client;

public class HttpConfig {
    private static final String DEFAULT_API_BASE_URI = "https://api.nexmo.com";
    private static final String DEFAULT_REST_BASE_URI = "https://rest.nexmo.com";
    private static final String DEFAULT_SNS_BASE_URI = "https://sns.nexmo.com";

    private String apiBaseUri;
    private String restBaseUri;
    private String snsBaseUri;

    private HttpConfig(Builder builder) {
        this.apiBaseUri = builder.apiBaseUri;
        this.restBaseUri = builder.restBaseUri;
        this.snsBaseUri = builder.snsBaseUri;
    }

    public String getApiBaseUri() {
        return apiBaseUri;
    }

    public String getRestBaseUri() {
        return restBaseUri;
    }

    public String getSnsBaseUri() {
        return snsBaseUri;
    }

    public boolean isDefaultApiBaseUri() {
        return DEFAULT_API_BASE_URI.equals(apiBaseUri);
    }

    public boolean isDefaultRestBaseUri() {
        return DEFAULT_REST_BASE_URI.equals(restBaseUri);
    }

    public boolean isDefaultSnsBaseUri() {
        return DEFAULT_SNS_BASE_URI.equals(snsBaseUri);
    }

    public String getVersionedApiBaseUri(String version) {
        return appendVersionToUri(apiBaseUri, version);
    }

    public String getVersionedRestBaseUri(String version) {
        return appendVersionToUri(restBaseUri, version);
    }

    public String getVersionedSnsBaseUri(String version) {
        return appendVersionToUri(snsBaseUri, version);
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

    public static class Builder {
        private String apiBaseUri;
        private String restBaseUri;
        private String snsBaseUri;

        public Builder() {
            this.apiBaseUri = DEFAULT_API_BASE_URI;
            this.restBaseUri = DEFAULT_REST_BASE_URI;
            this.snsBaseUri = DEFAULT_SNS_BASE_URI;
        }

        /**
         * @param apiBaseUri The base uri to use in place of {@link HttpConfig#DEFAULT_API_BASE_URI}
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder apiBaseUri(String apiBaseUri) {
            this.apiBaseUri = sanitizeUri(apiBaseUri);
            return this;
        }

        /**
         * @param restBaseUri The base uri to use in place of {@link HttpConfig#DEFAULT_REST_BASE_URI}
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder restBaseUri(String restBaseUri) {
            this.restBaseUri = sanitizeUri(restBaseUri);
            return this;
        }

        /**
         * @param snsBaseUri The base uri to use in place of {@link HttpConfig#DEFAULT_SNS_BASE_URI}
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder snsBaseUri(String snsBaseUri) {
            this.snsBaseUri = sanitizeUri(snsBaseUri);
            return this;
        }

        /**
         * @param baseUri The base uri to use in place of {@link HttpConfig#DEFAULT_REST_BASE_URI}, {@link HttpConfig#DEFAULT_API_BASE_URI}, and {@link HttpConfig#DEFAULT_SNS_BASE_URI}
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder baseUri(String baseUri) {
            String sanitizedUri = sanitizeUri(baseUri);
            this.apiBaseUri = sanitizedUri;
            this.restBaseUri = sanitizedUri;
            this.snsBaseUri = sanitizedUri;
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
