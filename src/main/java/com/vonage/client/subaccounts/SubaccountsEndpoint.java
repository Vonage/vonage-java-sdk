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
package com.vonage.client.subaccounts;

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.function.Function;

class SubaccountsEndpoint<T, R> extends DynamicEndpoint<T, R> {

	protected SubaccountsEndpoint(
			Class<T> requestType, Class<R> responseType, HttpWrapper httpWrapper,
			Function<T, String> pathGetter, HttpMethod method) {

		super(builder(requestType, responseType)
			.wrapper(httpWrapper)
			.addAuthMethod(TokenAuthMethod.class)
			.requestMethod(method)
			.pathGetter((de, req) -> {
					if (req instanceof CreateSubaccountRequest) {
						((CreateSubaccountRequest) req).primaryAccountApiKey = de.getApplicationIdOrApiKey();
					}
					return String.format(
							httpWrapper.getHttpConfig().getApiBaseUri() + "/accounts/%s/",
							de.getApplicationIdOrApiKey()
					) + pathGetter.apply(req);
				}
			)
			.responseExceptionType(SubaccountsResponseException.class)
		);
	}
}
