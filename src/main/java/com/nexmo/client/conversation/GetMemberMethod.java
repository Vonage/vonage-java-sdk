package com.nexmo.client.conversation;

import com.nexmo.client.AbstractMethod;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

public class GetMemberMethod extends AbstractMethod<SpecificMemberRequest,Member> {
    private static final String PATH = "/conversations/";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};

    GetMemberMethod(HttpWrapper httpWrapper){super(httpWrapper);}
    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(SpecificMemberRequest request) {
        return RequestBuilder.get(httpWrapper.getHttpConfig().getVersionedApiBaseUri("v0.1") + PATH + request.getConversation_id() + "/members/" + request.getMember_id());
    }

    @Override
    public Member parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        return Member.fromJson(json);
    }
}
