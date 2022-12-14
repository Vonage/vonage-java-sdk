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
import org.junit.function.ThrowingRunnable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

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

	void stubResponseAndAssertThrowsIAX(String response, ThrowingRunnable invocation) throws Exception {
		stubResponseAndAssertThrows(response, invocation, IllegalArgumentException.class);
	}

	void stubArchiveJsonAndAssertThrows(ThrowingRunnable invocation) throws Exception {
		stubResponseAndAssertThrowsIAX(archiveJson, invocation);
	}

	void stubListArchiveJsonAndAssertThrows(ThrowingRunnable invocation) throws Exception {
		stubResponseAndAssertThrowsIAX(listArchiveJson, invocation);
	}

	void stubArchiveJsonAndAssertEquals(Supplier<Archive> invocation) throws Exception {
		stubResponse(archiveJson);
		assertArchiveEqualsExpectedJson(invocation.get());
	}

	void stubListArchiveJsonAndAssertEquals(Supplier<List<Archive>> invocation) throws Exception {
		stubResponse(listArchiveJson);
		List<Archive> archives = invocation.get();
		assertEquals(1, archives.size());
		assertArchiveEqualsExpectedJson(archives.get(0));
	}

	void stubMuteResponseAndAssertEquals(Supplier<ProjectDetails> invocation) throws Exception {
		String responseJson = "{\n" +
				"  \"applicationId\": \"78d335fa-323d-0114-9c3d-d6f0d48968cf\",\n" +
				"  \"status\": \"ACTIVE\",\n" +
				"  \"name\": \"Joe Montana\",\n" +
				"  \"environment\": \"standard\",\n" +
				"  \"createdAt\": 1414642898000\n" +
				"}";
		stubResponse(responseJson);
		ProjectDetails response = invocation.get();
		assertEquals("78d335fa-323d-0114-9c3d-d6f0d48968cf", response.getApplicationId());
		assertEquals(ProjectStatus.ACTIVE, response.getStatus());
		assertEquals("Joe Montana", response.getName());
		assertEquals(ProjectEnvironment.STANDARD, response.getEnvironment());
		assertEquals(1414642898000L, response.getCreatedAt().longValue());
	}

	void stubResponseAndAssertThrowsIAX(int statusCode, ThrowingRunnable invocation) throws Exception {
		stubResponseAndAssertThrows(statusCode, invocation, IllegalArgumentException.class);
	}

	void stubResponseAndAssertThrowsIAX(ThrowingRunnable invocation) throws Exception {
		stubResponseAndAssertThrowsIAX(200, invocation);
	}

	void stubResponseAndAssertThrowsNPE(ThrowingRunnable invocation) throws Exception {
		stubResponseAndAssertThrows(200, invocation, NullPointerException.class);
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
			responseJson = "[{\n" +
				"    \"session_id\": \""+sessionId+"\",\n" +
				"    \"application_id\": \""+ applicationId +"\",\n" +
				"    \"create_dt\": \""+createDt+"\",\n" +
				"    \"media_server_url\": \""+msUrl+"\"\n" +
				"}]";

		stubResponse(responseJson);
		CreateSessionResponse response = client.createSession(request);
		assertEquals(sessionId, response.getSessionId());
		assertEquals(applicationId, response.getApplicationId());
		assertEquals(createDt, response.getCreateDt());
		assertEquals(msUrl, response.getMediaServerUrl());

		stubResponse(responseJson);
		response = client.createSession();
		assertNotNull(response);
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

		stubResponse(responseJson);
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

		stubResponse(responseJson);
		GetStreamResponse response = client.getStream(sessionId, streamId);
		assertEquals(streamId, response.getId());
		assertEquals(VideoType.CUSTOM, response.getVideoType());
		assertEquals(1, response.getLayoutClassList().size());
		assertEquals("full", response.getLayoutClassList().get(0));

		stubResponseAndAssertThrowsIAX(() -> client.getStream(null, streamId));
		stubResponseAndAssertThrowsIAX(() -> client.getStream(sessionId, null));
	}

	@Test
	public void testSetStreamLayout() throws Exception {
		SessionStream stream = SessionStream.builder(streamId).build();
		List<SessionStream> layoutsList = Collections.singletonList(stream);
		SessionStream[] layoutsArray = new SessionStream[]{stream};
		stubResponseAndRun(() -> client.setStreamLayout(sessionId, layoutsList));
		stubResponseAndRun(() -> client.setStreamLayout(sessionId, layoutsArray));
		stubResponseAndAssertThrowsIAX(() -> client.setStreamLayout(null, layoutsList));
		stubResponseAndAssertThrowsIAX(() -> client.setStreamLayout(null, layoutsArray));
		stubResponseAndAssertThrowsIAX(() -> client.setStreamLayout(sessionId, (List<SessionStream>) null));
		stubResponseAndAssertThrowsNPE(() -> client.setStreamLayout(sessionId, (SessionStream[]) null));
	}

	@Test
	public void testSignal() throws Exception {
		SignalRequest signalRequest = SignalRequest.builder().data("d").type("t").build();
		stubResponseAndRun(() -> client.signal(sessionId, connectionId, signalRequest));
		stubResponseAndAssertThrowsIAX(() -> client.signal(sessionId, connectionId, null));
		stubResponseAndAssertThrowsIAX(() -> client.signal(sessionId, null, signalRequest));
		stubResponseAndAssertThrowsIAX(() -> client.signal(null, connectionId, signalRequest));
	}

	@Test
	public void testSignalAll() throws Exception {
		SignalRequest signalRequest = SignalRequest.builder().data("d").type("t").build();
		stubResponseAndRun(() -> client.signalAll(sessionId, signalRequest));
		stubResponseAndAssertThrowsIAX(() -> client.signalAll(sessionId, null));
		stubResponseAndAssertThrowsIAX(() -> client.signalAll(null, signalRequest));
	}

	@Test
	public void testForceDisconnect() throws Exception {
		stubResponseAndRun(() -> client.forceDisconnect(sessionId, connectionId));
		stubResponseAndAssertThrowsIAX(() -> client.forceDisconnect(null, connectionId));
		stubResponseAndAssertThrowsIAX(() -> client.forceDisconnect(sessionId, null));
	}

	@Test
	public void testMuteStream() throws Exception {
		stubMuteResponseAndAssertEquals(() -> client.muteStream(sessionId, streamId));
		stubResponseAndAssertThrowsIAX(() -> client.muteStream(null, streamId));
		stubResponseAndAssertThrowsIAX(() -> client.muteStream(sessionId, null));
	}

	@Test
	public void testMuteSession() throws Exception {
		Collection<String>
				nullStreamIdsCol = null,
				emptyStreamIdsCol = Collections.emptyList(),
				singleStreamIdCol = Collections.singleton(streamId);
		String[]
				nullStreamIdsArr = null,
				emptyStreamIdsArr = {},
				singleStreamIdArr = {streamId};

		stubMuteResponseAndAssertEquals(() -> client.muteSession(sessionId, true));
		stubMuteResponseAndAssertEquals(() -> client.muteSession(sessionId, false, nullStreamIdsCol));
		stubMuteResponseAndAssertEquals(() -> client.muteSession(sessionId, true, nullStreamIdsArr));
		stubMuteResponseAndAssertEquals(() -> client.muteSession(sessionId, true, emptyStreamIdsCol));
		stubMuteResponseAndAssertEquals(() -> client.muteSession(sessionId, false, emptyStreamIdsArr));
		stubMuteResponseAndAssertEquals(() -> client.muteSession(sessionId, true, singleStreamIdCol));
		stubMuteResponseAndAssertEquals(() -> client.muteSession(sessionId, false, singleStreamIdArr));
		stubResponseAndAssertThrowsIAX(() -> client.muteSession(null, false));
	}

	@Test
	public void testSetArchiveLayout() throws Exception {
		ArchiveLayout request = ArchiveLayout.builder(ScreenLayoutType.HORIZONTAL).build();
		stubResponseAndRun(() -> client.setArchiveLayout(archiveId, request));
		stubResponseAndAssertThrowsIAX(() -> client.setArchiveLayout(null, request));
		stubResponseAndAssertThrowsIAX(() -> client.setArchiveLayout(archiveId, null));
	}

	@Test
	public void testDeleteArchive() throws Exception {
		stubResponseAndRun(() -> client.deleteArchive(archiveId));
		stubResponseAndAssertThrowsIAX(() -> client.deleteArchive(null));
	}

	@Test
	public void testAddArchiveStream() throws Exception {
		stubResponseAndRun(204, () -> client.addArchiveStream(archiveId, streamId));
		stubResponseAndRun(204, () -> client.addArchiveStream(archiveId, streamId, true, true));
		stubResponseAndRun(204, () -> client.addArchiveStream(archiveId, streamId, null, false));
		stubResponseAndRun(204, () -> client.addArchiveStream(archiveId, streamId, false, null));
		stubResponseAndAssertThrowsIAX(204, () -> client.addArchiveStream(null, streamId));
		stubResponseAndAssertThrowsIAX(204, () -> client.addArchiveStream(archiveId, null));
	}

	@Test
	public void testRemoveArchiveStream() throws Exception {
		stubResponseAndRun(204, () -> client.removeArchiveStream(archiveId, streamId));
		stubResponseAndAssertThrowsIAX(204, () -> client.removeArchiveStream(null, streamId));
		stubResponseAndAssertThrowsIAX(204, () -> client.removeArchiveStream(archiveId, null));
	}

	@Test
	public void testStopArchive() throws Exception {
		stubArchiveJsonAndAssertEquals(() -> client.stopArchive(archiveId));
		stubArchiveJsonAndAssertThrows(() -> client.stopArchive(null));
	}

	@Test
	public void testGetArchive() throws Exception {
		stubArchiveJsonAndAssertEquals(() -> client.getArchive(archiveId));
		stubArchiveJsonAndAssertThrows(() -> client.getArchive(null));
	}

	@Test
	public void testListArchives() throws Exception {
		stubListArchiveJsonAndAssertEquals(() -> client.listArchives());
		stubListArchiveJsonAndAssertEquals(() -> client.listArchives(ListArchivesRequest.builder().build()));
		stubListArchiveJsonAndAssertEquals(() -> client.listArchives(null));
	}

	@Test
	public void testCreateArchive() throws Exception {
		CreateArchiveRequest request = CreateArchiveRequest.builder(sessionId).build();
		stubArchiveJsonAndAssertEquals(() -> client.createArchive(request));
		stubArchiveJsonAndAssertThrows(() -> client.createArchive(null));
	}

	@Test
	public void testGenerateToken() throws Exception {
		String token = client.generateToken(sessionId, null);
		assertTrue(token.length() > 100);
		TokenOptions options = TokenOptions.builder().build();
		token = client.generateToken(sessionId, options);
		assertTrue(token.length() > 100);
		assertThrows(IllegalArgumentException.class, () -> client.generateToken(null, options));
	}
}
