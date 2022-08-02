/*
 *   Copyright 2022 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vonage.client.voice.TextToSpeechLanguage;

/**
 * An NCCO talk action which allows for synthesized speech to be sent to a call.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TalkAction implements Action {
    private static final String ACTION = "talk";

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
        return ACTION;
    }

    public String getText() {
        return text;
    }

    public Boolean getBargeIn() {
        return bargeIn;
    }

    public Integer getLoop() {
        return loop;
    }

    public Float getLevel() {
        return level;
    }

    public TextToSpeechLanguage getLanguage() { return language; }

    public Integer getStyle() { return style; }

    public Boolean getPremium() {
        return premium;
    }

    /**
     * @param text A string of up to 1,500 characters (excluding SSML tags) containing the message to be
     *             synthesized in the Call or Conversation. A single comma in text adds a short pause to the
     *             synthesized speech. To add a longer pause a break tag needs to be used in SSML.
     *             <p>
     *             To use SSML tags, you must enclose the text in a speak element.
     * @return A new {@linkplain Builder} with the text field initialised.
     */
    public static Builder builder(String text) {
        return new Builder(text);
    }

    public static class Builder {
        private String text;
        private Boolean bargeIn, premium;
        private Integer loop, style;
        private Float level;
        private TextToSpeechLanguage language;


        Builder(String text) {
            this.text = text;
        }

        /**
         * @param text A string of up to 1,500 characters (excluding SSML tags) containing the message to be
         *             synthesized in the Call or Conversation. A single comma in text adds a short pause to the
         *             synthesized speech. To add a longer pause a break tag needs to be used in SSML.
         *             <p>
         *             To use SSML tags, you must enclose the text in a speak element.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        /**
         * @param bargeIn Set to true so this action is terminated when the user presses a button on the keypad.
         *                Use this feature to enable users to choose an option without having to listen to the whole
         *                message in your Interactive Voice Response (IVR). If you set bargeIn to true the next
         *                action in the NCCO stack must be an input action. The default value is false.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder bargeIn(Boolean bargeIn) {
            this.bargeIn = bargeIn;
            return this;
        }

        /**
         * @param loop The number of times text is repeated before the Call is closed.
         *             The default value is 1. Set to 0 to loop infinitely.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder loop(Integer loop) {
            this.loop = loop;
            return this;
        }

        /**
         * @param level The volume level that the speech is played. This can be any value between -1 to 1 with 0
         *              being the default.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder level(Float level) {
            this.level = level;
            return this;
        }

        /**
         * @param language The Language to use when converting the text to speech.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder language(TextToSpeechLanguage language) {
            this.language = language;
            return this;
        }

        /**
         * @param style The vocal style to use.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder style(Integer style) {
            this.style = style;
            return this;
        }

        /**
         * @param premium Whether to use Premium text-to-speech. Set to <code>true</code> to use the premium version
         *                of the specified style if available, otherwise the standard version will be used.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder premium(Boolean premium) {
            this.premium = premium;
            return this;
        }

        /**
         * @return A new {@link TalkAction} object from the stored builder options.
         */
        public TalkAction build() {
            return new TalkAction(this);
        }
    }
}
