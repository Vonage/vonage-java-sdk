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
package com.vonage.client.camara;

import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.camara.AuthRequest;
import com.vonage.client.auth.camara.NetworkAuthMethod;
import com.vonage.client.auth.camara.FraudPreventionDetectionScope;
import com.vonage.client.auth.camara.NetworkAuthClient;

/**
 * Base class for Vonage Network API clients.
 *
 * @since 8.9.0
 */
public abstract class NetworkApiClient {
    private final HttpWrapper httpWrapper;
    private final NetworkAuthClient networkAuthClient;

    protected NetworkApiClient(HttpWrapper wrapper) {
        networkAuthClient = new NetworkAuthClient(httpWrapper = wrapper);
    }

    protected String getCamaraBaseUri() {
        return httpWrapper.getHttpConfig().getApiEuBaseUri() + "/camara/";
    }

    protected void setNetworkAuth(AuthRequest request) {
        httpWrapper.getAuthCollection().add(new NetworkAuthMethod(networkAuthClient, request));
    }
}
