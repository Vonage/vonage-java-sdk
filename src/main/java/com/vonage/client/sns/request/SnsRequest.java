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
package com.vonage.client.sns.request;


import java.util.HashMap;
import java.util.Map;


public abstract class SnsRequest {
    private final String command;
    private String to;
    private String topicArn;

    public SnsRequest(final String command,
                      final String to,
                      final String topicArn) {
        this.command = command;
        this.to = to;
        this.topicArn = topicArn;
    }

    public String getCommand() {
        return command;
    }

    public Map<String, String> getQueryParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("cmd", getCommand());
        params.put("to", to);
        params.put("topic", topicArn);
        return params;
    }
}
