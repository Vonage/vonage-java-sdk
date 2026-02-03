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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * An NCCO wait action which pauses execution of the running NCCO for a specified number of seconds.
 * <p>
 * The action is synchronous. The wait period starts when the wait action is executed in the NCCO and ends after
 * the provided or default timeout value. At this point, the NCCO resumes execution.
 * </p>
 */
public class WaitAction extends JsonableBaseObject implements Action {
    private Double timeout;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    WaitAction() {}

    private WaitAction(Builder builder) {
        this.timeout = builder.timeout;
    }

    @Override
    public String getAction() {
        return "wait";
    }

    /**
     * Controls the duration of the wait period before executing the next action in the NCCO.
     * Valid values range from 0.1 seconds to 7200 seconds. Values below 0.1 default to 0.1 seconds,
     * and values above 7200 default to 7200 seconds. The default value is 10 seconds.
     *
     * @return The timeout duration in seconds, or {@code null} if unspecified (will default to 10 seconds).
     */
    @JsonProperty("timeout")
    public Double getTimeout() {
        return timeout;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @param timeout The wait duration in seconds (0.1 to 7200).
     *
     * @return A new Builder with the timeout field initialised.
     */
    public static Builder builder(double timeout) {
        return builder().timeout(timeout);
    }

    /**
     * Entrypoint for constructing an instance of this class.
     * The timeout defaults to 10 seconds if not specified.
     *
     * @return A new Builder.
     * @since 8.17.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for specifying the properties of a WaitAction. All parameters are optional.
     */
    public static class Builder {
        private Double timeout;

        private Builder() {
        }

        /**
         * Controls the duration of the wait period before executing the next action in the NCCO.
         * This parameter is a float. Valid values range from 0.1 seconds to 7200 seconds.
         * Values below 0.1 default to 0.1 seconds, and values above 7200 default to 7200 seconds.
         * The default value is 10 seconds.
         *
         * @param timeout The wait duration in seconds.
         *
         * @return This builder.
         */
        public Builder timeout(double timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Builds the WaitAction.
         *
         * @return A new WaitAction object from the stored builder options.
         */
        public WaitAction build() {
            return new WaitAction(this);
        }
    }
}
