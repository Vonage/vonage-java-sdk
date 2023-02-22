/*
 *   Copyright 2022 Vonage
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

import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.time.Instant;
import java.util.UUID;

public class StopArchiveEndpointTest {
	private StopArchiveEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();
	
	@Before
	public void setUp() {
		endpoint = new StopArchiveEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequest() throws Exception {
		String archiveId = UUID.randomUUID().toString();
		RequestBuilder builder = endpoint.makeRequest(archiveId);
		assertEquals("POST", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/" +
				applicationId+"/archive/"+archiveId+"/stop";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		String expectedPayload = "{\n" +
				"  \"createdAt\": 1384221730000,\n" +
				"  \"duration\": 5049,\n" +
				"  \"hasAudio\": true,\n" +
				"  \"id\": \"b40ef09b-3811-4726-b508-e41a0f96c68f\",\n" +
				"  \"name\": \"Foo\",\n" +
				"  \"applicationId\": \"78d335fa-323d-0114-9c3d-d6f0d48968cf\",\n" +
				"  \"reason\": \"\",\n" +
				"  \"resolution\": \"720x1280\",\n" +
				"  \"sessionId\": \"flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN\",\n" +
				"  \"size\": 247748791,\n" +
				"  \"status\": \"available\",\n" +
				"  \"streamMode\": \"auto\",\n" +
				"  \"streams\": [\n" +
				"    {\n" +
				"      \"streamId\": \"482bce73-f882-40fd-8ca5-cb74ff416036\",\n" +
				"      \"hasAudio\": true,\n" +
				"      \"hasVideo\": true\n" +
				"    }\n" +
				"  ],\n" +
				"  \"url\": \"https://tokbox.com.archive2.s3.amazonaws.com/123456/09141e29-8770-439b-b180-337d7e637545/archive.mp4\"\n" +
				"}";
		Archive response = endpoint.parseResponse(TestUtils.makeJsonHttpResponse(200, expectedPayload));
		assertNotNull(response);
		assertEquals(5049, response.getDurationRaw().longValue());
		assertEquals(ArchiveStatus.AVAILABLE, response.getStatus());
		assertEquals(1, response.getStreams().size());
		assertEquals(UUID.fromString("482bce73-f882-40fd-8ca5-cb74ff416036"), response.getStreams().get(0).getStreamId());
		assertEquals(StreamMode.AUTO, response.getStreamMode());
		assertEquals(Resolution.HD_PORTRAIT, response.getResolution());
		assertEquals("", response.getReason());
		assertEquals("Foo", response.getName());
		assertTrue(response.hasAudio());
		assertNull(response.hasVideo());
		assertEquals(Instant.ofEpochSecond(1384221730L), response.getCreatedAt());
		assertEquals("flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN", response.getSessionId());
	}

	@Test(expected = HttpResponseException.class)
	public void test500Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""));
	}
}