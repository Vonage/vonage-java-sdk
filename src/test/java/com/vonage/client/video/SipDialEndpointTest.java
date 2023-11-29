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
package com.vonage.client.video;

import com.vonage.client.*;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.util.UUID;

public class SipDialEndpointTest {
	private SipDialEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();
	
	@BeforeEach
	public void setUp() {
		endpoint = new SipDialEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequest() throws Exception {
		SipDialRequest request = SipDialRequest.builder()
				.token("eYjwToken").sessionId("=2SessiondID")
				.uri(URI.create("sip.example.com"), true).build();

		RequestBuilder builder = endpoint.makeRequest(request);
		Assertions.assertEquals("POST", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/"+applicationId+"/dial";
		Assertions.assertEquals(expectedUri, builder.build().getURI().toString());
		Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		String expectedPayload = "{\"sessionId\":\"=2SessiondID\",\"token\":\"eYjwToken\"," +
				"\"sip\":{\"uri\":\"sip.example.com;transport=tls\"}}";
		Assertions.assertEquals(expectedPayload, EntityUtils.toString(builder.getEntity()));

		SipDialResponse parsed = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, "{}"));
		Assertions.assertNotNull(parsed);
		Assertions.assertNull(parsed.getConnectionId());
		Assertions.assertNull(parsed.getId());
		Assertions.assertNull(parsed.getStreamId());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(
				HttpConfig.builder().videoBaseUri(baseUri).build(),
				new JWTAuthMethod(applicationId, new byte[0])
		);
		endpoint = new SipDialEndpoint(wrapper);
		String expectedUri = baseUri + "/v2/project/"+applicationId+"/dial";
		SipDialRequest request = SipDialRequest.builder()
				.token("t").sessionId("s")
				.uri(URI.create("sip:u@example.com"), false).build();

		RequestBuilder builder = endpoint.makeRequest(request);
		Assertions.assertEquals(expectedUri, builder.build().getURI().toString());
		Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"sessionId\":\"s\",\"token\":\"t\",\"sip\":{\"uri\":\"sip:u@example.com\"}}";
		Assertions.assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		Assertions.assertEquals("POST", builder.getMethod());
	}

	@Test
	public void test500Response() throws Exception {
		assertThrows(HttpResponseException.class, () ->
				endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""))
		);
	}

	@Test
	public void testInvalidResponseJson() throws Exception {
		assertThrows(VonageResponseParseException.class, () -> SipDialResponse.fromJson("{malformed]"));
	}
}