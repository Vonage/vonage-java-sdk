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
package com.vonage.client.video;

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

class PatchArchiveStreamEndpoint extends AbstractMethod<PatchComposedStreamsRequest, Void> {
	private static final Class<?>[] ALLOWED_AUTH_METHODS = {JWTAuthMethod.class};
	private static final String PATH = "/v2/project/%s/archive/%s/streams";

	PatchArchiveStreamEndpoint(HttpWrapper httpWrapper) {
		super(httpWrapper);
	}

	@Override
	protected Class<?>[] getAcceptableAuthMethods() {
		return ALLOWED_AUTH_METHODS;
	}

	@Override
	public RequestBuilder makeRequest(PatchComposedStreamsRequest request) {
		String path = String.format(PATH, getApplicationIdOrApiKey(), request.id);
		String uri = httpWrapper.getHttpConfig().getVideoBaseUri() + path;
		return RequestBuilder.patch(uri)
				.setHeader("Content-Type", "application/json")
				.setEntity(new StringEntity(request.toJson(), ContentType.APPLICATION_JSON));
	}

	@Override
	public Void parseResponse(HttpResponse response) throws IOException {
		if (response.getStatusLine().getStatusCode() != 204) {
			throw new VonageBadRequestException(EntityUtils.toString(response.getEntity()));
		}
		return null;
	}
}