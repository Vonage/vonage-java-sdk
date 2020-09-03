/*
 * Copyright (c) 2020 Vonage
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
package com.nexmo.client.sms;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public enum MessageStatus {
    OK(0),
    THROTTLED(1),
    MISSING_PARAMS(2),
    INVALID_PARAMS(3),
    INVALID_CREDENTIALS(4),
    INTERNAL_ERROR(5),
    INVALID_MESSAGE(6),
    NUMBER_BARRED(7),
    PARTNER_ACCOUNT_BARRED(8),
    PARTNER_QUOTA_EXCEEDED(9),
    TOO_MANY_BINDS(10),
    ACCOUNT_NOT_HTTP(11),
    MESSAGE_TOO_LONG(12),
    COMMS_FAILURE(13),
    INVALID_SIGNATURE(14),
    INVALID_FROM_ADDRESS(15),
    INVALID_TTL(16),
    NUMBER_UNREACHABLE(17),
    TOO_MANY_DESTINATIONS(18),
    FACILITY_NOT_ALLOWED(19), INVALID_MESSAGE_CLASS(20), UNKNOWN(Integer.MAX_VALUE);

    private int messageStatus;

    private static final Map<Integer, MessageStatus> MESSAGE_STATUS_INDEX = new HashMap<>();

    static {
        for (MessageStatus messageStatus : MessageStatus.values()) {
            MESSAGE_STATUS_INDEX.put(messageStatus.messageStatus, messageStatus);
        }
    }

    /**
     * Look up the {@link MessageStatus} based on the int value.
     *
     * @param messageStatus the int value of the message status.
     *
     * @return MessageStatus based on the int value given.
     */
    @JsonCreator
    public static MessageStatus fromInt(int messageStatus) {
        MessageStatus foundStatus = MESSAGE_STATUS_INDEX.get(messageStatus);
        return (foundStatus != null) ? foundStatus : UNKNOWN;
    }

    MessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int getMessageStatus() {
        return this.messageStatus;
    }
}
