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
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.Assert.assertEquals;
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
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, MeetingsClientTest.SAMPLE_RECORDING_RESPONSE);
		MeetingsClientTest.assertEqualsSampleRecording(endpoint.parseResponse(mockResponse));
	}

	@Test
	public void testCustomUri() throws Exception {
		UUID recordingId = UUID.randomUUID();
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		GetRecordingEndpoint endpoint = new GetRecordingEndpoint(wrapper);
		String expectedUri = baseUri + "/beta/meetings/recordings/"+recordingId;
		RequestBuilder builder = endpoint.makeRequest(recordingId);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("GET", builder.getMethod());
        assertEquals(expectedUri, builder.build().getURI().toString());
	}
}