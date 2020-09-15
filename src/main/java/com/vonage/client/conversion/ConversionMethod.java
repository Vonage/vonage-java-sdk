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
package com.vonage.client.conversion;

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.VonageClientException;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

class ConversionMethod extends AbstractMethod<ConversionRequest, Void> {

    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class};

    private static final String PATH = "/conversions/";

    ConversionMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(ConversionRequest conversionRequest) throws UnsupportedEncodingException {
        String uri =
                httpWrapper.getHttpConfig().getApiBaseUri() + PATH + conversionRequest.getType().name().toLowerCase();
        return RequestBuilder
                .post(uri)
                .addParameter("message-id", conversionRequest.getMessageId())
                .addParameter("delivered", String.valueOf(conversionRequest.isDelivered()))
                .addParameter(
                        "timestamp",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(conversionRequest.getTimestamp())
                );
    }

    @Override
    public Void parseResponse(HttpResponse response) throws IOException, VonageClientException {
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new VonageBadRequestException(EntityUtils.toString(response.getEntity()));
        }

        return null;
    }

    public String getBaseUri() {
        return httpWrapper.getHttpConfig().getApiBaseUri() + PATH;
    }
}
