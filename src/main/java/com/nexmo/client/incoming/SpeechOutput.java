package com.nexmo.client.incoming;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SpeechOutput {
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

    public static class Result {
        private String text;
        private String confidence;

        public String getText() {
            return text;
        }

        /**
         * @param text transcript text representing the words the user spoke.
         */
        public void setText(String text) {
            this.text = text;
        }

        public String getConfidence() {
            return confidence;
        }

        /**
         * @param confidence The confidence estimate between 0.0 and 1.0. A higher number indicates an estimated greater
         *                   likelihood that the recognized words are correct.
         */
        public void setConfidence(String confidence) {
            this.confidence = confidence;
        }
    }
}
