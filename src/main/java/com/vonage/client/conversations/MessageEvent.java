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
import com.vonage.client.common.MessageType;
import java.net.URI;

/**
 * Represents a {@link EventType#MESSAGE} event. All possible fields are presented and accessible,
 * but only those applicable to the message type will be populated. Use {@linkplain #getMessageType()}
 * to determine the type of message, and query the other fields accordingly.
 */
public final class MessageEvent extends EventWithBody<MessageEventBody> {

    MessageEvent() {}

    MessageEvent(Builder builder) {
        super(builder);
        body = new MessageEventBody(builder);
    }

    /**
     *
     *
     * @return
     */
    @JsonIgnore
    public MessageType getMessageType() {
        return body.messageType;
    }

    /**
     *
     *
     * @return
     */
    @JsonIgnore
    public String getText() {
        return body.text;
    }

    /**
     *
     *
     * @return
     */
    @JsonIgnore
    public URI getUrl() {
        switch (getMessageType()) {
            default: return null;
            case FILE: return body.file.url;
            case IMAGE: return body.image.url;
            case AUDIO: return body.audio.url;
            case VIDEO: return body.video.url;
            case VCARD: return body.vcard.url;
        }
    }

    /**
     *
     *
     * @return
     */
    @JsonIgnore
    public Location getLocation() {
        return body.location;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder(MessageType messageType) {
        return new Builder(messageType);
    }

    /**
     * Builder for configuring parameters of the event request.
     */
    public static final class Builder extends EventWithBody.Builder<MessageEvent, Builder> {
        final MessageType messageType;
        String text;
        URI url;
        Location location;

        Builder(MessageType messageType) {
            super(EventType.MESSAGE);
            this.messageType = messageType;
        }

        /**
         *
         *
         * @param text
         *
         * @return This builder.
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        /**
         *
         *
         * @param url
         *
         * @return This builder.
         */
        public Builder url(String url) {
            this.url = URI.create(url);
            return this;
        }

        /**
         *
         *
         * @param location
         *
         * @return This builder.
         */
        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        @Override
        public MessageEvent build() {
            return new MessageEvent(this);
        }
    }
}
