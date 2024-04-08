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

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.MessageType;
import java.net.URI;
import java.util.Objects;

/**
 * Main body container for {@link MessageEvent}.
 */
class MessageEventBody extends JsonableBaseObject {
    @JsonProperty("message_type") MessageType messageType;
    @JsonProperty("text") String text;
    @JsonProperty("image") UrlContainer image;
    @JsonProperty("audio") UrlContainer audio;
    @JsonProperty("video") UrlContainer video;
    @JsonProperty("file") UrlContainer file;
    @JsonProperty("vcard") UrlContainer vcard;
    @JsonProperty("location") Location location;

    static class UrlContainer extends JsonableBaseObject {
        @JsonProperty("url") URI url;
    }

    MessageEventBody() {}

    MessageEventBody(MessageEvent.Builder builder) {
        messageType = Objects.requireNonNull(builder.messageType, "Message type is required.");
        if ((text = builder.text) != null && messageType != MessageType.TEXT) {
            throw new IllegalStateException("Text is not applicable to '"+messageType+"'.");
        }
        if ((location = builder.location) != null && messageType != MessageType.LOCATION) {
            throw new IllegalStateException("Location is not applicable to '"+messageType+"'.");
        }
        if (builder.url != null) {
            UrlContainer urlRef = new UrlContainer();
            urlRef.url = builder.url;
            switch (messageType) {
                default: throw new IllegalStateException(
                        "URL is not applicable for '"+messageType+"'."
                );
                case IMAGE: image = urlRef; break;
                case AUDIO: audio = urlRef; break;
                case VIDEO: video = urlRef; break;
                case FILE: file = urlRef; break;
                case VCARD: vcard = urlRef; break;
            }
        }
    }
}
