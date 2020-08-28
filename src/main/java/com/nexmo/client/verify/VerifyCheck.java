/*
 * Copyright (c) 2011-2018 Vonage Inc
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
package com.nexmo.client.verify;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class VerifyCheck {
    private final Date date;
    private final String code;
    private final Status status;
    private final String ipAddress;

    public VerifyCheck(@JsonProperty("date_received") Date date,
                       @JsonProperty("code") String code,
                       @JsonProperty("status") Status status,
                       @JsonProperty("ip_address") String ipAddress) {
        this.date = date;
        this.code = code;
        this.status = status;
        this.ipAddress = ipAddress;
    }

    public enum Status {
        VALID, INVALID,
    }

    public Date getDate() {
        return this.date;
    }

    public String getCode() {
        return this.code;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }
}
