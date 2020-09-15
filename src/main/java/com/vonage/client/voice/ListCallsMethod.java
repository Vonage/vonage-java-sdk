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
package com.vonage.client.voice;


import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

class ListCallsMethod extends AbstractMethod<CallsFilter, CallInfoPage> {
    private static final Log LOG = LogFactory.getLog(CreateCallMethod.class);

    private static final String PATH = "/calls";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};
    private String uri;

    ListCallsMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(CallsFilter filter) throws UnsupportedEncodingException {
        URIBuilder uriBuilder;
        String uri = httpWrapper.getHttpConfig().getVersionedApiBaseUri("v1") + PATH;

        try {
            uriBuilder = new URIBuilder(uri);
        } catch (URISyntaxException e) {
            throw new VonageUnexpectedException("Could not parse URI: " + uri);
        }
        if (filter != null) {
            List<NameValuePair> params = filter.toUrlParams();
            for (NameValuePair param : params) {
                uriBuilder.setParameter(param.getName(), param.getValue());
            }
        }
        return RequestBuilder.get().setUri(uriBuilder.toString());
    }

    @Override
    public CallInfoPage parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        return CallInfoPage.fromJson(json);
    }
}
