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
package com.vonage.client.verify;

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class VerifyMethod extends AbstractMethod<VerifyRequest, VerifyResponse> {
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};

    private static final String PATH = "/verify/json";

    VerifyMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(VerifyRequest request) throws UnsupportedEncodingException {
        RequestBuilder result = RequestBuilder
                .post(httpWrapper.getHttpConfig().getApiBaseUri() + PATH)
                .addParameter("number", request.getNumber())
                .addParameter("brand", request.getBrand());

        if (request.getFrom() != null) {
            result.addParameter("sender_id", request.getFrom());
        }

        if (request.getLength() != null && request.getLength() > 0) {
            result.addParameter("code_length", Integer.toString(request.getLength()));
        }

        if (request.getLocale() != null) {
            result.addParameter(new BasicNameValuePair("lg", (request.getDashedLocale())));
        }

        if (request.getType() != null) {
            result.addParameter("require_type", request.getType().toString());
        }

        if (request.getCountry() != null) {
            result.addParameter("country", request.getCountry());
        }

        if (request.getPinExpiry() != null) {
            result.addParameter("pin_expiry", request.getPinExpiry().toString());
        }

        if (request.getNextEventWait() != null) {
            result.addParameter("next_event_wait", request.getNextEventWait().toString());
        }

        if (request.getWorkflow() != null) {
            result.addParameter("workflow_id", String.valueOf(request.getWorkflow().getId()));
        }

        return result;
    }

    @Override
    public VerifyResponse parseResponse(HttpResponse response) throws IOException {
        return VerifyResponse.fromJson(new BasicResponseHandler().handleResponse(response));
    }
}
