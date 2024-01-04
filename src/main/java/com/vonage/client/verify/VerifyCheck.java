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
package com.vonage.client.verify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyCheck {
    private final Date date;
    private final String code;
    private final Status status;

    public VerifyCheck(@JsonProperty("date_received") Date date,
                       @JsonProperty("code") String code,
                       @JsonProperty("status") Status status) {
        this.date = date;
        this.code = code;
        this.status = status;
    }

    public enum Status {
        VALID, INVALID
    }

    /**
     * @return The date and time this check was received
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * @return The code supplied with this check request.
     */
    public String getCode() {
        return this.code;
    }

    public Status getStatus() {
        return this.status;
    }
}
