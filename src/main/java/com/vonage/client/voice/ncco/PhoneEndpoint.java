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
import com.vonage.client.voice.EndpointType;

/**
 * Represents a phone endpoint used in a {@link ConnectAction}. See
 * <a href=https://developer.vonage.com/voice/voice-api/ncco-reference#phone-endpoint>the documentation</a>
 * for an example.
 */
public class PhoneEndpoint extends JsonableBaseObject implements Endpoint {
    private final String number, dtmfAnswer;
    private final OnAnswer onAnswer;

    private PhoneEndpoint(Builder builder) {
        this.number = builder.number;
        this.dtmfAnswer = builder.dtmfAnswer;
        this.onAnswer = (builder.onAnswerUrl != null) ? new OnAnswer(builder.onAnswerUrl, builder.onAnswerRingback) : null;
    }

    @Override
    public String getType() {
        return EndpointType.PHONE.toString();
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

    @JsonProperty("onAnswer")
    public OnAnswer getOnAnswer() {
        return onAnswer;
    }

    public static Builder builder(String number) {
        return new Builder(number);
    }

    public static class Builder {
        private String number, dtmfAnswer, onAnswerUrl, onAnswerRingback;

        Builder(String number) {
            this.number = number;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder dtmfAnswer(String dtmfAnswer) {
            this.dtmfAnswer = dtmfAnswer;
            return this;
        }

        public Builder onAnswer(String url) {
            this.onAnswerUrl = url;
            return this;
        }

        public Builder onAnswer(String url, String ringback) {
            this.onAnswerUrl = url;
            this.onAnswerRingback = ringback;
            return this;
        }

        public PhoneEndpoint build() {
            return new PhoneEndpoint(this);
        }
    }

    /**
     * An object containing a required url key. The URL serves an NCCO to execute in the number being connected to,
     * before that call is joined to your existing conversation. Optionally, the ringbackTone key can be specified
     * with a URL value that points to a ringbackTone to be played back on repeat to the caller, so they do not hear
     * just silence. The ringbackTone will automatically stop playing when the call is fully connected. Please note,
     * the key ringback is still supported.
     */
        public static class OnAnswer {
        private final String url, ringback;

        private OnAnswer(String url, String ringback) {
            this.url = url;
            this.ringback = ringback;
        }

        @JsonProperty("url")
        public String getUrl() {
            return url;
        }

        @JsonProperty("ringback")
        public String getRingback() {
            return ringback;
        }
    }
}
