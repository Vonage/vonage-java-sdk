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

import com.vonage.client.ClientTest;
import static org.junit.Assert.*;
import org.junit.Test;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class MeetingsClientTest extends ClientTest<MeetingsClient> {

	static final String
			ROOM_ID = "b84cc862-0764-4887-9265-37e8a863164d",
			GET_ROOM_RESPONSE = "        {\n" +
			"            \"id\": \""+ROOM_ID+"\",\n" +
			"            \"display_name\": \"Sina's room\",\n" +
			"            \"metadata\": \"foo=bar\",\n" +
			"            \"type\": \"long_term\",\n" +
			"            \"expires_at\": \"2024-01-17T15:53:03.377Z\",\n" +
			"            \"recording_options\": {\n" +
			"                \"auto_record\": false,\n" +
			"                \"record_only_owner\": false\n" +
			"            },\n" +
			"            \"meeting_code\": \"365658578\",\n" +
			"            \"_links\": {\n" +
			"                \"host_url\": {\n" +
			"                    \"href\": \"https://meetings.vonage.com/?room_token=365658578&participant_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjU5N2NmYTAzLTY3NTQtNGE0ZC1hYjU1LWZiMTdkNzc4NzRjMSJ9.eyJwYXJ0aWNpcGFudElkIjoiYTc1NmI0ZGYtYjk3YS00N2JhLThiYjEtYjcyNzA5ZjNhOGFkIiwiaWF0IjoxNjc0MDcyNDM4fQ.8HbvDfIdPIsz9PFWZZM8psPNu5nZziuh0yBeHXkJoDI\"\n" +
			"                },\n" +
			"                \"guest_url\": {\n" +
			"                    \"href\": \"https://meetings.vonage.com/365658578\"\n" +
			"                }\n" +
			"            },\n" +
			"            \"created_at\": \"2023-01-17T16:19:13.518Z\",\n" +
			"            \"is_available\": true,\n" +
			"            \"expire_after_use\": false,\n" +
			"            \"theme_id\": null,\n" +
			"            \"initial_join_options\": {\n" +
			"                \"microphone_state\": \"off\"\n" +
			"            },\n" +
			"            \"joinApprovalLevel\": \"none\",\n" +
			"            \"callback_urls\": {\n" +
			"                \"rooms_callback_url\": \"https://example.com/rooms\",\n" +
			"                \"sessions_callback_url\": \"https://example.com/sessions\",\n" +
			"                \"recordings_callback_url\": \"https://example.com/recordings\"\n" +
			"            },\n" +
			"            \"ui_settings\": {\n" +
			"                \"language\": \"default\"\n" +
			"            },\n" +
			"            \"available_features\": {\n" +
			"                \"is_recording_available\": true,\n" +
			"                \"is_chat_available\": true,\n" +
			"                \"is_whiteboard_available\": true,\n" +
			"                \"is_locale_switcher_available\": true\n" +
			"            }\n" +
			"        }",

			GET_AVAILABLE_ROOMS_RESPONSE = "{\n" +
			"    \"page_size\": \"3\",\n" +
			"    \"_embedded\": [\n" +GET_ROOM_RESPONSE+",\n" +
			"        {\n" +
			"            \"id\": \"2f63e54b-adc1-4dda-a27c-24f04c0f1233\",\n" +
			"            \"display_name\": \"Manchucks\",\n" +
			"            \"metadata\": null,\n" +
			"            \"type\": \"long_term\",\n" +
			"            \"expires_at\": \"2024-01-17T15:53:03.377Z\",\n" +
			"            \"recording_options\": {\n" +
			"                \"auto_record\": false,\n" +
			"                \"record_only_owner\": false\n" +
			"            },\n" +
			"            \"meeting_code\": \"710363620\",\n" +
			"            \"_links\": {\n" +
			"                \"host_url\": {\n" +
			"                    \"href\": \"https://meetings.vonage.com/?room_token=710363620&participant_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjU5N2NmYTAzLTY3NTQtNGE0ZC1hYjU1LWZiMTdkNzc4NzRjMSJ9.eyJwYXJ0aWNpcGFudElkIjoiZTUxZDk3MTgtNGRmMS00ZWM3LTliNWUtNzRiYWIxNWVjMmQ0IiwiaWF0IjoxNjc0MDcyNDM4fQ.2HVqP5rfE38mtuwRHgPdEJ22RHxGzcnL6g64gngIqYk\"\n" +
			"                },\n" +
			"                \"guest_url\": {\n" +
			"                    \"href\": \"https://meetings.vonage.com/710363620\"\n" +
			"                }\n" +
			"            },\n" +
			"            \"created_at\": \"2023-01-18T16:57:27.828Z\",\n" +
			"            \"is_available\": true,\n" +
			"            \"expire_after_use\": false,\n" +
			"            \"theme_id\": null,\n" +
			"            \"initial_join_options\": {\n" +
			"                \"microphone_state\": \"default\"\n" +
			"            },\n" +
			"            \"joinApprovalLevel\": \"none\",\n" +
			"            \"ui_settings\": {\n" +
			"                \"language\": \"default\"\n" +
			"            },\n" +
			"            \"available_features\": {\n" +
			"                \"is_recording_available\": true,\n" +
			"                \"is_chat_available\": true,\n" +
			"                \"is_whiteboard_available\": true,\n" +
			"                \"is_locale_switcher_available\": false\n" +
			"            }\n" +
			"        }\n" +
			"    ],\n" +
			"    \"_links\": {\n" +
			"        \"first\": {\n" +
			"            \"href\": \"api-us.vonage.com/meetings/rooms?page_size=3\"\n" +
			"        },\n" +
			"        \"self\": {\n" +
			"            \"href\": \"api-us.vonage.com/meetings/rooms?page_size=3&start_id=1991085\"\n" +
			"        },\n" +
			"        \"prev\": {\n" +
			"            \"href\": \"api-us.vonage.com/meetings/rooms?page_size=3&end_id=1991084\"\n" +
			"        },\n" +
			"        \"next\": {\n" +
			"            \"href\": \"api-us.vonage.com/meetings/rooms?page_size=3&start_id=1994609\"\n" +
			"        }\n" +
			"    },\n" +
			"    \"total_items\": 5\n" +
			"}";

	public MeetingsClientTest() {
		client = new MeetingsClient(wrapper);
	}

	static void assertEqualsSampleRoom(MeetingRoom parsed) {
		assertNotNull(parsed);
		assertEquals("b84cc862-0764-4887-9265-37e8a863164d", parsed.getId().toString());
		assertEquals("Sina's room", parsed.getDisplayName());
		assertEquals("foo=bar", parsed.getMetadata());
		assertEquals(RoomType.LONG_TERM, parsed.getType());
		ZonedDateTime expiresAt = ZonedDateTime.of(
				2024, 1, 17,
				15, 53, 3,
				377_000_000, ZoneId.systemDefault()
		).truncatedTo(ChronoUnit.MILLIS).withFixedOffsetZone();
		assertEquals(expiresAt, parsed.getExpiresAt());
		assertEquals("2024-01-17T15:53:03.377Z", parsed.getExpiresAtAsString());
		RecordingOptions recordingOptions = parsed.getRecordingOptions();
		assertNotNull(recordingOptions);
		assertFalse(recordingOptions.getAutoRecord());
		assertFalse(recordingOptions.getRecordOnlyOwner());
		assertEquals("365658578", parsed.getMeetingCode());
		RoomLinks roomLinks = parsed.getLinks();
		assertNotNull(roomLinks);
		assertTrue(roomLinks.getHostUrl().getHref().toString().startsWith("https://meetings.vonage.com/?room_token=365658578&participant_token=ey"));
		assertEquals("https://meetings.vonage.com/365658578", roomLinks.getGuestUrl().getHref().toString());
		ZonedDateTime createdAt = ZonedDateTime.of(
				2023, 1, 17,
				16, 19, 13,
				518_000_000, ZoneId.systemDefault()
		).truncatedTo(ChronoUnit.MILLIS).withFixedOffsetZone();
		assertEquals(createdAt, parsed.getCreatedAt());
		assertEquals("2023-01-17T16:19:13.518Z", parsed.getCreatedAtAsString());
		assertTrue(parsed.getIsAvailable());
		assertFalse(parsed.getExpireAfterUse());
		assertNull(parsed.getThemeId());
		InitialJoinOptions initialJoinOptions = parsed.getInitialJoinOptions();
		assertNotNull(initialJoinOptions);
		assertEquals(MicrophoneState.OFF, initialJoinOptions.getMicrophoneState());
		assertEquals(ApprovalLevel.NONE, parsed.getJoinApprovalLevel());
		UISettings uiSettings = parsed.getUiSettings();
		assertNotNull(uiSettings);
		CallbackUrls callbackUrls = parsed.getCallbackUrls();
		assertNotNull(callbackUrls);
		assertEquals("https://example.com/rooms", callbackUrls.getRoomsCallbackUrl().toString());
		assertEquals("https://example.com/sessions", callbackUrls.getSessionsCallbackUrl().toString());
		assertEquals("https://example.com/recordings", callbackUrls.getRecordingsCallbackUrl().toString());
		assertEquals("default", uiSettings.getLanguage());
		AvailableFeatures availableFeatures = parsed.getAvailableFeatures();
		assertNotNull(availableFeatures);
		assertTrue(availableFeatures.getIsRecordingAvailable());
		assertTrue(availableFeatures.getIsChatAvailable());
		assertTrue(availableFeatures.getIsWhiteboardAvailable());
		assertTrue(availableFeatures.getIsLocaleSwitcherAvailable());
	}

	@Test
	public void testGetAvailableRooms() throws Exception {

	}

	@Test
	public void testGetRoom() throws Exception {
		assertEqualsSampleRoom(stubResponseAndGet(200, GET_ROOM_RESPONSE, () -> client.getRoom(ROOM_ID)));
	}

	@Test
	public void testUpdateRoom() throws Exception {

	}

	@Test
	public void testCreateRoom() throws Exception {

	}

	@Test
	public void testGetThemeRooms() throws Exception {

	}

	@Test
	public void testGetThemes() throws Exception {

	}

	@Test
	public void testGetTheme() throws Exception {

	}

	@Test
	public void testCreateTheme() throws Exception {

	}

	@Test
	public void testUpdateTheme() throws Exception {

	}

	@Test
	public void testDeleteTheme() throws Exception {

	}

	@Test
	public void testGetRecordings() throws Exception {

	}

	@Test
	public void testGetRecording() throws Exception {

	}

	@Test
	public void testDeleteRecording() throws Exception {

	}

	@Test
	public void testGetDialNumbers() throws Exception {

	}

	@Test
	public void testGetLogoUploadUrls() throws Exception {

	}

	@Test
	public void testFinalizeLogos() throws Exception {

	}

	@Test
	public void testUpdateApplication() throws Exception {

	}
}
