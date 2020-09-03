/*
 * Copyright (c) 2020 Vonage
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

import com.nexmo.client.AbstractMethod;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.VonageBadRequestException;
import com.nexmo.client.VonageClientException;
import com.nexmo.client.auth.SignatureAuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class RevokeSecretMethod extends AbstractMethod<SecretRequest, Void> {
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class};

    private static final String PATH = "/accounts/%s/secrets/%s";

    RevokeSecretMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(SecretRequest secretRequest) throws UnsupportedEncodingException {
        if (secretRequest.getApiKey() == null) {
            throw new IllegalArgumentException("API key is required.");
        }

        if (secretRequest.getSecretId() == null) {
            throw new IllegalArgumentException("Secret id is required.");
        }

        String uri = String.format(
                httpWrapper.getHttpConfig().getApiBaseUri() + PATH,
                secretRequest.getApiKey(),
                secretRequest.getSecretId()
        );
        return RequestBuilder.delete(uri);
    }

    @Override
    public Void parseResponse(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() != 204) {
            throw new VonageBadRequestException(EntityUtils.toString(response.getEntity()));
        }

        return null;
    }

    @Override
    protected RequestBuilder applyAuth(RequestBuilder request) throws VonageClientException {
        return getAuthMethod(getAcceptableAuthMethods()).applyAsBasicAuth(request);
    }
}
