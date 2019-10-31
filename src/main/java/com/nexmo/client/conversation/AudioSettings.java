package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

public class AudioSettings {
    private boolean enabled;
    private boolean earmuffed;
    private boolean muted;

    @JsonProperty("enabled")
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty("earmuffed")
    public boolean isEarmuffed() {
        return earmuffed;
    }

    public void setEarmuffed(boolean earmuffed) {
        this.earmuffed = earmuffed;
    }

    @JsonProperty("muted")
    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public static AudioSettings fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, AudioSettings.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from AudioSettings object.", jpe);
        }
    }
}
