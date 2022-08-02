/*
 *   Copyright 2022 Vonage
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
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import java.io.IOException;

class PrefixPricingEndpoint extends AbstractMethod<PrefixPricingRequest, PrefixPricingResponse> {
    private static final Class<?>[] ALLOWED_AUTH_METHODS = {TokenAuthMethod.class};

    private static final String PATH = "/account/get-prefix-pricing/outbound/%s";

    PrefixPricingEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class<?>[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(PrefixPricingRequest request) {
        String uri = httpWrapper.getHttpConfig().getRestBaseUri() + String.format(PATH, request.getServiceType());
        return RequestBuilder.get(uri)
                .setHeader("Accept", "application/json")
                .addParameter("prefix", request.getPrefix());
    }

    @Override
    public PrefixPricingResponse parseResponse(HttpResponse response) throws IOException {
        return PrefixPricingResponse.fromJson(basicResponseHandler.handleResponse(response));
    }
}
