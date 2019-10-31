package com.nexmo.client.conversation;

import com.nexmo.client.AbstractClient;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoResponseParseException;

public class ConversationClient extends AbstractClient {
    protected final ConversationEndpoint conversations;
    protected final UserEndpoint users;
    protected final MemberEndpoint members;
    protected final EventEndpoint events;

    public ConversationClient(HttpWrapper wrapper){
        super(wrapper);
        conversations = new ConversationEndpoint(wrapper);
        users = new UserEndpoint(wrapper);
        members = new MemberEndpoint(wrapper);
        events = new EventEndpoint(wrapper);
    }

    public Conversation CreateConversation(Conversation conversation) throws NexmoResponseParseException, NexmoClientException{
        return conversations.post(conversation);
    }

    public Conversation GetConversation(String id) throws NexmoResponseParseException, NexmoClientException{
        return conversations.get(id);
    }

    public ConversationPage ListConversations(CursorListFilter filter) throws NexmoResponseParseException, NexmoClientException{
        return conversations.get(filter);
    }

    public Conversation UpdateConversation(Conversation conversation) throws NexmoResponseParseException, NexmoClientException{
        return conversations.put(conversation);
    }
    public Integer DeleteConversation(String id) throws NexmoResponseParseException, NexmoClientException{
        return conversations.delete(id);
    }

    public User CreateUser(User user) throws NexmoResponseParseException, NexmoClientException{
        return users.post(user);
    }

    public User GetUser(String id) throws NexmoResponseParseException, NexmoClientException{
        return users.get(id);
    }

    public UserPage ListUsers(CursorListFilter filter) throws NexmoResponseParseException, NexmoClientException{
        return users.get(filter);
    }

    public User UpdateConversation(User user) throws NexmoResponseParseException, NexmoClientException{
        return users.put(user);
    }

    public Integer DeleteUser(String id) throws NexmoResponseParseException, NexmoClientException{
        return users.delete(id);
    }

    public Member CreateMember(CreateMemberRequest request) throws NexmoResponseParseException, NexmoClientException{
        return members.post(request);
    }

    public Member GetMember(SpecificMemberRequest request) throws NexmoResponseParseException, NexmoClientException{
        return members.get(request);
    }

    public MemberPage ListMembers(CursorListFilter request) throws NexmoResponseParseException, NexmoClientException{
        return members.get(request);
    }

    public Member UpdateMember(UpdateMemberRequest request) throws NexmoResponseParseException, NexmoClientException{
        return members.put(request);
    }

    public Integer DeleteMember(SpecificMemberRequest request) throws NexmoResponseParseException, NexmoClientException{
        return members.delete(request);
    }

    public Event CreateEvent(Event event) throws NexmoResponseParseException, NexmoClientException{
        return events.post(event);
    }

    public Event GetEvent(SpecificEventRequest request) throws NexmoResponseParseException, NexmoClientException{
        return events.get(request);
    }

    public EventPage ListEvents(ListEventRequest request) throws NexmoResponseParseException, NexmoClientException{
        return events.get(request);
    }

    public Integer DeleteEvent(SpecificEventRequest request) throws NexmoResponseParseException, NexmoClientException{
        return events.delete(request);
    }
}
