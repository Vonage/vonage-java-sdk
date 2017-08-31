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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * A client for accessing the Nexmo API calls that manage phone numbers.
 */
public class NumbersClient {
    private ListNumbersEndpoint listNumbers;
    private CancelNumberEndpoint cancelNumber;

    public NumbersClient(HttpWrapper httpWrapper) {
        this.listNumbers = new ListNumbersEndpoint(httpWrapper);
        this.cancelNumber = new CancelNumberEndpoint(httpWrapper);
    }

    /**
     * Get the first page of phone numbers assigned to the authenticated account.
     *
     * @return A ListNumbersResponse containing the first 10 phone numbers
     * @throws IOException if an error occurs contacting the Nexmo API
     * @throws NexmoClientException if an error is returned by the server.
     */
    public ListNumbersResponse listNumbers() throws IOException, NexmoClientException {
        return this.listNumbers.listNumbers(new ListNumbersFilter());
    }

    /**
     * Get a filtered set of numbers assigned to the authenticated account.
     * @param filter A ListNumbersFilter describing the filters to be applied to the request.
     * @return A ListNumbersResponse containing phone numbers matching the supplied filter.
     * @throws IOException if an error occurs contacting the Nexmo API
     * @throws NexmoClientException if an error is returned by the server.
     */
    public ListNumbersResponse listNumbers(ListNumbersFilter filter) throws IOException, NexmoClientException {
        return this.listNumbers.listNumbers(filter);
    }

    public void cancelNumber(String country, String msisdn)  throws IOException, NexmoClientException {
        CancelNumberResponse response = this.cancelNumber.execute(new CancelNumberRequest(country, msisdn));
    }
}
