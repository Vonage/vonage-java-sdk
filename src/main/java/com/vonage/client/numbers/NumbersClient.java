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
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.UUID;

/**
 * A client for accessing the Vonage API calls that manage phone numbers. The standard way to obtain an instance of
 * this class is to use {@link VonageClient#getNumbersClient()}.
 */
public class NumbersClient {
    final RestEndpoint<ListNumbersFilter, ListNumbersResponse> listNumbers;
    final RestEndpoint<SearchNumbersFilter, SearchNumbersResponse> searchNumbers;
    final RestEndpoint<BuyCancelNumberRequest, Void> buyNumber, cancelNumber;
    final RestEndpoint<UpdateNumberRequest, Void> updateNumber;

    public NumbersClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(final String path, HttpMethod method, R... type) {
                super(DynamicEndpoint.<T, R> builder(type)
                        .responseExceptionType(NumbersResponseException.class)
                        .wrapper(wrapper).requestMethod(method)
                        .authMethod(ApiKeyHeaderAuthMethod.class)
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
     * @throws NumbersResponseException If the API call returned an unsuccessful (4xx or 5xx) response.
     */
    public ListNumbersResponse listNumbers() throws NumbersResponseException {
        return listNumbers(ListNumbersFilter.builder().build());
    }

    /**
     * Get a filtered set of numbers assigned to the authenticated account.
     *
     * @param filter A ListNumbersFilter describing the filters to be applied to the request.
     *
     * @return A ListNumbersResponse containing phone numbers matching the supplied filter.
     *
     * @throws NumbersResponseException If the API call returned an unsuccessful (4xx or 5xx) response.
     */
    public ListNumbersResponse listNumbers(ListNumbersFilter filter) throws NumbersResponseException {
        return listNumbers.execute(filter);
    }

    /**
     * Search for available Vonage Virtual Numbers.
     *
     * @param country Country to search available numbers from.
     *
     * @return Available Vonage Virtual Numbers.
     *
     * @throws NumbersResponseException If the API call returned an unsuccessful (4xx or 5xx) response.
     */
    public SearchNumbersResponse searchNumbers(String country) throws NumbersResponseException {
        return searchNumbers(SearchNumbersFilter.builder().country(country).build());
    }

    /**
     * Search for available Vonage Virtual Numbers.
     *
     * @param filter search for available Vonage Virtual Number with filters
     * @return The available Vonage Virtual Numbers.
     *
     * @throws NumbersResponseException If the API call returned an unsuccessful (4xx or 5xx) response.
     */
    public SearchNumbersResponse searchNumbers(SearchNumbersFilter filter) throws NumbersResponseException {
        return searchNumbers.execute(filter);
    }

    /**
     * Start renting a Vonage Virtual Number.
     *
     * @param country The two character country code in ISO 3166-1 alpha-2 format.
     * @param msisdn  The phone number to be bought in E.164 format.
     *
     * @throws NumbersResponseException If the API call returned an unsuccessful (4xx or 5xx) response.
     */
    public void buyNumber(String country, String msisdn) throws NumbersResponseException {
        buyNumber(country, msisdn, null);
    }

    /**
     * Start renting a Vonage Virtual Number.
     *
     * @param country      The two character country code in ISO 3166-1 alpha-2 format.
     * @param msisdn       The phone number to be bought in E.164 format.
     * @param targetApiKey If you’d like to perform an action on a subaccount, provide the API key of that
     *                     account here. If you’d like to perform an action on your own account,
     *                     you do not need to provide this field.
     *
     * @throws NumbersResponseException If the API call returned an unsuccessful (4xx or 5xx) response.
     * @see #buyNumber(String, String)
     * @since 8.10.0
     */
    public void buyNumber(String country, String msisdn, String targetApiKey) throws NumbersResponseException {
        buyNumber.execute(new BuyCancelNumberRequest(country, msisdn, targetApiKey));
    }

    /**
     * Stop renting a Vonage Virtual Number.
     *
     * @param country The two character country code in ISO 3166-1 alpha-2 format.
     * @param msisdn  The phone number to be cancelled in E.164 format.
     *
     * @throws NumbersResponseException If the API call returned an unsuccessful (4xx or 5xx) response.
     */
    public void cancelNumber(String country, String msisdn) throws NumbersResponseException {
        cancelNumber(country, msisdn, null);
    }

    /**
     * Stop renting a Vonage Virtual Number.
     *
     * @param country      The two character country code in ISO 3166-1 alpha-2 format.
     * @param msisdn       The phone number to be cancelled in E.164 format.
     * @param targetApiKey If you’d like to perform an action on a subaccount, provide the API key of that
     *                     account here. If you’d like to perform an action on your own account,
     *                     you do not need to provide this field.
     *
     * @throws NumbersResponseException If the API call returned an unsuccessful (4xx or 5xx) response.
     * @see #cancelNumber(String, String)
     * @since 8.10.0
     */
    public void cancelNumber(String country, String msisdn, String targetApiKey) throws NumbersResponseException {
        cancelNumber.execute(new BuyCancelNumberRequest(country, msisdn, targetApiKey));
    }

    /**
     * Update the callbacks and/or application associations for a given Vonage Virtual Number.
     *
     * @param request Details of the updates to be made to the number association.
     *
     * @throws NumbersResponseException If the API call returned an unsuccessful (4xx or 5xx) response.
     */
    public void updateNumber(UpdateNumberRequest request) throws NumbersResponseException {
        updateNumber.execute(request);
    }

    /**
     * Link a given Vonage Virtual Number to a Vonage Application with the given ID.
     *
     * @param msisdn  The Vonage Virtual Number to be updated.
     * @param country The country for the given msisdn.
     * @param appId   The UUID for the Vonage Application to be associated with the number.
     *
     * @throws NumbersResponseException If the API call returned an unsuccessful (4xx or 5xx) response.
     */
    public void linkNumber(String msisdn, String country, String appId) throws NumbersResponseException {
        updateNumber(UpdateNumberRequest.builder(msisdn, country)
                .voiceCallback(UpdateNumberRequest.CallbackType.APP, UUID.fromString(appId).toString()).build());
    }
}
