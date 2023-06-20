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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.common.HttpMethod;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import java.net.URI;
import java.util.UUID;

public class VerifyCodeEndpointTest {
	VerifyCodeEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = new VerifyCodeEndpoint(new HttpWrapper());
	}

	@Test
	public void testVerifySuccess() throws Exception {
		VerifyCodeRequestWrapper request = new VerifyCodeRequestWrapper(UUID.randomUUID().toString(), "54321");
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(HttpMethod.POST.name(), builder.getMethod());
		assertEquals("https://api.nexmo.com/v2/verify/"+request.requestId, builder.getUri().toString());
		String expectedJson = "{\"code\":\""+request.code+"\"}";
		assertEquals(expectedJson, EntityUtils.toString(builder.getEntity()));
	}

	@Test
	public void testParse422AllParams() throws Exception {
		final int statusCode = 422;
		String title = "Conflict";
		URI type = URI.create("https://www.developer.vonage.com/api-errors/verify#conflict");
		String detail = "Concurrent verifications to the same number are not allowed.";
		String instance = "738f9313-418a-4259-9b0d-6670f06fa82d";
		UUID requestId = UUID.fromString("575a2054-aaaf-4405-994e-290be7b9a91f");
		String json = "{\n" +
				"   \"title\": \""+title+"\",\n" +
				"   \"type\": \""+type+"\",\n" +
				"   \"detail\": \""+detail+"\",\n" +
				"   \"instance\": \""+instance+"\",\n" +
				"   \"request_id\": \""+requestId+"\"\n" +
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
			assertEquals(type, vrx.getType());
			assertEquals(detail, vrx.getDetail());
			assertEquals(instance, vrx.getInstance());
			assertEquals(requestId, vrx.getRequestId());
		}
	}

	@Test
	public void testParseResponseFailureNoBody() throws Exception {
		final int statusCode = 500;
		try {
			endpoint.parseResponse(TestUtils.makeJsonHttpResponse(statusCode, ""));
			fail("Expected "+VerifyResponseException.class.getName());
		}
		catch (VerifyResponseException mrx) {
			// The mock returns "OK"
			VerifyResponseException expected = VerifyResponseException.fromJson("{\"title\":\"OK\"}");
			expected.setStatusCode(statusCode);
			assertEquals(expected, mrx);
		}
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().apiBaseUri(baseUri).build());
		endpoint = new VerifyCodeEndpoint(wrapper);

		VerifyCodeRequestWrapper request = new VerifyCodeRequestWrapper(UUID.randomUUID().toString(), "4567890");
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals("POST", builder.getMethod());
		assertEquals(baseUri+"/v2/verify/"+request.requestId, builder.getUri().toString());

		String expectedJson = "{\"code\":\""+request.code+"\"}";
		assertEquals(expectedJson, EntityUtils.toString(builder.getEntity()));
	}

	@Test(expected = VonageUnexpectedException.class)
	public void triggerJsonProcessingException() {
		class SelfRefrencing extends VerifyCodeRequestWrapper {
			@JsonProperty("self") SelfRefrencing self = this;
			SelfRefrencing() {
				super(null, null);
			}
		}
		new SelfRefrencing().toJson();
	}
}
