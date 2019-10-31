package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

public class Media {
    private AudioSettings audio_settings;

    @JsonProperty("audio_settings")
    public AudioSettings getAudio_settings() {
        return audio_settings;
    }

    public void setAudio_settings(AudioSettings audio_settings) {
        this.audio_settings = audio_settings;
    }

    public static Media fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Media.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from Media object.", jpe);
        }
    }
}
