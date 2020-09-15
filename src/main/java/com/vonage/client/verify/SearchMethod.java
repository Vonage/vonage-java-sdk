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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class SearchMethod extends AbstractMethod<SearchRequest, SearchVerifyResponse> {
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};

    private static final String PATH = "/verify/search/json";

    SearchMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(SearchRequest request) throws UnsupportedEncodingException {
        RequestBuilder result = RequestBuilder.post(httpWrapper.getHttpConfig().getApiBaseUri() + PATH);
        if (request.getRequestIds().length == 1) {
            result.addParameter("request_id", request.getRequestIds()[0]);
        } else {
            for (String requestId : request.getRequestIds())
                result.addParameter("request_ids", requestId);
        }
        return result;
    }

    @Override
    public SearchVerifyResponse parseResponse(HttpResponse response) throws IOException {
        return SearchVerifyResponse.fromJson(new BasicResponseHandler().handleResponse(response));
    }
}