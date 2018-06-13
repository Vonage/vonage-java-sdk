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
package com.nexmo.client.account;

import com.nexmo.client.AbstractClient;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;

import java.io.IOException;

/**
 * A client for talking to the Nexmo Number Insight API. The standard way to obtain an instance of this class is to use
 * {@link NexmoClient#getInsightClient()}.
 */
public class AccountClient extends AbstractClient {
    protected BalanceEndpoint balance;
    protected PricingEndpoint pricing;
    protected PrefixPricingEndpoint prefixPricing;
    protected TopUpEndpoint topUp;

    /**
     * Constructor.
     *
     * @param httpWrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public AccountClient(HttpWrapper httpWrapper) {
        super(httpWrapper);

        this.balance = new BalanceEndpoint(httpWrapper);
        this.pricing = new PricingEndpoint(httpWrapper);
        this.prefixPricing = new PrefixPricingEndpoint(httpWrapper);
        this.topUp = new TopUpEndpoint(httpWrapper);
    }

    public BalanceResponse getBalance() throws IOException, NexmoClientException {
        return this.balance.execute();
    }

    /**
     * Retrieve the voice pricing for a specified country.
     *
     * @param country The two-character country code for which you would like to retrieve pricing.
     * @return PricingResponse object which contains the results from the API.
     * @throws IOException          if a network error occurred contacting the Nexmo Account API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public PricingResponse getVoicePrice(String country) throws IOException, NexmoClientException {
        return getVoicePrice(new PricingRequest(country));
    }

    private PricingResponse getVoicePrice(PricingRequest pricingRequest) throws IOException, NexmoClientException {
        return this.pricing.getPrice(ServiceType.VOICE, pricingRequest);
    }

    /**
     * Retrieve the SMS pricing for a specified country.
     *
     * @param country The two-character country code for which you would like to retrieve pricing.
     * @return PricingResponse object which contains the results from the API.
     * @throws IOException          if a network error occurred contacting the Nexmo Account API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public PricingResponse getSmsPrice(String country) throws IOException, NexmoClientException {
        return getSmsPrice(new PricingRequest(country));
    }

    private PricingResponse getSmsPrice(PricingRequest pricingRequest) throws IOException, NexmoClientException {
        return this.pricing.getPrice(ServiceType.SMS, pricingRequest);
    }

    /**
     * Retrieve the pricing for a specified prefix.
     *
     * @param type   The type of service to retrieve pricing for.
     * @param prefix The prefix to retrieve the pricing for.
     * @return PrefixPricingResponse object which contains the results from the API.
     * @throws IOException          if a network error occurred contacting the Nexmo Account API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public PrefixPricingResponse getPrefixPrice(ServiceType type,
                                                String prefix) throws IOException, NexmoClientException {
        return getPrefixPrice(new PrefixPricingRequest(type, prefix));
    }

    private PrefixPricingResponse getPrefixPrice(PrefixPricingRequest prefixPricingRequest) throws IOException, NexmoClientException {
        return this.prefixPricing.getPrice(prefixPricingRequest);
    }

    /**
     * Top-up your account when you have enabled auto-reload in the dashboard. Amount added is based on your initial
     * reload-enabled payment.
     *
     * @param transaction The ID associated with your original auto-reload transaction
     * @return Boolean true if successful.
     * @throws IOException          if a network error occurred contacting the Nexmo Account API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response object.
     */
    public Boolean topUp(String transaction) throws IOException, NexmoClientException {
        return topUp(new TopUpRequest(transaction));
    }

    private Boolean topUp(TopUpRequest request) throws IOException, NexmoClientException {
        return this.topUp.topUp(request);
    }
}
