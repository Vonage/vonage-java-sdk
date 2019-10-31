package com.nexmo.client.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbeddedConvsersations {
    private Conversation[] conversations;

    @JsonProperty("conversations")
    public Conversation[] getConversations() {
        return conversations;
    }

    @JsonProperty("conversations")
    public void setConversations(Conversation[] conversations) {
        this.conversations = conversations;
    }
}
