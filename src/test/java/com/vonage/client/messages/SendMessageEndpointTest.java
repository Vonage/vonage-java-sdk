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
package com.vonage.client.messages;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.messages.mms.MmsVcardRequest;
import com.vonage.client.messages.sms.SmsTextRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class SendMessageEndpointTest {
	SendMessageEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = new SendMessageEndpoint(new HttpWrapper());
	}

	@Test
	public void testConstructPayloadSms() throws Exception {
		MessageRequest request = SmsTextRequest.builder()
				.from("447700900001").to("447700900000")
				.text("Hello, World!").build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals(HttpMethod.POST.name(), builder.getMethod());
		assertEquals("https://api.nexmo.com/v1/messages", builder.getUri().toString());

		final String expected = "{\"message_type\":\"text\",\"channel\":\"sms\",\"from\":" +
				"\"447700900001\",\"to\":\"447700900000\",\"text\":\"Hello, World!\"}";

		assertEquals(expected, EntityUtils.toString(builder.getEntity()));
	}

	@Test
	public void testParseResponse202() throws Exception {
		UUID uuid = UUID.randomUUID();
		String json = "{\"message_uuid\":\""+uuid+"\"}";
		MessageResponse response = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(202, json));
		assertEquals(uuid, response.getMessageUuid());
	}

	@Test
	public void testParseResponseFull() throws Exception {
		final int statusCode = 422;
		final String json = "{\n" +
				"  \"type\": \"https://developer.nexmo.com/api-errors/messages-olympus#1120\",\n" +
				"  \"title\": \"Invalid sender\",\n" +
				"  \"detail\": \"The `from` parameter is invalid for the given channel.\",\n" +
				"  \"instance\": \"bf0ca0bf927b3b52e3cb03217e1a1ddf\"\n" +
				"}";

		try {
			endpoint.parseResponse(TestUtils.makeJsonHttpResponse(statusCode, json));
			fail("Expected "+MessageResponseException.class.getName());
		}
		catch (MessageResponseException mrx) {
			MessageResponseException expected = MessageResponseException.fromJson(json);
			expected.statusCode = statusCode;
			assertEquals(expected, mrx);
		}
	}

	@Test
	public void testParseResponseNoBody() throws Exception {
		final int statusCode = 429;
		try {
			endpoint.parseResponse(TestUtils.makeJsonHttpResponse(statusCode, ""));
			fail("Expected "+MessageResponseException.class.getName());
		}
		catch (MessageResponseException mrx) {
			MessageResponseException expected = MessageResponseException.fromJson("");
			expected.title = "OK";  // This is what TestUtils.makeJsonHttpResponse sets it to
			expected.statusCode = statusCode;
			assertEquals(expected, mrx);
		}
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().apiBaseUri(baseUri).build());
		endpoint = new SendMessageEndpoint(wrapper);

		MessageRequest request = MmsVcardRequest.builder()
				.url("https://www.example.org/path/to/contacts.vcf")
				.from("447700900001").to("447700900000").build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("POST", builder.getMethod());
		assertEquals(baseUri+"/v1/messages", builder.getUri().toString());
	}
}
