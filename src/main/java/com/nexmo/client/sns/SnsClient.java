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
package com.nexmo.client.sns;


import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.sns.request.SnsPublishRequest;
import com.nexmo.client.sns.request.SnsSubscribeRequest;
import com.nexmo.client.sns.response.SnsPublishResponse;
import com.nexmo.client.sns.response.SnsSubscribeResponse;

/**
 * A client for talking to the Nexmo Voice API. The standard way to obtain an instance of this class is to use {@link
 * NexmoClient#getSnsClient()}.
 */
public class SnsClient {
    private SnsEndpoint endpoint;

    /**
     * Constructor.
     *
     * @param httpWrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public SnsClient(HttpWrapper httpWrapper) {
        this.endpoint = new SnsEndpoint(httpWrapper);
    }

    public SnsPublishResponse publish(SnsPublishRequest request) throws NexmoClientException, NexmoResponseParseException {
        return (SnsPublishResponse) this.endpoint.execute(request);
    }

    public SnsSubscribeResponse subscribe(SnsSubscribeRequest request) throws NexmoClientException, NexmoResponseParseException {
        return (SnsSubscribeResponse) this.endpoint.execute(request);
    }
}
