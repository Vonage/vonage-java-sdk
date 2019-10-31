package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TextEventBody {
    private String text;
    TextEventBody(){

    }
    public TextEventBody(String text){
        this.text = text;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }
}
