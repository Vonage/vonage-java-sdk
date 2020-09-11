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
package com.vonage.client.incoming;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public enum CallStatus {
    STARTED,
    RINGING,
    ANSWERED,
    COMPLETED,
    MACHINE,
    HUMAN,
    INPUT,
    BUSY,
    CANCELLED,
    FAILED,
    RECORDING,
    REJECTED,
    TIMEOUT,
    UNANSWERED,
    UNKNOWN;

    private static final Map<String, CallStatus> CALL_STATUS_INDEX = new HashMap<>();

    static {
        for (CallStatus callStatus : CallStatus.values()) {
            CALL_STATUS_INDEX.put(callStatus.name(), callStatus);
        }
    }

    @JsonCreator
    public static CallStatus fromString(String status) {
        CallStatus foundCallStatus = CALL_STATUS_INDEX.get(status.toUpperCase());
        return (foundCallStatus != null) ? foundCallStatus : UNKNOWN;
    }
}
