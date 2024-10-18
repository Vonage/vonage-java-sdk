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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

public class PhoneEndpoint extends JsonableBaseObject implements Endpoint {
    private String number, dtmfAnswer;

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
        this.number = number;
        this.dtmfAnswer = dtmfAnswer;
    }

    @Override
    public String getType() {
        return EndpointType.PHONE.toString();
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
}