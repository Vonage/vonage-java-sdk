/*
 *   Copyright 2025 Vonage
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
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.function.Function;

/**
 * A client for talking to the Vonage Number Insight API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getInsightClient()}.
 */
public class InsightClient {
    final RestEndpoint<BasicInsightRequest, BasicInsightResponse> basic;
    final RestEndpoint<StandardInsightRequest, StandardInsightResponse> standard;
    final RestEndpoint<AdvancedInsightRequest, AdvancedInsightResponse> advanced;
    final RestEndpoint<AdvancedInsightAsyncRequest, AdvancedAsyncInsightResponse> advancedAsync;

    /**
     * Constructor.
     *
     * @param wrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
     */
    public InsightClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(Function<T, String> pathGetter, R... type) {
                super(DynamicEndpoint.<T, R> builder(type)
                        .wrapper(wrapper).requestMethod(HttpMethod.POST)
                        .authMethod(SignatureAuthMethod.class, ApiKeyHeaderAuthMethod.class)
                        .pathGetter((de, req) -> {
                            String base = de.getHttpWrapper().getHttpConfig().getApiBaseUri();
                            return base + "/ni/" + pathGetter.apply(req) + "/json";
                        })
                );
            }
        }

        basic = new Endpoint<>(req -> "basic");
        standard = new Endpoint<>(req -> "standard");
        advanced = new Endpoint<>(req -> "advanced");
        advancedAsync = new Endpoint<>(req -> "advanced/async");
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
     * Perform an Advanced Insight Request with a {@link AdvancedInsightAsyncRequest}.
     *
     * @param request A request object containing the details of the request to make.
     *
     * @return A {@link AdvancedInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public AdvancedInsightResponse getAdvancedNumberInsight(AdvancedInsightRequest request) throws VonageResponseParseException, VonageClientException {
        return advanced.execute(request);
    }

    /**
     * Perform an Advanced Insight Request with a {@link AdvancedInsightAsyncRequest}.
     *
     * @param request A request object containing the details of the request to make.
     *
     * @return A {@link AdvancedInsightResponse} representing the response from the Vonage Number Insight API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     *
     * @since 9.0.0
     */
    public AdvancedAsyncInsightResponse getAdvancedAsyncNumberInsight(AdvancedInsightAsyncRequest request) throws VonageResponseParseException, VonageClientException {
        return advancedAsync.execute(request);
    }
}
