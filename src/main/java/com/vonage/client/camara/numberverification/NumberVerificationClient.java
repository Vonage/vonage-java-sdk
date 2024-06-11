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
package com.vonage.client.camara.numberverification;

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageClient;
import com.vonage.client.auth.camara.NetworkAuthMethod;
import com.vonage.client.camara.NetworkApiClient;

/**
 * A client for communicating with the Vonage Number Verification API. The standard way to obtain an instance
 * of this class is to use {@link VonageClient#getNumberVerificationClient()}.
 */
public class NumberVerificationClient extends NetworkApiClient {
    //final RestEndpoint<VerifyNumberRequest, VerifyNumberResponse> verifyNumber;

    /**
     * Create a new SimSwapClient.
     *
     * @param wrapper Http Wrapper used to create requests.
     */
    public NumberVerificationClient(HttpWrapper wrapper) {
        super(wrapper);

        /*@SuppressWarnings("unchecked")
        class Endpoint extends DynamicEndpoint<VerifyNumberRequest, VerifyNumberResponse> {
            Endpoint() {
                super(DynamicEndpoint.builder(VerifyNumberResponse.class)
                        .authMethod(NetworkAuthMethod.class)
                )
            }
        }*/
    }
}
