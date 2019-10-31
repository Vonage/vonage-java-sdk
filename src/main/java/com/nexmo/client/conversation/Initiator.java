package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

public class Initiator {
    private InitiatorType invited;
    private InitiatorType joined;

    @JsonProperty("invited")
    public InitiatorType getInvited() {
        return invited;
    }

    public void setInvited(InitiatorType invited) {
        this.invited = invited;
    }

    @JsonProperty("joined")
    public InitiatorType getJoined() {
        return joined;
    }

    public void setJoined(InitiatorType joined) {
        this.joined = joined;
    }

    public static Initiator fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Initiator.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from Initiator object.", jpe);
        }
    }
}
