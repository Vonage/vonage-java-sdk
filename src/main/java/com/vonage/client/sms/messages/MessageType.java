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

/**
 * Represents the type of message.
 *
 * @since 9.0.0 Moved from {@linkplain Message} to its own file.
 */
public enum MessageType {
    /**
     * Regular text SMS message.
     */
    TEXT,

    /**
     * Binary SMS message with a custom UDH and binary payload.
     */
    BINARY,

    /**
     * Unicode message, for sending messages in non-latin script to a supported handset.
     */
    UNICODE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
