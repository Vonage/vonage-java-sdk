/*
 * Copyright (c) 2011-2018 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.account;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoBadRequestException;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.SignatureAuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.voice.endpoints.AbstractMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CreateSecretMethod extends AbstractMethod<CreateSecretRequest, SecretResponse> {
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class};

    private static final String DEFAULT_URI = "https://api.nexmo.com/accounts/%s/secrets";

    private String uri = DEFAULT_URI;

    CreateSecretMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(CreateSecretRequest createSecretRequest) throws NexmoClientException, UnsupportedEncodingException {
        if (createSecretRequest.getApiKey() == null) {
            throw new IllegalArgumentException("API key is required.");
        }

        if (createSecretRequest.getSecret() == null) {
            throw new IllegalArgumentException("Secret is required.");
        }

        String uri = String.format(this.uri, createSecretRequest.getApiKey());
        return RequestBuilder
                .post(uri)
                .setEntity(new StringEntity(createSecretRequest.toJson(), ContentType.APPLICATION_JSON));
    }

    @Override
    public SecretResponse parseResponse(HttpResponse response) throws IOException, NexmoClientException {
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new NexmoBadRequestException(EntityUtils.toString(response.getEntity()));
        }

        return SecretResponse.fromJson(new BasicResponseHandler().handleResponse(response));
    }

    @Override
    protected RequestBuilder applyAuth(RequestBuilder request) throws NexmoClientException {
        return getAuthMethod(getAcceptableAuthMethods()).applyAsBasicAuth(request);
    }
}
