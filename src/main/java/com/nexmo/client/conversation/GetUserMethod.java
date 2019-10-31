package com.nexmo.client.conversation;

import com.nexmo.client.AbstractMethod;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

public class GetUserMethod extends AbstractMethod<String,User> {
    private static final String PATH = "/users/";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};

    GetUserMethod(HttpWrapper httpWrapper){super(httpWrapper);}
    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(String user_id) {
        return RequestBuilder.get(httpWrapper.getHttpConfig().getVersionedApiBaseUri("v0.1") + PATH + user_id);
    }

    @Override
    public User parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        return User.fromJson(json);
    }
}
