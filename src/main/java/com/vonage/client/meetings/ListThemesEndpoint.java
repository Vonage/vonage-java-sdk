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
package com.vonage.client.meetings;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

class ListThemesEndpoint extends AbstractMethod<Void, List<Theme>> {
	private static final Class<?>[] ALLOWED_AUTH_METHODS = {JWTAuthMethod.class};
	private static final String PATH = "/meetings/themes";

	ListThemesEndpoint(HttpWrapper httpWrapper) {
		super(httpWrapper);
	}

	@Override
	protected Class<?>[] getAcceptableAuthMethods() {
		return ALLOWED_AUTH_METHODS;
	}

	@Override
	public RequestBuilder makeRequest(Void request) {
		String uri = httpWrapper.getHttpConfig().getApiEuBaseUri() + PATH;
		return RequestBuilder.get(uri).setHeader("Accept", "application/json");
	}

	@Override
	public List<Theme> parseResponse(HttpResponse response) throws IOException {
		String json = basicResponseHandler.handleResponse(response);
		if (json == null || json.isEmpty()) {
			return Collections.emptyList();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, new TypeReference<List<Theme>>() {});
	}
}
