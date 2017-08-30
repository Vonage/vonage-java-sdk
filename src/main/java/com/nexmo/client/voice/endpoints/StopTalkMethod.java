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
package com.nexmo.client.voice.endpoints;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.voice.TalkResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class StopTalkMethod extends AbstractMethod<String, TalkResponse> {
    private static final Log LOG = LogFactory.getLog(StopTalkMethod.class);

    private static final String DEFAULT_URI  = "https://api.nexmo.com/v1/calls";
    private static final String DEFAULT_PATH = "/v1/calls";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};
    private String uri = DEFAULT_URI;

    public StopTalkMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    public StopTalkMethod(HttpWrapper httpWrapper, String baseUri) {
        super(httpWrapper);
        uri = baseUri + DEFAULT_PATH;
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(String uuid) throws NexmoClientException, UnsupportedEncodingException {
        String uri = this.uri + uuid + "/talk";
        return RequestBuilder.delete(uri)
                .setHeader("Content-Type", "application/json");
    }

    @Override
    public TalkResponse parseResponse(HttpResponse response) throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        LOG.info("Received: " + json);
        return TalkResponse.fromJson(json);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String baseUri) {
        this.uri = baseUri + DEFAULT_PATH;
    }
}
