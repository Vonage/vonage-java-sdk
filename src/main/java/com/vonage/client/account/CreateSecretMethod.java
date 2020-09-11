/*
 *   Copyright 2020 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.account;

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.VonageClientException;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class CreateSecretMethod extends AbstractMethod<CreateSecretRequest, SecretResponse> {
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class};

    private static final String PATH = "/accounts/%s/secrets";

    CreateSecretMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(CreateSecretRequest createSecretRequest) throws UnsupportedEncodingException {
        if (createSecretRequest.getApiKey() == null) {
            throw new IllegalArgumentException("API key is required.");
        }

        if (createSecretRequest.getSecret() == null) {
            throw new IllegalArgumentException("Secret is required.");
        }

        String uri = String.format(httpWrapper.getHttpConfig().getApiBaseUri() + PATH, createSecretRequest.getApiKey());
        return RequestBuilder
                .post(uri)
                .setEntity(new StringEntity(createSecretRequest.toJson(), ContentType.APPLICATION_JSON));
    }

    @Override
    public SecretResponse parseResponse(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new VonageBadRequestException(EntityUtils.toString(response.getEntity()));
        }

        return SecretResponse.fromJson(new BasicResponseHandler().handleResponse(response));
    }

    @Override
    protected RequestBuilder applyAuth(RequestBuilder request) throws VonageClientException {
        return getAuthMethod(getAcceptableAuthMethods()).applyAsBasicAuth(request);
    }
}
