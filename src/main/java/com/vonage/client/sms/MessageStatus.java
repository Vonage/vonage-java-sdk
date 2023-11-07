/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.sms;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enum representing the error status of an SMS message. See
 * <a href=https://developer.vonage.com/api-errors/sms>the documentation</a> for more details.
 */
public enum MessageStatus {
    /**
     * A status of zero does not indicate that Vonage delivered your message. Instead, this
     * status indicates the absence of an error - i.e. the REST call succeeded.
     */
    OK(0),

    /**
     * You are sending SMS faster than the account limit.
     */
    THROTTLED(1),

    /**
     * Your request is missing one of the required parameters from, to, api_key, api_secret or text.
     */
    MISSING_PARAMS(2),

    /**
     * The value of one or more parameters is invalid.
     */
    INVALID_PARAMS(3),

    /**
     * Your API key and/or secret are incorrect, invalid or disabled.
     */
    INVALID_CREDENTIALS(4),

    /**
     * An error has occurred in the platform whilst processing this message.
     */
    INTERNAL_ERROR(5),

    /**
     * The platform was unable to process this message, for example, an un-recognized number prefix.
     */
    INVALID_MESSAGE(6),

    /**
     * The number you are trying to send messages to is blacklisted and may not receive them.
     */
    NUMBER_BARRED(7),

    /**
     * Your Vonage account has been suspended.
     */
    PARTNER_ACCOUNT_BARRED(8),

    /**
     * You do not have sufficient credit to send the message.
     */
    PARTNER_QUOTA_EXCEEDED(9),

    /**
     * The number of simultaneous connections to the platform exceeds your account allocation.
     */
    TOO_MANY_BINDS(10),

    /**
     * This account is not provisioned for the SMS API.
     */
    ACCOUNT_NOT_HTTP(11),

    /**
     * The message length exceeds the maximum allowed.
     */
    MESSAGE_TOO_LONG(12),

    /**
     * No longer used.
     */
    @Deprecated
    COMMS_FAILURE(13),

    /**
     * The signature supplied could not be verified.
     */
    INVALID_SIGNATURE(14),

    /**
     * You are using a non-authorized sender ID in the from field.
     */
    INVALID_FROM_ADDRESS(15),

    /**
     * No longer used.
     */
    @Deprecated
    INVALID_TTL(16),

    /**
     * No longer used.
     */
    @Deprecated
    NUMBER_UNREACHABLE(17),

    /**
     * No longer used.
     */
    @Deprecated
    TOO_MANY_DESTINATIONS(18),

    /**
     * No longer used.
     */
    @Deprecated
    FACILITY_NOT_ALLOWED(19),

    /**
     * No longer used.
     */
    @Deprecated
    INVALID_MESSAGE_CLASS(20),

    /**
     * The network code supplied was either not recognized, or does not match the country of the destination address.
     */
    INVALID_NETWORK_CODE(22),

    /**
     * The callback URL supplied was either too long or contained illegal characters.
     */
    INVALID_CALLBACK(23),

    /**
     * Your Vonage account is still in demo mode.
     * While in demo mode you must add target numbers to your whitelisted destination list.
     */
    NON_WHITELISTED_DESTINATION(29),

    /**
     * A signed request may not also present an api_secret.
     */
    SIGNATURE_API_SECRET_DISALLOWED(32),

    /**
     * The number you are trying to send messages to is de-activated and may not receive them.
     */
    NUMBER_DEACTIVATED(33),

    /**
     * Unknown status code.
     */
    UNKNOWN(Integer.MAX_VALUE);

    private final int messageStatus;

    private static final Map<Integer, MessageStatus> MESSAGE_STATUS_INDEX =
        Arrays.stream(MessageStatus.values()).collect(Collectors.toMap(
            MessageStatus::getMessageStatus, Function.identity()
        ));

    /**
     * Look up the MessageStatus based on the int value.
     *
     * @param messageStatus the int value of the message status.
     *
     * @return MessageStatus based on the int value given.
     */
    @JsonCreator
    public static MessageStatus fromInt(int messageStatus) {
        return MESSAGE_STATUS_INDEX.getOrDefault(messageStatus, UNKNOWN);
    }

    MessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int getMessageStatus() {
        return messageStatus;
    }
}
