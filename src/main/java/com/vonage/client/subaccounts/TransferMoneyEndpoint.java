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
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.RequestBuilder;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

class TransferMoneyEndpoint<T extends AbstractMoneyTransfer> extends AbstractMethod<T, T> {
	private static final Class<?>[] ALLOWED_AUTH_METHODS = {TokenAuthMethod.class};
	private static final String PATH = "/accounts/%s/%s-transfers";
	private T cachedTransfer;
	private final String transferName;
	private final Class<T> transferType;

	TransferMoneyEndpoint(HttpWrapper httpWrapper, Class<T> transferType) {
		super(httpWrapper);
		String className = (this.transferType = transferType).getSimpleName();
		int transferIndex = className.indexOf("Transfer");
		transferName = className.substring(0, transferIndex > 0 ? transferIndex : className.length()).toLowerCase();
	}

	@Override
	protected Class<?>[] getAcceptableAuthMethods() {
		return ALLOWED_AUTH_METHODS;
	}

	@Override
	public RequestBuilder makeRequest(T request) {
		String path = String.format(PATH, getApplicationIdOrApiKey(), transferName);
		String uri = httpWrapper.getHttpConfig().getApiBaseUri() + path;
		return RequestBuilder.post(uri)
				.setHeader("Content-Type", "application/json")
				.setHeader("Accept", "application/json")
				.setEntity(new StringEntity((cachedTransfer = request).toJson(), ContentType.APPLICATION_JSON));
	}

	@Override
	public T parseResponse(HttpResponse response) throws IOException {
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode >= 200 && statusCode < 300) {
				String json = basicResponseHandler.handleResponse(response);
				if (cachedTransfer != null) {
					cachedTransfer.updateFromJson(json);
					return cachedTransfer;
				}
				else {
					T fresh = transferType.getConstructor().newInstance();
					fresh.updateFromJson(json);
					return fresh;
				}
			}
			else {
				throw SubaccountsResponseException.fromHttpResponse(response);
			}
		}
		catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException ex) {
			throw new VonageUnexpectedException(ex);
		}
		finally {
			cachedTransfer = null;
		}
	}
}
