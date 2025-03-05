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
 * Base class for events with the {@code channel} field.
 *
 * @since 8.19.0
 */
abstract class AbstractChannelEvent<B extends AbstractChannelEvent.Body> extends EventWithBody<B> {
    AbstractChannelEvent() {}

    @SuppressWarnings("unchecked")
    AbstractChannelEvent(Builder<?, ?, ?> builder) {
        super((Event.Builder<? extends EventWithBody<? extends B>, ?>) builder);
    }

    /**
     * The main body container for events with a {@linkplain MemberChannel}.
     */
    static class Body extends JsonableBaseObject {
        @JsonProperty("channel") MemberChannel channel;

        Body() {}

        Body(Builder<?, ?, ?> builder) {
            channel = builder.channel;
        }
    }

    /**
     * The {@code channel} field.
     *
     * @return The channel object, or {@code null} if absent.
     */
    @JsonIgnore
    public MemberChannel getChannel() {
        return body != null ? body.channel : null;
    }

    @SuppressWarnings("unchecked")
    static abstract class Builder<E extends AbstractChannelEvent<? extends T>, T extends AbstractChannelEvent.Body, B extends Builder<? extends E, ? extends T, ? extends B>> extends EventWithBody.Builder<E, B> {
        private MemberChannel channel;

        Builder(EventType type) {
            super(type);
        }

        /**
         * Sets the channel for the event.
         *
         * @param channel The channel for the event.
         *
         * @return This builder.
         */
        public B channel(MemberChannel channel) {
            this.channel = channel;
            return (B) this;
        }
    }
}
