/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.numbers;

import com.vonage.client.*;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;

/**
 * A client for accessing the Vonage API calls that manage phone numbers. The standard way to obtain an instance of
 * this class is to use {@link VonageClient#getNumbersClient()}.
 */
public class NumbersClient {
    final RestEndpoint<ListNumbersFilter, ListNumbersResponse> listNumbers;
    final RestEndpoint<SearchNumbersFilter, SearchNumbersResponse> searchNumbers;
    final RestEndpoint<CancelNumberRequest, Void> cancelNumber;
    final RestEndpoint<BuyNumberRequest, Void> buyNumber;
    final RestEndpoint<UpdateNumberRequest, Void> updateNumber;

    public NumbersClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(final String path, HttpMethod method, R... type) {
                super(DynamicEndpoint.<T, R> builder(type)
                        .responseExceptionType(NumbersResponseException.class)
                        .wrapper(wrapper).requestMethod(method)
                        .authMethod(TokenAuthMethod.class)
                        .urlFormEncodedContentType(method == HttpMethod.POST)
                        .pathGetter((de, req) -> de.getHttpWrapper().getHttpConfig().getRestBaseUri()+ "/" + path)
                );
            }
        }

        listNumbers = new Endpoint<>("account/numbers", HttpMethod.GET);
        searchNumbers = new Endpoint<>("number/search", HttpMethod.GET);
        cancelNumber = new Endpoint<>("number/cancel", HttpMethod.POST);
        buyNumber = new Endpoint<>("number/buy", HttpMethod.POST);
        updateNumber = new Endpoint<>("number/update", HttpMethod.POST);
    }

    /**
     * Get the first page of phone numbers assigned to the authenticated account.
     *
     * @return A ListNumbersResponse containing the first 10 phone numbers
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if an error is returned by the server.
     */
    public ListNumbersResponse listNumbers() throws VonageResponseParseException, VonageClientException {
        return listNumbers(new ListNumbersFilter());
    }

    /**
     * Get a filtered set of numbers assigned to the authenticated account.
     *
     * @param filter A ListNumbersFilter describing the filters to be applied to the request.
     *
     * @return A ListNumbersResponse containing phone numbers matching the supplied filter.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if an error is returned by the server.
     */
    public ListNumbersResponse listNumbers(ListNumbersFilter filter) throws VonageResponseParseException, VonageClientException {
        return listNumbers.execute(filter);
    }


    /**
     * Search for available Vonage Virtual Numbers.
     *
     * @param country country to search available numbers from
     * @return available Vonage Virtual Numbers
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if an error is returned by the server.
     */
    public SearchNumbersResponse searchNumbers(String country) throws VonageResponseParseException, VonageClientException {
        return searchNumbers(new SearchNumbersFilter(country));
    }

    /**
     * Search for available Vonage Virtual Numbers.
     *
     * @param filter search for available Vonage Virtual Number with filters
     * @return available Vonage Virtual Numbers
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if an error is returned by the server.
     */
    public SearchNumbersResponse searchNumbers(SearchNumbersFilter filter) throws VonageResponseParseException, VonageClientException {
        return searchNumbers.execute(filter);
    }

    /**
     * Start renting a Vonage Virtual Number.
     *
     * @param country A String containing a 2-character ISO country code.
     * @param msisdn  The phone number to be bought.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if an error is returned by the server.
     */
    public void buyNumber(String country, String msisdn) throws VonageResponseParseException, VonageClientException {
        buyNumber.execute(new BuyNumberRequest(country, msisdn));
    }

    /**
     * Stop renting a Vonage Virtual Number.
     *
     * @param country A String containing a 2-character ISO country code.
     * @param msisdn  The phone number to be cancelled.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if an error is returned by the server.
     */
    public void cancelNumber(String country, String msisdn) throws VonageResponseParseException, VonageClientException {
        cancelNumber.execute(new CancelNumberRequest(country, msisdn));
    }

    /**
     * Update the callbacks and/or application associations for a given Vonage Virtual Number.
     *
     * @param request Details of the updates to be made to the number association.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if an error is returned by the server.
     */
    public void updateNumber(UpdateNumberRequest request) throws VonageResponseParseException, VonageClientException {
        updateNumber.execute(request);
    }

    /**
     * Link a given Vonage Virtual Number to a Vonage Application with the given ID.
     *
     * @param msisdn  The Vonage Virtual Number to be updated.
     * @param country The country for the given msisdn.
     * @param appId   The ID for the Vonage Application to be associated with the number.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if an error is returned by the server.
     */
    public void linkNumber(String msisdn, String country, String appId) throws VonageResponseParseException, VonageClientException {
        UpdateNumberRequest request = new UpdateNumberRequest(msisdn, country);
        request.setVoiceCallbackType(UpdateNumberRequest.CallbackType.APP);
        request.setVoiceCallbackValue(appId);
        updateNumber(request);
    }
}
