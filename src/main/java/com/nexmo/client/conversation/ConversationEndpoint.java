package com.nexmo.client.conversation;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;

public class ConversationEndpoint {
    private final CreateConversationMethod createConversation;
    private final GetConversationMethod getConversation;
    private final ListConversationMethod listConversations;
    private final UpdateConversationMethod updateConversation;
    private final DeleteConversationMethod deleteConversation;

    ConversationEndpoint(HttpWrapper wrapper){
        this.createConversation = new CreateConversationMethod(wrapper);
        this.getConversation = new GetConversationMethod(wrapper);
        this.listConversations = new ListConversationMethod(wrapper);
        this.updateConversation = new UpdateConversationMethod(wrapper);
        this.deleteConversation = new DeleteConversationMethod(wrapper);
    }

    Conversation post(Conversation conversation) throws NexmoClientException {
        return this.createConversation.execute(conversation);
    }

    Conversation get(String id) throws NexmoClientException{
        return this.getConversation.execute(id);
    }

    ConversationPage get(CursorListFilter filter) throws NexmoClientException{
        return this.listConversations.execute(filter);
    }

    Conversation put(Conversation conversation) throws  NexmoClientException{
        return this.updateConversation.execute(conversation);
    }

    Integer delete(String id) throws NexmoClientException{
        return this.deleteConversation.execute(id);
    }
}
