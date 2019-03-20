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

import com.nexmo.client.AbstractClient;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;

import java.io.IOException;

/**
 * A client for talking to the Nexmo Number Insight API. The standard way to obtain an instance of this class is to use
 * {@link NexmoClient#getInsightClient()}.
 */
public class InsightClient extends AbstractClient {
    protected BasicInsightEndpoint basic;
    protected StandardInsightEndpoint standard;
    protected AdvancedInsightEndpoint advanced;

    /**
     * Constructor.
     *
     * @param httpWrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public InsightClient(HttpWrapper httpWrapper) {
        super(httpWrapper);

        this.basic = new BasicInsightEndpoint(httpWrapper);
        this.standard = new StandardInsightEndpoint(httpWrapper);
        this.advanced = new AdvancedInsightEndpoint(httpWrapper);
    }

    /**
     * Perform a Basic Insight Request with a number.
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A {@link BasicInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public BasicInsightResponse getBasicNumberInsight(String number) throws IOException, NexmoClientException {
        return getBasicNumberInsight(BasicInsightRequest.withNumber(number));
    }

    /**
     * Perform a Basic Insight Request with a number and country.
     *
     * @param number  A single phone number that you need insight about in national or international format
     * @param country If a number does not have a country code or it is uncertain, set the two-character country code.
     *
     * @return A {@link BasicInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public BasicInsightResponse getBasicNumberInsight(String number, String country) throws IOException, NexmoClientException {
        return getBasicNumberInsight(BasicInsightRequest.withNumberAndCountry(number, country));
    }

    /**
     * Perform a Basic Insight Request with a {@link BasicInsightRequest}.
     *
     * @param basicInsightRequest A request object containing the details of the request to make.
     *
     * @return A {@link BasicInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public BasicInsightResponse getBasicNumberInsight(BasicInsightRequest basicInsightRequest) throws IOException, NexmoClientException {
        return this.basic.execute(basicInsightRequest);
    }

    /**
     * Perform a Standard Insight Request with a number.
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A {@link StandardInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public StandardInsightResponse getStandardNumberInsight(String number) throws IOException, NexmoClientException {
        return getStandardNumberInsight(StandardInsightRequest.withNumber(number));
    }

    /**
     * Perform a Standard Insight Request with a number and country.
     *
     * @param number  A single phone number that you need insight about in national or international format.
     * @param country If a number does not have a country code or it is uncertain, set the two-character country code.
     *
     * @return A {@link StandardInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public StandardInsightResponse getStandardNumberInsight(String number, String country) throws IOException, NexmoClientException {
        return getStandardNumberInsight(StandardInsightRequest.withNumberAndCountry(number, country));
    }

    /**
     * Perform a Standard Insight Request with a number, country, and cnam.
     *
     * @param number  A single phone number that you need insight about in national or international format.
     * @param country If a number does not have a country code or it is uncertain, set the two-character country code.
     * @param cnam    Indicates if the name of the person who owns the phone number should also be looked up and returned.
     *                Set to true to receive phone number owner name in the response. This is only available for US numbers
     *                and incurs an additional charge.
     *
     * @return A {@link StandardInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     * @deprecated Create a {@link StandardInsightRequest} and use {@link InsightClient#getStandardNumberInsight(StandardInsightRequest)}
     */
    @Deprecated
    public StandardInsightResponse getStandardNumberInsight(String number, String country, boolean cnam) throws IOException, NexmoClientException {
        return getStandardNumberInsight(StandardInsightRequest.builder(number).country(country).cnam(cnam).build());
    }

    /**
     * Perform a Standard Insight Request with a {@link StandardInsightRequest}.
     *
     * @param standardInsightRequest A request object containing the details of the request to make.
     *
     * @return A {@link StandardInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public StandardInsightResponse getStandardNumberInsight(StandardInsightRequest standardInsightRequest) throws IOException, NexmoClientException {
        return this.standard.execute(standardInsightRequest);
    }

    /**
     * Perform an Advanced Insight Request with a number.
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A {@link AdvancedInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public AdvancedInsightResponse getAdvancedNumberInsight(String number) throws IOException, NexmoClientException {
        return getAdvancedNumberInsight(AdvancedInsightRequest.withNumber(number));
    }

    /**
     * Perform an Advanced Insight Request with a number and country.
     *
     * @param number  A single phone number that you need insight about in national or international format.
     * @param country If a number does not have a country code or it is uncertain, set the two-character country code.
     *
     * @return A {@link AdvancedInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public AdvancedInsightResponse getAdvancedNumberInsight(String number, String country) throws IOException, NexmoClientException {
        return getAdvancedNumberInsight(AdvancedInsightRequest.withNumberAndCountry(number, country));
    }

    /**
     * Perform an Advanced Insight Request with a number, country, and ipAddress.
     *
     * @param number    A single phone number that you need insight about in national or international format.
     * @param country   If a number does not have a country code or it is uncertain, set the two-character country code.
     * @param ipAddress The IP address of the user. If supplied, we will compare this to the country the user's phone
     *                  is located in and return an error if it does not match.
     *
     * @return A {@link AdvancedInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     * @deprecated Create a {@link AdvancedInsightRequest} and use {@link InsightClient#getAdvancedNumberInsight(AdvancedInsightRequest)}
     */
    @Deprecated
    public AdvancedInsightResponse getAdvancedNumberInsight(String number, String country, String ipAddress) throws IOException, NexmoClientException {
        return getAdvancedNumberInsight(AdvancedInsightRequest.builder(number)
                .country(country)
                .ipAddress(ipAddress)
                .build());
    }

    /**
     * Perform an Advanced Insight Request with a number, country, ipAddress, and cnam.
     *
     * @param number    A single phone number that you need insight about in national or international format.
     * @param country   If a number does not have a country code or it is uncertain, set the two-character country code.
     * @param ipAddress The IP address of the user. If supplied, we will compare this to the country the user's phone
     *                  is located in and return an error if it does not match.
     * @param cnam      Indicates if the name of the person who owns the phone number should also be looked up and returned.
     *                  Set to true to receive phone number owner name in the response. This is only available for US numbers
     *                  and incurs an additional charge.
     *
     * @return A {@link AdvancedInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     * @deprecated Create a {@link AdvancedInsightRequest} and use {@link InsightClient#getAdvancedNumberInsight(AdvancedInsightRequest)}
     */
    @Deprecated
    public AdvancedInsightResponse getAdvancedNumberInsight(String number, String country, String ipAddress, boolean cnam) throws IOException, NexmoClientException {
        return getAdvancedNumberInsight(AdvancedInsightRequest.builder(number)
                .country(country)
                .ipAddress(ipAddress)
                .cnam(cnam)
                .build());
    }

    /**
     * Perform an Advanced Insight Request with a {@link AdvancedInsightRequest}.
     *
     * @param advancedInsightRequest A request object containing the details of the request to make.
     *
     * @return A {@link AdvancedInsightResponse} representing the response from the Nexmo Number Insight API.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Nexmo Number Insight API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public AdvancedInsightResponse getAdvancedNumberInsight(AdvancedInsightRequest advancedInsightRequest) throws IOException, NexmoClientException {
        return this.advanced.execute(advancedInsightRequest);
    }
}
