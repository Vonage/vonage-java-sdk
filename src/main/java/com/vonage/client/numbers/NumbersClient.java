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
package com.vonage.client.numbers;


import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClientException;
import com.vonage.client.VonageResponseParseException;

/**
 * A client for accessing the Vonage API calls that manage phone numbers.
 */
public class NumbersClient {
    private ListNumbersEndpoint listNumbers;
    private SearchNumbersEndpoint searchNumbers;
    private CancelNumberEndpoint cancelNumber;
    private BuyNumberEndpoint buyNumber;
    private UpdateNumberEndpoint updateNumber;

    public NumbersClient(HttpWrapper httpWrapper) {
        this.listNumbers = new ListNumbersEndpoint(httpWrapper);
        this.searchNumbers = new SearchNumbersEndpoint(httpWrapper);
        this.cancelNumber = new CancelNumberEndpoint(httpWrapper);
        this.buyNumber = new BuyNumberEndpoint(httpWrapper);
        this.updateNumber = new UpdateNumberEndpoint(httpWrapper);
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
        return this.listNumbers.listNumbers(new ListNumbersFilter());
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
        return this.listNumbers.listNumbers(filter);
    }


    /**
     * Search for available Vonage Virtual Numbers.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if an error is returned by the server.
     */
    public SearchNumbersResponse searchNumbers(String country) throws VonageResponseParseException, VonageClientException {
        return this.searchNumbers(new SearchNumbersFilter(country));
    }

    /**
     * Search for available Vonage Virtual Numbers.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if an error is returned by the server.
     */
    public SearchNumbersResponse searchNumbers(SearchNumbersFilter filter) throws VonageResponseParseException, VonageClientException {
        return this.searchNumbers.searchNumbers(filter);
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
        this.buyNumber.execute(new BuyNumberRequest(country, msisdn));
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
        this.cancelNumber.execute(new CancelNumberRequest(country, msisdn));
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
        this.updateNumber.execute(request);
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
        this.updateNumber(request);
    }
}
