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
package com.vonage.client.video;

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import java.io.IOException;

class GetBroadcastEndpoint extends AbstractMethod<String, Broadcast> {
	private static final Class<?>[] ALLOWED_AUTH_METHODS = {JWTAuthMethod.class};
	private static final String PATH = "/v2/project/%s/broadcast/%s";

	GetBroadcastEndpoint(HttpWrapper httpWrapper) {
		super(httpWrapper);
	}

	@Override
	protected Class<?>[] getAcceptableAuthMethods() {
		return ALLOWED_AUTH_METHODS;
	}

	@Override
	public RequestBuilder makeRequest(String broadcastId) {
		String path = String.format(PATH, getApplicationIdOrApiKey(), broadcastId);
		String uri = httpWrapper.getHttpConfig().getVideoBaseUri() + path;
		return RequestBuilder.get(uri)
				.setHeader("Accept", "application/json");
	}

	@Override
	public Broadcast parseResponse(HttpResponse response) throws IOException {
		return Broadcast.fromJson(basicResponseHandler.handleResponse(response));
	}
}
