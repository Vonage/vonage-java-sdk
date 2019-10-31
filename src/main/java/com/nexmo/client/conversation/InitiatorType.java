package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

public class InitiatorType {
    private boolean is_system;

    @JsonProperty("is_system")
    public boolean isIs_system() {
        return is_system;
    }

    public void setIs_system(boolean is_system) {
        this.is_system = is_system;
    }

    public static InitiatorType fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, InitiatorType.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from InitiatorType object.", jpe);
        }
    }
}
