/*
 *   Copyright 2023 Vonage
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
    public static final String DEFAULT_API_BASE_URI = "https://api.nexmo.com";
    public static final String DEFAULT_REST_BASE_URI = "https://rest.nexmo.com";
    public static final String DEFAULT_SNS_BASE_URI = "https://sns.nexmo.com";
    public static final String DEFAULT_VIDEO_BASE_URI = "https://video.api.vonage.com";

    private final String apiBaseUri, restBaseUri, snsBaseUri, videoBaseUri;

    private HttpConfig(Builder builder) {
        apiBaseUri = builder.apiBaseUri;
        restBaseUri = builder.restBaseUri;
        snsBaseUri = builder.snsBaseUri;
        videoBaseUri = builder.videoBaseUri;
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

    public String getVideoBaseUri() {
        return videoBaseUri;
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

    public boolean isDefaultVideoBaseUri() {
        return DEFAULT_VIDEO_BASE_URI.equals(videoBaseUri);
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
        private String apiBaseUri, restBaseUri, snsBaseUri, videoBaseUri;

        public Builder() {
            apiBaseUri = DEFAULT_API_BASE_URI;
            restBaseUri = DEFAULT_REST_BASE_URI;
            snsBaseUri = DEFAULT_SNS_BASE_URI;
            videoBaseUri = DEFAULT_VIDEO_BASE_URI;
        }

        /**
         * @param apiBaseUri The base uri to use in place of {@link HttpConfig#DEFAULT_API_BASE_URI}.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder apiBaseUri(String apiBaseUri) {
            this.apiBaseUri = sanitizeUri(apiBaseUri);
            return this;
        }

        /**
         * @param restBaseUri The base uri to use in place of {@link HttpConfig#DEFAULT_REST_BASE_URI}.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder restBaseUri(String restBaseUri) {
            this.restBaseUri = sanitizeUri(restBaseUri);
            return this;
        }

        /**
         * @param snsBaseUri The base uri to use in place of {@link HttpConfig#DEFAULT_SNS_BASE_URI}.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder snsBaseUri(String snsBaseUri) {
            this.snsBaseUri = sanitizeUri(snsBaseUri);
            return this;
        }

        /**
         * @param videoBaseUri The base uri to use in place of {@link HttpConfig#DEFAULT_VIDEO_BASE_URI}.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder videoBaseUri(String videoBaseUri) {
            this.videoBaseUri = sanitizeUri(videoBaseUri);
            return this;
        }

        /**
         * @param baseUri The base uri to use in place of {@link HttpConfig#DEFAULT_REST_BASE_URI},
         * {@link HttpConfig#DEFAULT_API_BASE_URI}, {@link HttpConfig#DEFAULT_SNS_BASE_URI} and
         * {@link HttpConfig#DEFAULT_VIDEO_BASE_URI}.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder baseUri(String baseUri) {
            String sanitizedUri = sanitizeUri(baseUri);
            apiBaseUri = sanitizedUri;
            restBaseUri = sanitizedUri;
            snsBaseUri = sanitizedUri;
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
