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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

final class BackendAuthRequest implements QueryParamsRequest {
    private final Map<String, String> params = new LinkedHashMap<>(4);

    /**
     * Creates the parameters for a Back-End Authorization OIDC request.
     *
     * @param msisdn The MSISDN of the user you want to authenticate.
     * @param scope The scope of the request as an enum.
     */
    public BackendAuthRequest(String msisdn, FraudPreventionDetectionScope scope) {
        params.put("login_hint", "tel:" + new E164(msisdn));
        params.put("scope", "dpv:FraudPreventionAndDetection#" +
                Objects.requireNonNull(scope, "Scope is required.")
        );
    }

    @Override
    public Map<String, String> makeParams() {
        return params;
    }
}
