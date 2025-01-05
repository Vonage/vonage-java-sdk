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
     * Describes the media type for this event.
     *
     * @return The message type as an enum.
     */
    @JsonIgnore
    public MessageType getMessageType() {
        return body.messageType;
    }

    /**
     * If {@linkplain #getMessageType()} is {@linkplain MessageType#TEXT}, returns the text.
     *
     * @return The message text, or {@code null} if not applicable.
     */
    @JsonIgnore
    public String getText() {
        return body.text;
    }

    /**
     * If {@linkplain #getMessageType()} is multimedia, returns the URL of the media.
     *
     * @return The absolute media URL, or {@code null} if not applicable.
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
     * If {@linkplain #getMessageType()} is {@linkplain MessageType#LOCATION}, returns the location.
     *
     * @return The location details, or {@code null} if not applicable.
     */
    @JsonIgnore
    public Location getLocation() {
        return body.location;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param messageType The type of message for this event.
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
         * Sets the message text, if the type is {@linkplain MessageType#TEXT}.
         *
         * @param text The message text.
         *
         * @return This builder.
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        /**
         * Sets the URL, if appropriate for the type.
         *
         * @param url The absolute media URL as a string.
         *
         * @return This builder.
         */
        public Builder url(String url) {
            this.url = URI.create(url);
            return this;
        }

        /**
         * Sets the message location, if the type is {@linkplain MessageType#LOCATION}.
         *
         * @param location The location details.
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
