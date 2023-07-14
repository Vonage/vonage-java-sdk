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
package com.vonage.client;

import com.vonage.client.auth.AuthMethod;
import com.vonage.client.common.HttpMethod;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class DynamicEndpointTestSpec<T, R> {

	@SuppressWarnings("unchecked")
	@SafeVarargs
	private final Class<R> inferResponseType(R... varargs) {
		return (Class<R>) varargs.getClass().getComponentType();
	}

	protected Class<R> expectedResponseType() {
		return inferResponseType();
	}

	protected AbstractMethod<T, R> endpointAsAbstractMethod() {
		return (AbstractMethod<T, R>) endpoint();
	}

	protected abstract RestEndpoint<T, R> endpoint();
	protected abstract HttpMethod expectedHttpMethod();
	protected abstract Collection<Class<? extends AuthMethod>> expectedAuthMethods();
	protected abstract Class<? extends Exception> expectedResponseExceptionType();
	protected abstract String expectedDefaultBaseUri();
	protected abstract String expectedEndpointUri(T request);
	protected abstract T sampleRequest();
	protected abstract String sampleRequestString();

	public void runTests() throws Exception {
		if (Jsonable.class.isAssignableFrom(expectedResponseType())) {
			assertInvalidJsonResponse();
			R response = parseResponse("{}");
			assertNotNull(response);
		}
		assertAcceptableAuthMethods();
		assertErrorResponse(400);
		assertErrorResponse(500);
		assertDefaultUri(sampleRequestString(), sampleRequest());
		assertCustomUri(sampleRequestString(), sampleRequest());
	}

	protected Jsonable constructJsonableResponse() throws Exception {
		Class<R> responseType = expectedResponseType();
		assertTrue(Jsonable.class.isAssignableFrom(responseType));
		Constructor<R> constructor = responseType.getDeclaredConstructor();
		if (!constructor.isAccessible()) {
			constructor.setAccessible(true);
		}
		return (Jsonable) constructor.newInstance();
	}

	protected R parseResponse(String expectedResponse) throws IOException {
		return parseResponse(expectedResponse, 200);
	}

	protected R parseResponse(String expectedResponse, int statusCode) throws IOException {
		return endpointAsAbstractMethod().parseResponse(TestUtils.makeJsonHttpResponse(statusCode, expectedResponse));
	}

	protected void assertRequestParams(Map<String, String> expectedParams, T request) throws Exception {
		RequestBuilder builder = endpointAsAbstractMethod().makeRequest(request);
		List<NameValuePair> actualParams = builder.getParameters();
		assertEquals(expectedParams.size(), actualParams.size());
		for (NameValuePair nvp : actualParams) {
			assertEquals(nvp.getValue(), expectedParams.get(nvp.getName()));
		}
	}

	private RequestBuilder assertRequest(String expectedRequest, T request) throws Exception {
		RequestBuilder builder = endpointAsAbstractMethod().makeRequest(request);
		assertEquals(expectedHttpMethod().toString(), builder.getMethod());
		if (request instanceof Jsonable) {
			assertEquals(
					ContentType.APPLICATION_JSON.getMimeType(),
					builder.getFirstHeader("Content-Type").getValue()
			);
			assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		}
		if (Jsonable.class.isAssignableFrom(expectedResponseType())) {
			assertEquals(
					ContentType.APPLICATION_JSON.getMimeType(),
					builder.getFirstHeader("Accept").getValue()
			);
		}
		return builder;
	}

	protected void assertDefaultUri(String expectedRequest, T request) throws Exception {
		RequestBuilder builder = assertRequest(expectedRequest, request);
		String expectedUri = expectedDefaultBaseUri() + expectedEndpointUri(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
	}

	protected void assertCustomUri(String expectedRequest, T request) throws Exception {
		AbstractMethod<T, R> endpoint = endpointAsAbstractMethod();
		HttpConfig originalConfig = endpoint.httpWrapper.getHttpConfig();
		try {
			String baseUri = "http://example.com";
			endpoint.httpWrapper.setHttpConfig(HttpConfig.builder().baseUri(baseUri).build());
			RequestBuilder builder = assertRequest(expectedRequest, request);
			String expectedUri = baseUri + expectedEndpointUri(request);
			assertEquals(expectedUri, builder.build().getURI().toString());
		}
		finally {
			endpoint.httpWrapper.setHttpConfig(originalConfig);
		}
	}

	protected void assertAcceptableAuthMethods() {
		assertArrayEquals(expectedAuthMethods().toArray(), endpointAsAbstractMethod().getAcceptableAuthMethods());
	}

	protected void assertErrorResponse(int statusCode) {
		assertThrows(expectedResponseExceptionType(), () ->
				endpointAsAbstractMethod().parseResponse(TestUtils.makeJsonHttpResponse(statusCode, "{}"))
		);
	}

	protected void assertInvalidJsonResponse() {
		assertThrows(VonageResponseParseException.class, () ->
				constructJsonableResponse().updateFromJson("{malformed]")
		);
	}
}
