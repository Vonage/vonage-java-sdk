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
package com.vonage.client.messages;

import com.vonage.client.common.MessageType;

/**
 * Convenience interface to indicate that {@link MessageRequest#getMessageType()} is {@link MessageType#TEXT}.
 *
 * @since 8.11.0
 */
public interface TextMessageRequest {

    /**
     * Get the {@code text} field of the message.
     *
     * @return The message text as set on the builder.
     */
    String getText();

    /**
     * Interface for builders where {@linkplain MessageRequest#getMessageType()} is {@link MessageType#TEXT}.
     * @param <B> The concrete builder type.
     */
    interface Builder<B extends MessageRequest.Builder<? extends TextMessageRequest, ? extends B>> {

        /**
         * Sets the text of the message request.
         *
         * @param text The message text.
         * @return This builder.
         */
        B text(String text);
    }
}
