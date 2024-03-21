package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.MessageType;
import java.net.URI;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class MessageEventBody extends JsonableBaseObject {
    @JsonProperty("message_type") MessageType messageType;
    @JsonProperty("text") String text;
    @JsonProperty("image") UrlContainer image;
    @JsonProperty("audio") UrlContainer audio;
    @JsonProperty("video") UrlContainer video;
    @JsonProperty("file") UrlContainer file;
    @JsonProperty("vcard") UrlContainer vcard;
    @JsonProperty("location") Location location;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class UrlContainer extends JsonableBaseObject {
        @JsonProperty("url") URI url;
    }

    MessageEventBody() {}

    MessageEventBody(MessageEvent.Builder builder) {
        messageType = builder.messageType;
        text = builder.text;
        location = builder.location;
        if (builder.url != null) {
            UrlContainer urlRef = new UrlContainer();
            urlRef.url = builder.url;
            switch (messageType) {
                case IMAGE: image = urlRef; break;
                case AUDIO: audio = urlRef; break;
                case VIDEO: video = urlRef; break;
                case FILE: file = urlRef; break;
                case VCARD: vcard = urlRef; break;
            }
        }
    }
}
