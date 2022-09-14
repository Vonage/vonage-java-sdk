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
import static org.junit.Assert.assertThrows;
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
		CreateSessionRequest request = CreateSessionRequest.builder().build();
		String msUrl = "http://example.com/resource",
			createDt = "abc123",
			responseJson = "{\n" +
				"    \"session_id\": \""+sessionId+"\",\n" +
				"    \"application_id\": \""+ applicationId +"\",\n" +
				"    \"create_dt\": \""+createDt+"\",\n" +
				"    \"media_server_url\": \""+msUrl+"\"\n" +
				"}";
		wrapper.setHttpClient(stubHttpClient(200, responseJson));

		CreateSessionResponse response = client.createSession(request);
		assertEquals(sessionId, response.getSessionId());
		assertEquals(applicationId, response.getApplicationId());
		assertEquals(createDt, response.getCreateDt());
		assertEquals(msUrl, response.getMediaServerUrl());
		assertThrows(NullPointerException.class, () -> client.createSession(null));
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
		assertThrows(IllegalArgumentException.class, () -> client.listStreams(null));
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
		assertThrows(IllegalArgumentException.class, () -> client.getStream(null, streamId));
		assertThrows(IllegalArgumentException.class, () -> client.getStream(sessionId, null));
	}

	@Test
	public void testSetStreamLayout() throws Exception {
		wrapper.setHttpClient(stubHttpClient(200));
		List<SessionStream> layouts = Collections.emptyList();
		client.setStreamLayout(sessionId, layouts);
		assertThrows(IllegalArgumentException.class, () -> client.setStreamLayout(null, layouts));
		assertThrows(NullPointerException.class, () -> client.setStreamLayout(sessionId, null));
	}

	@Test
	public void testSignal() throws Exception {
		String connectionId = UUID.randomUUID().toString();
		SignalRequest signalRequest = SignalRequest.builder().data("d").type("t").build();
		wrapper.setHttpClient(stubHttpClient(200));

		client.signal(sessionId, connectionId, signalRequest);
		assertThrows(IllegalArgumentException.class, () ->
				client.signal(sessionId, connectionId, null)
		);
		assertThrows(IllegalArgumentException.class, () ->
				client.signal(sessionId, null, signalRequest)
		);
		assertThrows(IllegalArgumentException.class, () ->
				client.signal(null, connectionId, signalRequest)
		);
	}

	@Test
	public void testSignalAll() throws Exception {
		SignalRequest signalRequest = SignalRequest.builder().data("d").type("t").build();
		wrapper.setHttpClient(stubHttpClient(200));

		client.signalAll(sessionId, signalRequest);
		assertThrows(IllegalArgumentException.class, () -> client.signalAll(sessionId, null));
		assertThrows(IllegalArgumentException.class, () -> client.signalAll(null, signalRequest));
	}
}
