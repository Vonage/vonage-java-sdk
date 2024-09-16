package com.vonage.client.messages.mms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.messages.MessageType;
import java.net.URI;

public class Content extends JsonableBaseObject {
    @JsonProperty("type")
    protected MessageType type;
    @JsonProperty("url")
    protected URI url;
}
