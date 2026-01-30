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
import com.vonage.client.JsonableBaseObject;

public class PhoneEndpoint extends JsonableBaseObject implements CallEndpoint {
    private String number, dtmfAnswer, shaken;

    /**
     * Constructor used reflectively by Jackson for instantiation.
     */
    PhoneEndpoint() {}

    /**
     * Constructor.
     *
     * @param number The phone number to connect to in E.164 format.
     */
    public PhoneEndpoint(String number) {
        this.number = number;
    }

    /**
     * Constructor.
     *
     * @param number The phone number to connect to in E.164 format.
     *
     * @param dtmfAnswer Set the digits that are sent to the user as soon as the Call is answered.
     * The * and # digits are respected. You create pauses using p. Each pause is 500ms.
     */
    public PhoneEndpoint(String number, String dtmfAnswer) {
        this(number, dtmfAnswer, null);
    }

    /**
     * Constructor.
     *
     * @param number The phone number to connect to in E.164 format.
     *
     * @param dtmfAnswer Set the digits that are sent to the user as soon as the Call is answered.
     * The * and # digits are respected. You create pauses using p. Each pause is 500ms.
     *
     * @param shaken For Vonage customers who are required by the FCC to sign their own calls to the USA,
     * this allows you to place Voice API calls with your own signature. This feature is available by request only.
     * The STIR/SHAKEN Identity Header content that Vonage must use for this call.
     */
    public PhoneEndpoint(String number, String dtmfAnswer, String shaken) {
        this.number = number;
        this.dtmfAnswer = dtmfAnswer;
        this.shaken = shaken;
    }

    @Override
    public EndpointType getType() {
        return EndpointType.PHONE;
    }

    @Override
    public String toLog() {
        return number;
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
     * For Vonage customers who are required by the FCC to sign their own calls to the USA,
     * this allows you to place Voice API calls with your own signature.
     * This feature is available by request only. Please be aware calls with an invalid signature will be rejected.
     * The STIR/SHAKEN Identity Header content that Vonage must use for this call.
     * Expected format is composed of the JWT with the header, payload and signature, an info parameter with
     * a link for the certificate, the algorithm (alg) parameter indicating which encryption type was used
     * and the passport type (ppt) which should be shaken.
     *
     * @return The STIR/SHAKEN Identity Header as a string, or {@code null} if not set.
     */
    @JsonProperty("shaken")
    public String getShaken() {
        return shaken;
    }
}