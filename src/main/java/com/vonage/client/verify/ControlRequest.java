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

import com.vonage.client.AbstractQueryParamsRequest;
import java.util.Map;

public class ControlRequest extends AbstractQueryParamsRequest {
    private final String requestId;
    private final VerifyControlCommand command;

    public ControlRequest(String requestId, VerifyControlCommand command) {
        this.requestId = requestId;
        this.command = command;
    }

    public String getRequestId() {
        return requestId;
    }

    public VerifyControlCommand getCommand() {
        return command;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        conditionalAdd("request_id", requestId);
        conditionalAdd("cmd", command);
        return params;
    }
}
