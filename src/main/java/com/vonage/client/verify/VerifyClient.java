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
package com.vonage.client.verify;

import com.vonage.client.*;
import com.vonage.client.auth.ApiKeyQueryParamsAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.Locale;

/**
 * A client for talking to the Vonage Verify API. The standard way to obtain an instance of this class is to use {@link
 * VonageClient#getVerifyClient()}.
 * <p>
 * Send a verification request with a call to {@link #verify}, confirm the code entered by the user with {@link #check},
 * and search in-progress or completed verification requests with {@link #search}
 * <p>
 * More information on method parameters can be found on the
 * <a href="https://developer.vonage.com/verify/overview">Vonage developer portal</a>.
 */
public class VerifyClient {
    final RestEndpoint<CheckRequest, CheckResponse> check;
    final RestEndpoint<VerifyRequest, VerifyResponse> verify;
    final RestEndpoint<SearchRequest, SearchVerifyResponse> search;
    final RestEndpoint<ControlRequest, ControlResponse> control;
    final RestEndpoint<Psd2Request, VerifyResponse> psd2;

    /**
     * Constructor.
     *
     * @param wrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public VerifyClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(String path, boolean formEncoded, R... type) {
                super(DynamicEndpoint.<T, R> builder(type)
                        .wrapper(wrapper).requestMethod(HttpMethod.POST)
                        .authMethod(ApiKeyQueryParamsAuthMethod.class)
                        .urlFormEncodedContentType(formEncoded)
                        .pathGetter((de, req) -> de.getHttpWrapper().getHttpConfig()
                                .getApiBaseUri() + "/verify" + path + "/json"
                        )
                );
            }
        }

        verify = new Endpoint<>("", true);
        check = new Endpoint<>("/check", true);
        search = new Endpoint<>("/search", false);
        psd2 = new Endpoint<>("/psd2", true);
        control = new Endpoint<ControlRequest, ControlResponse>("/control", true) {
            @Override
            public ControlResponse postProcessParsedResponse(ControlResponse parsed) {
                if (parsed.getStatus().equals("0")) {
                    return parsed;
                }
                else {
                    throw new VerifyException(parsed.getStatus(), parsed.getErrorText());
                }
            }
        };
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
        return verify(new VerifyRequest.Builder(number, brand).build());
    }

    /**
     * Send a verification request to a phone number with a pin verification workflow
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @param workflow <a href="https://developer.vonage.com/verify/guides/workflows-and-events">workflow</a>
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
        return verify(new VerifyRequest.Builder(number, brand).workflow(workflow).build());
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
        return verify(new VerifyRequest.Builder(number, brand)
                .senderId(from)
                .build()
        );
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
        return verify(VerifyRequest.builder(number, brand).length(length).senderId(from).locale(locale).build());
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
        return verify.execute(request);
    }

    /**
     * Validate a code provided by a user in response to a call from {@link #verify}.
     *
     * @param request The request to send for validation.
     * @return a CheckResponse representing the response received from the API call.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @deprecated Use {@link #check(String, String)}.
     */
    @Deprecated
    public CheckResponse check(CheckRequest request) throws VonageClientException, VonageResponseParseException {
        return check.execute(request);
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
        return check(new CheckRequest(requestId, code));
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
        return search.execute(new SearchRequest(requestId));
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
        return search.execute(new SearchRequest(requestIds));
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
        return control.execute(new ControlRequest(requestId, VerifyControlCommand.TRIGGER_NEXT_EVENT));
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
        return control.execute(new ControlRequest(requestId, VerifyControlCommand.CANCEL));
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
        return psd2Verify(new Psd2Request.Builder(number, amount, payee).build());
    }

    /**
     * Send a PSD2 compliant payment token to a user for payment authorization with a pin verification workflow
     *
     * @param number   telephone number to verify, in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format
     * @param amount   payment amount
     * @param payee    name of the person the payment is for. Name will be included in the message
     * @param workflow <a href="https://developer.vonage.com/verify/guides/workflows-and-events">workflow</a>
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
        return psd2Verify(new Psd2Request.Builder(number, amount, payee).workflow(workflow).build());
    }

    /**
     * Send a PSD2 verification request to a phone number with optional parameters
     *
     * @param psd2Request request to send PSD2 verification to a phone.
     *
     * @return A VerifyResponse representing the response from the API.
     *
     * @throws VonageClientException          if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException   if the response from the API could not be parsed.
     *
     * @since 5.5.0
     */
    public VerifyResponse psd2Verify(Psd2Request psd2Request) throws VonageClientException, VonageResponseParseException {
        return psd2.execute(psd2Request);
    }
}
