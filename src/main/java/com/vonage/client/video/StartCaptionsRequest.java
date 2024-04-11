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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

/**
 * Defines values for the <code>properties</code> parameter of the
 * {@link VideoClient#startCaptions(StartCaptionsRequest)} method.
 *
 * @since 8.5.0
 */
public final class StartCaptionsRequest extends AbstractSessionTokenRequest {
    private Language languageCode;
    private Integer maxDuration;
    private Boolean partialCaptions;
    private URI statusCallbackUrl;

    private StartCaptionsRequest() {}

    private StartCaptionsRequest(Builder builder) {
        super(builder);
        statusCallbackUrl = builder.statusCallbackUrl;
        languageCode = builder.languageCode;
        partialCaptions = builder.partialCaptions;
        if ((maxDuration = builder.maxDuration) != null && (maxDuration < 300 || maxDuration > 14400)) {
            throw new IllegalArgumentException("Max duration must be between 300 and 14400 seconds.");
        }
    }

    /**
     * A publicly reachable URL controlled by the customer and capable of generating the content to
     * be rendered without user intervention.
     *
     * @return The status callback URL, or {@code null} if not set.
     */
    @JsonProperty("statusCallbackUrl")
    public URI getStatusCallbackUrl() {
        return statusCallbackUrl;
    }

    /**
     * Spoken language used on this call in BCP-47 format.
     *
     * @return The language code as an enum.
     */
    @JsonProperty("languageCode")
    public Language getLanguageCode() {
        return languageCode;
    }

    /**
     * The maximum duration for the audio captioning, in seconds.
     *
     * @return The maximum captioning duration as an integer.
     */
    @JsonProperty("maxDuration")
    public Integer getMaxDuration() {
        return maxDuration;
    }

    /**
     * Whether faster captioning is enabled at the cost of some degree of inaccuracies.
     *
     * @return {@code true} if the partial captions setting is enabled.
     */
    @JsonProperty("partialCaptions")
    public Boolean partialCaptions() {
        return partialCaptions;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for defining the fields in a StartCaptionsRequest object.
     */
    public static final class Builder extends AbstractSessionTokenRequest.Builder<StartCaptionsRequest, Builder> {
        private URI statusCallbackUrl;
        private Language languageCode;
        private Integer maxDuration;
        private Boolean partialCaptions;

        private Builder() {
        }

        /**
         * BCP-47 code for a spoken language used on this call. The default value is {@linkplain Language#EN_US}.
         *
         * @param languageCode The BCP-47 language code as an enum.
         *
         * @return This Builder with the languageCode property setting.
         */
        public Builder languageCode(Language languageCode) {
            this.languageCode = languageCode;
            return this;
        }

        /**
         * A publicly reachable URL controlled by the customer and capable of generating the content to
         * be rendered without user intervention. The minimum length of the URL is 15 characters and the
         * maximum length is 2048 characters.
         *
         * @param statusCallbackUrl The status callback URL as a string.
         *
         * @return This Builder with the statusCallbackUrl property setting.
         */
        public Builder statusCallbackUrl(String statusCallbackUrl) {
            if (statusCallbackUrl == null || statusCallbackUrl.length() < 15 || statusCallbackUrl.length() > 2048) {
                throw new IllegalArgumentException("Status callback URL must be between 15 and 2048 characters.");
            }
            this.statusCallbackUrl = URI.create(statusCallbackUrl);
            return this;
        }

        /**
         * The maximum duration for the audio captioning, in seconds.
         * The default value is 14,400 seconds (4 hours), the maximum duration allowed.
         * The minimum value is 300 seconds.
         *
         * @param maxDuration The maximum captions duration in seconds.
         *
         * @return This Builder with the maxDuration property setting.
         */
        public Builder maxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
            return this;
        }

        /**
         * Whether to enable this to faster captioning at the cost of some degree of inaccuracies.
         * The default value is {@code true}.
         *
         * @param partialCaptions Whether to enable faster captions.
         *
         * @return This Builder with the partialCaptions property setting.
         */
        public Builder partialCaptions(boolean partialCaptions) {
            this.partialCaptions = partialCaptions;
            return this;
        }

        /**
         * Builds the StartCaptionsRequest object.
         *
         * @return The StartCaptionsRequest object with this builder's settings.
         */
        @Override
        public StartCaptionsRequest build() {
            return new StartCaptionsRequest(this);
        }
    }
}
