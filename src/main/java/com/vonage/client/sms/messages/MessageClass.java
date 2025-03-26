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
package com.vonage.client.sms.messages;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * An enum of the valid values that may be supplied to as the message-class parameter of a rest submission.
 *
 * @since 9.0.0 Moved from {@linkplain Message} to its own file.
 */
public enum MessageClass {
    /**
     * Message Class 0
     */
    CLASS_0(0),

    /**
     * Message Class 1
     */
    CLASS_1(1),

    /**
     * Message Class 2
     */
    CLASS_2(2),

    /**
     * Message Class 3
     */
    CLASS_3(3);

    private final int messageClass;

    MessageClass(int messageClass) {
        this.messageClass = messageClass;
    }

    /**
     * Gets the message class as an integer.
     *
     * @return The message class number.
     */
    @JsonValue
    public int getMessageClass() {
        return messageClass;
    }
}
