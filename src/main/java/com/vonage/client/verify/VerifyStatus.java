/*
 *   Copyright 2022 Vonage
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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonDeserialize(using = VerifyStatusDeserializer.class)
public enum VerifyStatus {
    OK(0),
    THROTTLED(1),
    MISSING_PARAMS(2),
    INVALID_PARAMS(3),
    INVALID_CREDENTIALS(4),
    INTERNAL_ERROR(5),
    INVALID_REQUEST(6),
    NUMBER_BARRED(7),
    PARTNER_ACCOUNT_BARRED(8),
    PARTNER_QUOTA_EXCEEDED(9),
    ALREADY_REQUESTED(10),
    UNSUPPORTED_NETWORK(15),
    INVALID_CODE(16),
    WRONG_CODE_THROTTLED(17),
    TOO_MANY_DESTINATIONS(18),
    NO_RESPONSE(101),
    COMMS_FAILURE(-1),
    UNKNOWN(Integer.MAX_VALUE);

    private final int verifyStatus;

    private static final Map<Integer, VerifyStatus> VERIFY_STATUS_INDEX =
        Arrays.stream(VerifyStatus.values()).collect(Collectors.toMap(
                VerifyStatus::getVerifyStatus, Function.identity()
        ));

    /**
     * Look up the {@link VerifyStatus} based on the int value.
     *
     * @param verifyStatus the int value of the verify status.
     *
     * @return VerifyStatus based on the int value given.
     */
    public static VerifyStatus fromInt(int verifyStatus) {
        return VERIFY_STATUS_INDEX.getOrDefault(verifyStatus, UNKNOWN);
    }

    VerifyStatus(int verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public int getVerifyStatus() {
        return verifyStatus;
    }

    public boolean isTemporaryError() {
        return this == THROTTLED || this == INTERNAL_ERROR;
    }
}
