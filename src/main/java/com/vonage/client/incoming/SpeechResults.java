/*
 * Copyright (c) 2020  Vonage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vonage.client.incoming;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.Collection;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SpeechResults {
    @JsonProperty("timeout_reason")
    private TimeoutReason timeoutReason;
    private Collection<Result> results;
    private String error;

    public String getError() {
        return error;
    }

    public TimeoutReason getTimeoutReason() {
        return timeoutReason;
    }

    /**
     * @param timeoutReason Indicates whether the input ended when the user stopped speaking, by the max duration
     *                      timeout, or if the user didn't say anything
     */
    public void setTimeoutReason(TimeoutReason timeoutReason) {
        this.timeoutReason = timeoutReason;
    }


    public Collection<Result> getResults() {
        return results;
    }

    /**
     * @param results list of speech recognition results that displays the words(s) that the user spoke and the
     *                likelihood that the recognized word(s) in the list where the actual word(s) that the user spoke.
     */
    public void setResults(Collection<Result> results) {
        this.results = results;
    }

    public enum TimeoutReason {
        @JsonProperty("end_on_silence_timeout")
        END_ON_SILENCE_TIMEOUT,
        @JsonProperty("max_duration")
        MAX_DURATION,
        @JsonProperty("start_timeout")
        START_TIMEOUT
    }
}
