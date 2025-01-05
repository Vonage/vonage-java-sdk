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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

abstract class AudioPlayStatusEvent extends EventWithBody<AudioPlayEventBody> {

    AudioPlayStatusEvent() {}

    AudioPlayStatusEvent(Builder<?, ?> builder) {
        super(builder);
        body = new AudioPlayEventBody(builder.playId);
    }

    /**
     * Unique audio play identifier.
     *
     * @return The play ID, or {@code null} if unknown.
     */
    @JsonIgnore
    public UUID getPlayId() {
        return body != null ? body.playId : null;
    }


    @SuppressWarnings("unchecked")
    static abstract class Builder<E extends AudioPlayStatusEvent,
            B extends AudioPlayStatusEvent.Builder<? extends E, ? extends  B>>
            extends EventWithBody.Builder<E, B> {

        UUID playId;

        Builder(EventType type) {
            super(type);
        }

        /**
         * Unique audio play identifier.
         *
         * @param playId Unique audio play identifier as a string.
         *
         * @return This builder.
         */
        public B playId(String playId) {
            return playId(UUID.fromString(playId));
        }

        /**
         * Unique audio play identifier.
         *
         * @param playId Unique audio play identifier.
         *
         * @return This builder.
         */
        public B playId(UUID playId) {
            this.playId = playId;
            return (B) this;
        }
    }
}
