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
import com.vonage.client.messages.InboundMessage;
import com.vonage.client.messages.MessageType;
import java.net.URI;
import java.util.Objects;

/**
 * Represents MMS attachments in {@link InboundMessage#getContent()} and {@link MmsFileRequest}.
 *
 * @since 8.11.0
 */
public class Content extends JsonableBaseObject {
    private MessageType type;
    private URI url;
    private String caption;

    protected Content() {}

    /**
     * Creates a new Content object.
     *
     * @param type    The type of attachment.
     * @param url     URL of the attachment.
     * @param caption Additional text to accompany the attachment.
     *
     * @since 8.18.0
     */
    public Content(MessageType type, URI url, String caption) {
        this.type = Objects.requireNonNull(type, "Media type is required.");
        this.url = Objects.requireNonNull(url, "URL is required.");
        this.caption = caption;
    }

    /**
     * Creates a new Content object.
     *
     * @param type    The type of attachment.
     * @param url     URL of the attachment.
     * @param caption Additional text to accompany the attachment.
     *
     * @since 8.18.0
     */
    public Content(MessageType type, String url, String caption) {
        this(type, URI.create(Objects.requireNonNull(url, "URL is required.")), caption);
    }

    /**
     * Creates a new Content object.
     *
     * @param type    The type of attachment.
     * @param url     URL of the attachment.
     *
     * @since 8.18.0
     */
    public Content(MessageType type, URI url) {
        this(type, url, null);
    }

    /**
     * Creates a new Content object.
     *
     * @param type    The type of attachment.
     * @param url     URL of the attachment.
     *
     * @since 8.18.0
     */
    public Content(MessageType type, String url) {
        this(type, url, null);
    }

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

    /**
     * Additional text to accompany the attachment.
     *
     * @return The attachment caption, or {@code null} if unknown.
     */
    @JsonProperty("caption")
    public String getCaption() {
        return caption;
    }
}
