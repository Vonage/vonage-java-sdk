package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CreateMemberRequest {
    private String user_name;
    private String user_id;
    private Channel channel;
    private String action;
    private String conversation_id;

    public CreateMemberRequest(Builder builder){
        this.user_id=builder.user_id;
        this.channel=builder.channel;
        this.action=builder.action;
        this.conversation_id = builder.conversation_id;
        this.user_name = builder.user_name;
    }

    @JsonProperty("user_name")
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @JsonIgnore
    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    @JsonProperty("user_id")
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @JsonProperty("channel")
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String ret = mapper.writeValueAsString(this);
            return ret;
        } catch (JsonProcessingException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from Call object.", jpe);
        }
    }

    public static Builder builder(){return new Builder();}

    public static class Builder{
        private String conversation_id;
        private String user_id;
        private Channel channel;
        private String action;
        private String user_name;

        public Builder conversation_id(String conversation_id){
            this.conversation_id = conversation_id;
            return this;
        }

        public Builder user_id(String user_id){
            this.user_id = user_id;
            return this;
        }

        public Builder channel(Channel channel){
            this.channel = channel;
            return this;
        }

        public Builder action(String action){
            this.action = action;
            return this;
        }

        public Builder user_name(String user_name){
            this.user_name=user_name;
            return this;
        }

        public CreateMemberRequest build(){
            return new CreateMemberRequest(this);
        }

    }
}
