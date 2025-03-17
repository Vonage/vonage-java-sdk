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
package com.vonage.client.verify;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

public class ControlResponse extends JsonableBaseObject {
    private String status;
    private VerifyControlCommand command;
    private String errorText;

    private ControlResponse() {
    }

    public ControlResponse(String status, VerifyControlCommand command) {
        this.status = status;
        this.command = command;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("command")
    public VerifyControlCommand getCommand() {
        return command;
    }

    /**
     * If the status is non-zero, this explains the error encountered.
     *
     * @return The error description, or {@code null} if there was no error.
     */
    @JsonProperty("error_text")
    public String getErrorText() {
        return errorText;
    }
}
