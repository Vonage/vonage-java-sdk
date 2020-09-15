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
package com.vonage.client.verify;


import com.vonage.client.*;

import java.util.Locale;

/**
 * A client for talking to the Vonage Verify API. The standard way to obtain an instance of this class is to use {@link
 * VonageClient#getVerifyClient()}.
 * <p>
 * Send a verification request with a call to {@link #verify}, confirm the code entered by the user with {@link #check},
 * and search in-progress or completed verification requests with {@link #search}
 * <p>
 * More information on method parameters can be found at Vonage website:
 * <a href="https://developer.nexmo.com/verify/overview">https://developer.nexmo.com/verify/overview</a>
 */
public class VerifyClient extends AbstractClient {

    private CheckEndpoint check;
    private VerifyEndpoint verify;
    private SearchEndpoint search;
    private ControlEndpoint control;
    private Psd2Endpoint psd2;

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
        this.control = new ControlEndpoint(httpWrapper);
        this.psd2 = new Psd2Endpoint(httpWrapper);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     *
     * @return a VerifyResponse representing the response received from the Verify API call.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public VerifyResponse verify(final String number, final String brand) throws VonageResponseParseException, VonageClientException {
        return this.verify.verify(number, brand);
    }

    /**
     * Send a verification request to a phone number with a pin verification workflow
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @param workflow <a href="https://developer.nexmo.com/verify/guides/workflows-and-events">workflow</a>
     *                 to use for sending verification pin
     *
     * @return a VerifyResponse representing the response received from the Verify API call.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @since 5.5.0
     */
    public VerifyResponse verify(final String number, final String brand, VerifyRequest.Workflow workflow)
            throws VonageResponseParseException, VonageClientException {
        return this.verify.verify(number, brand, workflow);
    }



    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @param from   (optional The Vonage number to use as the sender for the verification SMS message and calls, in
     *               <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     *
     * @return a VerifyResponse representing the response received from the Verify API call.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public VerifyResponse verify(final String number,
                                 final String brand,
                                 final String from) throws VonageClientException, VonageResponseParseException {
        return this.verify.verify(number, brand, from);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @param from   (optional The Vonage number to use as the sender for the verification SMS message and calls, in
     *               <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @param length (optional) The length of the verification code to be sent to the user. Must be either 4 or 6. Use
     *               -1 to use the default value.
     * @param locale (optional) Override the default locale used for verification. By default the locale is determined
     *               from the country code included in {@code number}
     *
     * @return a VerifyResponse representing the response received from the Verify API call.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public VerifyResponse verify(final String number,
                                 final String brand,
                                 final String from,
                                 final int length,
                                 final Locale locale) throws VonageClientException, VonageResponseParseException {
        return this.verify.verify(number, brand, from, length, locale);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @param from   (optional The Vonage number to use as the sender for the verification SMS message and calls, in
     *               <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @param length (optional) The length of the verification code to be sent to the user. Must be either 4 or 6. Use
     *               -1 to use the default value.
     * @param locale (optional) Override the default locale used for verification. By default the locale is determined
     *               from the country code included in {@code number}
     * @param type   (optional) If provided, restrict the verification to the specified network type. Contact
     *               support@nexmo.com to enable this feature.
     *
     * @return a VerifyResponse representing the response received from the Verify API call.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public VerifyResponse verify(final String number,
                                 final String brand,
                                 final String from,
                                 final int length,
                                 final Locale locale,
                                 final VerifyRequest.LineType type) throws VonageClientException {
        return this.verify.verify(number, brand, from, length, locale, type);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param request validation request for the 2FA verification.
     * @return a VerifyResponse representing the response received from the Verify API call.
     *
     * @throws VonageClientException if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     *
     */
    public VerifyResponse verify(VerifyRequest request) throws VonageClientException, VonageResponseParseException {
        return this.verify.verify(request);
    }

    /**
     * Validate a code provided by a user in response to a call from {@link #verify}.
     *
     * @param requestId (required) The requestId returned by the {@code verify} call.
     * @param code      (required) The code entered by the user.
     *
     * @return a CheckResponse representing the response received from the API call.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public CheckResponse check(final String requestId, final String code) throws VonageClientException, VonageResponseParseException {
        return this.check.check(requestId, code);
    }

    /**
     * Validate a code provided by a user in response to a call from {@link #verify}.
     *
     * @param requestId (required) The requestId returned by the {@code verify} call.
     * @param code      (required) The code entered by the user.
     * @param ipAddress (optional) The IP address obtained from the HTTP request made when the user entered their code.
     *
     * @return a CheckResponse representing the response received from the API call.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public CheckResponse check(final String requestId,
                               final String code,
                               final String ipAddress) throws VonageClientException, VonageResponseParseException {
        return this.check.check(requestId, code, ipAddress);
    }

    /**
     * Search for a previous verification request.
     *
     * @param requestId The requestId of a single Verify request to be looked up.
     *
     * @return A SearchVerifyResponse containing the details of the Verify request that was looked up, or {@code null}
     * if no record was found.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public SearchVerifyResponse search(String requestId) throws VonageClientException, VonageResponseParseException {
        return this.search.search(requestId);
    }

    /**
     * Search for a previous verification request.
     *
     * @param requestIds The requestIds of Verify requests to be looked up.
     *
     * @return An array SearchVerifyResponse for each record that was found.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public SearchVerifyResponse search(String... requestIds) throws VonageClientException, VonageResponseParseException {
        return this.search.search(requestIds);
    }

    /**
     * Advance a current verification request to the next stage in the process.
     *
     * @param requestId The requestId of the ongoing verification request.
     *
     * @return A {@link ControlResponse} representing the response from the API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public ControlResponse advanceVerification(String requestId) throws VonageClientException, VonageResponseParseException {
        return this.control.execute(new ControlRequest(requestId, VerifyControlCommand.TRIGGER_NEXT_EVENT));
    }

    /**
     * Cancel a current verification request.
     *
     * @param requestId The requestId of the ongoing verification request.
     *
     * @return A {@link ControlResponse} representing the response from the API.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public ControlResponse cancelVerification(String requestId) throws VonageClientException, VonageResponseParseException {
        return this.control.execute(new ControlRequest(requestId, VerifyControlCommand.CANCEL));
    }

    /**
     * Send a PSD2 compliant payment token to a user for payment authorization
     *
     * @param number Telephone number to verify, in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format
     * @param amount payment amount
     * @param payee name of the person the payment is for. Name will be included in the message
     *
     * @return A {@link VerifyResponse} representing the response from the API.
     *
     * @throws VonageClientException          if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException   if the response from the API could not be parsed.
     *
     * @since 5.5.0
     */
    public VerifyResponse psd2Verify(String number, Double amount, String payee) throws VonageClientException, VonageResponseParseException {
        return this.psd2.psd2Verify(number, amount, payee);
    }

    /**
     * Send a PSD2 compliant payment token to a user for payment authorization with a pin verification workflow
     *
     * @param number   telephone number to verify, in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format
     * @param amount   payment amount
     * @param payee    name of the person the payment is for. Name will be included in the message
     * @param workflow <a href="https://developer.nexmo.com/verify/guides/workflows-and-events">workflow</a>
     *                 to use for sending verification pin
     *
     * @return A {@link VerifyResponse} representing the response from the API.
     *
     * @throws VonageClientException          if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException   if the response from the API could not be parsed.
     *
     * @since 5.5.0
     */
    public VerifyResponse psd2Verify(String number, Double amount, String payee, Psd2Request.Workflow workflow)
            throws VonageClientException, VonageResponseParseException {
        return this.psd2.psd2Verify(number, amount, payee, workflow);
    }

    /**
     * Send a PSD2 verification request to a phone number with optional parameters
     *
     * @param psd2Request request to to send PSD2 verification to a phone.
     *
     * @return A VerifyResponse representing the response from the API.
     *
     * @throws VonageClientException          if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException   if the response from the API could not be parsed.
     *
     * @since 5.5.0
     */
    public VerifyResponse psd2Verify(Psd2Request psd2Request) throws VonageClientException, VonageResponseParseException {
        return this.psd2.psd2Verify(psd2Request);
    }


}
