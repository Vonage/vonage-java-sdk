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
import com.vonage.client.voice.EndpointType;
import java.util.Objects;

/**
 * Represents a phone endpoint used in a {@link ConnectAction}. See
 * <a href=https://developer.vonage.com/voice/voice-api/ncco-reference#phone-endpoint>the documentation</a>
 * for an example.
 */
public class PhoneEndpoint extends JsonableBaseObject implements ConnectEndpoint {
    private final String number, dtmfAnswer;
    private final OnAnswer onAnswer;

    private PhoneEndpoint(Builder builder) {
        number = builder.number;
        dtmfAnswer = builder.dtmfAnswer;
        onAnswer = (builder.onAnswerUrl != null) ? new OnAnswer(builder.onAnswerUrl, builder.onAnswerRingback) : null;
    }

    @Override
    public EndpointType getType() {
        return EndpointType.PHONE;
    }

    /**
     * The phone number to connect to in E.164 format.
     *
     * @return The phone number as a string.
     */
    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    /**
     * Set the digits that are sent to the user as soon as the Call is answered.
     * The * and # digits are respected. You create pauses using p. Each pause is 500ms.
     *
     * @return The DTMF digits as a string.
     */
    @JsonProperty("dtmfAnswer")
    public String getDtmfAnswer() {
        return dtmfAnswer;
    }

    /**
     * An object containing a required URL key. The URL serves an NCCO to execute in the number being connected to,
     * before that call is joined to your existing conversation. Optionally, the ringbackTone key can be specified
     * with a URL value that points to a ringbackTone to be played back on repeat to the caller, so they do not hear
     * just silence. The ringbackTone will automatically stop playing when the call is fully connected. Please note,
     * the key ringback is still supported.
     *
     * @return The OnAnswer object, or {@code null} if unspecified.
     */
    @JsonProperty("onAnswer")
    public OnAnswer getOnAnswer() {
        return onAnswer;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param number The phone number to connect to in E.164 format.
     *
     * @return A new Builder.
     */
    public static Builder builder(String number) {
        return new Builder(number);
    }

    /**
     * Builder for specifying properties of a phone endpoint.
     */
    public static class Builder {
        private String number, dtmfAnswer, onAnswerUrl, onAnswerRingback;

        Builder(String number) {
            this.number = number;
        }

        /**
         * Phone number to connect to in E.164 format.
         *
         * @param number The phone number as a string.
         *
         * @return This builder.
         */
        public Builder number(String number) {
            this.number = number;
            return this;
        }

        /**
         * Set the digits that are sent to the user as soon as the Call is answered.
         * The * and # digits are respected. You create pauses using p. Each pause is 500ms.
         *
         * @param dtmfAnswer The DTMF digits as a string.
         *
         * @return This builder.
         */
        public Builder dtmfAnswer(String dtmfAnswer) {
            this.dtmfAnswer = dtmfAnswer;
            return this;
        }

        /**
         * Set the URL to an NCCO to execute in the number being connected to, before that call is joined to your
         * existing conversation.
         *
         * @param url The URL to an NCCO as a string.
         *
         * @return This builder.
         */
        public Builder onAnswer(String url) {
            this.onAnswerUrl = url;
            return this;
        }

        /**
         * Set the URL to an NCCO to execute in the number being connected to, before that call is joined to your
         * existing conversation. Optionally, the ringbackTone key can be specified with a URL value that points to a
         * ringbackTone to be played back on repeat to the caller, so they do not hear just silence. The ringbackTone
         * will automatically stop playing when the call is fully connected. Please note, the key ringback is still
         * supported.
         *
         * @param url (REQUIRED) The URL to an NCCO as a string.
         * @param ringback (OPTIONAL) The URL to a ringback tone as a string.
         *
         * @return This builder.
         */
        public Builder onAnswer(String url, String ringback) {
            this.onAnswerUrl = url;
            this.onAnswerRingback = ringback;
            return this;
        }

        /**
         * Builds the PhoneEndpoint with this builder's properties.
         *
         * @return A new PhoneEndpoint instance.
         */
        public PhoneEndpoint build() {
            return new PhoneEndpoint(this);
        }
    }

    /**
     * An object containing a required URL key. The URL serves an NCCO to execute in the number being connected to,
     * before that call is joined to your existing conversation. Optionally, the ringbackTone key can be specified
     * with a URL value that points to a ringbackTone to be played back on repeat to the caller, so they do not hear
     * just silence. The ringbackTone will automatically stop playing when the call is fully connected. Please note,
     * the key ringback is still supported.
     */
    public static class OnAnswer {
        private final String url, ringback;

        private OnAnswer(String url, String ringback) {
            this.url = Objects.requireNonNull(url);
            this.ringback = ringback;
        }

        /**
         * URL of the NCCO to execute.
         *
         * @return The URL as a string.
         */
        @JsonProperty("url")
        public String getUrl() {
            return url;
        }

        /**
         * URL of the ringback tone to play to the caller.
         *
         * @return The ringback tone URL as a string, or {@code null} if unspecified.
         */
        @JsonProperty("ringback")
        public String getRingback() {
            return ringback;
        }
    }
}
