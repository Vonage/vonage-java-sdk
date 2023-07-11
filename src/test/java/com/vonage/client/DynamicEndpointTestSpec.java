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
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collection;

public abstract class DynamicEndpointTestSpec<T, R> {

	@SuppressWarnings("unchecked")
	@SafeVarargs
	private final Class<R> inferResponseType(R... varargs) {
		return (Class<R>) varargs.getClass().getComponentType();
	}

	protected Class<R> expectedResponseType() {
		return inferResponseType();
	}

	protected abstract AbstractMethod<T, R> endpoint();
	protected abstract HttpMethod expectedHttpMethod();
	protected abstract Collection<Class<? extends AuthMethod>> expectedAuthMethods();
	protected abstract Class<? extends VonageApiResponseException> expectedResponseExceptionType();
	protected abstract String expectedDefaultBaseUri();
	protected abstract String expectedEndpointUri();
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
		return endpoint().parseResponse(TestUtils.makeJsonHttpResponse(200, expectedResponse));
	}

	private RequestBuilder assertRequest(String expectedRequest, T request) throws Exception {
		RequestBuilder builder = endpoint().makeRequest(request);
		assertEquals(expectedHttpMethod().toString(), builder.getMethod());
		if (expectedRequest != null) {
			assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		}
		if (request instanceof Jsonable) {
			assertEquals(
					ContentType.APPLICATION_JSON.getMimeType(),
					builder.getFirstHeader("Content-Type").getValue()
			);
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
		String expectedUri = expectedDefaultBaseUri() + expectedEndpointUri();
		assertEquals(expectedUri, builder.build().getURI().toString());
	}

	protected void assertCustomUri(String expectedRequest, T request) throws Exception {
		String baseUri = "http://example.com";
		endpoint().httpWrapper.setHttpConfig(HttpConfig.builder().baseUri(baseUri).build());
		RequestBuilder builder = assertRequest(expectedRequest, request);
		String expectedUri = baseUri + expectedEndpointUri();
		assertEquals(expectedUri, builder.build().getURI().toString());
	}

	protected void assertAcceptableAuthMethods() {
		assertArrayEquals(expectedAuthMethods().toArray(), endpoint().getAcceptableAuthMethods());
	}

	protected void assertErrorResponse(int statusCode) {
		assertThrows(expectedResponseExceptionType(), () ->
				endpoint().parseResponse(TestUtils.makeJsonHttpResponse(statusCode, "{}"))
		);
	}

	protected void assertInvalidJsonResponse() {
		assertThrows(VonageResponseParseException.class, () ->
				constructJsonableResponse().updateFromJson("{malformed]")
		);
	}
}
