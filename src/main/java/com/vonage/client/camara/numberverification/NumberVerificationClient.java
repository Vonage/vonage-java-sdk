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
package com.vonage.client.camara.numberverification;

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageClient;
import com.vonage.client.auth.camara.BackendAuthRequest;
import com.vonage.client.auth.camara.FrontendAuthRequest;
import com.vonage.client.auth.camara.NetworkAuthMethod;
import com.vonage.client.auth.camara.TokenRequest;
import com.vonage.client.camara.CamaraResponseException;
import com.vonage.client.camara.NetworkApiClient;
import com.vonage.client.common.HttpMethod;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * A client for communicating with the Vonage Number Verification API. The standard way to obtain an instance
 * of this class is to use {@link VonageClient#getNumberVerificationClient()}.
 */
public class NumberVerificationClient extends NetworkApiClient {
    final RestEndpoint<VerifyNumberRequest, VerifyNumberResponse> verifyNumber;
    private final Supplier<UUID> applicationIdGetter;
    private VerifyNumberRequest cachedRequest;

    /**
     * Create a new NumberVerificationClient.
     *
     * @param wrapper Http Wrapper used to create requests.
     */
    @SuppressWarnings("unchecked")
    public NumberVerificationClient(HttpWrapper wrapper) {
        super(wrapper);
        applicationIdGetter = wrapper::getApplicationId;

        verifyNumber = DynamicEndpoint.<VerifyNumberRequest, VerifyNumberResponse> builder(VerifyNumberResponse.class)
                .authMethod(NetworkAuthMethod.class).requestMethod(HttpMethod.POST).wrapper(wrapper)
                .responseExceptionType(CamaraResponseException.class).pathGetter((de, req) -> {
                    setNetworkAuth(new TokenRequest(req.redirectUrl, req.code));
                    return getCamaraBaseUri() + "number-verification/v031/verify";
                })
                .build();
    }

    /**
     *
     * @param phoneNumber
     * @param redirectUrl
     * @param state
     */
    public URI initiateVerification(String phoneNumber, URI redirectUrl, String state) {
        cachedRequest = new VerifyNumberRequest(phoneNumber, redirectUrl);
        return networkAuthClient.makeOpenIDConnectRequest(new FrontendAuthRequest(
                phoneNumber, redirectUrl, applicationIdGetter.get(), state
        ));
    }

    /**
     *
     * @param code
     * @return
     */
    public boolean verifyNumber(String code) {
        if (cachedRequest == null) {
            throw new IllegalStateException("You must first call initiateVerification using this client.");
        }
        cachedRequest.code = Objects.requireNonNull(code, "Code is required.");
        return verifyNumber.execute(cachedRequest).getDevicePhoneNumberVerified();
    }
}
