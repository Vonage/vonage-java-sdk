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
package com.vonage.client.verify;

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
