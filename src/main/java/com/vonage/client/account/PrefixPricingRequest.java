/*
 *   Copyright 2023 Vonage
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

import java.util.Objects;

public class PrefixPricingRequest {
    private final ServiceType serviceType;
    private final String prefix;

    public PrefixPricingRequest(ServiceType serviceType, String prefix) {
        this.serviceType = Objects.requireNonNull(serviceType, "Service type cannot be null.");
        this.prefix = prefix;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public String getPrefix() {
        return prefix;
    }
}
