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
package com.vonage.client.verify2;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.common.HttpMethod;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.net.URI;
import java.util.UUID;

public class CancelEndpointTest {
	CancelEndpoint endpoint;
	String requestId = UUID.randomUUID().toString();

	@Before
	public void setUp() {
		endpoint = new CancelEndpoint(new HttpWrapper());
	}

	@Test
	public void testParse404AllParams() throws Exception {
		int statusCode = 404;
		String title = "Not Found",
				type = "https://developer.vonage.com/api-errors#not-found",
				detail = "Request "+requestId+" was not found or it has been verified already.",
				instance = "bf0ca0bf927b3b52e3cb03217e1a1ddf",
				json = "{\n" +
				"   \"title\": \""+title+"\",\n" +
				"   \"type\": \""+type+"\",\n" +
				"   \"detail\": \""+detail+"\",\n" +
				"   \"instance\": \""+instance+"\"\n" +
				"}";
		try {
			endpoint.parseResponse(TestUtils.makeJsonHttpResponse(statusCode, json));
			fail("Expected "+ VerifyResponseException.class.getName());
		}
		catch (VerifyResponseException vrx) {
			VerifyResponseException expected = VerifyResponseException.fromJson(json);
			expected.setStatusCode(statusCode);
			assertEquals(expected, vrx);
			assertEquals(statusCode, vrx.getStatusCode());
			assertEquals(title, vrx.getTitle());
			assertEquals(URI.create(type), vrx.getType());
			assertEquals(detail, vrx.getDetail());
			assertEquals(instance, vrx.getInstance());
		}
	}

	@Test
	public void testParse204() throws Exception {
		assertNull(endpoint.parseResponse(TestUtils.makeJsonHttpResponse(204, "")));
	}

	@Test
	public void testDefaultUri() throws Exception {
		RequestBuilder builder = endpoint.makeRequest(requestId);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals(HttpMethod.DELETE.name(), builder.getMethod());
		assertEquals("https://api.nexmo.com/v2/verify/"+requestId, builder.getUri().toString());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(
				HttpConfig.builder().apiBaseUri(baseUri).build(),
				new JWTAuthMethod("app-id", new byte[0])
		);
		endpoint = new CancelEndpoint(wrapper);
		RequestBuilder builder = endpoint.makeRequest(requestId);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("DELETE", builder.getMethod());
		assertEquals(baseUri+"/v2/verify/"+requestId, builder.getUri().toString());
	}
}
