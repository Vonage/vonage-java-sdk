/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.messages;

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

class SendMessageEndpoint extends AbstractMethod<MessageRequest, MessageResponse> {
	private static final Class<?>[] ALLOWED_AUTH_METHODS = {JWTAuthMethod.class, TokenAuthMethod.class};
	private static final String PATH = "/v1/messages";
	private static final String SANDBOX_ENDPOINT_URI = "https://messages-sandbox.nexmo.com" + PATH;

	private boolean sandbox = false;

	SendMessageEndpoint(HttpWrapper httpWrapper) {
		super(httpWrapper);
	}

	public void setSandboxed(boolean sandbox) {
		this.sandbox = sandbox;
	}

	@Override
	protected Class<?>[] getAcceptableAuthMethods() {
		return ALLOWED_AUTH_METHODS;
	}

	@Override
	public RequestBuilder makeRequest(MessageRequest request) {
		String uri = sandbox ? SANDBOX_ENDPOINT_URI : httpWrapper.getHttpConfig().getApiBaseUri() + PATH;
		return RequestBuilder.post(uri)
				.setHeader("Content-Type", "application/json")
				.setHeader("Accept", "application/json")
				.setEntity(new StringEntity(request.toJson(), ContentType.APPLICATION_JSON));
	}

	@Override
	public MessageResponse parseResponse(HttpResponse response) throws IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		String json;

		if (statusCode >= 200 && statusCode < 300) {
			json = basicResponseHandler.handleResponse(response);
			return MessageResponse.fromJson(json);
		}
		else {
			json = EntityUtils.toString(response.getEntity());
			MessageResponseException mrx = MessageResponseException.fromJson(json);
			if (mrx.title == null) {
				mrx.title = response.getStatusLine().getReasonPhrase();
			}
			mrx.statusCode = statusCode;
			throw mrx;
		}
	}
}
