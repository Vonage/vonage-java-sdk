package com.nexmo.client.voice.endpoints;/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.voice.Call;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

// TODO: Create a package for these endpoint methods
public class ReadCallMethod extends AbstractMethod<String, Call> {
    private static final String DEFAULT_BASE_URI = "https://api.nexmo.com/v1";
    private final String baseUri;
    private static final Class[] allowed_auth_methods = new Class[]{JWTAuthMethod.class};

    public ReadCallMethod() {
        this(DEFAULT_BASE_URI);
    }

    public ReadCallMethod(String baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public HttpUriRequest makeRequest(String callId) {
        String uri = baseUri + "/calls/" + callId;
        HttpGet result = new HttpGet(uri);

        return result;
    }

    @Override
    public Call parseResponse(HttpResponse response) {
        return new Call();
    }
}
