package com.nexmo.client.conversation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.conversation.Timestamp;
import com.nexmo.client.voice.Call;

import java.io.IOException;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Conversation {
    private String id;
    private String name;
    private String display_name;
    private String image_url;
    private Timestamp timestamp;
    private Object properties;
    private PageLinks link;

    public Conversation(){}

    public Conversation(Builder builder)
    {
        this.name = builder.name;
        this.display_name = builder.display_name;
        this.properties = builder.properties;
        this.image_url = builder.image_url;
    }

    @JsonProperty("_links")
    public PageLinks getLink() {
        return link;
    }

    @JsonProperty("_links")
    public void setLink(PageLinks link) {
        this.link = link;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("display_name")
    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    @JsonProperty("image_url")
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @JsonProperty("timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("properties")
    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from Call object.", jpe);
        }
    }

    public static Conversation fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Conversation.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from Conversation object.", jpe);
        }
    }
    public static Builder builder(){return new Builder();}
    public static class Builder{
        private String name;
        private String display_name;
        private String image_url;
        private Object properties;

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder displayName(String displayName)
        {
            this.display_name = displayName;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            this.image_url = imageUrl;
            return this;
        }

        public Builder properties(Object properties){
            this.properties = properties;
            return this;
        }

        public Conversation build(){
            return new Conversation(this);
        }
    }
}
