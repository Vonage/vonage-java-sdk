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
package com.vonage.client.proactiveconnect;

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

class DownloadListItemsEndpoint extends AbstractMethod<DownloadListItemsRequestWrapper, byte[]> {
	private static final Class<?>[] ALLOWED_AUTH_METHODS = {JWTAuthMethod.class};
	private static final String PATH = "/v0.1/bulk/lists/%s/items/download";
	private DownloadListItemsRequestWrapper cachedWrapper;

	DownloadListItemsEndpoint(HttpWrapper httpWrapper) {
		super(httpWrapper);
	}

	@Override
	protected Class<?>[] getAcceptableAuthMethods() {
		return ALLOWED_AUTH_METHODS;
	}

	@Override
	public RequestBuilder makeRequest(DownloadListItemsRequestWrapper wrapper) {
		String path = String.format(PATH, (cachedWrapper = wrapper).listId);
		String uri = httpWrapper.getHttpConfig().getApiEuBaseUri() + path;
		return RequestBuilder.get(uri)
				.setHeader("Content-Type", "application/json")
				.setHeader("Accept", "application/json");
	}

	private static void writeToOutputStream(HttpResponse response, OutputStream os) throws IOException {
		try (InputStream is = response.getEntity().getContent()) {
			byte[] buffer = new byte[0xffff];
			for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
				os.write(buffer, 0, len);
			}
		}
	}

	@Override
	public byte[] parseResponse(HttpResponse response) throws IOException {
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode >= 200 && statusCode < 300) {
				if (cachedWrapper.file != null) {
					try (OutputStream os = Files.newOutputStream(cachedWrapper.file)) {
						writeToOutputStream(response, os);
					}
					return null;
				}
				else {
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					writeToOutputStream(response, os);
					return os.toByteArray();
				}
			}
			else {
				throw ProactiveConnectResponseException.fromHttpResponse(response);
			}
		}
		finally {
			cachedWrapper = null;
		}
	}
}
