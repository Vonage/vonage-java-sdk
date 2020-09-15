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
package com.vonage.client.numbers;


import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class CancelNumberEndpoint extends AbstractMethod<CancelNumberRequest, Void> {
    private static final String PATH = "/number/cancel";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};

    CancelNumberEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(CancelNumberRequest request) throws UnsupportedEncodingException {
        RequestBuilder requestBuilder = RequestBuilder
                .post()
                .setUri(httpWrapper.getHttpConfig().getRestBaseUri() + PATH);
        request.addParams(requestBuilder);
        return requestBuilder;
    }

    @Override
    public Void parseResponse(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new VonageBadRequestException(EntityUtils.toString(response.getEntity()));
        }

        return null;
    }
}
