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
package com.vonage.client.auth.camara;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

/**
 * Front-End auth request parameters for the first step in an OAuth2 three-legged check workflow.
 */
public class FrontendAuthRequest extends AuthRequest {

    /**
     * Creates the parameters for a Front-End Authorization OIDC request.
     *
     * @param msisdn The phone number of the user you want to authenticate in E.164 format.
     * @param redirectUrl The URL to Application's Redirect URI.
     * @param applicationId The Vonage Application ID.
     * @param state A unique identifier for the request. This is meant for the client to be able to know which
     *              request it is when it comes back to their redirect_uri. Useful to prevent CSRF attacks.
     */
    public FrontendAuthRequest(String msisdn, URI redirectUrl, UUID applicationId, String state) {
        super(msisdn, FraudPreventionDetectionScope.NUMBER_VERIFICATION_VERIFY_READ);
        params.put("client_id", Objects.requireNonNull(applicationId, "Application ID is required.").toString());
        params.put("redirect_uri", Objects.requireNonNull(redirectUrl, "Redirect URL is required.").toString());
        params.put("response_type", "code");
        params.put("state", state);
    }
}
