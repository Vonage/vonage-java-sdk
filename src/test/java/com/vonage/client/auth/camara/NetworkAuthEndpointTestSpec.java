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

import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.VonageApiResponseException;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.Map;
import java.util.Set;

abstract class NetworkAuthEndpointTestSpec<T, R> extends DynamicEndpointTestSpec<T, R> {

	@Override
	protected Set<Class<? extends AuthMethod>> expectedAuthMethods() {
		return Set.of(JWTAuthMethod.class);
	}

	@Override
	protected Class<? extends VonageApiResponseException> expectedResponseExceptionType() {
		return NetworkAuthResponseException.class;
	}

	@Override
	protected String expectedDefaultBaseUri() {
		return expectedHttpMethod() == HttpMethod.POST ?
				"https://api-eu.vonage.com" : "https://oidc.idp.vonage.com";
	}

	@Override
	protected String expectedContentTypeHeader(T request) {
		return "application/x-www-form-urlencoded";
	}

	@Override
	protected abstract Map<String, ?> sampleQueryParams();
}
