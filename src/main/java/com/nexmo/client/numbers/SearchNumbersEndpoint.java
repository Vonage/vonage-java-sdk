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
package com.nexmo.client.numbers;


import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.voice.endpoints.AbstractMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Internal class, representing the Nexmo API endpoint which can be used to search for available virtual numbers to buy.
 * <p>
 * Use {@link NumbersClient#searchNumbers} instead of this class directly.
 */
public class SearchNumbersEndpoint extends AbstractMethod<SearchNumbersFilter, SearchNumbersResponse> {
    private static final String DEFAULT_URI = "https://rest.nexmo.com/number/search";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};
    private String uri = DEFAULT_URI;

    public SearchNumbersEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(SearchNumbersFilter request) throws NexmoClientException,
                                                                        UnsupportedEncodingException {
        RequestBuilder requestBuilder = RequestBuilder.get().setUri(uri);
        request.addParams(requestBuilder);
        return requestBuilder;
    }

    @Override
    public SearchNumbersResponse parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        return SearchNumbersResponse.fromJson(json);
    }

    public SearchNumbersResponse searchNumbers(SearchNumbersFilter request) throws IOException, NexmoClientException {
        return this.execute(request);
    }
}
