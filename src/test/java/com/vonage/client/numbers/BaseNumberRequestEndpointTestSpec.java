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
package com.vonage.client.numbers;

import com.vonage.client.common.HttpMethod;
import java.util.LinkedHashMap;
import java.util.Map;

abstract class BaseNumberRequestEndpointTestSpec<T extends BaseNumberRequest> extends NumbersEndpointTestSpec<T, Void> {
    final String country = "DE", msisdn = "4930901820";
    final Map<String, String> params = new LinkedHashMap<>();

    @Override
    protected final String expectedContentTypeHeader(BaseNumberRequest request) {
        return "application/x-www-form-urlencoded";
    }

    @Override
    protected final HttpMethod expectedHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected final String expectedEndpointUri(T request) {
        return "/number/" + endpointName();
    }

    @Override
    protected final Map<String, String> sampleQueryParams() {
        populateSampleQueryParams(sampleRequest());
        return params;
    }

    abstract String endpointName();

    void populateSampleQueryParams(T request) {
        params.put("msisdn", request.getMsisdn());
        params.put("country", request.getCountry());
    }
}
