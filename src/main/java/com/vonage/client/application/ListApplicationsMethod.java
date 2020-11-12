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
package com.vonage.client.application;

import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.VonageClientException;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class ListApplicationsMethod extends ApplicationMethod<ListApplicationRequest, ApplicationList> {
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};

    private static final String PATH = "/applications";

    ListApplicationsMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(ListApplicationRequest request) throws UnsupportedEncodingException {
        RequestBuilder builder = RequestBuilder
                .get(httpWrapper.getHttpConfig().getVersionedApiBaseUri("v2") + PATH)
                .setHeader("Content-Type", "application/json");

        if (request != null) {
            if (request.getPageSize() > 0) {
                builder.addParameter("page_size", String.valueOf(request.getPageSize()));
            }

            if (request.getPage() > 0) {
                builder.addParameter("page", String.valueOf(request.getPage()));
            }
        }

        return builder;
    }

    @Override
    public ApplicationList parseResponse(HttpResponse response) throws IOException, VonageClientException {
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new VonageBadRequestException(EntityUtils.toString(response.getEntity()));
        }

        return ApplicationList.fromJson(new BasicResponseHandler().handleResponse(response));
    }
}
