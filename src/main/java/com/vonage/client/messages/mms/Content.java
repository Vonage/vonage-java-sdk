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
package com.vonage.client.messages.mms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.messages.MessageType;
import java.net.URI;

/**
 * Represents file attachments in {@link com.vonage.client.messages.InboundMessage#getContent()}.
 *
 * @since 8.11.0
 */
public class Content extends JsonableBaseObject {
    private MessageType type;
    private URI url;

    protected Content() {}

    /**
     * The type of attachment. Either {@linkplain MessageType#AUDIO},{@linkplain MessageType#VIDEO},
     * {@linkplain MessageType#FILE}, {@linkplain MessageType#IMAGE} or {@linkplain MessageType#VCARD}.
     *
     * @return The attachment type, or {@code null} if unknown.
     */
    @JsonProperty("type")
    public MessageType getType() {
        return type;
    }

    /**
     * URL of the attachment.
     *
     * @return The attachment URL, or {@code null} if unknown.
     */
    @JsonProperty("url")
    public URI getUrl() {
        return url;
    }
}
