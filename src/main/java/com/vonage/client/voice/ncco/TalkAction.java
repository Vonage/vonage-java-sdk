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
import com.vonage.client.voice.TextToSpeechLanguage;

/**
 * An NCCO talk action which allows for synthesized speech to be sent to a call.
 */
public class TalkAction extends JsonableBaseObject implements Action {
    private String text;
    private Boolean bargeIn;
    private Integer loop, style;
    private Float level;
    private TextToSpeechLanguage language;
    private Boolean premium;

    TalkAction() {}

    private TalkAction(Builder builder) {
        this.text = builder.text;
        this.bargeIn = builder.bargeIn;
        this.loop = builder.loop;
        this.level = builder.level;
        this.style = builder.style;
        this.language = builder.language;
        this.premium = builder.premium;
    }

    @Override
    public String getAction() {
        return "talk";
    }

    /**
     * A string of up to 1,500 characters (excluding SSML tags) containing the message to be synthesized into the
     * Call or Conversation. A single comma in text adds a short pause to the synthesized speech.
     *
     * @return The text or Speech Synthesis Markup Language to be spoken into the call.
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * Determines whether the action is terminated when the user presses a button on the keypad.
     * The default value is {@code false}.
     *
     * @return {@code true} if early termination by user is allowed, or {@code null} if unspecified.
     */
    @JsonProperty("bargeIn")
    public Boolean getBargeIn() {
        return bargeIn;
    }

    /**
     * Number of times text is repeated before the Call is closed. The default value is 1.
     *
     * @return The number of iterations of the text to be spoken, or {@code null} if unspecified.
     */
    @JsonProperty("loop")
    public Integer getLoop() {
        return loop;
    }

    /**
     * Volume level that the speech is played at. This can be any value between -1 to 1, with 0 being the default.
     *
     * @return The volume level as a Float between -1.0 and 1.0, or {@code null} if unspecified.
     */
    @JsonProperty("level")
    public Float getLevel() {
        return level;
    }

    /**
     * Language to use when converting the text to speech.
     *
     * @return The text-to-speech language as an enum, or {@code null} if unspecified.
     */
    @JsonProperty("language")
    public TextToSpeechLanguage getLanguage() {
        return language;
    }

    /**
     * Vocal style (vocal range, tessitura and timbre). Default is {@code 0}. Possible values are listed in the
     * <a href=https://developer.vonage.com/en/voice/voice-api/concepts/text-to-speech#supported-languages>
     * Text-To-Speech guide</a>.
     *
     * @return The vocal style number to use, or {@code null} if unspecified.
     */
    @JsonProperty("style")
    public Integer getStyle() {
        return style;
    }

    /**
     * Whether to use Premium text-to-speech. Premium voices incur an additional charge - see
     * <a href=https://www.vonage.com/communications-apis/voice/pricing/>the Voice Pricing page</a>
     * for details on the exact rate.
     *
     * @return {@code true} if the premium version will be used where available, or {@code null} if unspecified.
     */
    @JsonProperty("premium")
    public Boolean getPremium() {
        return premium;
    }

    /**
     * Entrypoint for constructing an instance of this class.
     *
     * @param text The text to be spoken into the call.
     *
     * @return A new {@linkplain Builder} with the text field initialised.
     */
    public static Builder builder(String text) {
        return builder().text(text);
    }

    /**
     * Entrypoint for constructing an instance of this class.
     * You must provide the text to be spoken using the {@linkplain Builder#text(String)} method.
     *
     * @return A new Builder.
     * @since 8.17.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for specifying the properties of a TalkAction. The text is mandatory, all other parameters are optional.
     */
    public static class Builder {
        private String text;
        private Boolean bargeIn, premium;
        private Integer loop, style;
        private Float level;
        private TextToSpeechLanguage language;

        Builder() {}

        /**
         * A string of up to 1,500 characters (excluding SSML tags) containing the message to be synthesized into the
         * Call or Conversation. A single comma in text adds a short pause to the synthesized speech. To add a longer
         * pause, a {@code <break>} tag needs to be used in SSML. To use SSML tags, you must enclose the text in a
         * {@code <speak>} element.
         *
         * @param text The text to be spoken in the call.
         *
         * @return This builder.
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        /**
         * Set to true so this action is terminated when the user presses a button on the keypad. Use this feature to
         * enable users to choose an option without having to listen to the whole message in your Interactive Voice
         * Response (IVR). If you set this to {@code true} the next action in the NCCO stack must be an input action.
         * The default value is {@code false}.
         *
         * @param bargeIn Whether to allow early termination of the action upon user input.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #bargeIn(boolean)} instead. This method will be removed in the next major release.
         */
        @Deprecated
        public Builder bargeIn(Boolean bargeIn) {
            this.bargeIn = bargeIn;
            return this;
        }

        /**
         * Set to true so this action is terminated when the user presses a button on the keypad. Use this feature to
         * enable users to choose an option without having to listen to the whole message in your Interactive Voice
         * Response (IVR). If you set this to {@code true} the next action in the NCCO stack must be an input action.
         * The default value is {@code false}.
         *
         * @param bargeIn Whether to allow early termination of the action upon user input.
         *
         * @return This builder.
         */
        public Builder bargeIn(boolean bargeIn) {
            return bargeIn(Boolean.valueOf(bargeIn));
        }

        /**
         * Number of times text is repeated before the Call is closed.
         * The default value is 1. Set to 0 to loop infinitely.
         *
         * @param loop The loop count as an integer.
         *
         * @return This builder.
         */
        public Builder loop(Integer loop) {
            this.loop = loop;
            return this;
        }

        /**
         * Volume level that the speech is played at. This can be any value between -1 to 1 with 0 being the default.
         *
         * @param level The volume level as a Float between -1.0 and 1.0.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #level(double)} instead. This method will be removed in the next major release.
         */
        @Deprecated
        public Builder level(Float level) {
            this.level = level;
            return this;
        }

        /**
         * Volume level that the speech is played at. This can be any value between -1 to 1 with 0 being the default.
         *
         * @param level The volume level, between -1.0 and 1.0 (inclusive).
         *
         * @return This builder.
         */
        public Builder level(double level) {
            return level(Float.valueOf((float) level));
        }

        /**
         * Language to use when converting the text to speech.
         *
         * @param language The text-to-speech language as an enum.
         *
         * @return This builder.
         */
        public Builder language(TextToSpeechLanguage language) {
            this.language = language;
            return this;
        }

        /**
         * Vocal style (vocal range, tessitura and timbre). Default is {@code 0}. Possible values are listed in the
         * <a href=https://developer.vonage.com/en/voice/voice-api/concepts/text-to-speech#supported-languages>
         * Text-To-Speech guide</a>.
         *
         * @param style The vocal style number to use.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #style(int)} instead. This method will be removed in the next major release.
         */
        @Deprecated
        public Builder style(Integer style) {
            this.style = style;
            return this;
        }

        /**
         * Vocal style (vocal range, tessitura and timbre). Default is {@code 0}. Possible values are listed in the
         * <a href=https://developer.vonage.com/en/voice/voice-api/concepts/text-to-speech#supported-languages>
         * Text-To-Speech guide</a>.
         *
         * @param style The vocal style number to use.
         *
         * @return This builder.
         */
        public Builder style(int style) {
            return style(Integer.valueOf(style));
        }

        /**
         * Whether to use Premium text-to-speech. Set to {@code true} to use the premium version of the specified
         * style if available, otherwise the standard version will be used. Premium voices incur an additional
         * charge - see <a href=https://www.vonage.com/communications-apis/voice/pricing/>the Voice Pricing page</a>
         * for details on the exact rate.
         *
         * @param premium {@code true} to use the premium version where possible, {@code false} for standard.
         *
         * @return This builder.
         *
         * @deprecated Use {@link #premium(boolean)} instead. This method will be removed in the next major release.
         */
        @Deprecated
        public Builder premium(Boolean premium) {
            this.premium = premium;
            return this;
        }

        /**
         * Whether to use Premium text-to-speech. Set to {@code true} to use the premium version of the specified
         * style if available, otherwise the standard version will be used. Premium voices incur an additional
         * charge - see <a href=https://www.vonage.com/communications-apis/voice/pricing/>the Voice Pricing page</a>
         * for details on the exact rate.
         *
         * @param premium {@code true} to use the premium version where possible, {@code false} for standard.
         *
         * @return This builder.
         */
        public Builder premium(boolean premium) {
            return premium(Boolean.valueOf(premium));
        }

        /**
         * Builds the TalkAction.
         *
         * @return A new TalkAction object from the stored builder options.
         */
        public TalkAction build() {
            return new TalkAction(this);
        }
    }
}
