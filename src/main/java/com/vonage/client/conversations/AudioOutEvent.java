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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Groups common audio controls together.
 */
abstract class AudioOutEvent<T extends AudioOutEvent.Body> extends EventWithBody<T> {

    AudioOutEvent() {}

    AudioOutEvent(Builder<? extends AudioOutEvent<? extends T>, ?> builder) {
        super(builder);
    }

    public static class Body extends JsonableBaseObject {
        @JsonProperty("queue") Boolean queue;
        @JsonProperty("level") Double level;
        @JsonProperty("loop") Integer loop;

        Body() {}

        Body(Builder<?, ?> builder) {
            queue = builder.queue;
            if ((level = builder.level) != null && (level > 1.0 || level < -1.0)) {
                throw new IllegalArgumentException("Level must be between -1 and 1.");
            }
            if ((loop = builder.loop) != null && loop < 0) {
                throw new IllegalArgumentException("Loop cannot be negative.");
            }
        }
    }

    /**
     * Whether to queue the audio.
     *
     * @return {@code true} if queuing is enabled, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Boolean getQueue() {
        return body != null ? body.queue : null;
    }

    /**
     * Audio volume level, with -1 being quietest, +1 being loudest and 0 the default.
     *
     * @return The volume as a Double, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Double getLevel() {
        return body != null ? body.level : null;
    }

    /**
     * Number of times to repeat the audio.
     *
     * @return The loop count as an Integer, or {@code null} if unspecified.
     */
    @JsonIgnore
    public Integer getLoop() {
        return body != null ? body.loop : null;
    }

    @SuppressWarnings("unchecked")
    static abstract class Builder<E extends AudioOutEvent<?>, B extends Builder<? extends E, ? extends B>>
            extends EventWithBody.Builder<E, B> {

        Builder(EventType type) {
            super(type);
        }

        Boolean queue;
        Double level;
        Integer loop;

        /**
         * Whether to queue the audio.
         *
         * @param queue {@code true} to enable queuing, {@code false} otherwise.
         *
         * @return This builder.
         */
        public B queue(boolean queue) {
            this.queue = queue;
            return (B) this;
        }

        /**
         * Audio volume level, with -1 being quietest, +1 being loudest and 0 the default.
         *
         * @param level The volume as a double.
         *
         * @return This builder.
         */
        public B level(double level) {
            this.level = level;
            return (B) this;
        }

        /**
         * Number of times to repeat the audio. Default is 1.
         *
         * @param loop The loop count as an int.
         *
         * @return This builder.
         */
        public B loop(int loop) {
            this.loop = loop;
            return (B) this;
        }
    }
}
