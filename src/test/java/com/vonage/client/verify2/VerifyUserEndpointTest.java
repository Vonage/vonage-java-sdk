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
import com.vonage.client.common.HttpMethod;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class VerifyUserEndpointTest {
	VerifyUserEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = new VerifyUserEndpoint(new HttpWrapper());
	}

	@Test
	public void testVerifySmsAllParams() throws Exception {
		VerificationRequest request = SmsVerificationRequest.builder()
				.to("447700900001")
				.brand("ACME, Inc")
				.codeLength(6)
				.timeout(320)
				.clientRef("my-personal-reference")
				.appHash("FA+9qCX9VSu")
				.locale(Locale.ENGLISH_UK)
				.build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals(HttpMethod.POST.name(), builder.getMethod());
		assertEquals("https://api.nexmo.com/v2/verify", builder.getUri().toString());

		String expectedJson = "{\"brand\":\"ACME, Inc\",\"workflow\":[{" +
				"\"channel\":\"sms\",\"to\":\"447700900001\",\"app_hash\":\"FA+9qCX9VSu\"}]," +
				"\"locale\":\"en-gb\",\"channel_timeout\":320,\"code_length\":6," +
				"\"client_ref\":\"my-personal-reference\"}";

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
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().apiBaseUri(baseUri).build());
		endpoint = new VerifyUserEndpoint(wrapper);

		VerificationRequest request = SilentAuthVerificationRequest.builder()
				.to("447700900002").brand("Megacorp").build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("POST", builder.getMethod());
		assertEquals(baseUri+"/v2/verify", builder.getUri().toString());

		String expectedJson = "{\"brand\":\"Megacorp\",\"workflow\":[{\"channel\":\"silent_auth\",\"to\":\"447700900002\"}]}";
		assertEquals(expectedJson, EntityUtils.toString(builder.getEntity()));
	}
}
