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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import java.util.List;

public class VideoClientTest extends ClientTest<VideoClient> {
	static final String
			applicationId = "78d335fa-323d-0114-9c3d-d6f0d48968cf",
			sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN",
			streamId = "abc123",
			archiveId = "b40ef09b-3811-4726-b508-e41a0f96c68f",
			connectionId = "09141e29-8770-439b-b180-337d7e637545",
			archiveJson = "{\n" +
					"  \"createdAt\": 1384221730000,\n" +
					"  \"duration\": 5049,\n" +
					"  \"hasAudio\": true,\n" +
					"  \"hasVideo\": true,\n" +
					"  \"id\": \""+archiveId+"\",\n" +
					"  \"name\": \"Foo\",\n" +
					"  \"applicationId\": \""+applicationId+"\",\n" +
					"  \"reason\": \"\",\n" +
					"  \"resolution\": \"1280x720\",\n" +
					"  \"sessionId\": \""+sessionId+"\",\n" +
					"  \"size\": 247748791,\n" +
					"  \"status\": \"available\",\n" +
					"  \"streamMode\": \"manual\",\n" +
					"  \"streams\": [\n" +
					"    {\n" +
					"      \"streamId\": \""+streamId+"\",\n" +
					"      \"hasAudio\": false,\n" +
					"      \"hasVideo\": true\n" +
					"    }\n" +
					"  ],\n" +
					"  \"url\": \"https://tokbox.s3.amazonaws.com/"+connectionId+"/archive.mp4\"\n" +
					"}",
			listArchiveJson = "{\"count\":1,\"items\":["+archiveJson+"]}";

	@Before
	public void setUp() {
		wrapper.getAuthCollection().add(new JWTAuthMethod(applicationId, new byte[0]));
		client = new VideoClient(wrapper);
	}

	static void assertArchiveEqualsExpectedJson(Archive response) {
		assertNotNull(response);
		assertEquals(Long.valueOf(1384221730000L), response.getCreatedAt());
		assertEquals(Integer.valueOf(5049), response.getDuration());
		assertTrue(response.hasAudio());
		assertTrue(response.hasVideo());
		assertEquals("b40ef09b-3811-4726-b508-e41a0f96c68f", response.getId());
		assertEquals("Foo", response.getName());
		assertEquals("", response.getReason());
		assertEquals(Resolution.HD_LANDSCAPE, response.getResolution());
		assertEquals(sessionId, response.getSessionId());
		assertEquals(Long.valueOf(247748791L), response.getSize());
		assertEquals(ArchiveStatus.AVAILABLE, response.getStatus());
		assertEquals(StreamMode.MANUAL, response.getStreamMode());
		List<ArchiveStream> streams = response.getStreams();
		assertEquals(1, streams.size());
		ArchiveStream archiveStream = streams.get(0);
		assertEquals(streamId, archiveStream.getStreamId());
		assertFalse(archiveStream.hasAudio());
		assertTrue(archiveStream.hasVideo());
		assertEquals("https://tokbox.s3.amazonaws.com/"+connectionId+"/archive.mp4", response.getUrl());
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

	@Test
	public void testForceDisconnect() throws Exception {
		wrapper.setHttpClient(stubHttpClient(200));

		client.forceDisconnect(sessionId, connectionId);
		assertThrows(IllegalArgumentException.class, () -> client.forceDisconnect(null, connectionId));
		assertThrows(IllegalArgumentException.class, () -> client.forceDisconnect(sessionId, null));
	}

	@Test
	public void testMuteStream() throws Exception {
		String responseJson = "{\n" +
				"  \"applicationId\": \"78d335fa-323d-0114-9c3d-d6f0d48968cf\",\n" +
				"  \"status\": \"ACTIVE\",\n" +
				"  \"name\": \"Joe Montana\",\n" +
				"  \"environment\": \"standard\",\n" +
				"  \"createdAt\": 1414642898000\n" +
				"}";
		wrapper.setHttpClient(stubHttpClient(200, responseJson));

		ProjectDetails response = client.muteStream(sessionId, streamId);
		assertEquals("78d335fa-323d-0114-9c3d-d6f0d48968cf", response.getApplicationId());
		assertEquals(ProjectStatus.ACTIVE, response.getStatus());
		assertEquals("Joe Montana", response.getName());
		assertEquals(ProjectEnvironment.STANDARD, response.getEnvironment());
		assertEquals(1414642898000L, response.getCreatedAt().longValue());

		assertThrows(IllegalArgumentException.class, () -> client.muteStream(null, streamId));
		assertThrows(IllegalArgumentException.class, () -> client.muteStream(sessionId, null));
	}

	@Test
	public void testMuteSession() throws Exception {
		MuteSessionRequest request = new MuteSessionRequest(true);
		String responseJson = "{\n" +
				"  \"applicationId\": \"78d335fa-323d-0114-9c3d-d6f0d48968cf\",\n" +
				"  \"status\": \"ACTIVE\",\n" +
				"  \"name\": \"Joe Montana\",\n" +
				"  \"environment\": \"standard\",\n" +
				"  \"createdAt\": 1414642898000\n" +
				"}";
		wrapper.setHttpClient(stubHttpClient(200, responseJson));

		ProjectDetails response = client.muteSession(sessionId, request);
		assertEquals("78d335fa-323d-0114-9c3d-d6f0d48968cf", response.getApplicationId());
		assertEquals(ProjectStatus.ACTIVE, response.getStatus());
		assertEquals("Joe Montana", response.getName());
		assertEquals(ProjectEnvironment.STANDARD, response.getEnvironment());
		assertEquals(1414642898000L, response.getCreatedAt().longValue());

		assertThrows(IllegalArgumentException.class, () -> client.muteSession(null, request));
		assertThrows(IllegalArgumentException.class, () -> client.muteSession(sessionId, null));
	}

	@Test
	public void testSetArchiveLayout() throws Exception {
		ArchiveLayout request = ArchiveLayout.builder()
				.type(ScreenLayoutType.HORIZONTAL).build();

		wrapper.setHttpClient(stubHttpClient(200));
		client.setArchiveLayout(archiveId, request);
		assertThrows(IllegalArgumentException.class, () -> client.setArchiveLayout(null, request));
		assertThrows(IllegalArgumentException.class, () -> client.setArchiveLayout(archiveId, null));
	}

	@Test
	public void testDeleteArchive() throws Exception {
		wrapper.setHttpClient(stubHttpClient(200));
		client.deleteArchive(archiveId);
		assertThrows(IllegalArgumentException.class, () -> client.deleteArchive(null));
	}

	@Test
	public void testAddArchiveStream() throws Exception {
		wrapper.setHttpClient(stubHttpClient(204));

		client.addArchiveStream(archiveId, streamId);
		client.addArchiveStream(archiveId, streamId, true, true);
		client.addArchiveStream(archiveId, streamId, null, false);
		client.addArchiveStream(archiveId, streamId, false, null);

		assertThrows(IllegalArgumentException.class, () -> client.addArchiveStream(null, streamId));
		assertThrows(IllegalArgumentException.class, () -> client.addArchiveStream(archiveId, null));
	}

	@Test
	public void testRemoveArchiveStream() throws Exception {
		wrapper.setHttpClient(stubHttpClient(204));
		client.removeArchiveStream(archiveId, streamId);
		assertThrows(IllegalArgumentException.class, () -> client.removeArchiveStream(null, streamId));
		assertThrows(IllegalArgumentException.class, () -> client.removeArchiveStream(archiveId, null));
	}

	@Test
	public void testStopArchive() throws Exception {
		wrapper.setHttpClient(stubHttpClient(200, archiveJson));
		assertArchiveEqualsExpectedJson(client.stopArchive(archiveId));
		assertThrows(IllegalArgumentException.class, () -> client.stopArchive(null));
	}

	@Test
	public void testGetArchive() throws Exception {
		wrapper.setHttpClient(stubHttpClient(200, archiveJson));
		assertArchiveEqualsExpectedJson(client.getArchive(archiveId));
		assertThrows(IllegalArgumentException.class, () -> client.getArchive(null));
	}

	@Test
	public void testListArchives() throws Exception {
		wrapper.setHttpClient(stubHttpClient(200, listArchiveJson));
		List<Archive> archives = client.listArchives();
		assertEquals(1, archives.size());
		assertArchiveEqualsExpectedJson(archives.get(0));

		wrapper.setHttpClient(stubHttpClient(200, listArchiveJson));
		archives = client.listArchives(sessionId);
		assertArchiveEqualsExpectedJson(archives.get(0));

		wrapper.setHttpClient(stubHttpClient(200, listArchiveJson));
		archives = client.listArchives(sessionId, 0, 1);
		assertArchiveEqualsExpectedJson(archives.get(0));

		wrapper.setHttpClient(stubHttpClient(200, listArchiveJson));
		archives = client.listArchives(null, null, 1000);
		assertArchiveEqualsExpectedJson(archives.get(0));

		wrapper.setHttpClient(stubHttpClient(200, listArchiveJson));
		archives = client.listArchives(null, 2, null);
		assertArchiveEqualsExpectedJson(archives.get(0));

		wrapper.setHttpClient(stubHttpClient(200, listArchiveJson));
		assertThrows(IllegalArgumentException.class, () -> client.listArchives(null));

		wrapper.setHttpClient(stubHttpClient(200, listArchiveJson));
		assertThrows(IllegalArgumentException.class, () -> client.listArchives(sessionId, 0, -1));

		wrapper.setHttpClient(stubHttpClient(200, listArchiveJson));
		assertThrows(IllegalArgumentException.class, () -> client.listArchives(sessionId, 1, -1));
	}

	@Test
	public void testCreateArchive() throws Exception {
		String sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN";
		CreateArchiveRequest request = CreateArchiveRequest.builder().build();

		wrapper.setHttpClient(stubHttpClient(200, archiveJson));
		assertArchiveEqualsExpectedJson(client.createArchive(sessionId, request));

		wrapper.setHttpClient(stubHttpClient(200, archiveJson));
		assertArchiveEqualsExpectedJson(client.createArchive(sessionId, null));

		wrapper.setHttpClient(stubHttpClient(200, archiveJson));
		assertThrows(IllegalArgumentException.class, () -> client.createArchive(null, request));
	}
}
