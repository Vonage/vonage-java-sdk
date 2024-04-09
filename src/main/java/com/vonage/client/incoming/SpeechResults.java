/*
 * Copyright 2024 Vonage
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
import java.util.Collection;

/**
 * @deprecated Use {@link com.vonage.client.voice.SpeechResults}.
 */
@Deprecated
public class SpeechResults {
    private TimeoutReason timeoutReason;
    private Collection<Result> results;
    private String error;
    private URI recordingUrl;

    /**
     * Speech recording URL. Included if the {@code saveAudio} flag is set to {@code true} in
     * the input action. Requires JWT authorization for downloading see <a
     * href=https://developer.vonage.com/en/voice/voice-api/code-snippets/recording-calls/download-a-recording>
     * Download a Recording</a> documentation for details.
     *
     * @return The recording URL, or {@code null} if absent.
     *
     * @since 8.2.0
     */
    @JsonProperty("recording_url")
    public URI getRecordingUrl() {
        return recordingUrl;
    }

    /**
     * Error message.
     *
     * @return The error message, or {@code null} if absent / not applicable.
     */
    @JsonProperty("error")
    public String getError() {
        return error;
    }

    /**
     * Indicates if the input ended when user stopped speaking, by max duration timeout
     * or if the user didn't say anything.
     *
     * @return Reason for the timeout as an enum.
     */
    @JsonProperty("timeout_reason")
    public TimeoutReason getTimeoutReason() {
        return timeoutReason;
    }

    /**
     * @param timeoutReason Indicates whether the input ended when the user stopped speaking, by the max duration
     *                      timeout, or if the user didn't say anything.
     *
     * @deprecated This setter will be removed in a future release.
     */
    @Deprecated
    public void setTimeoutReason(TimeoutReason timeoutReason) {
        this.timeoutReason = timeoutReason;
    }

    /**
     * The recognised text objects.
     *
     * @return The collection of transcript texts, or {@code null} if there are none.
     */
    @JsonProperty("results")
    public Collection<Result> getResults() {
        return results;
    }

    /**
     * @param results list of speech recognition results that displays the words(s) that the user spoke and the
     *                likelihood that the recognized word(s) in the list where the actual word(s) that the user spoke.
     *
     * @deprecated This setter will be removed in a future release.
     */
    @Deprecated
    public void setResults(Collection<Result> results) {
        this.results = results;
    }

    /**
     * Represents the timeout reason in {@linkplain #getTimeoutReason()}.
     */
    public enum TimeoutReason {
        END_ON_SILENCE_TIMEOUT,
        MAX_DURATION,
        START_TIMEOUT;

        @JsonValue
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
