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
package com.vonage.client.account;

import com.vonage.client.HttpWrapper;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

@Value
class PricingEndpoint {
    Map<ServiceType, PricingMethod> methods;

    PricingEndpoint(HttpWrapper httpWrapper) {
        Map<ServiceType, PricingMethod> methods = new HashMap<>();
        methods.put(ServiceType.SMS, new SmsPricingMethod(httpWrapper));
        methods.put(ServiceType.VOICE, new VoicePricingMethod(httpWrapper));

        this.methods = unmodifiableMap(methods);
    }

    PricingResponse getPrice(ServiceType serviceType, PricingRequest request) {
        return ofNullable(methods.get(serviceType))
                .map(method -> method.execute(request))
                .orElseThrow(() -> new IllegalArgumentException("Unknown Service Type: " + serviceType));
    }
}
