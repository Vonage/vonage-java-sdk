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

import com.vonage.client.ClientTest;
import com.vonage.client.auth.JWTAuthMethod;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class VideoClientTest extends ClientTest<VideoClient> {
	final String applicationId = UUID.randomUUID().toString();
	final String sessionId = UUID.randomUUID().toString();

	@Before
	public void setUp() {
		wrapper.getAuthCollection().add(new JWTAuthMethod(applicationId, new byte[0]));
		client = new VideoClient(wrapper);
	}

	@Test
	public void testCreateSession() throws Exception {
		String msUrl = "http://example.com/resource",
			createDt = "abc123",
			responseJson = "{\n" +
				"    \"session_id\": \""+sessionId+"\",\n" +
				"    \"application_id\": \""+ applicationId +"\",\n" +
				"    \"create_dt\": \""+createDt+"\",\n" +
				"    \"media_server_url\": \""+msUrl+"\"\n" +
				"}";
		wrapper.setHttpClient(stubHttpClient(200, responseJson));
		CreateSessionRequest request = CreateSessionRequest.builder().build();
		CreateSessionResponse response = client.createSession(request);
		assertEquals(sessionId, response.getSessionId());
		assertEquals(applicationId, response.getApplicationId());
		assertEquals(createDt, response.getCreateDt());
		assertEquals(msUrl, response.getMediaServerUrl());
	}

	@Test
	public void testListStreams() throws Exception {
		String responseJson = "{\n" +
				"  \"count\": 1,\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"id\": \"8b732909-0a06-46a2-8ea8-074e64d43422\",\n" +
				"      \"videoType\": \"screen\",\n" +
				"      \"name\": \"\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";
		wrapper.setHttpClient(stubHttpClient(200, responseJson));
		List<GetStreamResponse> response = client.listStreams(sessionId);
		assertEquals(1, response.size());
		assertEquals(VideoType.SCREEN, response.get(0).getVideoType());
		assertEquals("", response.get(0).getName());
	}

	@Test
	public void testGetStream() throws Exception {
		String streamId = UUID.randomUUID().toString();
		String responseJson = "{\n" +
				"  \"id\": \""+streamId+"\",\n" +
				"  \"videoType\": \"custom\",\n" +
				"  \"name\": \"My Stream\",\n" +
				"  \"layoutClassList\": [\n" +
				"    \"full\"\n" +
				"  ]\n" +
				"}";
		wrapper.setHttpClient(stubHttpClient(200, responseJson));
		GetStreamResponse response = client.getStream(sessionId, streamId);
		assertEquals(streamId, response.getId());
		assertEquals(VideoType.CUSTOM, response.getVideoType());
		assertEquals(1, response.getLayoutClassList().size());
		assertEquals("full", response.getLayoutClassList().get(0));
	}

	@Test
	public void testSetStreamLayout() throws Exception {
		wrapper.setHttpClient(stubHttpClient(200));
		client.setStreamLayout(sessionId, Collections.emptyList());
	}
}
