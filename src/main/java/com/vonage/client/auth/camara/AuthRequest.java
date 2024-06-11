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

import com.vonage.client.QueryParamsRequest;
import com.vonage.client.common.E164;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Base request query parameters for the intermediate three-legged OAuth2 check network authentication.
 */
public abstract class AuthRequest implements QueryParamsRequest {
    final Map<String, String> params = new LinkedHashMap<>(8);

    /**
     * Creates the base auth request parameters.
     *
     * @param msisdn The phone number of the user you want to authenticate in E.164 format.
     * @param scope The scope of the request as an enum.
     */
    protected AuthRequest(String msisdn, FraudPreventionDetectionScope scope) {
        params.put("login_hint", "tel:+" + new E164(msisdn));
        params.put("scope", "dpv:FraudPreventionAndDetection#" +
                Objects.requireNonNull(scope, "Scope is required.")
        );
    }

    @Override
    public final Map<String, String> makeParams() {
        return params;
    }
}
