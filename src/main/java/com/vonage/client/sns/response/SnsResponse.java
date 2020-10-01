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
package com.vonage.client.sns.response;


public class SnsResponse {

    public static final int STATUS_OK = 0;
    public static final int STATUS_BAD_COMMAND = 1;
    public static final int STATUS_INTERNAL_ERROR = 2;
    public static final int STATUS_INVALID_ACCOUNT = 3;
    public static final int STATUS_MISSING_TOPIC = 4;
    public static final int STATUS_INVALID_OR_MISSING_MSISDN = 5;
    public static final int STATUS_INVALID_OR_MISSING_FROM = 6;
    public static final int STATUS_INVALID_OR_MISSING_MSG = 7;
    public static final int STATUS_TOPIC_NOT_FOUND = 8;
    public static final int STATUS_TOPIC_PERMISSION_FAILURE = 9;
    public static final int STATUS_COMMS_FAILURE = 13;

    private final String command;
    private final int resultCode;
    private final String resultMessage;

    public SnsResponse(final String command,
                       final int resultCode,
                       final String resultMessage) {
        this.command = command;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public String getCommand() {
        return command;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}
