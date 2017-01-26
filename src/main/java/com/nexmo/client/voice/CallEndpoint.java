package com.nexmo.client.voice;/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CallEndpoint {
    private String type = "phone";
    private String number;
    private String dtmfAnswer = null;

    public CallEndpoint() {
    }

    public CallEndpoint(String number) {
        this.number = number;
    }

    public CallEndpoint(String number, String dtmfAnswer) {
        this.number = number;
        this.dtmfAnswer = dtmfAnswer;
    }

    public String getType() {
        return type;
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
        CallEndpoint rhs = (CallEndpoint) obj;
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
