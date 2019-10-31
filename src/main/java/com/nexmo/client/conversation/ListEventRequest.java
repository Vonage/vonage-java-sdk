package com.nexmo.client.conversation;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ListEventRequest {
    private String start_id;
    private String end_id;
    private String conversation_id;

    ListEventRequest(Builder builder){
        this.start_id = builder.start_id;
        this.end_id = builder.end_id;
        this.conversation_id = builder.conversation_id;
    }

    List<NameValuePair> toUrlParams() {
        List<NameValuePair> result = new ArrayList<>(2);
        conditionalAdd(result, "start_id", start_id);
        conditionalAdd(result, "end_id", end_id );
        return result;
    }
    private void conditionalAdd(List<NameValuePair> params, String name, String value){
        if (value != null) {
            params.add(new BasicNameValuePair(name, value));
        }
    }

    public String getStart_id() {
        return start_id;
    }

    public void setStart_id(String start_id) {
        this.start_id = start_id;
    }

    public String getEnd_id() {
        return end_id;
    }

    public void setEnd_id(String end_id) {
        this.end_id = end_id;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public static Builder builder(){return new Builder();}
    public static class Builder{
        private String start_id;
        private String end_id;
        private String conversation_id;

        public Builder start_id(String start_id){
            this.start_id=start_id;
            return this;
        }

        public Builder end_id(String end_id){
            this.end_id = end_id;
            return this;
        }

        public Builder conversation_id(String conversation_id){
            this.conversation_id = conversation_id;
            return this;
        }

        public ListEventRequest build(){
            return new ListEventRequest(this);
        }
    }

}
