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
package com.vonage.client.verify2;

import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.VonageApiResponseException;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import java.util.Arrays;
import java.util.Collection;

abstract class Verify2EndpointTestSpec<T, R> extends DynamicEndpointTestSpec<T, R> {

	@Override
	protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
		return Arrays.asList(JWTAuthMethod.class, ApiKeyHeaderAuthMethod.class);
	}

	@Override
	protected Class<? extends VonageApiResponseException> expectedResponseExceptionType() {
		return VerifyResponseException.class;
	}

	@Override
	protected String expectedDefaultBaseUri() {
		return "https://api.nexmo.com";
	}
}
