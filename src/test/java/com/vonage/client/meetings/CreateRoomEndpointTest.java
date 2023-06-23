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
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.time.Instant;
import java.util.UUID;

public class CreateRoomEndpointTest {
	private CreateRoomEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new CreateRoomEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		MeetingRoom request = MeetingRoom.builder("Srs bzns meeting")
			.metadata("code=1123")
			.type(RoomType.LONG_TERM)
			.isAvailable(false)
			.expireAfterUse(true)
			.recordingOptions(RecordingOptions.builder().autoRecord(true).recordOnlyOwner(false).build())
			.initialJoinOptions(InitialJoinOptions.builder().microphoneState(MicrophoneState.OFF).build())
			.callbackUrls(CallbackUrls.builder()
					.recordingsCallbackUrl("example.com/re")
					.roomsCallbackUrl("example.com/ro")
					.sessionsCallbackUrl("example.com/se")
					.build()
			)
			.availableFeatures(AvailableFeatures.builder()
					.isChatAvailable(false)
					.isLocaleSwitcherAvailable(false)
					.isRecordingAvailable(true)
					.isWhiteboardAvailable(false)
					.build()
			)
			.themeId(UUID.fromString("ef2b46f3-8ebb-437e-a671-272e4990fbc8"))
			.joinApprovalLevel(JoinApprovalLevel.EXPLICIT_APPROVAL)
			.expiresAt(Instant.MAX)
			.uiSettings(UISettings.builder().language(RoomLanguage.IT).build())
			.build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("POST", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/meetings/rooms";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{" +
				"\"expires_at\":\"+1000000000-12-31T23:59:59.999Z\"," +
				"\"theme_id\":\"ef2b46f3-8ebb-437e-a671-272e4990fbc8\"," +
				"\"display_name\":\"Srs bzns meeting\",\"metadata\":\"code=1123\"," +
				"\"is_available\":false,\"expire_after_use\":true,\"type\":\"long_term\"," +
				"\"join_approval_level\":\"explicit_approval\",\"recording_options\":" +
				"{\"auto_record\":true,\"record_only_owner\":false}," +
				"\"initial_join_options\":{\"microphone_state\":\"off\"}," +
				"\"ui_settings\":{\"language\":\"it\"},\"callback_urls\":" +
				"{\"rooms_callback_url\":\"example.com/ro\"," +
				"\"sessions_callback_url\":\"example.com/se\"," +
				"\"recordings_callback_url\":\"example.com/re\"}," +
				"\"available_features\":{" +
				"\"is_recording_available\":true,\"is_chat_available\":false," +
				"\"is_whiteboard_available\":false,\"is_locale_switcher_available\":false}" +
				"}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		String expectedResponse = "{\"meeting_code\":\"1234567890\",\"nonsense\":true,\"available_features\":{}}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		MeetingRoom parsed = endpoint.parseResponse(mockResponse);
		assertEquals("1234567890", parsed.getMeetingCode());
		AvailableFeatures availableFeatures = parsed.getAvailableFeatures();
		assertNotNull(availableFeatures);
		assertNull(availableFeatures.getIsChatAvailable());
		assertNull(availableFeatures.getIsRecordingAvailable());
		assertNull(availableFeatures.getIsWhiteboardAvailable());
		assertNull(availableFeatures.getIsLocaleSwitcherAvailable());
		assertNull(parsed.getInitialJoinOptions());
		assertNull(parsed.getLinks());
		assertNull(parsed.getIsAvailable());
		assertNull(parsed.getCallbackUrls());
		assertNull(parsed.getMetadata());
		assertNull(parsed.getExpiresAt());
		assertNull(parsed.getType());
		assertNull(parsed.getThemeId());
		assertNull(parsed.getCreatedAt());
		assertNull(parsed.getId());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new CreateRoomEndpoint(wrapper);
		String expectedUri = baseUri + "/meetings/rooms";
		String displayName = "My custom room";
		MeetingRoom request = MeetingRoom.builder(displayName).build();
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"display_name\":\""+displayName+"\"}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("POST", builder.getMethod());
	}

	@Test(expected = MeetingsResponseException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}
}