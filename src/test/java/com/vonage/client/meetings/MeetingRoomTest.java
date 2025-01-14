/*
 *   Copyright 2025 Vonage
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.common.UrlContainer;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.time.Instant;
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
		assertTrue(request.toString().contains(json));
		assertTrue(json.contains("\"display_name\":\""+displayName+"\""));
		assertTrue(json.contains("\"metadata\":\""+metadata+"\""));
		assertTrue(json.contains("\"type\":\""+type+"\""));
		assertTrue(json.contains("\"is_available\":"+isAvailable));
		assertTrue(json.contains("\"expire_after_use\":"+expireAfterUse));
		assertTrue(json.contains("\"recording_options\":{" +
				"\"auto_record\":"+autoRecord +
				",\"record_only_owner\":"+recordOnlyOwner+"}"
		));
		assertTrue(json.contains("\"callback_urls\":{" +
				"\"rooms_callback_url\":\""+roomsCallbackUrl +
				"\",\"sessions_callback_url\":\""+sessionsCallbackUrl +
				"\",\"recordings_callback_url\":\""+recordingsCallbackUrl+"\"}"
		));
		assertTrue(json.contains("\"available_features\":{" +
				"\"is_recording_available\":"+isRecordingAvailable +
				",\"is_chat_available\":"+isChatAvailable +
				",\"is_whiteboard_available\":"+isWhiteboardAvailable +
				",\"is_locale_switcher_available\":"+isLocaleSwitcherAvailable+"}"
		));
		assertTrue(json.contains("\"initial_join_options\":{\"microphone_state\":\""+microphoneState+"\"}"));
		assertTrue(json.contains("\"ui_settings\":{\"language\":\""+language+"\"}"));
		assertTrue(json.contains("\"theme_id\":\""+themeId+"\""));
		assertTrue(json.contains("\"join_approval_level\":\""+joinApprovalLevel+"\""));
		assertTrue(json.contains("\"expires_at\":\""+request.getExpiresAt()+"\""));

		MeetingRoom response = MeetingRoom.fromJson(json);
		assertEquals(request, response);
		assertEquals(request.hashCode(), response.hashCode());
		assertEquals(request.toString(), response.toString());

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
	public void testUpdateFromJson() {
		MeetingRoom room = MeetingRoom.builder("Room name 0")
				.expiresAt(Instant.now().plusSeconds(7200))
				.type(RoomType.LONG_TERM).expireAfterUse(true)
				.availableFeatures(AvailableFeatures.builder()
						.isChatAvailable(false).isRecordingAvailable(true).build()
				)
				.joinApprovalLevel(JoinApprovalLevel.NONE).build();

		assertNull(room.getInitialJoinOptions());
		assertNull(room.getUiSettings());
		assertNull(room.getIsAvailable());
		assertNull(room.getCallbackUrls());
		assertFalse(room.getAvailableFeatures().getIsChatAvailable());
		assertTrue(room.getAvailableFeatures().getIsRecordingAvailable());
		assertNull(room.getAvailableFeatures().getIsLocaleSwitcherAvailable());
		assertNull(room.getAvailableFeatures().getIsWhiteboardAvailable());

		int ogHashcode = room.hashCode();
		String ogToString = room.toString();

		room.updateFromJson("{\"name\":\"Updated name 1\",\"join_approval_level\":\"after_owner_only\"," +
				"\"is_available\":true,\"type\":\"instant\",\"ui_settings\":{\"language\":\"ca\"}," +
				"\"expire_after_use\":false,\"initial_join_options\":{},\"callback_urls\":{}," +
				"\"available_features\":{\"is_locale_switcher_available\":false,\"is_chat_available\":false}}"
		);
		assertNotEquals(ogHashcode, room.hashCode());
		assertNotEquals(ogToString, room.toString());

		assertTrue(room.getIsAvailable());
		assertFalse(room.getExpireAfterUse());
		assertNotNull(room.getInitialJoinOptions());
		assertNotNull(room.getCallbackUrls());
		assertEquals(RoomLanguage.CA, room.getUiSettings().getLanguage());
		assertEquals(RoomType.INSTANT, room.getType());
		assertEquals(JoinApprovalLevel.AFTER_OWNER_ONLY, room.getJoinApprovalLevel());
		assertFalse(room.getAvailableFeatures().getIsLocaleSwitcherAvailable());
		assertFalse(room.getAvailableFeatures().getIsChatAvailable());
		assertNull(room.getAvailableFeatures().getIsRecordingAvailable());
		assertNull(room.getAvailableFeatures().getIsWhiteboardAvailable());

		assertThrows(VonageResponseParseException.class, () -> room.updateFromJson("{malformed]"));
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
						.getExpiresAt()
		);
	}

	@Test
	public void testFromJsonResponseOnlyFields() {
		UUID id = UUID.randomUUID();
		String meetingCode = "9876543201";
		String guestUrl = "https://meetings.vonage.com/"+meetingCode;
		String hostUrl = guestUrl + "?participant_token=xyz";
		Instant createdAt = Instant.now();
		RoomLinks links = new RoomLinks();
		class TestContainer extends UrlContainer {
			TestContainer(URI href) {
				this.href = href;
			}
		}
		links.guestUrl = new TestContainer(URI.create(guestUrl));
		links.hostUrl = new TestContainer(URI.create(hostUrl));
	
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
	
	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> MeetingRoom.fromJson("{malformed]"));
	}

	@Test
	public void testFromJsonEmpty() {
		MeetingRoom response = MeetingRoom.fromJson("{}");
		assertEquals("MeetingRoom {}", response.toString());
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

	@Test
	public void testInvalidEnums() {
		MeetingRoom room = MeetingRoom.fromJson(
				"{\"join_approval_level\":\"bar\"," +
				"\"initial_join_options\":{\"microphone_state\":\"bombastic\"}," +
				"\"type\":\"casual\",\"ui_settings\":{\"language\":\"yoda\"}}"
		);
		assertNull(room.getJoinApprovalLevel());
		assertNull(room.getInitialJoinOptions().getMicrophoneState());
		assertNull(room.getType());
		assertNull(room.getUiSettings().getLanguage());
	}

	@Test
	public void testRoomLanguageEnum() {
		assertEquals(RoomLanguage.DE, RoomLanguage.fromString("de"));
		assertEquals(RoomLanguage.AR, RoomLanguage.fromString("AR"));
		assertEquals(RoomLanguage.ZH_CN, RoomLanguage.fromString("zh-CN"));
		assertEquals("PT-BR", RoomLanguage.PT_BR.toString());
		assertEquals("ZH-TW", RoomLanguage.ZH_TW.toString());
	}

	@Test
	public void triggerJsonProcessingException() {
		class SelfRefrencing extends MeetingRoom {
			@JsonProperty("self") final SelfRefrencing self = this;
		}
		assertThrows(VonageUnexpectedException.class, () -> new SelfRefrencing().toJson());
	}
}