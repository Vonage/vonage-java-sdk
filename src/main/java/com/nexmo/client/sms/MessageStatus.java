/*
 * Copyright (c) 2011-2017 Nexmo Inc
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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

@JsonDeserialize(using = MessageStatusDeserializer.class)
public enum MessageStatus {
    STATUS_OK(0),
    STATUS_THROTTLED(1),
    STATUS_MISSING_PARAMS(2),
    STATUS_INVALID_PARAMS(3),
    STATUS_INVALID_CREDENTIALS(4),
    STATUS_INTERNAL_ERROR(5),
    STATUS_INVALID_MESSAGE(6),
    STATUS_NUMBER_BARRED(7),
    STATUS_PARTNER_ACCOUNT_BARRED(8),
    STATUS_PARTNER_QUOTA_EXCEEDED(9),
    STATUS_TOO_MANY_BINDS(10),
    STATUS_ACCOUNT_NOT_HTTP(11),
    STATUS_MESSAGE_TOO_LONG(12),
    STATUS_COMMS_FAILURE(13),
    STATUS_INVALID_SIGNATURE(14),
    STATUS_INVALID_FROM_ADDRESS(15),
    STATUS_INVALID_TTL(16),
    STATUS_NUMBER_UNREACHABLE(17),
    STATUS_TOO_MANY_DESTINATIONS(18),
    STATUS_FACILITY_NOT_ALLOWED(19),
    STATUS_INVALID_MESSAGE_CLASS(20);

    private int messageStatus;

    private static Map<Integer, MessageStatus> integerStatusValues = new HashMap<>();

    static {
        for(MessageStatus messageStatus : MessageStatus.values()) {
            integerStatusValues.put(messageStatus.messageStatus, messageStatus);
        }
    }

    /**
     * Look up the {@link MessageStatus} based on the int value.
     *
     * @param messageStatus the int value of the message status.
     * @return MessageStatus based on the int value given.
     */
    public static MessageStatus fromInt(int messageStatus) {
        return integerStatusValues.get(messageStatus);
    }

    MessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int getMessageStatus() {
        return this.messageStatus;
    }
}
