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

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import java.io.IOException;

class TransferMoneyEndpoint extends AbstractMethod<MoneyTransfer, MoneyTransfer> {
	private static final Class<?>[] ALLOWED_AUTH_METHODS = {TokenAuthMethod.class};
	private MoneyTransfer cachedTransfer;
	private final String path;

	TransferMoneyEndpoint(HttpWrapper httpWrapper, String transferName) {
		super(httpWrapper);
		path = "/accounts/%s/"+transferName+"-transfers";
	}

	@Override
	protected Class<?>[] getAcceptableAuthMethods() {
		return ALLOWED_AUTH_METHODS;
	}

	@Override
	public RequestBuilder makeRequest(MoneyTransfer request) {
		String uri = httpWrapper.getHttpConfig().getApiBaseUri() + String.format(path, getApplicationIdOrApiKey());
		return RequestBuilder.post(uri)
				.setHeader("Content-Type", "application/json")
				.setHeader("Accept", "application/json")
				.setEntity(new StringEntity((cachedTransfer = request).toJson(), ContentType.APPLICATION_JSON));
	}

	@Override
	public MoneyTransfer parseResponse(HttpResponse response) throws IOException {
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode >= 200 && statusCode < 300) {
				String json = basicResponseHandler.handleResponse(response);
				if (cachedTransfer != null) {
					cachedTransfer.updateFromJson(json);
					return cachedTransfer;
				}
				else {
					return MoneyTransfer.fromJson(json);
				}
			}
			else {
				throw SubaccountsResponseException.fromHttpResponse(response);
			}
		}
		finally {
			cachedTransfer = null;
		}
	}
}