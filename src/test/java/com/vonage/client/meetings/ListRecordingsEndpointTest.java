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
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ListRecordingsEndpointTest {
	private ListRecordingsEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new ListRecordingsEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
	}
	
	@Test
	public void testMakeRequest() throws Exception {
		String sessionId = "2_MX40NjMwODczMn5-MTU3NTgyODEwNzQ2M...";
		RequestBuilder builder = endpoint.makeRequest(sessionId);
		assertEquals("GET", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/beta/meetings/sessions/"+sessionId+"/recordings";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		String expectedPayload = "{\"_embedded\":{\"recordings\":[{},"+MeetingsClientTest.SAMPLE_RECORDING_RESPONSE+"]}}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedPayload);
		ListRecordingsResponse parsed = endpoint.parseResponse(mockResponse);
		assertEquals(2, parsed.getRecordings().size());
		MeetingsClientTest.assertEqualsSampleRecording(parsed.getRecordings().get(1));
		Recording empty = parsed.getRecordings().get(0);
		assertNotNull(empty);
		assertNull(empty.getId());
		assertNull(empty.getLinks());
		assertNull(empty.getEndedAt());
		assertNull(empty.getEndedAtAsString());
		assertNull(empty.getStartedAt());
		assertNull(empty.getStartedAtAsString());
		assertNull(empty.getStatus());
		assertNull(empty.getSessionId());
	}

	@Test
	public void testCustomUri() throws Exception {
		String sessionId = "2_MX40NjMwODczMn5-MTU3NTgyODEwNzQ2Jk.." , baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new ListRecordingsEndpoint(wrapper);
		String expectedUri = baseUri + "/beta/meetings/sessions/"+sessionId+"/recordings";
		RequestBuilder builder = endpoint.makeRequest(sessionId);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("GET", builder.getMethod());
	}

	@Test(expected = HttpResponseException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, ""));
	}
}