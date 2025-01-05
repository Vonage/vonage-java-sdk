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
package com.vonage.client.application;

import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

abstract class ApplicationEndpointTestSpec<T, R> extends DynamicEndpointTestSpec<T, R> {

	@Override
	protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
		return Collections.singletonList(ApiKeyHeaderAuthMethod.class);
	}

	@Override
	protected Class<? extends Exception> expectedResponseExceptionType() {
		return ApplicationResponseException.class;
	}

	@Override
	protected String expectedDefaultBaseUri() {
		return "https://api.nexmo.com";
	}

	@Override
	protected String expectedEndpointUri(T request) {
		String base = "/v2/applications", suffix;
		if (request instanceof UUID) {
			suffix = request.toString();
		}
		else if (request instanceof Application && HttpMethod.PUT.equals(expectedHttpMethod())) {
			suffix = ((Application) request).getId();
		}
		else {
			suffix = null;
		}
		return suffix != null ? base + "/" + suffix : base;
	}

}
