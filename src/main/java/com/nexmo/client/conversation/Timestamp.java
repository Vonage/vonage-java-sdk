package com.nexmo.client.conversation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

public class Timestamp {
    private String created;
    private String invited;
    private String joined;
    private String left;

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("invited")
    public String getInvited() {
        return invited;
    }

    public void setInvited(String invited) {
        this.invited = invited;
    }

    @JsonProperty("joined")
    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    @JsonProperty("left")
    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public static Timestamp fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Timestamp.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from Timestamp object.", jpe);
        }
    }
}
