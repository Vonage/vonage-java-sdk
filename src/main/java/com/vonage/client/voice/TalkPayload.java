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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines the text-to-speech properties.
 */
public final class TalkPayload extends UuidRequestWrapper {
    private final String text;
    private final Integer loop, style;
    private final Double level;
    private final Boolean premium;
    private final TextToSpeechLanguage language;

    private TalkPayload(Builder builder) {
        if ((text = builder.text) == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text is required.");
        }
        if ((loop = builder.loop) != null && loop < 0) {
            throw new IllegalArgumentException("Loop cannot be negative.");
        }
        if ((style = builder.style) != null && style < 0) {
            throw new IllegalArgumentException("Style cannot be negative.");
        }
        if ((level = builder.level) != null && (level < -1 || level > 1)) {
            throw new IllegalArgumentException("Volume level must be between -1 and 1 (inclusive).");
        }
        premium = builder.premium;
        language = builder.language;
    }

    /**
     * The number of times the audio file at stream_url is repeated before the stream ends (0 for infinite).
     *
     * @return The number of repetitions, or {@code null} if unset (the default).
     */
    @JsonProperty("loop")
    public Integer getLoop() {
        return loop;
    }

    /**
     * A string of up to 1500 characters containing the message to be synthesized in the Call or Conversation.
     * Each comma in text adds a short pause to the synthesized speech.
     *
     * @return The speech text, or {@code null} if unset (the default).
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * The Language that will be used to convert {@code text} into speech.
     *
     * @return The TTS language as an enum, or {@code null} if unset (the default).
     */
    @JsonProperty("language")
    public TextToSpeechLanguage getLanguage() {
        return language;
    }

    /**
     * The Vocal Style to use (range, tessitura, timbre) to use in the speech.
     *
     * @return The voice style, or {@code null} if unset (the default).
     */
    @JsonProperty("style")
    public Integer getStyle() {
        return style;
    }

    /**
     * The volume level that the speech is played at, between -1 and 1.
     *
     * @return The speech volume, or {@code null} if unset (the default).
     */
    @JsonProperty("level")
    public Double getLevel() {
        return level;
    }

    /**
     * Whether the premium version of the specified voice style should be used if available.
     *
     * @return {@code true} for premium, {@code false} for standard or {@code null} if unset (the default).
     */
    @JsonProperty("premium")
    public Boolean getPremium() {
        return premium;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param text The text to convert to speech.
     *
     * @return A new Builder.
     */
    public static Builder builder(String text) {
        return new Builder(text);
    }

    /**
     * Builder for configuring properties of TalkPayload.
     */
    public static final class Builder {
        private final String text;
        private Integer loop, style;
        private Double level;
        private Boolean premium;
        private TextToSpeechLanguage language;

        Builder(String text) {
            this.text = text;
        }

        /**
         * The number of times to repeat the message. The default value is {@code 1}, or you can use {@code 0}
         *  to indicate that the message should be repeated indefinitely.
         *
         * @param loop The number of repetitions.
         *
         * @return This builder.
         */
        public Builder loop(int loop) {
            this.loop = loop;
            return this;
        }

        /**
         * The vocal style (vocal range, tessitura, and timbre) to use for text-to-speech.
         * Each language has varying number of styles. Refer to
         * <a href=https://developer.vonage.com/en/voice/voice-api/concepts/text-to-speech#supported-languages>
         * the documentation</a> for available values.
         *
         * @param style The language style as an integer.
         *
         * @return This builder.
         */
        public Builder style(int style) {
            this.style = style;
            return this;
        }

        /**
         * The volume level that the speech is played.
         * This can be any value between -1 to 1 in 0.1 increments, with 0 being the default.
         *
         * @param level The volume of speech.
         *
         * @return This builder.
         */
        public Builder level(double level) {
            this.level = level;
            return this;
        }

        /**
         * Specify whether to use the premium version of the specified style if available.
         * Find out more information about Premium Voices in the
         * <a href=https://developer.vonage.com/en/voice/voice-api/guides/text-to-speech#premium-voices>
         * Text-To-Speech guide</a>.
         *
         * @param premium {@code true} to use premium voice or {@code false} for standard version (the default).
         *
         * @return This builder.
         */
        public Builder premium(boolean premium) {
            this.premium = premium;
            return this;
        }

        /**
         * The speech language to use.
         *
         * @param language Language code as an enum.
         *
         * @return This builder.
         */
        public Builder language(TextToSpeechLanguage language) {
            this.language = language;
            return this;
        }

        /**
         * Constructs the TalkPayload object.
         *
         * @return A new {@linkplain TalkPayload} instance with this builder's properties.
         */
        public TalkPayload build() {
            return new TalkPayload(this);
        }
    }
}
