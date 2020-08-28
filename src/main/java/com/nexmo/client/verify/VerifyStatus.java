/*
 * Copyright (c) 2011-2018 Vonage Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.verify;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

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
    NO_RESPONSE(101), COMMS_FAILURE(-1), UNKNOWN(Integer.MAX_VALUE);

    private int verifyStatus;

    private static final Map<Integer, VerifyStatus> VERIFY_STATUS_INDEX = new HashMap<>();

    static {
        for (VerifyStatus verifyStatus : VerifyStatus.values()) {
            VERIFY_STATUS_INDEX.put(verifyStatus.verifyStatus, verifyStatus);
        }
    }

    /**
     * Look up the {@link VerifyStatus} based on the int value.
     *
     * @param verifyStatus the int value of the verify status.
     *
     * @return VerifyStatus based on the int value given.
     */
    public static VerifyStatus fromInt(int verifyStatus) {
        VerifyStatus foundVerifyStatus = VERIFY_STATUS_INDEX.get(verifyStatus);
        return (foundVerifyStatus != null) ? foundVerifyStatus : UNKNOWN;
    }

    VerifyStatus(int verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public int getVerifyStatus() {
        return this.verifyStatus;
    }

    public boolean isTemporaryError() {
        return this == THROTTLED || this == INTERNAL_ERROR;
    }
}
