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
package com.vonage.client.camara.auth;

import com.vonage.client.QueryParamsRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

final class TokenRequest implements QueryParamsRequest {
    private final Map<String, String> params = new LinkedHashMap<>(4);

    TokenRequest(UUID authReqId) {
        params.put("grant_type", "urn:openid:params:grant-type:ciba");
        params.put("auth_req_id", "arid/" +
                Objects.requireNonNull(authReqId, "Auth request ID is required.")
        );
    }

    @Override
    public Map<String, String> makeParams() {
        return params;
    }
}
