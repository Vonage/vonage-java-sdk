/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nexmo.client.voice.VoiceName;

/**
 * An NCCO talk action which allows for synthesized speach to be sent to a call.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TalkAction implements Action {
    private static final String ACTION = "talk";

    private String text;
    private Boolean bargeIn;
    private Integer loop;
    private Float level;
    private VoiceName voiceName;

    private TalkAction(Builder builder) {
        this.text = builder.text;
        this.bargeIn = builder.bargeIn;
        this.loop = builder.loop;
        this.level = builder.level;
        this.voiceName = builder.voiceName;
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

    public VoiceName getVoiceName() {
        return voiceName;
    }

    public static class Builder {
        private String text;
        private Boolean bargeIn = null;
        private Integer loop = null;
        private Float level = null;
        private VoiceName voiceName = null;

        /**
         * @param text A string of up to 1,500 characters (excluding SSML tags) containing the message to be
         *             synthesized in the Call or Conversation. A single comma in text adds a short pause to the
         *             synthesized speech. To add a longer pause a break tag needs to be used in SSML.
         *             <p>
         *             To use SSML tags, you must enclose the text in a speak element.
         */
        public Builder(String text) {
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
         * @param voiceName The name of the voice used to deliver text. You use the voiceName that has the correct
         *                  language, gender and accent for the message you are sending.
         *                  <p>
         *                  For example, the default
         *                  voice {@link VoiceName#KIMBERLY} is a female who speaks English with an American accent (en-US).
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder voiceName(VoiceName voiceName) {
            this.voiceName = voiceName;
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
