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
package com.nexmo.client.insight;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.insight.advanced.AdvancedInsightEndpoint;
import com.nexmo.client.insight.advanced.AdvancedInsightRequest;
import com.nexmo.client.insight.advanced.AdvancedInsightResponse;
import com.nexmo.client.insight.basic.BasicInsightEndpoint;
import com.nexmo.client.insight.basic.BasicInsightRequest;
import com.nexmo.client.insight.basic.BasicInsightResponse;
import com.nexmo.client.insight.standard.StandardInsightEndpoint;
import com.nexmo.client.insight.standard.StandardInsightRequest;
import com.nexmo.client.insight.standard.StandardInsightResponse;

import java.io.IOException;

/**
 * A client for talking to the Nexmo Number Insight API. The standard way to obtain an instance of this class is to use
 * {@link NexmoClient#getInsightClient()}.
 */
public class InsightClient {
    protected BasicInsightEndpoint basic;
    protected StandardInsightEndpoint standard;
    protected AdvancedInsightEndpoint advanced;

    /**
     * Constructor.
     *
     * @param httpWrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public InsightClient(HttpWrapper httpWrapper) {
        this.basic = new BasicInsightEndpoint(httpWrapper);
        this.standard = new StandardInsightEndpoint(httpWrapper);
        this.advanced = new AdvancedInsightEndpoint(httpWrapper);
    }

    public BasicInsightResponse getBasicNumberInsight(String number) throws IOException, NexmoClientException {
        return this.basic.execute(new BasicInsightRequest(number));
    }

    public BasicInsightResponse getBasicNumberInsight(String number, String country) throws IOException,
                                                                                            NexmoClientException {
        return this.basic.execute(new BasicInsightRequest(number, country));
    }

    public StandardInsightResponse getStandardNumberInsight(String number) throws IOException, NexmoClientException {
        return this.standard.execute(new StandardInsightRequest(number));
    }

    public StandardInsightResponse getStandardNumberInsight(String number, String country) throws IOException,
                                                                                                  NexmoClientException {
        return this.standard.execute(new StandardInsightRequest(number, country));
    }

    public AdvancedInsightResponse getAdvancedNumberInsight(String number) throws IOException, NexmoClientException {
        return this.advanced.execute(new AdvancedInsightRequest(number));
    }

    public AdvancedInsightResponse getAdvancedNumberInsight(String number, String country, String ipAddress) throws
                                                                                                             IOException,
                                                                                                             NexmoClientException {
        return this.advanced.execute(new AdvancedInsightRequest(number, country, ipAddress));
    }
}
