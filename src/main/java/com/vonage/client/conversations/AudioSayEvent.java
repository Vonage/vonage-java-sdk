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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vonage.client.voice.TextToSpeechLanguage;
import java.util.UUID;

/**
 * Represents an {@link EventType#AUDIO_SAY} event.
 */
public final class AudioSayEvent extends AudioOutEvent<AudioSayEventBody> {

    AudioSayEvent() {}

    private AudioSayEvent(Builder builder) {
        super(builder);
        body = new AudioSayEventBody(builder);
    }

    /**
     * Unique audio say identifier.
     *
     * @return The say ID, or {@code null} if unknown.
     */
    public UUID getSayId() {
        return body != null ? body.sayId : null;
    }

    /**
     * Text to be spoken.
     *
     * @return The speech text.
     */
    @JsonIgnore
    public String getText() {
        return body.text;
    }

    /**
     * Text-to-speech voice style. See the
     * <a href=https://developer.vonage.com/en/voice/voice-api/concepts/text-to-speech#supported-languagesVoice>
     * Voice API documentation</a> for valid options.
     *
     * @return The TTS style as an Integer, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Integer getStyle() {
        return body.style;
    }

    /**
     * Language for the spoken text.
     *
     * @return The TTS language as an enum, or {@code null} if unspecified.
     */
    @JsonIgnore
    public TextToSpeechLanguage getLanguage() {
        return body.language;
    }

    /**
     * Whether to use the premium version of the text-to-speech voice.
     *
     * @return {@code true} to use Premium TTS, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Boolean getPremium() {
        return body.premium;
    }

    /**
     * Whether to enable Synthesized Speech Markup Language (SSML).
     *
     * @return {@code true} to use SSML, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Boolean getSsml() {
        return body.ssml;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends AudioOutEvent.Builder<AudioSayEvent, Builder> {
        String text;
        Integer style;
        TextToSpeechLanguage language;
        Boolean premium, ssml;

        Builder() {
            super(EventType.AUDIO_SAY);
        }

        /**
         * (REQUIRED) Text to be spoken.
         *
         * @param text The speech text.
         * @return This builder.
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        /**
         * Text-to-speech voice style. See the
         * <a href=https://developer.vonage.com/en/voice/voice-api/concepts/text-to-speech#supported-languagesVoice>
         * Voice API documentation</a> for valid options.
         *
         * @param style The TTS style as an int.
         * @return This builder.
         */
        public Builder style(int style) {
            this.style = style;
            return this;
        }

        /**
         * Language for the spoken text.
         *
         * @param language The TTS language as an enum.
         * @return This builder.
         */
        public Builder language(TextToSpeechLanguage language) {
            this.language = language;
            return this;
        }

        /**
         * Whether to use the premium version of the text-to-speech voice.
         *
         * @param premium {@code true} to use Premium TTS.
         * @return This builder.
         */
        public Builder premium(boolean premium) {
            this.premium = premium;
            return this;
        }

        /**
         * Whether to enable Synthesized Speech Markup Language (SSML).
         *
         * @param ssml {@code true} to use SSML.
         * @return This builder.
         */
        public Builder ssml(boolean ssml) {
            this.ssml = ssml;
            return this;
        }

        @Override
        public AudioSayEvent build() {
            return new AudioSayEvent(this);
        }
    }
}
