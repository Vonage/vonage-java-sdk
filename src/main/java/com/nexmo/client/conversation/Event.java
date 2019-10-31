package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

public class Event {
    private Object body;
    private String type;
    private String from;
    private String to;
    private String conversation_id;
    private String id;
    private String timestamp;
    private PageLinks links;

    public Event(){

    }

    Event(Builder builder){
        this.body=builder.body;
        this.type = builder.type;
        this.from = builder.from;
        this.to = builder.to;
        this.conversation_id = builder.conversation_id;
    }

    @JsonProperty("_links")
    public PageLinks getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(PageLinks links) {
        this.links = links;
    }

    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    @JsonProperty("to")
    public void setTo(String to) {
        this.to = to;
    }

    @JsonProperty("body")
    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("from")
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static Event fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Event.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from Event object.", jpe);
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
        private Object body;
        private String type;
        private String from;
        private String to;
        private String conversation_id;


        public Builder conversation_id(String conversation_id){
            this.conversation_id = conversation_id;
            return this;
        }
        public Builder body(Object body){
            this.body = body;
            return this;
        }

        public Builder type(String type){
            this.type = type;
            return this;
        }

        public Builder from(String from){
            this.from = from;
            return this;
        }

        public Builder to(String to){
            this.to = to;
            return this;
        }

        public Event build(){
            return new Event(this);
        }
    }
}
