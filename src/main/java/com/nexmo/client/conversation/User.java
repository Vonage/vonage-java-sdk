package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

public class User {

    private String id;
    private String name;
    private String display_name;
    private String image_url;
    private PageLinks links;
    private Object custom_data;

    public User(){}

    public User(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.display_name = builder.display_name;
        this.image_url = builder.image_url;
        this.custom_data = builder.custom_data;
    }

    @JsonProperty("_links")
    public PageLinks getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(PageLinks links) {
        this.links = links;
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

    @JsonProperty("custom_data")
    public Object getCustom_data() {
        return custom_data;
    }

    public void setCustom_data(Object custom_data) {
        this.custom_data = custom_data;
    }

    public static User fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, User.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from User object.", jpe);
        }
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from User object.", jpe);
        }
    }

    public static Builder builder(){return new Builder();}

    public static class Builder{
        private String id;
        private String name;
        private String display_name;
        private String image_url;
        private Object custom_data;

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder display_name(String displayName){
            this.display_name = displayName;
            return this;
        }

        public Builder image_url(String image_url){
            this.image_url = image_url;
            return this;
        }

        public Builder custom_data(Object customData){
            this.custom_data = customData;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
