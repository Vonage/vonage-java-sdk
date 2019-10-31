package com.nexmo.client.conversation;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class CursorListFilter {
    private Integer page_size;
    private String order;
    private String cursor;
    private String conversation_id;

    public CursorListFilter(Builder builder){
        page_size = builder.page_size;
        order = builder.order;
        cursor = builder.cursor;
        conversation_id = builder.conversation_id;
    }
    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    List<NameValuePair> toUrlParams() {
        List<NameValuePair> result = new ArrayList<>(3);
        conditionalAdd(result, "page_size", page_size);
        conditionalAdd(result, "order", order );
        conditionalAdd(result,"cursor",cursor);
        return result;
    }

    private void conditionalAdd(List<NameValuePair> params, String name, String value){
        if (value != null) {
            params.add(new BasicNameValuePair(name, value));
        }
    }
    private void conditionalAdd(List<NameValuePair> params, String name, Object value) {
        if (value != null) {
            params.add(new BasicNameValuePair(name, value.toString()));
        }
    }
    public static Builder builder(){return new Builder();}
    public static class Builder{
        private Integer page_size;
        private String order;
        private String cursor;
        private String conversation_id;

        public CursorListFilter build(){
            return new CursorListFilter(this);
        }

        public Builder page_size(Integer page_size){
            this.page_size = page_size;
            return this;
        }

        public Builder order(String order){
            this.order = order;
            return this;
        }

        public Builder cursor(String cursor){
            this.cursor= cursor;
            return this;
        }
        public Builder conversation_id(String conversation_id){
            this.conversation_id=conversation_id;
            return this;
        }

    }
}
