package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

public class UpdateMemberRequest {

    private String conversation_id;
    private String member_id;
    private String state;
    private Channel channel;

    public UpdateMemberRequest(Builder builder){
        this.conversation_id = builder.conversation_id;
        this.member_id = builder.member_id;
        this.state = builder.state;
        this.channel = builder.channel;
    }
    @JsonIgnore
    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    @JsonIgnore
    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("channel")
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public static Builder builder(){
        return new Builder();
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from User object.", jpe);
        }
    }


    public static class Builder{
        private String conversation_id;
        private String member_id;
        private String state;
        private Channel channel;

        public Builder conversation_id(String conversation_id){
            this.conversation_id = conversation_id;
            return this;
        }

        public Builder member_id(String member_id){
            this.member_id = member_id;
            return this;
        }

        public Builder state(String state){
            this.state = state;
            return this;
        }

        public Builder channel(Channel channel){
            this.channel = channel;
            return this;
        }

        public UpdateMemberRequest build(){
            return new UpdateMemberRequest(this);
        }
    }

}
