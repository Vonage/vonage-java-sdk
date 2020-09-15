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
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class DownloadRecordingEndpoint extends AbstractMethod<String, Recording> {
    private static final Log LOG = LogFactory.getLog(DownloadRecordingEndpoint.class);

    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};

    DownloadRecordingEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    public RequestBuilder makeRequest(String uri) throws UnsupportedEncodingException {
        return RequestBuilder.get().setUri(uri);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public Recording parseResponse(HttpResponse response) throws IOException {
        return new Recording(response);
    }
}
