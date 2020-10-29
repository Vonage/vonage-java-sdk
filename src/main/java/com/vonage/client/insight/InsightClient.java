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
package com.vonage.client.insight;


import com.vonage.client.*;

/**
 * A client for talking to the Vonage Number Insight API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getInsightClient()}.
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

        basic = new BasicInsightEndpoint(httpWrapper);
        standard = new StandardInsightEndpoint(httpWrapper);
        advanced = new AdvancedInsightEndpoint(httpWrapper);
    }

    /**
     * Perform a Basic Insight Request with a number.
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A {@link BasicInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public BasicInsightResponse getBasicNumberInsight(String number) throws VonageResponseParseException, VonageClientException {
        return getBasicNumberInsight(BasicInsightRequest.withNumber(number));
    }

    /**
     * Perform a Basic Insight Request with a number and country.
     *
     * @param number  A single phone number that you need insight about in national or international format
     * @param country If a number does not have a country code or it is uncertain, set the two-character country code.
     *
     * @return A {@link BasicInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public BasicInsightResponse getBasicNumberInsight(String number, String country) throws VonageResponseParseException, VonageClientException {
        return getBasicNumberInsight(BasicInsightRequest.withNumberAndCountry(number, country));
    }

    /**
     * Perform a Basic Insight Request with a {@link BasicInsightRequest}.
     *
     * @param basicInsightRequest A request object containing the details of the request to make.
     *
     * @return A {@link BasicInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public BasicInsightResponse getBasicNumberInsight(BasicInsightRequest basicInsightRequest) throws VonageResponseParseException, VonageClientException {
        return basic.execute(basicInsightRequest);
    }

    /**
     * Perform a Standard Insight Request with a number.
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A {@link StandardInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public StandardInsightResponse getStandardNumberInsight(String number) throws VonageResponseParseException, VonageClientException {
        return getStandardNumberInsight(StandardInsightRequest.withNumber(number));
    }

    /**
     * Perform a Standard Insight Request with a number and country.
     *
     * @param number  A single phone number that you need insight about in national or international format.
     * @param country If a number does not have a country code or it is uncertain, set the two-character country code.
     *
     * @return A {@link StandardInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public StandardInsightResponse getStandardNumberInsight(String number, String country) throws VonageResponseParseException, VonageClientException {
        return getStandardNumberInsight(StandardInsightRequest.withNumberAndCountry(number, country));
    }

    /**
     * Perform a Standard Insight Request with a number, country, and cnam.
     *
     * @param number  A single phone number that you need insight about in national or international format.
     * @param country If a number does not have a country code or it is uncertain, set the two-character country code.
     * @param cnam    Indicates if the name of the person who owns the phone number should also be looked up and
     *                returned. Set to true to receive phone number owner name in the response. This is only available
     *                for US numbers and incurs an additional charge.
     *
     * @return A {@link StandardInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @deprecated Create a {@link StandardInsightRequest} and use {@link InsightClient#getStandardNumberInsight(StandardInsightRequest)}
     */
    @Deprecated
    public StandardInsightResponse getStandardNumberInsight(String number, String country, boolean cnam) throws VonageResponseParseException, VonageClientException {
        return getStandardNumberInsight(StandardInsightRequest.builder(number).country(country).cnam(cnam).build());
    }

    /**
     * Perform a Standard Insight Request with a {@link StandardInsightRequest}.
     *
     * @param standardInsightRequest A request object containing the details of the request to make.
     *
     * @return A {@link StandardInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public StandardInsightResponse getStandardNumberInsight(StandardInsightRequest standardInsightRequest) throws VonageResponseParseException, VonageClientException {
        return standard.execute(standardInsightRequest);
    }

    /**
     * Perform an Advanced Insight Request with a number.
     *
     * @param number A single phone number that you need insight about in national or international format.
     *
     * @return A {@link AdvancedInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public AdvancedInsightResponse getAdvancedNumberInsight(String number) throws VonageResponseParseException, VonageClientException {
        return getAdvancedNumberInsight(AdvancedInsightRequest.withNumber(number));
    }

    /**
     * Perform an Advanced Insight Request with a number and country.
     *
     * @param number  A single phone number that you need insight about in national or international format.
     * @param country If a number does not have a country code or it is uncertain, set the two-character country code.
     *
     * @return A {@link AdvancedInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public AdvancedInsightResponse getAdvancedNumberInsight(String number, String country) throws VonageResponseParseException, VonageClientException {
        return getAdvancedNumberInsight(AdvancedInsightRequest.withNumberAndCountry(number, country));
    }

    /**
     * Perform an Advanced Insight Request with a number, country, and ipAddress.
     *
     * @param number    A single phone number that you need insight about in national or international format.
     * @param country   If a number does not have a country code or it is uncertain, set the two-character country
     *                  code.
     * @param ipAddress The IP address of the user. If supplied, we will compare this to the country the user's phone is
     *                  located in and return an error if it does not match.
     *
     * @return A {@link AdvancedInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @deprecated Create a {@link AdvancedInsightRequest} and use {@link InsightClient#getAdvancedNumberInsight(AdvancedInsightRequest)}
     */
    @Deprecated
    public AdvancedInsightResponse getAdvancedNumberInsight(String number, String country, String ipAddress) throws VonageResponseParseException, VonageClientException {
        return getAdvancedNumberInsight(AdvancedInsightRequest.builder(number)
                .country(country)
                .ipAddress(ipAddress)
                .build());
    }

    /**
     * Perform an Advanced Insight Request with a number, country, ipAddress, and cnam.
     *
     * @param number    A single phone number that you need insight about in national or international format.
     * @param country   If a number does not have a country code or it is uncertain, set the two-character country
     *                  code.
     * @param ipAddress The IP address of the user. If supplied, we will compare this to the country the user's phone is
     *                  located in and return an error if it does not match.
     * @param cnam      Indicates if the name of the person who owns the phone number should also be looked up and
     *                  returned. Set to true to receive phone number owner name in the response. This is only available
     *                  for US numbers and incurs an additional charge.
     *
     * @return A {@link AdvancedInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @deprecated Create a {@link AdvancedInsightRequest} and use {@link InsightClient#getAdvancedNumberInsight(AdvancedInsightRequest)}
     */
    @Deprecated
    public AdvancedInsightResponse getAdvancedNumberInsight(String number, String country, String ipAddress, boolean cnam) throws VonageResponseParseException, VonageClientException {
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
     * @return A {@link AdvancedInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public AdvancedInsightResponse getAdvancedNumberInsight(AdvancedInsightRequest advancedInsightRequest) throws VonageResponseParseException, VonageClientException {
        return advanced.execute(advancedInsightRequest);
    }
}
