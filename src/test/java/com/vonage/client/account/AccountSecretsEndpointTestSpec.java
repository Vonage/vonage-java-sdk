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
package com.vonage.client.account;

import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.SignatureAuthMethod;
import java.util.Collection;

abstract class AccountSecretsEndpointTestSpec<T, R> extends AccountEndpointTestSpec<T, R> {

	@Override
	protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
		Collection<Class<? extends AuthMethod>> authMethods = super.expectedAuthMethods();
		authMethods.add(SignatureAuthMethod.class);
		return authMethods;
	}

	@Override
	protected String expectedDefaultBaseUri() {
		return "https://api.nexmo.com";
	}

	@Override
	protected String expectedEndpointUri(T request) {
		String uri = "/accounts/%s/secrets";
        switch (request) {
            case SecretRequest secretRequest -> {
                String apiKey = secretRequest.apiKey;
                String secretId = secretRequest.secretId;
                uri = String.format(uri, apiKey) + "/" + secretId;
            }
            case CreateSecretRequest createSecretRequest -> uri = String.format(uri, createSecretRequest.apiKey);
            case String ignored -> uri = String.format(uri, request);
            case null, default -> throw new IllegalStateException();
        }
		return uri;
	}
}
