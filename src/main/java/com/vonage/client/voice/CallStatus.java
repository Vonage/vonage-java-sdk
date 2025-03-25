/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.Jsonable;

/**
 * Describes the status of the call, and also the event in {@link EventWebhook#getStatus()}.
 */
public enum CallStatus {
    /**
     * Indicates that the call has been created.
     */
    STARTED,

    /**
     * The call is ringing.
     */
    RINGING,

    /**
     * The call was answered.
     */
    ANSWERED,

    /**
     * The duration of the ringing phase exceeded the specified ringing timeout duration.
     */
    TIMEOUT,

    /**
     * For an outbound call made programmatically with machine detection enabled,
     * this indicates a machine / voicemail answered the call.
     */
    MACHINE,

    /**
     * The call has finished.
     */
    COMPLETED,

    /**
     * The outgoing call could not be connected.
     */
    FAILED,

    /**
     * The call was rejected by Vonage before it was connected.
     */
    REJECTED,

    /**
     * The destination is on the line with another caller.
     */
    BUSY,

    /**
     * An outgoing call is cancelled by the originator before being answered.
     */
    CANCELLED,

    /**
     * A leg has been transferred from one conversation to another.
     */
    TRANSFER,

    /**
     * An NCCO {@code input} action has finished.
     */
    INPUT,

    /**
     * For an outbound call made programmatically with machine detection enabled,
     * this indicates a human answered the call.
     */
    HUMAN,

    /**
     * If the WebSocket connection is terminated from the application side for any reason,
     * then the disconnected event callback will be sent. If the response contains an NCCO
     * then this will be processed, if no NCCO is present then normal execution will continue.
     */
    DISCONNECTED,

    /**
     * Either the recipient is unreachable or the recipient declined the call.
     */
    UNANSWERED;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * Convert a string value to a CallStatus enum.
     *
     * @param name The string value to convert.
     *
     * @return The status as an enum, or {@code null} if invalid.
     */
    @JsonCreator
    public static CallStatus fromString(String name) {
       return Jsonable.fromString(name, CallStatus.class);
    }
}
