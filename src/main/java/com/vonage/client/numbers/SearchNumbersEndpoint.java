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
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class SearchNumbersEndpoint extends AbstractMethod<SearchNumbersFilter, SearchNumbersResponse> {
    private static final String PATH = "/number/search";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};

    SearchNumbersEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(SearchNumbersFilter request) throws UnsupportedEncodingException {
        RequestBuilder requestBuilder = RequestBuilder
                .get()
                .setUri(httpWrapper.getHttpConfig().getRestBaseUri() + PATH);
        request.addParams(requestBuilder);
        return requestBuilder;
    }

    @Override
    public SearchNumbersResponse parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        return SearchNumbersResponse.fromJson(json);
    }

    SearchNumbersResponse searchNumbers(SearchNumbersFilter request) {
        return execute(request);
    }
}
