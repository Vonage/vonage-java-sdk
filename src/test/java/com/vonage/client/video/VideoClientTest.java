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

import com.vonage.client.ClientTest;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageBadRequestException;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.client.HttpResponseException;
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

	void stubResponseAndRun(Runnable invocation) throws Exception {
		stubResponseAndRun(200, invocation);
	}

	void stubResponseAndAssertThrowsHttpResponseException(int statusCode, String response,
																	Executable invocation) throws Exception {
		stubResponseAndAssertThrows(statusCode, response, invocation, HttpResponseException.class);
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

	void stubResponseAndAssertThrowsBadRequestException(int statusCode, String response,
																  Executable invocation) throws Exception {
		stubResponseAndAssertThrows(statusCode, response, invocation, VonageBadRequestException.class);
	}

	void stubResponseAndAssertThrowsResponseParseException(int statusCode, String response,
																	 Executable invocation) throws Exception {
		stubResponseAndAssertThrows(statusCode, response, invocation, VonageResponseParseException.class);
	}
	
	void stubArchiveJsonAndAssertThrows(Executable invocation) throws Exception {
		stubResponseAndAssertThrowsIAX(archiveJson, invocation);
	}

	void stubListArchiveJsonAndAssertThrows(Executable invocation) throws Exception {
		stubResponseAndAssertThrowsIAX(listArchiveJson, invocation);
	}

	void stubArchiveJsonAndAssertEquals(Supplier<Archive> invocation) throws Exception {
		stubResponse(archiveJson);
		assertArchiveEqualsExpectedJson(invocation.get());
	}

	void stubListArchiveJsonAndAssertEquals(Supplier<List<Archive>> invocation) throws Exception {
		stubResponse(listArchiveJson);
		List<Archive> archives = invocation.get();
		Assertions.assertEquals(1, archives.size());
		assertArchiveEqualsExpectedJson(archives.get(0));
	}

	static void assertArchiveEqualsExpectedJson(Archive response) {
		Assertions.assertNotNull(response);
		Assertions.assertEquals("https://tokbox.s3.amazonaws.com/"+connectionId+"/archive.mp4", response.getUrl().toString());
		Assertions.assertEquals(Long.valueOf(1384221730000L), response.getCreatedAtMillis());
		Assertions.assertEquals(Instant.ofEpochSecond(1384221730L), response.getCreatedAt());
		Assertions.assertEquals(Integer.valueOf(5049), response.getDurationSeconds());
		Assertions.assertEquals(Duration.ofSeconds(5049), response.getDuration());
		Assertions.assertTrue(response.hasAudio());
		Assertions.assertTrue(response.hasVideo());
		Assertions.assertEquals(archiveId, response.getId().toString());
		Assertions.assertEquals("Foo", response.getName());
		Assertions.assertEquals("", response.getReason());
		Assertions.assertEquals(Resolution.HD_LANDSCAPE, response.getResolution());
		Assertions.assertEquals(sessionId, response.getSessionId());
		Assertions.assertEquals(applicationId, response.getApplicationId().toString());
		Assertions.assertEquals(Long.valueOf(247748791L), response.getSize());
		Assertions.assertEquals(ArchiveStatus.AVAILABLE, response.getStatus());
		assertVideoStreamsEqualsExpectedJson(response);
	}

	static void assertVideoStreamsEqualsExpectedJson(StreamComposition response) {
		Assertions.assertEquals(StreamMode.MANUAL, response.getStreamMode());
		List<VideoStream> streams = response.getStreams();
		Assertions.assertNotNull(streams);
		Assertions.assertEquals(2, streams.size());
		VideoStream stream1 = streams.get(0);
		Assertions.assertNotNull(stream1);
		Assertions.assertEquals(streamId, stream1.getStreamId().toString());
		Assertions.assertTrue(stream1.hasAudio());
		Assertions.assertFalse(stream1.hasVideo());
		VideoStream stream2 = streams.get(1);
		Assertions.assertNotNull(stream2);
		Assertions.assertEquals(UUID.fromString("482bce73-f882-40fd-8ca5-cb74ff416036"), stream2.getStreamId());
		Assertions.assertTrue(stream2.hasVideo());
		Assertions.assertFalse(stream2.hasAudio());
	}

	void stubBroadcastJsonAndAssertThrows(Executable invocation) throws Exception {
		stubResponseAndAssertThrowsIAX(broadcastJson, invocation);
	}

	void stubListBroadcastJsonAndAssertThrows(Executable invocation) throws Exception {
		stubResponseAndAssertThrowsIAX(listBroadcastJson, invocation);
	}

	void stubBroadcastJsonAndAssertEquals(Supplier<Broadcast> invocation) throws Exception {
		stubResponse(broadcastJson);
		assertBroadcastEqualsExpectedJson(invocation.get());
	}

	void stubListBroadcastJsonAndAssertEquals(Supplier<List<Broadcast>> invocation) throws Exception {
		stubResponse(listBroadcastJson);
		List<Broadcast> broadcasts = invocation.get();
		Assertions.assertEquals(1, broadcasts.size());
		assertBroadcastEqualsExpectedJson(broadcasts.get(0));
	}

	static void assertBroadcastEqualsExpectedJson(Broadcast response) {
		Assertions.assertNotNull(response);
		Assertions.assertEquals(sessionId, response.getSessionId());
		Assertions.assertEquals(broadcastId, response.getId().toString());
		Assertions.assertEquals(applicationId, response.getApplicationId().toString());
		Assertions.assertTrue(response.hasAudio());
		Assertions.assertTrue(response.hasVideo());
		Assertions.assertEquals(BroadcastStatus.STARTED, response.getStatus());
		Assertions.assertEquals(Resolution.HD_PORTRAIT, response.getResolution());
		Assertions.assertEquals(1437676551000L, response.getCreatedAtMillis().longValue());
		Assertions.assertEquals(1437876551000L, response.getUpdatedAtMillis().longValue());
		Assertions.assertEquals("broadcast-1234b", response.getMultiBroadcastTag());
		Assertions.assertEquals(Duration.ofSeconds(5400), response.getMaxDuration());
		Assertions.assertEquals(2000000, response.getMaxBitrate().intValue());
		BroadcastUrls broadcastUrls = response.getBroadcastUrls();
		Assertions.assertNotNull(broadcastUrls);
		Assertions.assertEquals("http://server/fakepath/playlist.m3u8", broadcastUrls.getHls().toString());
		List<Rtmp> rtmps = broadcastUrls.getRtmps();
		Assertions.assertNotNull(rtmps);
		Assertions.assertEquals(1, rtmps.size());
		Rtmp rtmp = rtmps.get(0);
		Assertions.assertNotNull(rtmp);
		Assertions.assertEquals("rtmps://myfooserver/myfooapp", rtmp.getServerUrl().toString());
		Assertions.assertEquals(RtmpStatus.LIVE, rtmp.getStatus());
		Assertions.assertEquals("foo", rtmp.getId());
		Assertions.assertEquals("myfoostream", rtmp.getStreamName());
		Hls hls = response.getHlsSettings();
		Assertions.assertNotNull(hls);
		Assertions.assertFalse(hls.dvr());
		Assertions.assertTrue(hls.lowLatency());
		assertVideoStreamsEqualsExpectedJson(response);
	}

	@BeforeEach
	public void setUp() {
		wrapper.getAuthCollection().add(new JWTAuthMethod(applicationId, new byte[0]));
		client = new VideoClient(wrapper);
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
		Assertions.assertEquals(sessionId, response.getSessionId());
		Assertions.assertEquals(applicationId, response.getApplicationId().toString());
		Assertions.assertEquals(createDt, response.getCreateDt());
		Assertions.assertEquals(msUrl, response.getMediaServerUrl().toString());

		stubResponse(responseJson);
		response = client.createSession();
		Assertions.assertNotNull(response);
		Assertions.assertEquals(sessionId, response.getSessionId());
		Assertions.assertEquals(applicationId, response.getApplicationId().toString());
		Assertions.assertEquals(createDt, response.getCreateDt());
		Assertions.assertEquals(msUrl, response.getMediaServerUrl().toString());

		responseJson = "{\n" + "  \"code\": 400,\n" + "  \"message\": "+
				"\"Invalid request. This response may indicate that data in your request data is invalid JSON. "+
			    "Or it may indicate that you do not pass in a session ID or you passed in an invalid stream ID.\"\n}";

		stubResponseAndAssertThrowsResponseParseException(400, responseJson, () -> client.createSession(null));
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
		Assertions.assertEquals(1, response.size());
		Assertions.assertEquals(VideoType.SCREEN, response.get(0).getVideoType());
		Assertions.assertEquals("", response.get(0).getName());
		assertThrows(IllegalArgumentException.class, () -> client.listStreams(null));

		responseJson = "{\n" +
			  "  \"code\": 404,\n" +
			  "  \"message\": \"The session exists but has not had any streams added to it yet.\"\n" +
			  "}";
		stubResponseAndAssertThrowsResponseParseException(404, responseJson, () -> client.listStreams(sessionId));
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
		Assertions.assertEquals(streamId, response.getId().toString());
		Assertions.assertEquals(VideoType.CUSTOM, response.getVideoType());
		Assertions.assertEquals(1, response.getLayoutClassList().size());
		Assertions.assertEquals("full", response.getLayoutClassList().get(0));

		stubResponseAndAssertThrowsIAX(() -> client.getStream(null, streamId));
		stubResponseAndAssertThrowsIAX(() -> client.getStream(sessionId, null));

		responseJson = "{\n" +
			  "  \"code\": 408,\n" +
			  "  \"message\": \"You passed in an invalid stream ID.\"\n" +
			  "}";
		stubResponseAndAssertThrowsResponseParseException(408, responseJson,
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
		stubResponseAndAssertThrowsBadRequestException(403, responseJson,
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
		stubResponseAndAssertThrowsBadRequestException(413, responseJson,
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
		stubResponseAndAssertThrowsBadRequestException(413, responseJson,
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
		stubResponseAndAssertThrowsBadRequestException(403, responseJson,
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
		stubResponseAndAssertThrowsBadRequestException(404, responseJson,
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
		stubResponseAndAssertThrowsBadRequestException(404, responseJson,
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
		stubResponseAndAssertThrowsBadRequestException(403, responseJson,
			  () -> client.updateArchiveLayout(archiveId, request)
	    );
	}

	@Test
	public void testDeleteArchive() throws Exception {
		stubResponseAndRun(204, () -> client.deleteArchive(archiveId));
		stubResponseAndAssertThrowsIAX(() -> client.deleteArchive(null));

		String responseJson = "{\n" +
			  "  \"code\": 409,\n" +
			  "  \"message\": \"Status of the archive is not \"uploaded\", \"available\", or \"deleted\"\"\n" +
			  "}";
		stubResponseAndAssertThrowsBadRequestException(409, responseJson, () -> client.deleteArchive(archiveId));
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
		stubResponseAndAssertThrowsBadRequestException(404, responseJson,
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
		stubResponseAndAssertThrowsBadRequestException(404, responseJson,
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
		stubResponseAndAssertThrowsResponseParseException(409, responseJson, () -> client.stopArchive(archiveId));
	}

	@Test
	public void testGetArchive() throws Exception {
		stubArchiveJsonAndAssertEquals(() -> client.getArchive(archiveId));
		stubArchiveJsonAndAssertThrows(() -> client.getArchive(null));

		String responseJson = "{\n" +
			  "  \"code\": 403,\n" +
			  "  \"message\": \"You passed in an invalid JWT token.\"\n" +
			  "}";
		stubResponseAndAssertThrowsResponseParseException(403, responseJson, () -> client.getArchive(archiveId));
	}

	@Test
	public void testListArchives() throws Exception {
		stubListArchiveJsonAndAssertEquals(() -> client.listArchives());
		stubListArchiveJsonAndAssertEquals(() -> client.listArchives(ListStreamCompositionsRequest.builder().build()));
		stubListArchiveJsonAndAssertEquals(() -> client.listArchives(null));

		String responseJson = "{\n" +
			  "  \"code\": 403,\n" +
			  "  \"message\": \"Authentication error\"\n" +
			  "}";
		stubResponseAndAssertThrowsResponseParseException(403, responseJson, () -> client.listArchives());
	}

	@Test
	public void testCreateArchive() throws Exception {
		Archive request = Archive.builder(sessionId).build();
		stubArchiveJsonAndAssertEquals(() -> client.createArchive(request));
		stubArchiveJsonAndAssertThrows(() -> client.createArchive(null));

		String responseJson = "{\n  \"code\": 409,\n  \"message\": \"You attempted to " +
			  "start an archive for a session that does not use the Vonage Video Media Router.\"\n}";
		stubResponseAndAssertThrowsResponseParseException(409, responseJson, () -> client.createArchive(request));
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
		stubResponseAndAssertThrowsBadRequestException(403, responseJson,
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
		stubResponseAndAssertThrowsBadRequestException(404, responseJson,
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
		stubResponseAndAssertThrowsBadRequestException(404, responseJson,
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
		stubResponseAndAssertThrowsResponseParseException(409, responseJson, () -> client.stopBroadcast(broadcastId));
	}

	@Test
	public void testGetBroadcast() throws Exception {
		stubBroadcastJsonAndAssertEquals(() -> client.getBroadcast(broadcastId));
		stubBroadcastJsonAndAssertThrows(() -> client.getBroadcast(null));

		String responseJson = "{\n" +
				"  \"code\": 403,\n" +
				"  \"message\": \"You passed in an invalid JWT token.\"\n" +
				"}";
		stubResponseAndAssertThrowsResponseParseException(403, responseJson, () -> client.getBroadcast(broadcastId));
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
		stubResponseAndAssertThrowsResponseParseException(403, responseJson, () -> client.listBroadcasts());
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
		stubResponseAndAssertThrowsResponseParseException(409, responseJson, () -> client.createBroadcast(request));
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
		Assertions.assertEquals(id, parsed.getId());
		Assertions.assertEquals(connectionId, parsed.getConnectionId());
		Assertions.assertEquals(streamId, parsed.getStreamId());

		stubResponseAndAssertThrowsResponseParseException(409, "{\"code\":409}", () -> client.sipDial(request));
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
	}

	@Test
	public void testGenerateToken() {
		String token = client.generateToken(sessionId);
		Map<String, String> claims = TestUtils.decodeTokenBody(token);

		Assertions.assertEquals("session.connect", claims.get("scope"));
		Assertions.assertEquals(sessionId, claims.get("session_id"));
		Assertions.assertNotNull(claims.get("application_id"));	// TODO test value
		long exp = Long.parseLong(claims.get("exp"));
		long iat = Long.parseLong(claims.get("iat"));
		// One minute less than a day = 86340
		Assertions.assertTrue((iat + 86340) < exp);
		Assertions.assertTrue((iat + 86401) > exp);
		Assertions.assertTrue(token.length() > 100);

		token = client.generateToken(sessionId, TokenOptions.builder().build());
		Assertions.assertEquals(claims.keySet(), TestUtils.decodeTokenBody(token).keySet());
		token = client.generateToken(sessionId, null);
		Assertions.assertEquals(claims.keySet(), TestUtils.decodeTokenBody(token).keySet());
		assertThrows(IllegalArgumentException.class, () -> client.generateToken(null));

		token = client.generateToken(sessionId,TokenOptions.builder()
				.role(Role.SUBSCRIBER)
				.expiryLength(Duration
			    .ofMinutes(12))
				.data("foo bar, blah blah")
				.initialLayoutClassList(Arrays.asList("c1", "c2", "min", "full"))
		        .build()
		);
		claims = TestUtils.decodeTokenBody(token);
		Assertions.assertEquals("subscriber", claims.get("role"));
		Assertions.assertEquals("foo bar, blah blah", claims.get("connection_data"));
		Assertions.assertEquals("c1 c2 min full", claims.get("initial_layout_class_list"));
		Assertions.assertEquals("session.connect", claims.get("scope"));
		Assertions.assertEquals(sessionId, claims.get("session_id"));
		//assertEquals(applicationId, claims.get("application_id")); TODO test value
		exp = Long.parseLong(claims.get("exp"));
		iat = Long.parseLong(claims.get("iat"));
		Assertions.assertTrue((iat + 721) > exp);
		Assertions.assertTrue((iat + 700) < exp);
	}
}
