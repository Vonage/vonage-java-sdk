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


import java.util.Map;


public class SnsPublishRequest extends SnsRequest {
    private String from;
    private String message;

    public SnsPublishRequest(final String to,
                             final String topicArn,
                             final String from,
                             final String message) throws Exception {
        super("publish", to, topicArn);
        this.from = from;
        this.message = message;
    }

    @Override
    public Map<String, String> getQueryParameters() {
        Map<String, String> params = super.getQueryParameters();
        params.put("from", from);
        params.put("message", message);
        return params;
    }
}
