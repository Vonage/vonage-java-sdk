/*
 *   Copyright 2024 Vonage
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

/**
 * Convenience interface to indicate that {@link MessageRequest#getMessageType()} is media (has a URL).
 *
 * @since 8.11.0
 */
public interface MediaMessageRequest {

    /**
     * Interface for builders with a media URL.
     * @param <B> The concrete builder type.
     */
    interface Builder<B extends MessageRequest.Builder<? extends MediaMessageRequest, ? extends B>> {

        /**
         * Sets the media URL of the message request.
         *
         * @param url The media URL as a string.
         * @return This builder.
         */
        B url(String url);
    }
}
