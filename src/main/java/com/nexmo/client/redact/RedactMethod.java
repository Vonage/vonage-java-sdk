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
package com.nexmo.client.redact;

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
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class RedactMethod extends AbstractMethod<RedactRequest, RedactResponse> {
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class};

    private static final String PATH = "/v1/redact/transaction";

    RedactMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(RedactRequest redactRequest) throws NexmoClientException, UnsupportedEncodingException {
        if (redactRequest.getId() == null || redactRequest.getProduct() == null) {
            throw new IllegalArgumentException("Redact transaction id and product are required.");
        }

        if (redactRequest.getProduct() == RedactRequest.Product.SMS && redactRequest.getType() == null) {
            throw new IllegalArgumentException("Redacting SMS requires a type.");
        }

        return RequestBuilder
                .post(httpWrapper.getHttpConfig().getApiBaseUri() + PATH)
                .setEntity(new StringEntity(redactRequest.toJson(), ContentType.APPLICATION_JSON));
    }

    @Override
    public RedactResponse parseResponse(HttpResponse response) throws IOException, NexmoClientException {
        if (response.getStatusLine().getStatusCode() != 204) {
            throw new NexmoBadRequestException(EntityUtils.toString(response.getEntity()));
        }

        return null;
    }

    @Override
    protected RequestBuilder applyAuth(RequestBuilder request) throws NexmoClientException {
        return getAuthMethod(getAcceptableAuthMethods()).applyAsBasicAuth(request);
    }
}
