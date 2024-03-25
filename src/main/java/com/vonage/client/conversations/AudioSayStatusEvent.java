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
package com.vonage.client.conversations;

import java.util.UUID;

abstract class AudioSayStatusEvent extends EventWithBody<AudioSayEventBody> {

    AudioSayStatusEvent() {}

    AudioSayStatusEvent(Builder<?, ?> builder) {
        super(builder);
        body = new AudioSayEventBody(builder.sayId);
    }

    /**
     * Unique audio say identifier.
     *
     * @return The say ID, or {@code null} if unknown.
     */
    public UUID getSayId() {
        return body != null ? body.sayId : null;
    }


    @SuppressWarnings("unchecked")
    static abstract class Builder<E extends AudioSayStatusEvent,
            B extends AudioSayStatusEvent.Builder<? extends E, ? extends  B>>
            extends EventWithBody.Builder<AudioSayStatusEvent, AudioSayStatusEvent.Builder<E, B>> {

        UUID sayId;

        Builder(EventType type) {
            super(type);
        }

        /**
         * Unique audio say identifier.
         *
         * @param sayId The say ID as a string.
         *
         * @return This builder.
         */
        public B sayId(String sayId) {
            return sayId(UUID.fromString(sayId));
        }

        /**
         * Unique audio say identifier.
         *
         * @param sayId The say ID
         *
         * @return This builder.
         */
        public B sayId(UUID sayId) {
            this.sayId = sayId;
            return (B) this;
        }
    }
}
