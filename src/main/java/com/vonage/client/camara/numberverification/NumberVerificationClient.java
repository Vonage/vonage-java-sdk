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
package com.vonage.client.camara.numberverification;

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageClient;
import com.vonage.client.auth.camara.FrontendAuthRequest;
import com.vonage.client.auth.camara.NetworkAuthMethod;
import com.vonage.client.auth.camara.TokenRequest;
import com.vonage.client.camara.CamaraResponseException;
import com.vonage.client.camara.NetworkApiClient;
import com.vonage.client.common.HttpMethod;
import java.net.URI;
import java.util.UUID;

/**
 * A client for communicating with the Vonage Number Verification API. The standard way to obtain an instance
 * of this class is to use {@link VonageClient#getNumberVerificationClient()}.
 *
 * @deprecated This API will be removed in the next major release.
 */
@Deprecated
public class NumberVerificationClient extends NetworkApiClient {
    final RestEndpoint<VerifyNumberRequest, VerifyNumberResponse> verifyNumber;
    private final UUID appId;
    private VerifyNumberRequest cachedRequest;

    /**
     * Create a new NumberVerificationClient.
     *
     * @param wrapper Http Wrapper used to create requests.
     */
    @SuppressWarnings("unchecked")
    public NumberVerificationClient(HttpWrapper wrapper) {
        super(wrapper);
        appId = wrapper.getApplicationId();

        verifyNumber = DynamicEndpoint.<VerifyNumberRequest, VerifyNumberResponse> builder(VerifyNumberResponse.class)
                .authMethod(NetworkAuthMethod.class).requestMethod(HttpMethod.POST)
                .responseExceptionType(CamaraResponseException.class).pathGetter((de, req) -> {
                    setNetworkAuth(new TokenRequest(req.redirectUrl, req.getCode()));
                    return getCamaraBaseUri() + "number-verification/v031/verify";
                })
                .wrapper(wrapper).build();
    }

    /**
     * Sets up the client for verifying the given phone number. This method will cache the provided
     * number and redirect URL for verification when calling {@link #verifyNumber(String)}.
     * The intended usage is to call this method first, and follow the returned URL on the target
     * device which the phone number is supposed to be associated with. When the URL is followed,
     * it will trigger an inbound request to the {@code redirectUrl} with two query parameters:
     * {@code CODE} and {@code STATE} (if provided as a parameter to this method). The code should
     * then be extracted from the query parameters and passed to {@link #verifyNumber(String)}.
     *
     * @param phoneNumber The MSISDN to verify.
     * @param redirectUrl Redirect URL, as set in your Vonage application for Network APIs.
     * @param state An optional string for identifying the request. For simplicity, this could
     *              be set to the same value as {@code phoneNumber}, or it may be {@code null}.
     *
     * @return A link with appropriate parameters which should be followed on the end user's device.
     * The link should be followed when using the SIM card associated the provided phone number.
     * Therefore, on the target device, Wi-Fi should be disabled when doing this, otherwise the result
     * of {@link #verifyNumber(String)} will be {@code false}.
     */
    public URI initiateVerification(String phoneNumber, URI redirectUrl, String state) {
        cachedRequest = new VerifyNumberRequest(phoneNumber, redirectUrl);
        return new FrontendAuthRequest(phoneNumber, redirectUrl, appId, state).buildOidcUrl();
    }

    /**
     * Exchanges the code for an access token, makes the API call to the
     * Number Verification API's "verify" endpoint and extracts the result.
     *
     * @param code The code obtained from the inbound callback's query parameters.
     *
     * @return {@code true} if the device that followed the link was using the SIM card associated
     * with the phone number provided in {@linkplain #initiateVerification(String, URI, String)},
     * {@code false} otherwise (e.g. it was unknown, the link was not followed, the device that followed
     * the link didn't use the SIM card with that phone number when doing so).
     *
     * @throws com.vonage.client.auth.camara.NetworkAuthResponseException If there was an error
     * exchanging the code for an access token when using the Vonage Network Auth API.
     *
     * @throws CamaraResponseException If there was an error in communicating with the Number Verification API.
     */
    public boolean verifyNumber(String code) {
        if (cachedRequest == null) {
            throw new IllegalStateException("You must first call initiateVerification using this client.");
        }
        return verifyNumber(cachedRequest.withCode(code));
    }

    /**
     * Stateless implementation of {@link #verifyNumber(String)}, which creates a new request
     * without having to call {@link #initiateVerification(String, URI, String)} first. This is
     * useful in cases where concurrent verifications are required, or the URL is obtained another way.
     *
     * @param phoneNumber MSISDN of the target device to verify.
     * @param redirectUri Redirect URL, as set in your Vonage application for Network APIs.
     * @param code The code obtained from the inbound callback's query parameters.
     *
     * @return {@code true} if the device that followed the link was using the SIM card associated
     * with the phone number provided in {@linkplain #initiateVerification(String, URI, String)},
     * {@code false} otherwise (e.g. it was unknown, the link was not followed, the device that followed
     * the link didn't use the SIM card with that phone number when doing so).
     *
     * @throws com.vonage.client.auth.camara.NetworkAuthResponseException If there was an error
     * exchanging the code for an access token when using the Vonage Network Auth API.
     *
     * @throws CamaraResponseException If there was an error in communicating with the Number Verification API.
     */
    public boolean verifyNumber(String phoneNumber, URI redirectUri, String code) {
        return verifyNumber(new VerifyNumberRequest(phoneNumber, redirectUri).withCode(code));
    }

    private boolean verifyNumber(VerifyNumberRequest request) {
        try {
            return verifyNumber.execute(request).getDevicePhoneNumberVerified();
        }
        finally {
            cachedRequest = null;
        }
    }
}
