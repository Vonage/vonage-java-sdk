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
import com.vonage.client.TestUtils;
import com.vonage.client.auth.JWTAuthMethod;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import java.net.URI;
import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;

public class VideoClientTest extends ClientTest<VideoClient> {
	static final String
			applicationId = "78d335fa-323d-0114-9c3d-d6f0d48968cf",
			sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN",
			streamId = "abc321",
			token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.X.Z",
			id = "b0a5a8c7-dc38-459f-a48d-a7f2008da853",
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
		assertEquals(1, response.size());
		assertEquals(VideoType.SCREEN, response.get(0).getVideoType());
		assertEquals("", response.get(0).getName());
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
		assertEquals(streamId, response.getId());
		assertEquals(VideoType.CUSTOM, response.getVideoType());
		assertEquals(1, response.getLayoutClassList().size());
		assertEquals("full", response.getLayoutClassList().get(0));

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
	public void testSetArchiveLayout() throws Exception {
		ArchiveLayout request = ArchiveLayout.builder(ScreenLayoutType.HORIZONTAL).build();
		stubResponseAndRun(() -> client.setArchiveLayout(archiveId, request));
		stubResponseAndAssertThrowsIAX(() -> client.setArchiveLayout(null, request));
		stubResponseAndAssertThrowsIAX(() -> client.setArchiveLayout(archiveId, null));

		String responseJson = "{\n" +
			  "  \"code\": 403,\n" +
			  "  \"message\": \"Authentication error.\"\n" +
			  "}";
		stubResponseAndAssertThrowsBadRequestException(403, responseJson,
			  () -> client.setArchiveLayout(archiveId, request)
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
		stubListArchiveJsonAndAssertEquals(() -> client.listArchives(ListArchivesRequest.builder().build()));
		stubListArchiveJsonAndAssertEquals(() -> client.listArchives(null));

		String responseJson = "{\n" +
			  "  \"code\": 403,\n" +
			  "  \"message\": \"Authentication error\"\n" +
			  "}";
		stubResponseAndAssertThrowsResponseParseException(403, responseJson, () -> client.listArchives());
	}

	@Test
	public void testCreateArchive() throws Exception {
		CreateArchiveRequest request = CreateArchiveRequest.builder(sessionId).build();
		stubArchiveJsonAndAssertEquals(() -> client.createArchive(request));
		stubArchiveJsonAndAssertThrows(() -> client.createArchive(null));

		String responseJson = "{\n  \"code\": 409,\n  \"message\": \"You attempted to " +
			  "start an archive for a session that does not use the Vonage Video Media Router.\"\n}";
		stubResponseAndAssertThrowsResponseParseException(409, responseJson, () -> client.createArchive(request));
	}

	@Test
	public void testSipDial() throws Exception {
		OutboundSipRequest request = OutboundSipRequest.builder()
				.uri(URI.create("sip:user@sip.partner.com"), false)
				.sessionId(sessionId).token(token).build();

		stubResponse(200, "{\n" +
				"  \"id\": \""+id+"\",\n" +
				"  \"connectionId\": \""+connectionId+"\",\n" +
				"  \"streamId\": \""+streamId+"\"\n" +
				"}"
		);

		OutboundSipResponse parsed = client.sipDial(request);
		assertEquals(id, parsed.getId());
		assertEquals(connectionId, parsed.getConnectionId());
		assertEquals(streamId, parsed.getStreamId());

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

		assertEquals("session.connect", claims.get("scope"));
		assertEquals(sessionId, claims.get("session_id"));
		assertEquals(applicationId, claims.get("application_id"));
		long exp = Long.parseLong(claims.get("exp"));
		long iat = Long.parseLong(claims.get("iat"));
		// One minute less than a day = 86340
		assertTrue((iat + 86340) < exp);
		assertTrue((iat + 86401) > exp);
		assertTrue(token.length() > 100);

		token = client.generateToken(sessionId, TokenOptions.builder().build());
		assertEquals(claims.keySet(), TestUtils.decodeTokenBody(token).keySet());
		token = client.generateToken(sessionId, null);
		assertEquals(claims.keySet(), TestUtils.decodeTokenBody(token).keySet());
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
}
