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

import com.nexmo.client.AbstractMethod;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.auth.SignatureAuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Psd2Method extends AbstractMethod<Psd2Request, VerifyResponse> {
    private static final Log LOG = LogFactory.getLog(Psd2Method.class);

    private static final String PATH = "/verify/psd2/json";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};

    public Psd2Method(HttpWrapper wrapper) {
        super(wrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(Psd2Request request) throws UnsupportedEncodingException {
        RequestBuilder builder =  RequestBuilder
                .post(httpWrapper.getHttpConfig().getApiBaseUri() + PATH)
                .addParameter("number", request.getNumber())
                .addParameter("payee", request.getPayee())
                .addParameter("amount", Double.toString(request.getAmount()));

        //add additional parameters if they are present
        if(request.getWorkflow() != null){
            builder.addParameter("workflow_id", Integer.toString(request.getWorkflow().getId()));
        }

        optionalParams(builder, "code_length", request.getLength());
        optionalParams(builder, "lg", request.getDashedLocale());
        optionalParams(builder, "country", request.getCountry());
        optionalParams(builder, "pin_expiry", request.getPinExpiry());
        optionalParams(builder, "next_event_wait", request.getNextEventWait());

        return builder;

    }

    @Override
    public VerifyResponse parseResponse(HttpResponse response) throws IOException {
        return VerifyResponse.fromJson(new BasicResponseHandler().handleResponse(response));
    }

    private RequestBuilder optionalParams(RequestBuilder builder, String paramName, Object value){
        if (value != null) {
            builder.addParameter(paramName, value + "");
        }
        return null;
    }
}
