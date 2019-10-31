package com.nexmo.client.conversation;

import com.nexmo.client.AbstractMethod;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

public class ListUsersMethod extends AbstractMethod<CursorListFilter, UserPage> {
    private static final String PATH = "/users";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};

    ListUsersMethod(HttpWrapper httpWrapper){super(httpWrapper);}

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(CursorListFilter filter) throws UnsupportedEncodingException {
        URIBuilder uriBuilder;
        String uri = httpWrapper.getHttpConfig().getVersionedApiBaseUri("v0.1") + PATH;

        try{
            uriBuilder = new URIBuilder(uri);
        } catch (URISyntaxException e){
            throw new NexmoUnexpectedException("Could not parse URI: " + uri);
        }
        if (filter !=null){
            List<NameValuePair> params = filter.toUrlParams();
            for (NameValuePair param : params) {
                uriBuilder.setParameter(param.getName(), param.getValue());
            }
        }
        return RequestBuilder.get().setUri(uriBuilder.toString());
    }

    @Override
    public UserPage parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        return UserPage.fromJson(json);
    }
}
