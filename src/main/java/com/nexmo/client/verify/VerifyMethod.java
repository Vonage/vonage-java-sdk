/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.verify;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.SignatureAuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.voice.endpoints.AbstractMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class VerifyMethod extends AbstractMethod<VerifyRequest, VerifyResponse> {
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class};

    private static final String DEFAULT_URI = "https://api.nexmo.com/verify/json";

    private String uri = DEFAULT_URI;

    VerifyMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(VerifyRequest request) throws NexmoClientException, UnsupportedEncodingException {
        RequestBuilder result = RequestBuilder.post(this.uri)
                .addParameter("number", request.getNumber())
                .addParameter("brand", request.getBrand());

        if (request.getFrom() != null) {
            result.addParameter("sender_id", request.getFrom());
        }

        if (request.getLength() > 0) {
            result.addParameter("code_length", Integer.toString(request.getLength()));
        }

        if (request.getLocale() != null) {
            result.addParameter(new BasicNameValuePair("lg",
                    (request.getLocale().getLanguage() + "-" + request.getLocale().getCountry()).toLowerCase()));
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

        return result;
    }

    @Override
    public VerifyResponse parseResponse(HttpResponse response) throws IOException {
        return VerifyResponse.fromJson(new BasicResponseHandler().handleResponse(response));
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
