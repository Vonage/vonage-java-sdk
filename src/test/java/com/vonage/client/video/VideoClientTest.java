/*
 *   Copyright 2024 Vonage
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
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import static com.vonage.client.TestUtils.*;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

public class VideoClientTest extends ClientTest<VideoClient> {
	static final String
			applicationId = "78d335fa-323d-0114-9c3d-d6f0d48968cf",
			sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN",
			streamId = "9c18b42f-ee38-4b38-99bb-d37b2eca9741",
			token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.X.Z",
			id = "b0a5a8c7-dc38-459f-a48d-a7f2008da853",
			archiveId = "b40ef09b-3811-4726-b508-e41a0f96c68f",
			broadcastId = "1748b707-0a81-464c-9759-c46ad10d3734",
			connectionId = "09141e29-8770-439b-b180-337d7e637545",
			captionsId = "7c0680fc-6274-4de5-a66f-d0648e8d3ac2",
			randomId = UUID.randomUUID().toString(),
			wssUri = "wss://example.com/ws-endpoint",
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
					"      \"hasAudio\": \"true\",\n" +
					"      \"hasVideo\": \"false\"\n" +
					"    },\n" +
					"    {\n" +
					"      \"streamId\": \"482bce73-f882-40fd-8ca5-cb74ff416036\",\n" +
					"      \"hasAudio\": \"false\",\n" +
					"      \"hasVideo\": \"true\"\n" +
					"    }\n" +
					"  ],\n" +
					"  \"url\": \"https://tokbox.s3.amazonaws.com/"+connectionId+"/archive.mp4\"\n" +
					"}",
			listArchiveJson = "{\"count\":1,\"items\":["+archiveJson+"]}",
			broadcastJson = "{\n" +
					"  \"id\": \""+broadcastId+"\",\n" +
					"  \"sessionId\": \""+sessionId+"\",\n" +
					"  \"multiBroadcastTag\": \"broadcast-1234b\",\n" +
					"  \"applicationId\": \""+applicationId+"\",\n" +
					"  \"createdAt\": 1437676551000,\n" +
					"  \"updatedAt\": 1437876551000,\n" +
					"  \"maxDuration\": 5400,\n" +
					"  \"maxBitrate\": 2000000,\n" +
					"  \"broadcastUrls\": {\n" +
					"    \"hls\": \"http://server/fakepath/playlist.m3u8\",\n" +
					"    \"rtmp\": [\n" +
					"      {\n" +
					"        \"id\": \"foo\",\n" +
					"        \"status\": \"live\",\n" +
					"        \"serverUrl\": \"rtmps://myfooserver/myfooapp\",\n" +
					"        \"streamName\": \"myfoostream\"\n" +
					"      }\n" +
					"    ]\n" +
					"  },\n" +
					"  \"settings\": {\n" +
					"    \"hls\": {\n" +
					"      \"lowLatency\": true,\n" +
					"      \"dvr\": false\n" +
					"    }\n" +
					"  },\n" +
					"  \"resolution\": \"720x1280\",\n" +
					"  \"hasAudio\": true,\n" +
					"  \"hasVideo\": true,\n" +
					"  \"streamMode\": \"manual\",\n" +
					"  \"status\": \"started\",\n" +
					"  \"streams\": [\n" +
					"    {\n" +
					"      \"streamId\": \""+streamId+"\",\n" +
					"      \"hasAudio\": \"true\",\n" +
					"      \"hasVideo\": \"false\"\n" +
					"    },\n" +
					"    {\n" +
					"      \"streamId\": \"482bce73-f882-40fd-8ca5-cb74ff416036\",\n" +
					"      \"hasAudio\": \"false\",\n" +
					"      \"hasVideo\": \"true\"\n" +
					"    }\n" +
					"  ]\n" +
					"}",
			listBroadcastJson = "{\"count\":1,\"items\":["+broadcastJson+"]}";

	public VideoClientTest() {
		wrapper = new HttpWrapper(new JWTAuthMethod(applicationId, new byte[0]));
		client = new VideoClient(wrapper);
	}

	void stubResponseAndRun(Runnable invocation) throws Exception {
		stubResponseAndRun(200, invocation);
	}

	void stubResponseAndAssertThrowsIAX(int statusCode, Executable invocation) throws Exception {
		stubResponseAndAssertThrows(statusCode, invocation, IllegalArgumentException.class);
	}

	void stubResponseAndAssertThrowsIAX(Executable invocation) throws Exception {
		stubResponseAndAssertThrowsIAX(200, invocation);
	}

	void stubResponseAndAssertThrowsIAX(String response, Executable invocation) throws Exception {
		stubResponseAndAssertThrows(response, invocation, IllegalArgumentException.class);
	}

	void stubResponseAndAssertThrowsNPE(Executable invocation) throws Exception {
		stubResponseAndAssertThrows(200, invocation, NullPointerException.class);
	}

	void stubResponseAndAssertThrowsVideoException(int statusCode, String response,
																  Executable invocation) throws Exception {
		assertApiResponseException(statusCode, response, VideoResponseException.class, invocation);
	}
	
	void stubArchiveJsonAndAssertThrows(Executable invocation) throws Exception {
		stubResponseAndAssertThrowsIAX(archiveJson, invocation);
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

	static void assertArchiveEqualsExpectedJson(Archive response) {
		testJsonableBaseObject(response);
		assertEquals("https://tokbox.s3.amazonaws.com/"+connectionId+"/archive.mp4", response.getUrl().toString());
		assertEquals(Long.valueOf(1384221730000L), response.getCreatedAtMillis());
		assertEquals(Instant.ofEpochSecond(1384221730L), response.getCreatedAt());
		assertEquals(Integer.valueOf(5049), response.getDurationSeconds());
		assertEquals(Duration.ofSeconds(5049), response.getDuration());
		assertTrue(response.hasAudio());
		assertTrue(response.hasVideo());
		assertEquals(archiveId, response.getId().toString());
		assertEquals("Foo", response.getName());
		assertEquals("", response.getReason());
		assertEquals(Resolution.HD_LANDSCAPE, response.getResolution());
		assertEquals(sessionId, response.getSessionId());
		assertEquals(applicationId, response.getApplicationId().toString());
		assertEquals(Long.valueOf(247748791L), response.getSize());
		assertEquals(ArchiveStatus.AVAILABLE, response.getStatus());
		assertVideoStreamsEqualsExpectedJson(response);
	}

	static void assertVideoStreamsEqualsExpectedJson(StreamComposition response) {
		assertEquals(StreamMode.MANUAL, response.getStreamMode());
		List<VideoStream> streams = response.getStreams();
		assertNotNull(streams);
		assertEquals(2, streams.size());
		VideoStream stream1 = streams.get(0);
		assertNotNull(stream1);
		assertEquals(streamId, stream1.getStreamId().toString());
		assertTrue(stream1.hasAudio());
		assertFalse(stream1.hasVideo());
		VideoStream stream2 = streams.get(1);
		assertNotNull(stream2);
		assertEquals(UUID.fromString("482bce73-f882-40fd-8ca5-cb74ff416036"), stream2.getStreamId());
		assertTrue(stream2.hasVideo());
		assertFalse(stream2.hasAudio());
	}

	void stubBroadcastJsonAndAssertThrows(Executable invocation) throws Exception {
		stubResponseAndAssertThrowsIAX(broadcastJson, invocation);
	}

	void stubBroadcastJsonAndAssertEquals(Supplier<Broadcast> invocation) throws Exception {
		stubResponse(broadcastJson);
		assertBroadcastEqualsExpectedJson(invocation.get());
	}

	void stubListBroadcastJsonAndAssertEquals(Supplier<List<Broadcast>> invocation) throws Exception {
		stubResponse(listBroadcastJson);
		List<Broadcast> broadcasts = invocation.get();
		assertEquals(1, broadcasts.size());
		assertBroadcastEqualsExpectedJson(broadcasts.get(0));
	}

	static void assertBroadcastEqualsExpectedJson(Broadcast response) {
		assertNotNull(response);
		assertEquals(sessionId, response.getSessionId());
		assertEquals(broadcastId, response.getId().toString());
		assertEquals(applicationId, response.getApplicationId().toString());
		assertTrue(response.hasAudio());
		assertTrue(response.hasVideo());
		assertEquals(BroadcastStatus.STARTED, response.getStatus());
		assertEquals(Resolution.HD_PORTRAIT, response.getResolution());
		assertEquals(1437676551000L, response.getCreatedAtMillis().longValue());
		assertEquals(1437876551000L, response.getUpdatedAtMillis().longValue());
		assertEquals("broadcast-1234b", response.getMultiBroadcastTag());
		assertEquals(Duration.ofSeconds(5400), response.getMaxDuration());
		assertEquals(2000000, response.getMaxBitrate().intValue());
		BroadcastUrls broadcastUrls = response.getBroadcastUrls();
		assertNotNull(broadcastUrls);
		assertEquals("http://server/fakepath/playlist.m3u8", broadcastUrls.getHls().toString());
		List<Rtmp> rtmps = broadcastUrls.getRtmps();
		assertNotNull(rtmps);
		assertEquals(1, rtmps.size());
		Rtmp rtmp = rtmps.get(0);
		assertNotNull(rtmp);
		assertEquals("rtmps://myfooserver/myfooapp", rtmp.getServerUrl().toString());
		assertEquals(RtmpStatus.LIVE, rtmp.getStatus());
		assertEquals("foo", rtmp.getId());
		assertEquals("myfoostream", rtmp.getStreamName());
		Hls hls = response.getHlsSettings();
		assertNotNull(hls);
		assertFalse(hls.dvr());
		assertTrue(hls.lowLatency());
		assertVideoStreamsEqualsExpectedJson(response);
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
		assertEquals(applicationId, response.getApplicationId().toString());
		assertEquals(createDt, response.getCreateDt());
		assertEquals(msUrl, response.getMediaServerUrl().toString());

		stubResponse(responseJson);
		response = client.createSession();
		assertNotNull(response);
		assertEquals(sessionId, response.getSessionId());
		assertEquals(applicationId, response.getApplicationId().toString());
		assertEquals(createDt, response.getCreateDt());
		assertEquals(msUrl, response.getMediaServerUrl().toString());

		responseJson = "{\n" + "  \"code\": 400,\n" + "  \"message\": "+
				"\"Invalid request. This response may indicate that data in your request data is invalid JSON. "+
			    "Or it may indicate that you do not pass in a session ID or you passed in an invalid stream ID.\"\n}";

		stubResponseAndAssertThrowsVideoException(400, responseJson, () -> client.createSession(null));
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

		responseJson = "{\n" +
			  "  \"code\": 404,\n" +
			  "  \"message\": \"The session exists but has not had any streams added to it yet.\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(404, responseJson, () -> client.listStreams(sessionId));
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

		stubResponse(200, responseJson);
		GetStreamResponse response = client.getStream(sessionId, streamId);
		assertEquals(streamId, response.getId().toString());
		assertEquals(VideoType.CUSTOM, response.getVideoType());
		assertEquals(1, response.getLayoutClassList().size());
		assertEquals("full", response.getLayoutClassList().get(0));

		stubResponseAndAssertThrowsIAX(() -> client.getStream(null, streamId));
		stubResponseAndAssertThrowsIAX(() -> client.getStream(sessionId, null));

		responseJson = "{\n" +
			  "  \"code\": 408,\n" +
			  "  \"message\": \"You passed in an invalid stream ID.\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(408, responseJson,
			  () -> client.getStream(sessionId, streamId)
		);
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

		String responseJson = "{\n" +
			  "  \"code\": 403,\n" +
			  "  \"message\": \"Authentication error\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(403, responseJson,
			  () -> client.setStreamLayout(sessionId, layoutsList)
		);
	}

	@Test
	public void testSignal() throws Exception {
		SignalRequest signalRequest = SignalRequest.builder().data("d").type("t").build();
		stubResponseAndRun(204, () -> client.signal(sessionId, connectionId, signalRequest));
		stubResponseAndAssertThrowsIAX(() -> client.signal(sessionId, connectionId, null));
		stubResponseAndAssertThrowsIAX(() -> client.signal(sessionId, null, signalRequest));
		stubResponseAndAssertThrowsIAX(() -> client.signal(null, connectionId, signalRequest));

		String responseJson = "{\n  \"code\": 413,\n  \"message\": \"The type string exceeds the maximum" +
			  "length (128 bytes), or the data string exceeds the maximum size (8 kB).\"\n}";
		stubResponseAndAssertThrowsVideoException(413, responseJson,
			  () -> client.signal(sessionId, connectionId, signalRequest)
		);
	}

	@Test
	public void testSignalAll() throws Exception {
		SignalRequest signalRequest = SignalRequest.builder().data("d").type("t").build();
		stubResponseAndRun(204, () -> client.signalAll(sessionId, signalRequest));
		stubResponseAndAssertThrowsIAX(() -> client.signalAll(sessionId, null));
		stubResponseAndAssertThrowsIAX(() -> client.signalAll(null, signalRequest));

		String responseJson = "{\n  \"code\": 413,\n  \"message\": \"The type string exceeds the maximum" +
			  "length (128 bytes), or the data string exceeds the maximum size (8 kB).\"\n}";
		stubResponseAndAssertThrowsVideoException(413, responseJson,
			  () -> client.signalAll(sessionId, signalRequest)
		);
	}

	@Test
	public void testForceDisconnect() throws Exception {
		stubResponseAndRun(204, () -> client.forceDisconnect(sessionId, connectionId));
		stubResponseAndAssertThrowsIAX(() -> client.forceDisconnect(null, connectionId));
		stubResponseAndAssertThrowsIAX(() -> client.forceDisconnect(sessionId, null));

		String responseJson = "{\n  \"code\": 403,\n  \"message\": " +
			  "\"You are not authorized to forceDisconnect, check your authentication credentials.\"\n}";
		stubResponseAndAssertThrowsVideoException(403, responseJson,
			  () -> client.forceDisconnect(sessionId, connectionId)
		);
	}

	@Test
	public void testMuteStream() throws Exception {
		stubResponseAndRun(() -> client.muteStream(sessionId, streamId));
		stubResponseAndAssertThrowsIAX(() -> client.muteStream(null, streamId));
		stubResponseAndAssertThrowsIAX(() -> client.muteStream(sessionId, null));

		String responseJson = "{\n" +
			  "  \"code\": 404,\n" +
			  "  \"message\": \"Not found. The session or stream is not found\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(404, responseJson,
			  () -> client.muteStream(sessionId, streamId)
		);
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

		stubResponseAndRun(() -> client.muteSession(sessionId, true));
		stubResponseAndRun(() -> client.muteSession(sessionId, false, nullStreamIdsCol));
		stubResponseAndRun(() -> client.muteSession(sessionId, true, nullStreamIdsArr));
		stubResponseAndRun(() -> client.muteSession(sessionId, true, emptyStreamIdsCol));
		stubResponseAndRun(() -> client.muteSession(sessionId, false, emptyStreamIdsArr));
		stubResponseAndRun(() -> client.muteSession(sessionId, true, singleStreamIdCol));
		stubResponseAndRun(() -> client.muteSession(sessionId, false, singleStreamIdArr));
		stubResponseAndAssertThrowsIAX(() -> client.muteSession(null, false));

		String responseJson = "{\n" +
			  "  \"code\": 404,\n" +
			  "  \"message\": \"Not found. The session or stream is not found\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(404, responseJson,
			  () -> client.muteSession(sessionId, false)
		);
	}

	@Test
	public void testUpdateArchiveLayout() throws Exception {
		StreamCompositionLayout request = StreamCompositionLayout.builder(ScreenLayoutType.HORIZONTAL).build();
		stubResponseAndRun(() -> client.updateArchiveLayout(archiveId, request));
		stubResponseAndAssertThrowsIAX(() -> client.updateArchiveLayout(null, request));
		stubResponseAndAssertThrowsIAX(() -> client.updateArchiveLayout(archiveId, null));

		String responseJson = "{\n" +
			  "  \"code\": 403,\n" +
			  "  \"message\": \"Authentication error.\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(403, responseJson,
			  () -> client.updateArchiveLayout(archiveId, request)
	    );
	}

	@Test
	public void testDeleteArchive() throws Exception {
		stubResponseAndRun(204, () -> client.deleteArchive(archiveId));
		stubResponseAndAssertThrowsIAX(() -> client.deleteArchive(null));

		String responseJson = "{\n" +
			  "  \"code\": 409,\n" +
			  "  \"message\": \"Status of the archive is not 'uploaded', 'available', or 'deleted'\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(409, responseJson, () -> client.deleteArchive(archiveId));
	}

	@Test
	public void testAddArchiveStream() throws Exception {
		stubResponseAndRun(204, () -> client.addArchiveStream(archiveId, streamId));
		stubResponseAndRun(204, () -> client.addArchiveStream(archiveId, streamId, true, true));
		stubResponseAndRun(204, () -> client.addArchiveStream(archiveId, streamId, null, false));
		stubResponseAndRun(204, () -> client.addArchiveStream(archiveId, streamId, false, null));
		stubResponseAndAssertThrowsIAX(204, () -> client.addArchiveStream(null, streamId));
		stubResponseAndAssertThrowsIAX(204, () -> client.addArchiveStream(archiveId, null));

		String responseJson = "{\n" +
			  "  \"code\": 404,\n" +
			  "  \"message\": \"Archive or stream not found\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(404, responseJson,
			  () -> client.addArchiveStream(archiveId, streamId)
		);
	}

	@Test
	public void testRemoveArchiveStream() throws Exception {
		stubResponseAndRun(204, () -> client.removeArchiveStream(archiveId, streamId));
		stubResponseAndAssertThrowsIAX(204, () -> client.removeArchiveStream(null, streamId));
		stubResponseAndAssertThrowsIAX(204, () -> client.removeArchiveStream(archiveId, null));

		String responseJson = "{\n" +
			  "  \"code\": 404,\n" +
			  "  \"message\": \"Archive or stream not found\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(404, responseJson,
			  () -> client.removeArchiveStream(archiveId, streamId)
		);
	}

	@Test
	public void testStopArchive() throws Exception {
		stubArchiveJsonAndAssertEquals(() -> client.stopArchive(archiveId));
		stubArchiveJsonAndAssertThrows(() -> client.stopArchive(null));

		String responseJson = "{\n" +
			  "  \"code\": 409,\n" +
			  "  \"message\": \"You attempted to stop an archive that was not being recorded\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(409, responseJson, () -> client.stopArchive(archiveId));
	}

	@Test
	public void testGetArchive() throws Exception {
		stubArchiveJsonAndAssertEquals(() -> client.getArchive(archiveId));
		stubArchiveJsonAndAssertThrows(() -> client.getArchive(null));

		String responseJson = "{\n" +
			  "  \"code\": 403,\n" +
			  "  \"message\": \"You passed in an invalid JWT token.\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(403, responseJson, () -> client.getArchive(archiveId));
	}

	@Test
	public void testListArchives() throws Exception {
		stubListArchiveJsonAndAssertEquals(client::listArchives);
		stubListArchiveJsonAndAssertEquals(() -> client.listArchives(ListStreamCompositionsRequest.builder().build()));
		stubListArchiveJsonAndAssertEquals(() -> client.listArchives(null));

		String responseJson = "{\n" +
			  "  \"code\": 403,\n" +
			  "  \"message\": \"Authentication error\"\n" +
			  "}";
		stubResponseAndAssertThrowsVideoException(403, responseJson, () -> client.listArchives());
	}

	@Test
	public void testCreateArchive() throws Exception {
		Archive request = Archive.builder(sessionId).build();
		stubArchiveJsonAndAssertEquals(() -> client.createArchive(request));
		stubArchiveJsonAndAssertThrows(() -> client.createArchive(null));

		String responseJson = "{\n  \"code\": 409,\n  \"message\": \"You attempted to " +
			  "start an archive for a session that does not use the Vonage Video Media Router.\"\n}";
		stubResponseAndAssertThrowsVideoException(409, responseJson, () -> client.createArchive(request));
	}

	@Test
	public void testUpdateBroadcastLayout() throws Exception {
		StreamCompositionLayout request = StreamCompositionLayout.builder(ScreenLayoutType.HORIZONTAL).build();
		stubResponseAndRun(() -> client.updateBroadcastLayout(broadcastId, request));
		stubResponseAndAssertThrowsIAX(() -> client.updateBroadcastLayout(null, request));
		stubResponseAndAssertThrowsIAX(() -> client.updateBroadcastLayout(broadcastId, null));

		String responseJson = "{\n" +
				"  \"code\": 403,\n" +
				"  \"message\": \"Authentication error.\"\n" +
				"}";
		stubResponseAndAssertThrowsVideoException(403, responseJson,
				() -> client.updateBroadcastLayout(broadcastId, request)
		);
	}

	@Test
	public void testAddBroadcastStream() throws Exception {
		stubResponseAndRun(204, () -> client.addBroadcastStream(broadcastId, streamId));
		stubResponseAndRun(204, () -> client.addBroadcastStream(broadcastId, streamId, true, true));
		stubResponseAndRun(204, () -> client.addBroadcastStream(broadcastId, streamId, null, false));
		stubResponseAndRun(204, () -> client.addBroadcastStream(broadcastId, streamId, false, null));
		stubResponseAndAssertThrowsIAX(204, () -> client.addBroadcastStream(null, streamId));
		stubResponseAndAssertThrowsIAX(204, () -> client.addBroadcastStream(broadcastId, null));

		String responseJson = "{\n" +
				"  \"code\": 404,\n" +
				"  \"message\": \"Broadcast or stream not found\"\n" +
				"}";
		stubResponseAndAssertThrowsVideoException(404, responseJson,
				() -> client.addBroadcastStream(broadcastId, streamId)
		);
	}

	@Test
	public void testRemoveBroadcastStream() throws Exception {
		stubResponseAndRun(204, () -> client.removeBroadcastStream(broadcastId, streamId));
		stubResponseAndAssertThrowsIAX(204, () -> client.removeBroadcastStream(null, streamId));
		stubResponseAndAssertThrowsIAX(204, () -> client.removeBroadcastStream(broadcastId, null));

		String responseJson = "{\n" +
				"  \"code\": 404,\n" +
				"  \"message\": \"Broadcast or stream not found\"\n" +
				"}";
		stubResponseAndAssertThrowsVideoException(404, responseJson,
				() -> client.removeBroadcastStream(broadcastId, streamId)
		);
	}

	@Test
	public void testStopBroadcast() throws Exception {
		stubBroadcastJsonAndAssertEquals(() -> client.stopBroadcast(broadcastId));
		stubBroadcastJsonAndAssertThrows(() -> client.stopBroadcast(null));

		String responseJson = "{\n" +
				"  \"code\": 409,\n" +
				"  \"message\": \"You attempted to stop an archive that was not being recorded\"\n" +
				"}";
		stubResponseAndAssertThrowsVideoException(409, responseJson, () -> client.stopBroadcast(broadcastId));
	}

	@Test
	public void testGetBroadcast() throws Exception {
		stubBroadcastJsonAndAssertEquals(() -> client.getBroadcast(broadcastId));
		stubBroadcastJsonAndAssertThrows(() -> client.getBroadcast(null));

		String responseJson = "{\n" +
				"  \"code\": 403,\n" +
				"  \"message\": \"You passed in an invalid JWT token.\"\n" +
				"}";
		stubResponseAndAssertThrowsVideoException(403, responseJson, () -> client.getBroadcast(broadcastId));
	}

	@Test
	public void testListBroadcasts() throws Exception {
		stubListBroadcastJsonAndAssertEquals(() -> client.listBroadcasts());
		stubListBroadcastJsonAndAssertEquals(() -> client.listBroadcasts(ListStreamCompositionsRequest.builder().build()));
		stubListBroadcastJsonAndAssertEquals(() -> client.listBroadcasts(null));

		String responseJson = "{\n" +
				"  \"code\": 403,\n" +
				"  \"message\": \"Authentication error\"\n" +
				"}";
		stubResponseAndAssertThrowsVideoException(403, responseJson, () -> client.listBroadcasts());
	}

	@Test
	public void testCreateBroadcast() throws Exception {
		Broadcast request = Broadcast.builder(sessionId)
				.addRtmpStream(Rtmp.builder().streamName("YT_key").serverUrl("https://youtu.be").id("uuID").build())
				.hls(Hls.builder().dvr(true).lowLatency(false).build())
				.resolution(Resolution.FHD_LANDSCAPE).streamMode(StreamMode.AUTO)
				.layout(StreamCompositionLayout.builder(ScreenLayoutType.VERTICAL).build())
				.multiBroadcastTag("broadcast_tag_provided")
				.maxBitrate(128_000_000).maxDuration(1200).build();

		stubBroadcastJsonAndAssertEquals(() -> client.createBroadcast(request));
		stubBroadcastJsonAndAssertThrows(() -> client.createBroadcast(null));

		String responseJson = "{\n  \"code\": 409,\n  \"message\": \"The broadcast has already started for the" +
				"session.Or if you attempt to start a simultaneous broadcast for a session without setting a" +
				"unique multiBroadcastTag value.\"\n}";
		stubResponseAndAssertThrowsVideoException(409, responseJson, () -> client.createBroadcast(request));
	}

	@Test
	public void testSipDial() throws Exception {
		SipDialRequest request = SipDialRequest.builder()
				.uri(URI.create("sip:user@sip.partner.com"), false)
				.sessionId(sessionId).token(token).build();

		stubResponse(200, "{\n" +
				"  \"id\": \""+id+"\",\n" +
				"  \"connectionId\": \""+connectionId+"\",\n" +
				"  \"streamId\": \""+streamId+"\"\n" +
				"}"
		);

		SipDialResponse parsed = client.sipDial(request);
		assertEquals(id, parsed.getId());
		assertEquals(connectionId, parsed.getConnectionId());
		assertEquals(streamId, parsed.getStreamId());

		stubResponseAndAssertThrowsVideoException(409, "{\"code\":409}", () -> client.sipDial(request));
	}

	@Test
	public void testSendDtmf() throws Exception {
		String digits = "*012p9#3p45*86p7#123";
		stubResponseAndRun(200, () -> client.sendDtmf(sessionId, connectionId, digits));
		stubResponseAndRun(200, () -> client.sendDtmf(sessionId, digits));
		stubResponseAndRun(200, () -> client.sendDtmf(sessionId, "0"));

		stubResponseAndAssertThrowsIAX(() -> client.sendDtmf(null, connectionId, digits));
		stubResponseAndAssertThrowsIAX(() -> client.sendDtmf(sessionId, null, digits));
		stubResponseAndAssertThrowsIAX(() -> client.sendDtmf(sessionId, connectionId, null));
		stubResponseAndAssertThrowsIAX(() -> client.sendDtmf(sessionId, connectionId, ""));
		stubResponseAndAssertThrowsIAX(() -> client.sendDtmf(sessionId, connectionId, "1abc#"));
		stubResponseAndAssertThrowsIAX(() -> client.sendDtmf(sessionId, connectionId, null));
		stubResponseAndAssertThrowsIAX(() -> client.sendDtmf(null, digits));

		stubResponseAndAssertThrowsVideoException(403, "{\"code\":403}",
				() -> client.sendDtmf(sessionId, connectionId, digits)
		);
	}

	@Test
	public void testStartLiveCaptions() throws Exception {
		var request = StartCaptionsRequest.builder().token(token).sessionId(sessionId).build();
		var response = stubResponseAndGet(202,
				"{\"captionsId\": \""+captionsId+"\"}",
				() -> client.startCaptions(request)
		);
		testJsonableBaseObject(response);
		assertEquals(UUID.fromString(captionsId), response.getCaptionsId());

		stubResponseAndAssertThrowsIAX(202, () -> client.startCaptions(null));

		stubResponseAndAssertThrowsVideoException(409, "{\"code\":409}",
				() -> client.startCaptions(request)
		);
	}

	@Test
	public void testStopLiveCaptions() throws Exception {
		stubResponseAndRun(202, () -> client.stopCaptions(captionsId));

		stubResponseAndAssertThrowsIAX(202, () -> client.stopCaptions(null));
		stubResponseAndAssertThrowsIAX(202, () -> client.stopCaptions(sessionId));

		stubResponseAndAssertThrowsVideoException(404, "{\"code\":404}",
				() -> client.stopCaptions(captionsId)
		);
	}

	@Test
	public void testAudioConnector() throws Exception {
		var request = ConnectRequest.builder().token(token).sessionId(sessionId).uri(wssUri).build();
		var response = stubResponseAndGet(202,
				"{\"id\":\""+connectionId+"\",\"captionsId\": \""+captionsId+"\"}",
				() -> client.connectToWebsocket(request)
		);
		testJsonableBaseObject(response);
		assertEquals(UUID.fromString(connectionId), response.getId());
		assertEquals(UUID.fromString(captionsId), response.getCaptionsId());

		stubResponseAndAssertThrowsIAX(202, () -> client.startCaptions(null));

		stubResponseAndAssertThrowsVideoException(409, "{\"code\":409}",
				() -> client.connectToWebsocket(request)
		);
	}

	@Test
	public void testGenerateToken() {
		String token = client.generateToken(sessionId);
		Map<String, String> claims = decodeTokenBody(token);

		assertEquals("session.connect", claims.get("scope"));
		assertEquals(sessionId, claims.get("session_id"));
		assertNotNull(claims.get("application_id"));
		long exp = Long.parseLong(claims.get("exp"));
		long iat = Long.parseLong(claims.get("iat"));
		// One minute less than a day = 86340
		assertTrue((iat + 86340) < exp);
		assertTrue((iat + 86401) > exp);
		assertTrue(token.length() > 100);

		token = client.generateToken(sessionId, TokenOptions.builder().build());
		assertEquals(claims.keySet(), decodeTokenBody(token).keySet());
		token = client.generateToken(sessionId, null);
		assertEquals(claims.keySet(), decodeTokenBody(token).keySet());
		assertThrows(IllegalArgumentException.class, () -> client.generateToken(null));

		token = client.generateToken(sessionId,TokenOptions.builder()
				.role(Role.SUBSCRIBER)
				.expiryLength(Duration
			    .ofMinutes(12))
				.data("foo bar, blah blah")
				.initialLayoutClassList(Arrays.asList("c1", "c2", "min", "full"))
		        .build()
		);
		claims = decodeTokenBody(token);
		assertEquals("subscriber", claims.get("role"));
		assertEquals("foo bar, blah blah", claims.get("connection_data"));
		assertEquals("c1 c2 min full", claims.get("initial_layout_class_list"));
		assertEquals("session.connect", claims.get("scope"));
		assertEquals(sessionId, claims.get("session_id"));
		assertEquals(applicationId, claims.get("application_id"));
		exp = Long.parseLong(claims.get("exp"));
		iat = Long.parseLong(claims.get("iat"));
		assertTrue((iat + 721) > exp);
		assertTrue((iat + 700) < exp);
	}

	// ENDPOINT TESTS

	@Test
	public void testCreateArchiveEndpoint() throws Exception {
		new VideoEndpointTestSpec<Archive, Archive>() {

			@Override
			protected RestEndpoint<Archive, Archive> endpoint() {
				return client.createArchive;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(Archive request) {
				return "/v2/project/"+applicationId+"/archive";
			}

			@Override
			protected Archive sampleRequest() {
				return Archive.builder(sessionId)
						.name("My Archive").hasVideo(false).build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"sessionId\":\""+sessionId+"\",\"hasVideo\":false,\"name\":\"My Archive\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testCreateBroadcastEndpoint() throws Exception {
		new VideoEndpointTestSpec<Broadcast, Broadcast>() {

			@Override
			protected RestEndpoint<Broadcast, Broadcast> endpoint() {
				return client.createBroadcast;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(Broadcast request) {
				return "/v2/project/"+applicationId+"/broadcast";
			}

			@Override
			protected Broadcast sampleRequest() {
				return Broadcast.builder("SESSION_id123")
						.addRtmpStream(Rtmp.builder()
								.streamName("My Test Stream")
								.serverUrl("rtmps://mytestserver/mytestapp")
								.build()
						).build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"sessionId\":\"SESSION_id123\",\"outputs\":{\"rtmp\":[{" +
						"\"streamName\":\"My Test Stream\",\"serverUrl\":\"rtmps://mytestserver/mytestapp\"}]}}";
			}
		}
		.runTests();
	}

	@Test
	public void testCreateSessionEndpoint() throws Exception {
		new VideoEndpointTestSpec<CreateSessionRequest, CreateSessionResponse[]>() {

			@Override
			protected RestEndpoint<CreateSessionRequest, CreateSessionResponse[]> endpoint() {
				return client.createSession;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(CreateSessionRequest request) {
				return "/session/create";
			}

			@Override
			protected CreateSessionRequest sampleRequest() {
				return CreateSessionRequest.builder()
						.mediaMode(MediaMode.ROUTED).location("192.168.1.2")
						.archiveMode(ArchiveMode.ALWAYS).build();
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				CreateSessionRequest request = sampleRequest();
				String mediaMode = request.getMediaMode().toString();
				assertEquals("disabled", mediaMode);
				String archiveMode = request.getArchiveMode().toString();
				assertEquals("always", archiveMode);

				Map<String, String> params = new LinkedHashMap<>(4);
				params.put("location", request.getLocation().getHostAddress());
				params.put("p2p.preference", mediaMode);
				params.put("archiveMode", archiveMode);
				return params;
			}
		}
		.runTests();
	}

	@Test
	public void testDeleteArchiveEndpoint() throws Exception {
		new VideoEndpointTestSpec<String, Void>() {

			@Override
			protected RestEndpoint<String, Void> endpoint() {
				return client.deleteArchive;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.DELETE;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v2/project/"+applicationId+"/archive/"+request;
			}

			@Override
			protected String sampleRequest() {
				return archiveId;
			}
		}
		.runTests();
	}

	@Test
	public void testForceDisconnectEndpoint() throws Exception {
		new VideoEndpointTestSpec<SessionResourceRequestWrapper, Void>() {

			@Override
			protected RestEndpoint<SessionResourceRequestWrapper, Void> endpoint() {
				return client.forceDisconnect;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.DELETE;
			}

			@Override
			protected String expectedEndpointUri(SessionResourceRequestWrapper request) {
				return "/v2/project/"+applicationId+"/session/"+request.sessionId+"/connection/"+request.resourceId;
			}

			@Override
			protected SessionResourceRequestWrapper sampleRequest() {
				return new SessionResourceRequestWrapper(sessionId, connectionId);
			}
		}
		.runTests();
	}

	@Test
	public void testGetArchiveEndpoint() throws Exception {
		new VideoEndpointTestSpec<String, Archive>() {

			@Override
			protected RestEndpoint<String, Archive> endpoint() {
				return client.getArchive;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v2/project/"+applicationId+"/archive/"+request;
			}

			@Override
			protected String sampleRequest() {
				return archiveId;
			}
		}
		.runTests();
	}

	@Test
	public void testGetBroadcastEndpoint() throws Exception {
		new VideoEndpointTestSpec<String, Broadcast>() {

			@Override
			protected RestEndpoint<String, Broadcast> endpoint() {
				return client.getBroadcast;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v2/project/"+applicationId+"/broadcast/"+request;
			}

			@Override
			protected String sampleRequest() {
				return broadcastId;
			}
		}
		.runTests();
	}

	@Test
	public void testGetStreamEndpoint() throws Exception {
		new VideoEndpointTestSpec<SessionResourceRequestWrapper, GetStreamResponse>() {

			@Override
			protected RestEndpoint<SessionResourceRequestWrapper, GetStreamResponse> endpoint() {
				return client.getStream;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(SessionResourceRequestWrapper request) {
				return "/v2/project/"+applicationId+"/session/"+request.sessionId+"/stream/"+request.resourceId;
			}

			@Override
			protected SessionResourceRequestWrapper sampleRequest() {
				return new SessionResourceRequestWrapper(sessionId, streamId);
			}
		}
		.runTests();
	}

	@Test
	public void testListArchivesEndpoint() throws Exception {
		new VideoEndpointTestSpec<ListStreamCompositionsRequest, ListArchivesResponse>() {

			@Override
			protected RestEndpoint<ListStreamCompositionsRequest, ListArchivesResponse> endpoint() {
				return client.listArchives;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListStreamCompositionsRequest request) {
				return "/v2/project/"+applicationId+"/archive";
			}

			@Override
			protected ListStreamCompositionsRequest sampleRequest() {
				return ListStreamCompositionsRequest.builder()
						.offset(2).count(6).sessionId(sessionId).build();
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				ListStreamCompositionsRequest request = sampleRequest();
				assertEquals(2, request.getOffset());
				assertEquals(6, request.getCount());
				assertEquals(sessionId, request.getSessionId());

				Map<String, String> params = new LinkedHashMap<>(8);
				params.put("offset", request.getOffset().toString());
				params.put("count", request.getCount().toString());
				params.put("sessionId", request.getSessionId());
				return params;
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testParseValidResponse();
			}

			private void testParseValidResponse() throws Exception {
				String responseJson = "{\"count\":2,\"items\":[{},{\"applicationId\":\""+applicationId+"\"}]}";
				stubResponse(200, responseJson);
				ListArchivesResponse parsed = endpoint().execute(ListStreamCompositionsRequest.builder().build());
				assertNotNull(parsed);
				assertEquals(2, parsed.getCount().intValue());
				assertEquals(2, parsed.getItems().size());
				assertNotNull(parsed.getItems().get(0));
				assertEquals(applicationId, parsed.getItems().get(1).getApplicationId().toString());
			}
		}
		.runTests();
	}

	@Test
	public void testListBroadcastsEndpoint() throws Exception {
		new VideoEndpointTestSpec<ListStreamCompositionsRequest, ListBroadcastsResponse>() {

			@Override
			protected RestEndpoint<ListStreamCompositionsRequest, ListBroadcastsResponse> endpoint() {
				return client.listBroadcasts;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListStreamCompositionsRequest request) {
				return "/v2/project/"+applicationId+"/broadcast";
			}

			@Override
			protected ListStreamCompositionsRequest sampleRequest() {
				return ListStreamCompositionsRequest.builder()
						.offset(7).count(25).sessionId(sessionId).build();
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				Map<String, String> params = new LinkedHashMap<>(8);
				params.put("offset", "7");
				params.put("count", "25");
				params.put("sessionId", sessionId);
				return params;
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testParseValidResponse();
			}

			private void testParseValidResponse() throws Exception {
				String responseJson = "{\"count\":31,\"items\":[{},{},{\"applicationId\":\""+applicationId+"\"},{},{}]}";
				stubResponse(200, responseJson);
				ListBroadcastsResponse parsed = endpoint().execute(ListStreamCompositionsRequest.builder().build());
				assertNotNull(parsed);
				assertEquals(31, parsed.getCount().intValue());
				assertEquals(5, parsed.getItems().size());
				assertNotNull(parsed.getItems().get(0));
				assertEquals(applicationId, parsed.getItems().get(2).getApplicationId().toString());
			}
		}
		.runTests();
	}

	@Test
	public void testListStreamsEndpoint() throws Exception {
		new VideoEndpointTestSpec<String, ListStreamsResponse>() {

			@Override
			protected RestEndpoint<String, ListStreamsResponse> endpoint() {
				return client.listStreams;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v2/project/"+applicationId+"/session/"+request+"/stream";
			}

			@Override
			protected String sampleRequest() {
				return sessionId;
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testParseValidResponse();
			}

			private void testParseValidResponse() throws Exception {
				Integer count = 1;
				VideoType videoType0 = VideoType.CUSTOM;
				UUID id0 = UUID.randomUUID();
				String name0 = "My Stream",
						layoutClass0 = "full",
						json = "{\n" +
								"  \"count\": "+count+",\n" +
								"  \"items\": [\n" +
								"    {\n" +
								"      \"id\": \""+id0+"\",\n" +
								"      \"videoType\": \""+videoType0+"\",\n" +
								"      \"name\": \""+name0+"\",\n" +
								"      \"layoutClassList\": [\n" +
								"        \""+layoutClass0+"\"\n" +
								"      ]\n" +
								"    }\n" +
								"  ]\n" +
								"}";

				stubResponse(200, json);
				ListStreamsResponse response = endpoint().execute(sessionId);
				assertEquals(count, response.getCount());
				List<GetStreamResponse> streams = response.getItems();
				assertEquals(1, streams.size());
				GetStreamResponse stream0 = streams.get(0);
				assertEquals(id0, stream0.getId());
				assertEquals(videoType0, stream0.getVideoType());
				assertEquals(name0, stream0.getName());
				List<String> layoutClassList0 = stream0.getLayoutClassList();
				assertEquals(1, layoutClassList0.size());
				assertEquals(layoutClass0, layoutClassList0.get(0));
			}
		}
		.runTests();
	}

	@Test
	public void testMuteSessionEndpoint() throws Exception {
		new VideoEndpointTestSpec<MuteSessionRequest, MuteSessionResponse>() {

			@Override
			protected RestEndpoint<MuteSessionRequest, MuteSessionResponse> endpoint() {
				return client.muteSession;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(MuteSessionRequest request) {
				return "/v2/project/"+applicationId+"/session/"+request.sessionId+"/mute";
			}

			@Override
			protected MuteSessionRequest sampleRequest() {
				return new MuteSessionRequest(sessionId, true, Arrays.asList("ID_0", "ID_1", "ID_2"));
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"active\":true,\"excludedStreamIds\":[\"ID_0\",\"ID_1\",\"ID_2\"]}";
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testParseResponse();
			}

			private void testParseResponse() throws Exception {
				long createdAt = 1414642898000L;
				String name = "Joe Montana", responseJson = "{\n" +
						"   \"applicationId\": \""+applicationId+"\",\n" +
						"   \"status\": \"ACTIVE\",\n" +
						"   \"name\": \""+name+"\",\n" +
						"   \"environment\": \"standard\",\n" +
						"   \"createdAt\": "+createdAt+"\n" +
						"}";
				MuteSessionResponse parsed = stubResponseAndGet(responseJson, this::executeEndpoint);
				assertNotNull(parsed);
				assertEquals(applicationId, parsed.getApplicationId());
				assertEquals(name, parsed.getName());
				assertEquals(ProjectStatus.ACTIVE, parsed.getStatus());
				assertEquals(ProjectEnvironment.STANDARD, parsed.getEnvironment());
				assertEquals(createdAt, parsed.getCreatedAt());
			}
		}
		.runTests();
	}

	@Test
	public void testMuteStreamEndpoint() throws Exception {
		new VideoEndpointTestSpec<SessionResourceRequestWrapper, Void>() {

			@Override
			protected RestEndpoint<SessionResourceRequestWrapper, Void> endpoint() {
				return client.muteStream;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(SessionResourceRequestWrapper request) {
				return "/v2/project/"+applicationId+"/session/"+request.sessionId+"/stream/"+request.resourceId+"/mute";
			}

			@Override
			protected SessionResourceRequestWrapper sampleRequest() {
				return new SessionResourceRequestWrapper(sessionId, streamId);
			}
		}
		.runTests();
	}

	@Test
	public void testPatchArchiveStreamEndpoint() throws Exception {
		new VideoEndpointTestSpec<PatchComposedStreamsRequest, Void>() {

			@Override
			protected RestEndpoint<PatchComposedStreamsRequest, Void> endpoint() {
				return client.patchArchiveStream;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PATCH;
			}

			@Override
			protected String expectedEndpointUri(PatchComposedStreamsRequest request) {
				return "/v2/project/"+applicationId+"/archive/"+request.id+"/streams";
			}

			@Override
			protected PatchComposedStreamsRequest sampleRequest() {
				return new PatchComposedStreamsRequest(streamId, true, false);
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"addStream\":\""+streamId+"\",\"hasAudio\":true,\"hasVideo\":false}";
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testRemoveStream();
				testAddStreamDefaultOptions();
			}

			private void testRemoveStream() throws Exception {
				PatchComposedStreamsRequest request = new PatchComposedStreamsRequest(streamId);
				request.id = archiveId;
				assertRequestUriAndBody(request, "{\"removeStream\":\""+streamId+"\"}");
			}

			private void testAddStreamDefaultOptions() throws Exception {
				PatchComposedStreamsRequest request = new PatchComposedStreamsRequest(streamId, null, null);
				request.id = archiveId;
				assertRequestUriAndBody(request, "{\"addStream\":\""+streamId+"\"}");
			}
		}
		.runTests();
	}

	@Test
	public void testPatchBroadcastStreamEndpoint() throws Exception {
		new VideoEndpointTestSpec<PatchComposedStreamsRequest, Void>() {

			@Override
			protected RestEndpoint<PatchComposedStreamsRequest, Void> endpoint() {
				return client.patchBroadcastStream;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PATCH;
			}

			@Override
			protected String expectedEndpointUri(PatchComposedStreamsRequest request) {
				return "/v2/project/"+applicationId+"/broadcast/"+request.id+"/streams";
			}

			@Override
			protected PatchComposedStreamsRequest sampleRequest() {
				return new PatchComposedStreamsRequest(streamId, false, true);
			}

			@Override
			protected String sampleRequestBodyString() {
				PatchComposedStreamsRequest request = sampleRequest();
				return "{\"addStream\":\"" + request.getAddStream() +
						"\",\"hasAudio\":" + request.hasAudio() +
						",\"hasVideo\":" + request.hasVideo() + "}";
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testRemoveStream();
				testAddStreamDefaultOptions();
			}

			private void testRemoveStream() throws Exception {
				PatchComposedStreamsRequest request = new PatchComposedStreamsRequest(streamId);
				request.id = broadcastId;
				assertRequestUriAndBody(request, "{\"removeStream\":\""+streamId+"\"}");
			}

			private void testAddStreamDefaultOptions() throws Exception {
				PatchComposedStreamsRequest request = new PatchComposedStreamsRequest(streamId, null, null);
				request.id = broadcastId;
				assertRequestUriAndBody(request, "{\"addStream\":\""+streamId+"\"}");
			}
		}
		.runTests();
	}

	@Test
	public void testSendDtmfToConnectionEndpoint() throws Exception {
		new VideoEndpointTestSpec<SendDtmfRequest, Void>() {

			@Override
			protected RestEndpoint<SendDtmfRequest, Void> endpoint() {
				return client.sendDtmfToConnection;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(SendDtmfRequest request) {
				return "/v2/project/" + applicationId + "/session/"+request.sessionId +
						"/connection/" + request.connectionId + "/play-dtmf";
			}

			@Override
			protected SendDtmfRequest sampleRequest() {
				return new SendDtmfRequest(sessionId, connectionId, "*0123456789#");
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"digits\":\"*0123456789#\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testSendDtmfToSessionEndpoint() throws Exception {
		new VideoEndpointTestSpec<SendDtmfRequest, Void>() {

			@Override
			protected RestEndpoint<SendDtmfRequest, Void> endpoint() {
				return client.sendDtmfToSession;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(SendDtmfRequest request) {
				return "/v2/project/"+applicationId+"/session/"+request.sessionId+"/play-dtmf";
			}

			@Override
			protected SendDtmfRequest sampleRequest() {
				return new SendDtmfRequest(sessionId, null, "p90");
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"digits\":\""+sampleRequest().digits+"\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testSetStreamLayoutEndpoint() throws Exception {
		new VideoEndpointTestSpec<SetStreamLayoutRequest, Void>() {

			@Override
			protected RestEndpoint<SetStreamLayoutRequest, Void> endpoint() {
				return client.setStreamLayout;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PUT;
			}

			@Override
			protected String expectedEndpointUri(SetStreamLayoutRequest request) {
				return "/v2/project/"+applicationId+"/session/"+request.sessionId+"/stream";
			}

			@Override
			protected SetStreamLayoutRequest sampleRequest() {
				return new SetStreamLayoutRequest(sessionId, Arrays.asList(
						SessionStream.builder(streamId).build(),
						SessionStream.builder(connectionId).layoutClassList(Arrays.asList("min", "full")).build(),
						SessionStream.builder(broadcastId).layoutClassList().build()
				));
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"items\":[{\"id\":\""+streamId+"\"},"+
						"{\"id\":\""+connectionId+"\",\"layoutClassList\":[\"min\",\"full\"]}," +
						"{\"id\":\""+broadcastId+"\",\"layoutClassList\":[]}]}";
			}
		}
		.runTests();
	}

	@Test
	public void testSignalAllEndpoint() throws Exception {
		new VideoEndpointTestSpec<SignalRequest, Void>() {
			final String data = "The actual payload as a string", type = "Signal channel";

			@Override
			protected RestEndpoint<SignalRequest, Void> endpoint() {
				return client.signalAll;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(SignalRequest request) {
				return "/v2/project/"+applicationId+"/session/"+request.sessionId+"/signal";
			}

			@Override
			protected SignalRequest sampleRequest() {
				SignalRequest request = SignalRequest.builder().data(data).type(type).build();
				request.sessionId = sessionId;
				return request;
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"type\":\""+type+"\",\"data\":\""+data+"\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testSignalEndpoint() throws Exception {
		new VideoEndpointTestSpec<SignalRequest, Void>() {
			final String data = "Payload of the signal", type = "chat";

			@Override
			protected RestEndpoint<SignalRequest, Void> endpoint() {
				return client.signal;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(SignalRequest request) {
				return "/v2/project/" + applicationId + "/session/" + request.sessionId +
						"/connection/" + request.connectionId + "/signal";
			}

			@Override
			protected SignalRequest sampleRequest() {
				SignalRequest request = SignalRequest.builder().data(data).type(type).build();
				request.sessionId = sessionId;
				request.connectionId = connectionId;
				return request;
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"type\":\""+type+"\",\"data\":\""+data+"\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testSipDialEndpoint() throws Exception {
		new VideoEndpointTestSpec<SipDialRequest, SipDialResponse>() {

			@Override
			protected RestEndpoint<SipDialRequest, SipDialResponse> endpoint() {
				return client.sipDial;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(SipDialRequest request) {
				return "/v2/project/"+applicationId+"/dial";
			}

			@Override
			protected SipDialRequest sampleRequest() {
				return SipDialRequest.builder().token(token).sessionId(sessionId)
						.uri(URI.create("sip.example.com"), true).build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"sessionId\":\""+sessionId+"\",\"token\":\""+token+"\"," +
						"\"sip\":{\"uri\":\"sip.example.com;transport=tls\"}}";
			}
		}
		.runTests();
	}

	@Test
	public void testStopArchiveEndpoint() throws Exception {
		new VideoEndpointTestSpec<String, Archive>() {

			@Override
			protected RestEndpoint<String, Archive> endpoint() {
				return client.stopArchive;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v2/project/"+applicationId+"/archive/"+request+"/stop";
			}

			@Override
			protected String sampleRequest() {
				return archiveId;
			}
		}
		.runTests();
	}

	@Test
	public void testStopBroadcastEndpoint() throws Exception {
		new VideoEndpointTestSpec<String, Broadcast>() {

			@Override
			protected RestEndpoint<String, Broadcast> endpoint() {
				return client.stopBroadcast;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v2/project/"+applicationId+"/broadcast/"+broadcastId+"/stop";
			}

			@Override
			protected String sampleRequest() {
				return broadcastId;
			}
		}
		.runTests();
	}

	@Test
	public void testUpdateArchiveLayoutEndpoint() throws Exception {
		new VideoEndpointTestSpec<StreamCompositionLayout, Void>() {

			@Override
			protected RestEndpoint<StreamCompositionLayout, Void> endpoint() {
				return client.updateArchiveLayout;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PUT;
			}

			@Override
			protected String expectedEndpointUri(StreamCompositionLayout request) {
				return "/v2/project/"+applicationId+"/archive/"+request.id+"/layout";
			}

			@Override
			protected StreamCompositionLayout sampleRequest() {
				StreamCompositionLayout request = StreamCompositionLayout.builder(ScreenLayoutType.VERTICAL).build();
				request.id = archiveId;
				return request;
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"type\":\"verticalPresentation\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testUpdateBroadcastLayoutEndpoint() throws Exception {
		new VideoEndpointTestSpec<StreamCompositionLayout, Void>() {

			@Override
			protected RestEndpoint<StreamCompositionLayout, Void> endpoint() {
				return client.updateBroadcastLayout;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PUT;
			}

			@Override
			protected String expectedEndpointUri(StreamCompositionLayout request) {
				return "/v2/project/"+applicationId+"/broadcast/"+request.id+"/layout";
			}

			@Override
			protected StreamCompositionLayout sampleRequest() {
				StreamCompositionLayout request = StreamCompositionLayout.builder(ScreenLayoutType.HORIZONTAL).build();
				request.id = broadcastId;
				return request;
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"type\":\"horizontalPresentation\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testStartLiveCaptionsEndpoint() throws Exception {
		new VideoEndpointTestSpec<StartCaptionsRequest, StartCaptionsResponse>() {
			final String statusCallbackUrl = "https://send-status-to.me";

			@Override
			protected RestEndpoint<StartCaptionsRequest, StartCaptionsResponse> endpoint() {
				return client.startCaptions;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(StartCaptionsRequest request) {
				return "/v2/project/"+applicationId+"/captions";
			}

			@Override
			protected StartCaptionsRequest sampleRequest() {
				return StartCaptionsRequest.builder()
						.token(token).partialCaptions(true)
						.statusCallbackUrl(statusCallbackUrl)
						.sessionId(sessionId).maxDuration(1800)
						.languageCode(Language.EN_AU).build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"sessionId\":\""+sessionId+"\"," +
						"\"token\":\""+token+"\"," +
						"\"languageCode\":\"en-AU\"," +
						"\"maxDuration\":1800," +
						"\"partialCaptions\":true," +
						"\"statusCallbackUrl\":\""+statusCallbackUrl+"\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testStopLiveCaptionsEndpoint() throws Exception {
		new VideoEndpointTestSpec<String, Void>() {

			@Override
			protected RestEndpoint<String, Void> endpoint() {
				return client.stopCaptions;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v2/project/"+applicationId+"/captions/"+request+"/stop";
			}

			@Override
			protected String sampleRequest() {
				return captionsId;
			}
		}
		.runTests();
	}

	@Test
	public void testConnectEndpoint() throws Exception {
		new VideoEndpointTestSpec<ConnectRequest, ConnectResponse>() {
			final Map<String, String> headers = Map.of(
					"prop1", "value",
					"Another Property", "Custom string"
			);

			@Override
			protected RestEndpoint<ConnectRequest, ConnectResponse> endpoint() {
				return client.connect;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(ConnectRequest request) {
				return "/v2/project/"+applicationId+"/connect";
			}

			@Override
			protected ConnectRequest sampleRequest() {
				return ConnectRequest.builder()
						.sessionId(sessionId).token(token)
						.uri(wssUri).streams(streamId)
						.audioRate(Websocket.AudioRate.L16_8K)
						.headers(headers).build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"sessionId\":\""+sessionId+"\",\"token\":\""+token+"\",\"websocket\":{" +
						"\"uri\":\""+wssUri+"\",\"streams\":[\""+streamId+"\"]," +
						"\"headers\":"+mapToJson(headers)+",\"audioRate\":8000}}";
			}
		}
		.runTests();
	}
}
