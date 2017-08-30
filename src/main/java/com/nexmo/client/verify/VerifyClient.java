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
package com.nexmo.client.verify;


import com.nexmo.client.AbstractClient;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.verify.endpoints.CheckEndpoint;
import com.nexmo.client.verify.endpoints.SearchEndpoint;
import com.nexmo.client.verify.endpoints.VerifyEndpoint;

import java.io.IOException;
import java.util.Locale;

/**
 * A client for talking to the Nexmo Verify API. The standard way to obtain an instance of this class is to use
 * {@link NexmoClient#getVerifyClient()}.
 * <p>
 * Send a verification request with a call to {@link #verify}, confirm the code entered by the user with
 * {@link #check}, and search in-progress or completed verification requests with {@link #search}
 * <p>
 * More information on method parameters can be found at Nexmo website:
 * <a href="https://docs.nexmo.com/verify">https://docs.nexmo.com/verify</a>
 */
public class VerifyClient extends AbstractClient {

    private CheckEndpoint check;
    private VerifyEndpoint verify;
    private SearchEndpoint search;
    private String baseUri;

    /**
     * Constructor.
     *
     * @param httpWrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public VerifyClient(HttpWrapper httpWrapper) {
        super(httpWrapper);

        this.check = new CheckEndpoint(httpWrapper);
        this.search = new SearchEndpoint(httpWrapper);
        this.verify = new VerifyEndpoint(httpWrapper);
    }

    public VerifyClient(HttpWrapper httpWrapper, String baseUri) {
        super(httpWrapper);
        this.baseUri = baseUri;

        this.check = new CheckEndpoint(httpWrapper, baseUri);
        this.search = new SearchEndpoint(httpWrapper, baseUri);
        this.verify = new VerifyEndpoint(httpWrapper, baseUri);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @return a VerifyResult representing the response received from the Verify API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public VerifyResult verify(final String number,
                               final String brand) throws IOException,
                                                          NexmoClientException {
        return verify.verify(number, brand);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @param from   (optional The Nexmo number to use as the sender for the verification SMS message and calls, in
     *               <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @return a VerifyResult representing the response received from the Verify API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public VerifyResult verify(final String number,
                               final String brand,
                               final String from) throws IOException,
                                                         NexmoClientException {
        return verify.verify(number, brand, from);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @param from   (optional The Nexmo number to use as the sender for the verification SMS message and calls, in
     *               <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @param length (optional) The length of the verification code to be sent to the user. Must be either 4 or 6. Use
     *               -1 to use the default value.
     * @param locale (optional) Override the default locale used for verification. By default the locale is determined
     *               from the country code included in <tt>number</tt>
     * @return a VerifyResult representing the response received from the Verify API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public VerifyResult verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale) throws IOException,
                                                           NexmoClientException {
        return verify.verify(number, brand, from, length, locale);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @param from   (optional The Nexmo number to use as the sender for the verification SMS message and calls, in
     *               <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @param length (optional) The length of the verification code to be sent to the user. Must be either 4 or 6. Use
     *               -1 to use the default value.
     * @param locale (optional) Override the default locale used for verification. By default the locale is determined
     *               from the country code included in <tt>number</tt>
     * @param type   (optional) If provided, restrict the verification to the specified network type. Contact
     *               support@nexmo.com to enable this feature.
     * @return a VerifyResult representing the response received from the Verify API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public VerifyResult verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale,
                               final VerifyRequest.LineType type) throws IOException,
                                                                         NexmoClientException {
        return verify.verify(number, brand, from, length, locale, type);
    }

    /**
     * Validate a code provided by a user in response to a call from {@link #verify}.
     *
     * @param requestId (required) The requestId returned by the <tt>verify</tt> call.
     * @param code      (required) The code entered by the user.
     * @return a CheckResult representing the response received from the API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public CheckResult check(final String requestId,
                             final String code) throws IOException, NexmoClientException {
        return check.check(requestId, code, null);
    }

    /**
     * Validate a code provided by a user in response to a call from {@link #verify}.
     *
     * @param requestId (required) The requestId returned by the <tt>verify</tt> call.
     * @param code      (required) The code entered by the user.
     * @param ipAddress (optional) The IP address obtained from the HTTP request made when the user entered their code.
     * @return a CheckResult representing the response received from the API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public CheckResult check(final String requestId,
                             final String code,
                             final String ipAddress) throws IOException, NexmoClientException {
        return check.check(requestId, code, ipAddress);
    }

    /**
     * @param requestId The requestId of a single Verify request to be looked up.
     * @return A SearchResult containing the details of the Verify request that was looked up, or <tt>null</tt> if no
     * record was found.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public SearchResult search(String requestId) throws IOException, NexmoClientException {
        return search.search(requestId);
    }

    /**
     * @param requestIds The requestIds of Verify requests to be looked up.
     * @return An array SearchResult for each record that was found.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public SearchResult[] search(String... requestIds) throws IOException, NexmoClientException {
        return search.search(requestIds);
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        this.check.setBaseUri(baseUri);
        this.verify.setUri(baseUri);
        this.search.setUri(baseUri);
    }
}
