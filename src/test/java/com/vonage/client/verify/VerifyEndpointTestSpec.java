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
package com.vonage.client.verify;

import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.VonageApiResponseException;
import com.vonage.client.VonageClientException;
import com.vonage.client.auth.ApiKeyQueryParamsAuthMethod;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.Collection;
import java.util.List;
import java.util.Map;

abstract class VerifyEndpointTestSpec<T, R> extends DynamicEndpointTestSpec<T, R> {

	@Override
	protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
		return List.of(ApiKeyQueryParamsAuthMethod.class);
	}

	@Override
	protected Class<? extends VonageClientException> expectedResponseExceptionType() {
		return VonageApiResponseException.class;
	}

	@Override
	protected String expectedDefaultBaseUri() {
		return "https://api.nexmo.com";
	}

	@Override
	protected HttpMethod expectedHttpMethod() {
		return HttpMethod.POST;
	}

	@Override
	protected String expectedContentTypeHeader(T request) {
		return request instanceof SearchRequest ? null : "application/x-www-form-urlencoded";
	}

	@Override
	protected abstract Map<String, ?> sampleQueryParams();
}
