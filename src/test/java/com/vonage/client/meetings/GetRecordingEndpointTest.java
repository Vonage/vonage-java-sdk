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
package com.vonage.client.meetings;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class GetRecordingEndpointTest {
	private GetRecordingEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = new GetRecordingEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
	}
	
	@Test
	public void testDefaultUri() throws Exception {
		UUID recordingId = UUID.randomUUID();
		RequestBuilder builder = endpoint.makeRequest(recordingId);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/beta/meetings/recordings/"+recordingId;
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		String expectedResponse = "{\"nonsense\":0,\"_links\":{\"url\":{}}}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		Recording parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
		assertNull(parsed.getUrl());
		assertNull(parsed.getStatus());
		assertNull(parsed.getSessionId());
		assertNull(parsed.getStartedAt());
		assertNull(parsed.getEndedAt());
		assertNull(parsed.getId());
	}

	@Test
	public void testCustomUri() throws Exception {
		UUID recordingId = UUID.randomUUID();
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new GetRecordingEndpoint(wrapper);
		String expectedUri = baseUri + "/beta/meetings/recordings/"+recordingId;
		RequestBuilder builder = endpoint.makeRequest(recordingId);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("GET", builder.getMethod());
	}

	@Test(expected = HttpResponseException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, ""));
	}

	@Test(expected = VonageResponseParseException.class)
	public void testParseMalformedResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, "{malformed]"));
	}
}