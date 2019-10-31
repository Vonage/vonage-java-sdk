package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

public class Channel {
    private String type;

    public Channel(){}
    public Channel(String type) {
        this.type = type;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public static Channel fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Channel.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from Channel object.", jpe);
        }
    }
}
