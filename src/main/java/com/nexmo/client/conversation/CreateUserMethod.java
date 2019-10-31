package com.nexmo.client.conversation;

import com.nexmo.client.AbstractMethod;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CreateUserMethod extends AbstractMethod<User,User> {
    private static final String PATH = "/users";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};

    public CreateUserMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(User request) throws UnsupportedEncodingException {
        return RequestBuilder.post(httpWrapper.getHttpConfig().getVersionedApiBaseUri("v0.1") + PATH)
                .setHeader("Content-Type", "application/json")
                .setEntity(new StringEntity(request.toJson(), ContentType.APPLICATION_JSON));
    }

    @Override
    public User parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        return User.fromJson(json);
    }
}
