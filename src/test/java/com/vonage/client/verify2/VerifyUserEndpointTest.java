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
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.net.URI;
import java.util.UUID;

public class VerifyUserEndpointTest {
	VerifyUserEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = new VerifyUserEndpoint(new HttpWrapper());
	}

	@Test
	public void testVerifySmsAllParams() throws Exception {
		VerificationRequest request = VerificationRequest.builder()
				.brand("ACME, Inc")
				.codeLength(6)
				.channelTimeout(320)
				.clientRef("my-personal-reference")
				.locale(Locale.ENGLISH_UK)
				.addWorkflow(new SmsWorkflow("447700900001", "FA+9qCX9VSu"))
				.build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals(HttpMethod.POST.name(), builder.getMethod());
		assertEquals("https://api.nexmo.com/v2/verify", builder.getUri().toString());

		String expectedJson = "{\"locale\":\"en-gb\",\"channel_timeout\":320,\"code_length\":6," +
				"\"brand\":\"ACME, Inc\",\"client_ref\":\"my-personal-reference\",\"workflow\":" +
				"[{\"channel\":\"sms\",\"to\":\"447700900001\",\"app_hash\":\"FA+9qCX9VSu\"}]}";

		assertEquals(expectedJson, EntityUtils.toString(builder.getEntity()));
	}

	@Test
	public void testParseResponse202() throws Exception {
		UUID uuid = UUID.randomUUID();
		String json = "{\"request_id\":\""+uuid+"\"}";
		VerificationResponse response = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(202, json));
		assertEquals(uuid, response.getRequestId());
	}

	@Test
	public void testParseResponseFailureAllParams() throws Exception {
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
	public void testCodelessAndBasicAuth() throws Exception {
		VerificationRequest request = VerificationRequest.builder()
				.brand("Vonage").addWorkflow(new SilentAuthWorkflow("447700900001")).build();
		assertThrows(IllegalStateException.class, () -> endpoint.makeRequest(request));

		endpoint = new VerifyUserEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
		RequestBuilder rqBuilder = endpoint.makeRequest(request);
		String actualJson = EntityUtils.toString(rqBuilder.getEntity());
		String expectedJson = "{\"brand\":\"Vonage\",\"workflow\":[{\"channel\":\"silent_auth\",\"to\":\"447700900001\"}]}";
		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(
				HttpConfig.builder().apiBaseUri(baseUri).build(),
				new JWTAuthMethod("app-id", new byte[0])
		);
		endpoint = new VerifyUserEndpoint(wrapper);

		VerificationRequest request = VerificationRequest.builder()
				.addWorkflow(new WhatsappCodelessWorkflow("447700900002")).brand("Megacorp").build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("POST", builder.getMethod());
		assertEquals(baseUri+"/v2/verify", builder.getUri().toString());

		String expectedJson = "{\"brand\":\"Megacorp\",\"workflow\":[{\"channel\":\"whatsapp_interactive\",\"to\":\"447700900002\"}]}";
		assertEquals(expectedJson, EntityUtils.toString(builder.getEntity()));
	}
}
