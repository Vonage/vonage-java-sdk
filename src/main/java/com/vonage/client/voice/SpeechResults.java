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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.List;

/**
 * Represents the speech recognition results in {@link EventWebhook#getSpeech()}.
 *
 * @since 8.2.0
 */
public class SpeechResults extends JsonableBaseObject {
    private SpeechTimeoutReason timeoutReason;
    private List<SpeechTranscript> results;
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
    public SpeechTimeoutReason getTimeoutReason() {
        return timeoutReason;
    }

    /**
     * The recognised text objects.
     *
     * @return The list of transcript texts, or {@code null} if there are none.
     */
    @JsonProperty("results")
    public List<SpeechTranscript> getResults() {
        return results;
    }
}
