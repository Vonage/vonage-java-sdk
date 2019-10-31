package com.nexmo.client.conversation;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class EventListFilter {
    private String start_id;
    private String end_id;

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
    private void conditionalAdd(List<NameValuePair> params, String name, Object value) {
        if (value != null) {
            params.add(new BasicNameValuePair(name, value.toString()));
        }
    }
}
