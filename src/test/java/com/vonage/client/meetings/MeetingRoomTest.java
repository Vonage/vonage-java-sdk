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

import com.vonage.client.VonageUnexpectedException;
import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class MeetingRoomTest {
	
	@Test
	public void testSerializeAndParseAllParameters() {
		String
			displayName = "Sample room",
			metadata = "var=val",
			roomsCallbackUrl = "http://example.org/roomsCb",
			recordingsCallbackUrl = "http://example.org/recordingsCb",
			sessionsCallbackUrl = "http://example.org/sessionsCb";
		RoomType type = RoomType.LONG_TERM;
		RoomLanguage language = RoomLanguage.EN;
		MicrophoneState microphoneState = MicrophoneState.ON;
		JoinApprovalLevel joinApprovalLevel = JoinApprovalLevel.EXPLICIT_APPROVAL;
		boolean
			isAvailable = false,
			expireAfterUse = true,
			autoRecord = true,
			recordOnlyOwner = false,
			isChatAvailable = true,
			isLocaleSwitcherAvailable = false,
			isRecordingAvailable = true,
			isWhiteboardAvailable = false;
		RecordingOptions recordingOptions = RecordingOptions.builder()
				.autoRecord(autoRecord).recordOnlyOwner(recordOnlyOwner).build();
		InitialJoinOptions initialJoinOptions = InitialJoinOptions.builder()
				.microphoneState(microphoneState).build();
		CallbackUrls callbackUrls = CallbackUrls.builder()
				.roomsCallbackUrl(roomsCallbackUrl)
				.recordingsCallbackUrl(recordingsCallbackUrl)
				.sessionsCallbackUrl(sessionsCallbackUrl).build();
		AvailableFeatures availableFeatures = AvailableFeatures.builder()
				.isChatAvailable(isChatAvailable)
				.isLocaleSwitcherAvailable(isLocaleSwitcherAvailable)
				.isRecordingAvailable(isRecordingAvailable)
				.isWhiteboardAvailable(isWhiteboardAvailable).build();
		UUID themeId = UUID.randomUUID();
		Instant expiresAt = Instant.now().plusSeconds(3600);
		UISettings uiSettings = UISettings.builder().language(language).build();

		MeetingRoom request = MeetingRoom.builder(displayName)
			.metadata(metadata)
			.type(type)
			.isAvailable(isAvailable)
			.expireAfterUse(expireAfterUse)
			.recordingOptions(recordingOptions)
			.initialJoinOptions(initialJoinOptions)
			.callbackUrls(callbackUrls)
			.availableFeatures(availableFeatures)
			.themeId(themeId)
			.joinApprovalLevel(joinApprovalLevel)
			.expiresAt(expiresAt)
			.uiSettings(uiSettings)
			.build();
		
		String json = request.toJson();
		assertTrue(json.contains("\"display_name\":\""+displayName+"\""));
		assertTrue(json.contains("\"metadata\":\""+metadata+"\""));
		assertTrue(json.contains("\"type\":\""+type+"\""));
		assertTrue(json.contains("\"is_available\":"+isAvailable));
		assertTrue(json.contains("\"expire_after_use\":"+expireAfterUse));
		assertTrue(json.contains("\"recording_options\":"+recordingOptions.toJson()));
		assertTrue(json.contains("\"initial_join_options\":"+initialJoinOptions.toJson()));
		assertTrue(json.contains("\"callback_urls\":"+callbackUrls.toJson()));
		assertTrue(json.contains("\"available_features\":"+availableFeatures.toJson()));
		assertTrue(json.contains("\"theme_id\":\""+themeId+"\""));
		assertTrue(json.contains("\"joinApprovalLevel\":\""+joinApprovalLevel+"\""));
		assertTrue(json.contains("\"expires_at\":\""+request.getExpiresAtAsString()+"\""));
		assertTrue(json.contains("\"ui_settings\":"+uiSettings.toJson()));

		MeetingRoom response = MeetingRoom.fromJson(json);
		assertEquals(request.getDisplayName(), response.getDisplayName());
		assertEquals(request.getMetadata(), response.getMetadata());
		assertEquals(request.getType(), response.getType());
		assertEquals(request.getIsAvailable(), response.getIsAvailable());
		assertEquals(request.getExpireAfterUse(), response.getExpireAfterUse());
		assertEquals(request.getThemeId(), response.getThemeId());
		assertEquals(request.getJoinApprovalLevel(), response.getJoinApprovalLevel());
		assertEquals(request.getExpiresAt(), response.getExpiresAt());

		RecordingOptions responseRecOpts = response.getRecordingOptions();
		assertEquals(autoRecord, responseRecOpts.getAutoRecord());
		assertEquals(recordOnlyOwner, responseRecOpts.getRecordOnlyOwner());

		InitialJoinOptions responseJoinOpts = response.getInitialJoinOptions();
		assertEquals(microphoneState, responseJoinOpts.getMicrophoneState());

		UISettings responseUi = response.getUiSettings();
		assertEquals(language, responseUi.getLanguage());

		AvailableFeatures responseFeatures = response.getAvailableFeatures();
		assertEquals(isChatAvailable, responseFeatures.getIsChatAvailable());
		assertEquals(isLocaleSwitcherAvailable, responseFeatures.getIsLocaleSwitcherAvailable());
		assertEquals(isRecordingAvailable, responseFeatures.getIsRecordingAvailable());
		assertEquals(isWhiteboardAvailable, responseFeatures.getIsWhiteboardAvailable());

		CallbackUrls responseCallbacks = response.getCallbackUrls();
		assertEquals(roomsCallbackUrl, responseCallbacks.getRoomsCallbackUrl().toString());
		assertEquals(recordingsCallbackUrl, responseCallbacks.getRecordingsCallbackUrl().toString());
		assertEquals(sessionsCallbackUrl, responseCallbacks.getSessionsCallbackUrl().toString());
	}

	@Test
	public void testNullOrEmptyDisplayName() {
		assertThrows(IllegalArgumentException.class, () -> MeetingRoom.builder(null).build());
		assertThrows(IllegalArgumentException.class, () -> MeetingRoom.builder(" ").build());
	}

	@Test
	public void testExpireAfterUseForInstantRoomType() {
		assertThrows(IllegalStateException.class, () ->
				MeetingRoom.builder("A room")
						.expireAfterUse(false)
						.type(RoomType.INSTANT)
						.build()
		);
		assertThrows(IllegalStateException.class, () ->
				MeetingRoom.builder("A room")
						.expireAfterUse(true)
						.type(RoomType.INSTANT)
						.build()
		);
	}

	@Test
	public void testExpiresAtAndRoomTypeValidation() {
		Instant expire = Instant.now().plusSeconds(10_000);
		assertNull(MeetingRoom.builder("My Room").expiresAt(expire).build().getType());
		assertEquals(expire.truncatedTo(ChronoUnit.MILLIS),
				MeetingRoom.builder("r").type(RoomType.LONG_TERM).expiresAt(expire).build()
						.getExpiresAt()
		);
		assertThrows(IllegalStateException.class, () ->
				MeetingRoom.builder("My Room").expiresAt(expire).type(RoomType.INSTANT).build()
		);
		assertThrows(IllegalStateException.class, () ->
				MeetingRoom.builder("My Room").type(RoomType.LONG_TERM).build()
		);
	}

	@Test
	public void testExpiresAtLeast10MinutesFromNow() {
		assertThrows(IllegalArgumentException.class, () -> MeetingRoom.builder("My Room")
				.expiresAt(Instant.now().plusSeconds(599)).build()
		);
		assertNotNull(
				MeetingRoom.builder("My Room")
						.expiresAt(Instant.now().plusSeconds(601))
						.build()
						.getExpiresAtAsString()
		);
	}

	@Test
	public void testFromJsonResponseOnlyFields() {
		UUID id = UUID.randomUUID();
		String meetingCode = "9876543201";
		String guestUrl = "https://meetings.vonage.com/"+meetingCode;
		String hostUrl = guestUrl + "?participant_token=xyz";
		ZonedDateTime createdAt = ZonedDateTime.now().withFixedOffsetZone();
		RoomLinks links = new RoomLinks();
		(links.guestUrl = new UrlContainer()).href = URI.create(guestUrl);
		(links.hostUrl = new UrlContainer()).href = URI.create(hostUrl);
	
		MeetingRoom response = MeetingRoom.fromJson("{\n" +
				"\"id\":\""+id+"\",\n" +
				"\"meeting_code\":\""+meetingCode+"\",\n" +
				"\"created_at\":\""+createdAt+"\",\n" +
				"\"_links\": {\n" +
				"  \"guest_url\": {\n" +
				"    \"href\":\""+guestUrl+"\"\n" +
				"  },\n" +
				"  \"host_url\": {\n"+
				"    \"href\":\""+hostUrl+"\"\n" +
				"  }\n" +
				"}}"
		);
		assertEquals(id, response.getId());
		assertEquals(guestUrl, response.getLinks().getGuestUrl().toString());
		assertEquals(hostUrl, response.getLinks().getHostUrl().toString());
		assertEquals(meetingCode, response.getMeetingCode());
	}

	@Test
	public void testParseEmptyContainers() {
		MeetingRoom response = MeetingRoom.fromJson("{\n" +
				"\"initial_join_options\":{},\n" +
				"\"recording_options\":{},\n" +
				"\"available_features\":{},\n" +
				"\"ui_settings\":{},\n" +
				"\"callback_urls\":{},\n" +
				"\"_links\":{}\n" +
		"}");

		assertNotNull(response.getInitialJoinOptions());
		assertNull(response.getInitialJoinOptions().getMicrophoneState());
		assertNotNull(response.getRecordingOptions());
		assertNull(response.getRecordingOptions().getAutoRecord());
		assertNull(response.getRecordingOptions().getRecordOnlyOwner());
		assertNotNull(response.getAvailableFeatures());
		assertNull(response.getAvailableFeatures().getIsLocaleSwitcherAvailable());
		assertNull(response.getAvailableFeatures().getIsWhiteboardAvailable());
		assertNull(response.getAvailableFeatures().getIsRecordingAvailable());
		assertNull(response.getAvailableFeatures().getIsChatAvailable());
		assertNotNull(response.getUiSettings());
		assertNull(response.getUiSettings().getLanguage());
		assertNotNull(response.getCallbackUrls());
		assertNull(response.getCallbackUrls().getRecordingsCallbackUrl());
		assertNull(response.getCallbackUrls().getRoomsCallbackUrl());
		assertNull(response.getCallbackUrls().getSessionsCallbackUrl());
		assertNotNull(response.getLinks());
		assertNull(response.getLinks().getGuestUrl());
		assertNull(response.getLinks().getHostUrl());
	}
	
	@Test(expected = VonageUnexpectedException.class)
	public void testFromJsonInvalid() {
		MeetingRoom.fromJson("{malformed]");
	}

	@Test
	public void testFromJsonEmpty() {
		MeetingRoom response = MeetingRoom.fromJson("{}");
		assertNull(response.getDisplayName());
		assertNull(response.getMetadata());
		assertNull(response.getType());
		assertNull(response.getIsAvailable());
		assertNull(response.getExpireAfterUse());
		assertNull(response.getRecordingOptions());
		assertNull(response.getInitialJoinOptions());
		assertNull(response.getCallbackUrls());
		assertNull(response.getAvailableFeatures());
		assertNull(response.getThemeId());
		assertNull(response.getJoinApprovalLevel());
		assertNull(response.getCreatedAt());
		assertNull(response.getExpiresAt());
		assertNull(response.getId());
		assertNull(response.getLinks());
		assertNull(response.getMeetingCode());
		assertNull(response.getUiSettings());
	}
}