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

import com.vonage.client.auth.BearerAuthMethod;
import com.vonage.client.common.E164;

public class FraudBackendAuthMethod extends BearerAuthMethod {
    private final NetworkAuthClient networkAuthClient;
    private final String msisdn;
    private final FraudPreventionDetectionScope scope;

    public FraudBackendAuthMethod(NetworkAuthClient client,
                                  String telNumber,
                                  FraudPreventionDetectionScope scope) {
        this.networkAuthClient = client;
        this.msisdn = new E164(telNumber).toString();
        this.scope = scope;
    }

    @Override
    protected final String getBearerToken() {
        return networkAuthClient.getCamaraAccessToken(msisdn, scope);
    }

    @Override
    public int getSortKey() {
        return 5;
    }
}
