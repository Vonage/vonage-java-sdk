/*
 *   Copyright 2020 Vonage
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

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PhoneEndpoint implements Endpoint {
    private String type = "phone";
    private String number;
    private String dtmfAnswer = null;

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
        return type;
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        PhoneEndpoint rhs = (PhoneEndpoint) obj;
        return new EqualsBuilder()
                .append(this.type, rhs.type)
                .append(this.number, rhs.number)
                .append(this.dtmfAnswer, rhs.dtmfAnswer)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(277, 11)
                .append(this.type)
                .append(this.number)
                .append(this.dtmfAnswer)
                .toHashCode();
    }
}