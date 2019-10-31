package com.nexmo.client.conversation;

import com.nexmo.client.AbstractMethod;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

public class GetConversationMethod extends AbstractMethod<String, Conversation> {
    private static final String PATH = "/conversations/";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};

    GetConversationMethod(HttpWrapper httpWrapper){super(httpWrapper);}
    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(String conversation_id) {
        return RequestBuilder.get(httpWrapper.getHttpConfig().getVersionedApiBaseUri("v0.1") + PATH + conversation_id);
    }

    @Override
    public Conversation parseResponse(HttpResponse response) throws IOException{
        String json = new BasicResponseHandler().handleResponse(response);
        return Conversation.fromJson(json);
    }
}
