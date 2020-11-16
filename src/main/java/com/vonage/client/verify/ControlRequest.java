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

import org.apache.http.client.methods.RequestBuilder;

public class ControlRequest {
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

    public void addParams(RequestBuilder request) {
        request
                .addParameter("request_id", getRequestId())
                .addParameter("cmd", getCommand().toString());
    }

}
