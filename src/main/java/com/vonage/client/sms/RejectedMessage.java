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
package com.vonage.client.sms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RejectedMessage {
    private String accountId;
    private String from;
    private String to;
    private Date dateReceived;
    private Integer errorCode;
    private String errorCodeLabel;

    @JsonProperty("account-id")
    public String getAccountId() {
        return accountId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @JsonProperty("date-received")
    public Date getDateReceived() {
        return dateReceived;
    }

    @JsonProperty("error-code")
    public Integer getErrorCode() {
        return errorCode;
    }

    @JsonProperty("error-code-label")
    public String getErrorCodeLabel() {
        return errorCodeLabel;
    }
}
