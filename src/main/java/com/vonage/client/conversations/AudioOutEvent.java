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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class Body extends JsonableBaseObject {
        @JsonProperty("queue") Boolean queue;
        @JsonProperty("level") Double level;
        @JsonProperty("loop") Integer loop;

        Body() {}

        Body(Builder<?, ?> builder) {
            queue = builder.queue;
            level = builder.level;
            loop = builder.loop;
        }
    }

    /**
     *
     *
     * @return
     */
    @JsonIgnore
    public Boolean getQueue() {
        return body != null ? body.queue : null;
    }

    /**
     *
     *
     * @return
     */
    @JsonIgnore
    public Double getLevel() {
        return body != null ? body.level : null;
    }

    /**
     *
     *
     * @return
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
         *
         *
         * @param queue
         *
         * @return This builder.
         */
        public B queue(boolean queue) {
            this.queue = queue;
            return (B) this;
        }

        /**
         *
         *
         * @param level
         *
         * @return This builder.
         */
        public B level(double level) {
            this.level = level;
            return (B) this;
        }

        /**
         *
         *
         * @param loop
         *
         * @return This builder.
         */
        public B loop(int loop) {
            this.loop = loop;
            return (B) this;
        }
    }
}
