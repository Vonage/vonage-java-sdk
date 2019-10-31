package com.nexmo.client.conversation;

public class SpecificEventRequest {
    private String conversation_id;
    private String event_id;

    SpecificEventRequest(Builder builder){
        this.conversation_id = builder.conversation_id;
        this.event_id = builder.event_id;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public static Builder builder(){return new Builder();}
    public static class Builder{
        private String conversation_id;
        private String event_id;

        public Builder conversation_id(String conversation_id){
            this.conversation_id = conversation_id;
            return this;
        }

        public Builder event_id(String event_id){
            this.event_id = event_id;
            return this;
        }

        public SpecificEventRequest build(){
            return new SpecificEventRequest(this);
        }
    }


}
