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
import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * Customisable settings for call recording transcription.
 *
 * @since 8.2.0
 */
public final class TranscriptionSettings extends JsonableBaseObject {
    private SpeechSettings.Language language;
    private List<URI> eventUrl;
    private EventMethod eventMethod;
    private Boolean sentimentAnalysis;

    TranscriptionSettings() {}

    private TranscriptionSettings(Builder builder) {
        language = builder.language;
        eventMethod = builder.eventMethod;
        sentimentAnalysis = builder.sentimentAnalysis;
        if (builder.eventUrl != null) {
            eventUrl = Collections.singletonList(URI.create(builder.eventUrl));
        }
    }

    /**
     * Language for the recording transcription.
     *
     * @return The language as an enum, or {@code null} if unspecified.
     */
    @JsonProperty("language")
    public SpeechSettings.Language getLanguage() {
        return language;
    }

    /**
     * The URL to the webhook endpoint that is called asynchronously when a transcription is finished.
     *
     * @return The event URL wrapped in a list, or {@code null} if unspecified.
     */
    @JsonProperty("eventUrl")
    public List<URI> getEventUrl() {
        return eventUrl;
    }

    /**
     * The HTTP method Vonage uses to make the request to {@linkplain #getEventUrl()}.
     *
     * @return The event method, or {@code null} if unspecified.
     */
    @JsonProperty("eventMethod")
    public EventMethod getEventMethod() {
        return eventMethod;
    }

    /**
     * Perform sentiment analysis on the call recording transcription segments.
     *
     * @return Whether sentiment analysis is enabled, or {@code null} if unspecified.
     */
    @JsonProperty("sentimentAnalysis")
    public Boolean getSentimentAnalysis() {
        return sentimentAnalysis;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @return A new builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for setting the TranscriptionSettings parameters. All settings / fields are optional.
     */
    public static final class Builder {
        private SpeechSettings.Language language;
        private String eventUrl;
        private EventMethod eventMethod;
        private Boolean sentimentAnalysis;

        private Builder() {}

        /**
         * The language (BCP-47 format) for the recording you're transcribing.
         * This currently supports the same languages as Automatic Speech Recording.
         *
         * @param language The recording language as an enum.
         * @return This builder.
         */
        public Builder language(SpeechSettings.Language language) {
            this.language = language;
            return this;
        }

        /**
         * The URL to the webhook endpoint that is called asynchronously when a transcription is finished.
         *
         * @param eventUrl The event URL as a string.
         * @return This builder.
         */
        public Builder eventUrl(String eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * The HTTP method Vonage uses to make the request to {@code eventUrl}.
         * The default value is {@link EventMethod#POST}.
         *
         * @param eventMethod The HTTP method as an enum.
         * @return This builder.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        /**
         * Whether to perform sentiment analysis on the call recording transcription segments. Will return a value
         * between -1 (negative sentiment) and 1 (positive sentiment) for each segment. Defaults to {@code false}.
         *
         * @param sentimentAnalysis {@code true} to enable sentiment analysis on the recording.
         * @return This builder.
         */
        public Builder sentimentAnalysis(boolean sentimentAnalysis) {
            this.sentimentAnalysis = sentimentAnalysis;
            return this;
        }

        /**
         * Builds the TranscriptionSettings object.
         *
         * @return A new TranscriptionSettings instance with this builder's settings.
         */
        public TranscriptionSettings build() {
            return new TranscriptionSettings(this);
        }
    }
}
