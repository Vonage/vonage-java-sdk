/*
 *   Copyright 2023 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneEndpoint implements Endpoint {
    private static final String TYPE = "phone";
    private String number, dtmfAnswer;

    public PhoneEndpoint() {
    }

    public PhoneEndpoint(String number) {
        this.number = number;
    }

    public PhoneEndpoint(String number, String dtmfAnswer) {
        this.number = number;
        this.dtmfAnswer = dtmfAnswer;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String toLog() {
        return number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDtmfAnswer() {
        return dtmfAnswer;
    }

    public void setDtmfAnswer(String dtmfAnswer) {
        this.dtmfAnswer = dtmfAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneEndpoint that = (PhoneEndpoint) o;
        return Objects.equals(number, that.number) &&
                Objects.equals(dtmfAnswer, that.dtmfAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TYPE, number, dtmfAnswer);
    }
}