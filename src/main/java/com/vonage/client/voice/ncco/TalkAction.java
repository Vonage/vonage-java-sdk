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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.voice.TextToSpeechLanguage;

/**
 * An NCCO talk action which allows for synthesized speech to be sent to a call.
 */
public class TalkAction extends JsonableBaseObject implements Action {
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

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("bargeIn")
    public Boolean getBargeIn() {
        return bargeIn;
    }

    @JsonProperty("loop")
    public Integer getLoop() {
        return loop;
    }

    @JsonProperty("level")
    public Float getLevel() {
        return level;
    }

    @JsonProperty("language")
    public TextToSpeechLanguage getLanguage() {
        return language;
    }

    @JsonProperty("style")
    public Integer getStyle() {
        return style;
    }

    @JsonProperty("premium")
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
         * @return This builder.
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
         * @return This builder.
         */
        public Builder bargeIn(Boolean bargeIn) {
            this.bargeIn = bargeIn;
            return this;
        }

        /**
         * @param loop The number of times text is repeated before the Call is closed.
         *             The default value is 1. Set to 0 to loop infinitely.
         *
         * @return This builder.
         */
        public Builder loop(Integer loop) {
            this.loop = loop;
            return this;
        }

        /**
         * @param level The volume level that the speech is played. This can be any value between -1 to 1 with 0
         *              being the default.
         *
         * @return This builder.
         */
        public Builder level(Float level) {
            this.level = level;
            return this;
        }

        /**
         * @param language The Language to use when converting the text to speech.
         *
         * @return This builder.
         */
        public Builder language(TextToSpeechLanguage language) {
            this.language = language;
            return this;
        }

        /**
         * @param style The vocal style to use.
         *
         * @return This builder.
         */
        public Builder style(Integer style) {
            this.style = style;
            return this;
        }

        /**
         * @param premium Whether to use Premium text-to-speech. Set to {@code true} to use the premium version
         *                of the specified style if available, otherwise the standard version will be used.
         *
         * @return This builder.
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
