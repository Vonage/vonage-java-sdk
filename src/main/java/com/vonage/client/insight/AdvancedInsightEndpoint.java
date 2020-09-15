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
package com.vonage.client.insight;

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class AdvancedInsightEndpoint extends AbstractMethod<AdvancedInsightRequest, AdvancedInsightResponse> {
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class};

    private static final String PATH = "/ni/advanced/json";
    private static final String ASYNC_PATH = "/ni/advanced/async/json";

    AdvancedInsightEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(AdvancedInsightRequest request) throws UnsupportedEncodingException {
        RequestBuilder requestBuilder = RequestBuilder
                .post(httpWrapper.getHttpConfig().getApiBaseUri() + ((request.isAsync()) ? ASYNC_PATH : PATH))
                .addParameter("number", request.getNumber());
        if (request.getCountry() != null) {
            requestBuilder.addParameter("country", request.getCountry());
        }
        if (request.getIpAddress() != null) {
            requestBuilder.addParameter("ip", request.getIpAddress());
        }
        if (request.getCnam() != null) {
            requestBuilder.addParameter("cnam", request.getCnam().toString());
        }
        if (request.getCallback() != null) {
            requestBuilder.addParameter("callback", request.getCallback());
        }
        return requestBuilder;
    }

    @Override
    public AdvancedInsightResponse parseResponse(HttpResponse response) throws IOException {
        return AdvancedInsightResponse.fromJson(new BasicResponseHandler().handleResponse(response));
    }
}
