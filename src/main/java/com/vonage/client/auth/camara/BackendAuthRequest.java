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
package com.vonage.client.auth.camara;

/**
 * Back-End auth request parameters for the first step in an OAuth2 three-legged check workflow.
 */
public final class BackendAuthRequest extends AuthRequest {

    /**
     * Creates the parameters for a Back-End Authorization OIDC request.
     *
     * @param msisdn The phone number of the user you want to authenticate in E.164 format.
     * @param scope The scope of the request as an enum.
     */
    public BackendAuthRequest(String msisdn, FraudPreventionDetectionScope scope) {
        super(msisdn, scope);
    }
}
