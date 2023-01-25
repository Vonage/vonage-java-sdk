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
import java.util.*;
import java.util.function.Supplier;

public class MeetingsClientTest extends ClientTest<MeetingsClient> {

	static final UUID
			RANDOM_ID = UUID.randomUUID(),
			ROOM_ID = UUID.fromString("b84cc862-0764-4887-9265-37e8a863164d");
	static final String
			GET_ROOM_RESPONSE = "        {\n" +
			"            \"id\": \""+ROOM_ID+"\",\n" +
			"            \"display_name\": \"Sina's room\",\n" +
			"            \"metadata\": \"foo=bar\",\n" +
			"            \"type\": \"long_term\",\n" +
			"            \"expires_at\": \"3000-01-17T15:53:03.377Z\",\n" +
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

			LIST_ROOMS_RESPONSE = "{\n" +
			"    \"page_size\": \"3\",\n" +
			"    \"_embedded\": [\n" +GET_ROOM_RESPONSE+",\n" +
			"        {\n" +
			"            \"id\": \"2f63e54b-adc1-4dda-a27c-24f04c0f1233\",\n" +
			"            \"display_name\": \"Manchucks\",\n" +
			"            \"metadata\": null,\n" +
			"            \"type\": \"instant\",\n" +
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
			"            \"expire_after_use\": true,\n" +
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
			"}",

			SAMPLE_THEME_RESPONSE = "{\n" +
					"    \"theme_id\": \"d03e71e5-9336-49c1-8adc-12fb3fa98110\",\n" +
					"    \"theme_name\": \"Theme1\",\n" +
					"    \"domain\": \"VBC\",\n" +
					"    \"account_id\": \"94a99d02-24b2-445a-9526-2846aa5f846\",\n" +
					"    \"application_id\": \"862f8c7b-d203-4729-68a3-7eded210c9ca\",\n" +
					"    \"main_color\": \"#12f64e\",\n" +
					"    \"short_company_url\": \"https://t.co/acme\",\n" +
					"    \"brand_text\": \"Looney Tunes Ltd.\",\n" +
					"    \"brand_image_colored\": \"color-key\",\n" +
					"    \"brand_image_white\": \"white-key\",\n" +
					"    \"branded_favicon\": \"favicon-key\",\n" +
					"    \"brand_image_colored_url\": \"https://example.com/color.png\",\n" +
					"    \"brand_image_white_url\": \"https://example.com/white.png\",\n" +
					"    \"branded_favicon_url\": \"https://example.com/favicon.png\"\n" +
					"  }",

			SAMPLE_RECORDING_RESPONSE = "{\n" +
					"   \"id\": \"497f6eca-6276-4993-bfeb-53cbbbba6f08\",\n" +
					"   \"session_id\": \"2_MX40NjMwODczMn5-MTU3NTgyODEwNzQ2MH5OZDJrVmdBRUNDbG5MUzNqNXgya20yQ1Z-fg\",\n" +
					"   \"started_at\": \"2019-08-24T14:15:22Z\",\n" +
					"   \"ended_at\": \"2019-08-24T14:15:22Z\",\n" +
					"   \"status\": \"started\",\n" +
					"   \"_links\": {\n" +
					"      \"url\": {\n" +
					"         \"href\": \"http://example.com/recording.mp4\"\n" +
					"      }\n" +
					"   }\n" +
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
				3000, 1, 17,
				15, 53, 3,
				377_000_000, ZoneId.systemDefault()
		).truncatedTo(ChronoUnit.MILLIS).withFixedOffsetZone();
		assertEquals(expiresAt, parsed.getExpiresAt());
		assertEquals("3000-01-17T15:53:03.377Z", parsed.getExpiresAtAsString());
		RecordingOptions recordingOptions = parsed.getRecordingOptions();
		assertNotNull(recordingOptions);
		assertFalse(recordingOptions.getAutoRecord());
		assertFalse(recordingOptions.getRecordOnlyOwner());
		assertEquals("365658578", parsed.getMeetingCode());
		RoomLinks roomLinks = parsed.getLinks();
		assertNotNull(roomLinks);
		assertTrue(roomLinks.getHostUrl().toString().startsWith("https://meetings.vonage.com/?room_token=365658578&participant_token=ey"));
		assertEquals("https://meetings.vonage.com/365658578", roomLinks.getGuestUrl().toString());
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
		assertEquals(JoinApprovalLevel.NONE, parsed.getJoinApprovalLevel());
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

	void stubResponseAndAssertEqualsSampleRoom(Supplier<? extends MeetingRoom> call) throws Exception {
		assertEqualsSampleRoom(stubResponseAndGet(200, GET_ROOM_RESPONSE, call));
	}

	static void assertEqualsGetAvailableRooms(List<MeetingRoom> rooms) {
		assertEquals(2, rooms.size());
		assertEqualsSampleRoom(rooms.get(0));
		MeetingRoom otherRoom = rooms.get(1);
		assertEquals("2f63e54b-adc1-4dda-a27c-24f04c0f1233", otherRoom.getId().toString());
		assertNull(otherRoom.getMetadata());
		assertEquals(RoomType.INSTANT, otherRoom.getType());
		assertEquals("710363620", otherRoom.getMeetingCode());
		assertTrue(otherRoom.getIsAvailable());
		assertTrue(otherRoom.getExpireAfterUse());
		assertEquals(MicrophoneState.DEFAULT, otherRoom.getInitialJoinOptions().getMicrophoneState());
		assertTrue(otherRoom.getAvailableFeatures().getIsChatAvailable());
		assertFalse(otherRoom.getAvailableFeatures().getIsLocaleSwitcherAvailable());
		assertEquals(JoinApprovalLevel.NONE, otherRoom.getJoinApprovalLevel());
		assertEquals("default", otherRoom.getUiSettings().getLanguage());
	}

	static void assertEqualsGetAvailableRooms(ListRoomsResponse parsed) {
		assertEqualsGetAvailableRooms(parsed.getMeetingRooms());
		NavigationLinks links = parsed.getLinks();
		assertEquals("api-us.vonage.com/meetings/rooms?page_size=3", links.getFirst().toString());
		assertEquals("api-us.vonage.com/meetings/rooms?page_size=3&start_id=1991085", links.getSelf().toString());
		assertEquals("api-us.vonage.com/meetings/rooms?page_size=3&end_id=1991084", links.getPrev().toString());
		assertEquals("api-us.vonage.com/meetings/rooms?page_size=3&start_id=1994609", links.getNext().toString());
	}

	static void assertEqualsSampleTheme(Theme parsed) {
		assertNotNull(parsed);
		assertEquals("d03e71e5-9336-49c1-8adc-12fb3fa98110", parsed.getThemeId().toString());
		assertEquals("Theme1", parsed.getThemeName());
		assertEquals(ThemeDomain.VBC, parsed.getDomain());
		assertEquals("94a99d02-24b2-445a-9526-2846aa5f846", parsed.getAccountId());
		assertEquals("862f8c7b-d203-4729-68a3-7eded210c9ca", parsed.getApplicationId());
		assertEquals("#12f64e", parsed.getMainColor());
		assertEquals("https://t.co/acme", parsed.getShortCompanyUrl());
		assertEquals("Looney Tunes Ltd.", parsed.getBrandText());
		assertEquals("color-key", parsed.getBrandImageColored());
		assertEquals("white-key", parsed.getBrandImageWhite());
		assertEquals("favicon-key", parsed.getBrandedFavicon());
		assertEquals("https://example.com/color.png", parsed.getBrandImageColoredUrl().toString());
		assertEquals("https://example.com/white.png", parsed.getBrandImageWhiteUrl().toString());
		assertEquals("https://example.com/favicon.png", parsed.getBrandedFaviconUrl().toString());
	}

	void stubResponseAndAssertEqualsSampleTheme(Supplier<? extends Theme> call) throws Exception {
		assertEqualsSampleTheme(stubResponseAndGet(200, SAMPLE_THEME_RESPONSE, call));
	}

	static void assertEqualsSampleRecording(Recording parsed) {
		assertNotNull(parsed);
		assertEquals("497f6eca-6276-4993-bfeb-53cbbbba6f08", parsed.getId().toString());
		assertEquals("2_MX40NjMwODczMn5-MTU3NTgyODEwNzQ2MH5OZDJrVmdBRUNDbG5MUzNqNXgya20yQ1Z-fg", parsed.getSessionId());
		assertEquals("2019-08-24T14:15:22Z", parsed.getStartedAtAsString());
		assertEquals("2019-08-24T14:15:22Z", parsed.getEndedAtAsString());
		assertEquals(RecordingStatus.STARTED, parsed.getStatus());
		assertEquals("http://example.com/recording.mp4", parsed.getLinks().getUrl().toString());
	}

	void stubResponseAndAssertEqualsSampleRecording(Supplier<? extends Recording> call) throws Exception {
		assertEqualsSampleRecording(stubResponseAndGet(200, SAMPLE_RECORDING_RESPONSE, call));
	}

	@Test
	public void testListRooms() throws Exception {
		assertEqualsGetAvailableRooms(stubResponseAndGet(200, LIST_ROOMS_RESPONSE,
				() -> client.listRooms(1991085, 1994609, 3))
		);
		assertEqualsGetAvailableRooms(stubResponseAndGet(200, LIST_ROOMS_RESPONSE,
				() -> client.listRooms(1, 2, 1))
		);
		assertEqualsGetAvailableRooms(stubResponseAndGet(200, LIST_ROOMS_RESPONSE,
				() -> client.listRooms(null, null, null))
		);
		assertEqualsGetAvailableRooms(stubResponseAndGet(200, LIST_ROOMS_RESPONSE,
				() -> client.listRooms(null, null, 20))
		);
		assertEqualsGetAvailableRooms(stubResponseAndGet(200, LIST_ROOMS_RESPONSE,
				() -> client.listRooms(29, 31, null))
		);
		assertEqualsGetAvailableRooms(stubResponseAndGet(200, LIST_ROOMS_RESPONSE,
				() -> client.listRooms())
		);

		stubResponseAndAssertThrows(200, LIST_ROOMS_RESPONSE,
			() -> client.listRooms(7, 6, 9), IllegalArgumentException.class
		);
	}

	@Test
	public void testGetRoom() throws Exception {
		stubResponseAndAssertEqualsSampleRoom(() -> client.getRoom(ROOM_ID));

		stubResponseAndAssertThrows(200, GET_ROOM_RESPONSE,
			() -> client.getRoom(null), NullPointerException.class
		);
	}

	@Test
	public void testUpdateRoom() throws Exception {
		UpdateRoomRequest request = UpdateRoomRequest.builder().build();
		stubResponseAndAssertEqualsSampleRoom(() -> client.updateRoom(ROOM_ID, request));

		stubResponseAndAssertThrows(200, GET_ROOM_RESPONSE,
			() -> client.updateRoom(ROOM_ID, null), NullPointerException.class
		);
		stubResponseAndAssertThrows(200, GET_ROOM_RESPONSE,
			() -> client.updateRoom(null, request), NullPointerException.class
		);
	}

	@Test
	public void testCreateRoom() throws Exception {
		MeetingRoom request = MeetingRoom.builder("Sample mr").build();
		stubResponseAndAssertEqualsSampleRoom(() -> client.createRoom(request));

		stubResponseAndAssertThrows(201, GET_ROOM_RESPONSE,
			() -> client.createRoom(null), NullPointerException.class
		);
	}

	@Test
	public void testSearchRoomsByTheme() throws Exception {
		assertEqualsGetAvailableRooms(stubResponseAndGet(200, LIST_ROOMS_RESPONSE,
				() -> client.searchRoomsByTheme(RANDOM_ID, null, null, null))
		);
		assertEqualsGetAvailableRooms(stubResponseAndGet(200, LIST_ROOMS_RESPONSE,
				() -> client.searchRoomsByTheme(RANDOM_ID, 1, 12, 3))
		);
		assertEqualsGetAvailableRooms(stubResponseAndGet(200, LIST_ROOMS_RESPONSE,
				() -> client.searchRoomsByTheme(RANDOM_ID))
		);

		stubResponseAndAssertThrows(200, LIST_ROOMS_RESPONSE,
			() -> client.searchRoomsByTheme(null, 3, 9, 1), NullPointerException.class
		);
		stubResponseAndAssertThrows(200, LIST_ROOMS_RESPONSE,
			() -> client.searchRoomsByTheme(RANDOM_ID, 2, 1, 30), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200, LIST_ROOMS_RESPONSE,
			() -> client.searchRoomsByTheme(null), NullPointerException.class
		);
	}

	@Test
	public void testListThemes() throws Exception {
		String responseJson = "["+SAMPLE_THEME_RESPONSE+",{\"theme_id\":\""+RANDOM_ID+"\"}]";
		List<Theme> parsed = stubResponseAndGet(200, responseJson, () -> client.listThemes());
		assertEquals(2, parsed.size());
		assertEqualsSampleTheme(parsed.get(0));
		assertEquals(RANDOM_ID, parsed.get(1).getThemeId());
	}

	@Test
	public void testGetTheme() throws Exception {
		stubResponseAndAssertEqualsSampleTheme(() -> client.getTheme(RANDOM_ID));

		stubResponseAndAssertThrows(200, SAMPLE_THEME_RESPONSE,
			() -> client.getTheme(null), NullPointerException.class
		);
	}

	@Test
	public void testCreateTheme() throws Exception {
		Theme request = Theme.builder().brandText("My Company").mainColor("#fff000").build();
		stubResponseAndAssertEqualsSampleTheme(() -> client.createTheme(request));

		stubResponseAndAssertThrows(200, SAMPLE_THEME_RESPONSE,
			() -> client.createTheme(null), NullPointerException.class
		);
	}

	@Test
	public void testUpdateTheme() throws Exception {
		Theme request = Theme.builder().build();
		stubResponseAndAssertEqualsSampleTheme(() -> client.updateTheme(RANDOM_ID, request));

		stubResponseAndAssertThrows(200, SAMPLE_THEME_RESPONSE,
			() -> client.updateTheme(RANDOM_ID, null), NullPointerException.class
		);
		stubResponseAndAssertThrows(200, SAMPLE_THEME_RESPONSE,
			() -> client.updateTheme(null, request), NullPointerException.class
		);
	}

	@Test
	public void testDeleteTheme() throws Exception {
		stubResponseAndRun(204, () -> client.deleteTheme(RANDOM_ID, false));

		stubResponseAndAssertThrows(204,
			() -> client.deleteTheme(null, true), NullPointerException.class
		);
	}

	@Test
	public void testListRecordings() throws Exception {
		String responseJson = "{\"_embedded\":{\"recordings\":[" + SAMPLE_RECORDING_RESPONSE + ",{}]}}";
		stubResponse(200, responseJson);
		List<Recording> recordings = client.listRecordings("session_id");
		assertEquals(2, recordings.size());
		assertEqualsSampleRecording(recordings.get(0));
		assertNotNull(recordings.get(1));

		stubResponseAndAssertThrows(200,
			() -> client.listRecordings(null), IllegalArgumentException.class
		);
	}

	@Test
	public void testGetRecording() throws Exception {
		stubResponseAndAssertEqualsSampleRecording(() -> client.getRecording(RANDOM_ID));

		stubResponseAndAssertThrows(200,
			() -> client.getRecording(null), NullPointerException.class
		);
	}

	@Test
	public void testDeleteRecording() throws Exception {
		stubResponseAndRun(204, () -> client.deleteRecording(RANDOM_ID));

		stubResponseAndAssertThrows(204,
			() -> client.deleteRecording(null), NullPointerException.class
		);
	}

	@Test
	public void testListDialNumbers() throws Exception {
		String responseJson = "[\n" +
				"{\"number\":\"17329672755\",\"displayName\":\"United States\",\"locale\":\"en_US\"},\n" +
				"{\"number\":\"48123964788\",\"displayName\":\"Poland\",\"locale\":\"pl_PL\"},\n" +
				"{\"number\":\"827047844377\",\"displayName\":\"South Korea\",\"locale\":\"ko_KR\"}\n" +
			"]";
		List<DialInNumber> parsed = stubResponseAndGet(200, responseJson, () -> client.listDialNumbers());
		assertEquals(3, parsed.size());
		assertEquals("17329672755", parsed.get(0).getNumber());
		assertEquals("United States", parsed.get(0).getDisplayName());
		assertEquals(Locale.US, parsed.get(0).getLocale());
		assertEquals("48123964788", parsed.get(1).getNumber());
		assertEquals("Poland", parsed.get(1).getDisplayName());
		assertEquals(Locale.forLanguageTag("pl-PL"), parsed.get(1).getLocale());
		assertEquals("827047844377", parsed.get(2).getNumber());
		assertEquals("South Korea", parsed.get(2).getDisplayName());
		assertEquals(Locale.KOREA, parsed.get(2).getLocale());
	}

	@Test
	public void testListLogoUploadUrls() throws Exception {
		String responseJson = "[\n" +
				"{},   {\n" +
				"      \"url\": \"https://storage-url.com\",\n" +
				"      \"fields\": {\n" +
				"         \"Content-Type\": \"image/png\",\n" +
				"         \"key\": \"auto-expiring-temp/logos/white/ca63a155-d\",\n" +
				"         \"logoType\": \"white\",\n" +
				"         \"bucket\": \"s3\",\n" +
				"         \"X-Amz-Algorithm\": \"stringA\",\n" +
				"         \"X-Amz-Credential\": \"stringC\",\n" +
				"         \"X-Amz-Date\": \"stringD\",\n" +
				"         \"X-Amz-Security-Token\": \"stringT\",\n" +
				"         \"Policy\": \"stringP\",\n" +
				"         \"X-Amz-Signature\": \"stringS\"\n" +
				"      }\n" +
				"   }, {\"url\": \"http://example.com\"}\n" +
				"]";
		stubResponse(200, responseJson);
		List<LogoUploadsUrlResponse> parsed = client.listLogoUploadUrls();
		assertEquals(3, parsed.size());
		assertNotNull(parsed.get(0));
		assertEquals("http://example.com", parsed.get(2).getUrl().toString());
		LogoUploadsUrlResponse sample = parsed.get(1);
		LogoUploadsUrlResponse.Fields fields = sample.getFields();
		assertNotNull(fields);
		assertEquals("https://storage-url.com", sample.getUrl().toString());
		assertEquals("image/png", fields.getContentType().toString());
		assertEquals("auto-expiring-temp/logos/white/ca63a155-d", fields.getKey());
		assertEquals(LogoType.WHITE, fields.getLogoType());
		assertEquals("s3", fields.getBucket());
		assertEquals("stringA", fields.getAmzAlgorithm());
		assertEquals("stringC", fields.getAmzCredential());
		assertEquals("stringD", fields.getAmzDate());
		assertEquals("stringT", fields.getAmzSecurityToken());
		assertEquals("stringP", fields.getPolicy());
		assertEquals("stringS", fields.getAmzSignature());
	}

	@Test
	public void testFinalizeLogos() throws Exception {
		stubResponseAndRun(200,() -> client.finalizeLogos(RANDOM_ID, Arrays.asList("key1", "l-k-2", "k3")));
		stubResponseAndRun(200, () -> client.finalizeLogos(RANDOM_ID, Collections.singletonList("a")));

		stubResponseAndAssertThrows(200,
			() -> client.finalizeLogos(null, Arrays.asList("logo_key0")), NullPointerException.class
		);
		stubResponseAndAssertThrows(200,
			() -> client.finalizeLogos(RANDOM_ID, null), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200,
			() -> client.finalizeLogos(RANDOM_ID, Collections.emptyList()), IllegalArgumentException.class
		);
	}

	@Test
	public void testUpdateApplication() throws Exception {
		UpdateApplicationRequest request = UpdateApplicationRequest.builder().defaultThemeId(RANDOM_ID).build();
		String accId = "account-id_123";
		UUID appId = UUID.randomUUID();
		String responseJson = "{\n" +
				"  \"application_id\": \""+appId+"\",\n" +
				"  \"account_id\": \""+accId+"\",\n" +
				"  \"default_theme_id\": \""+RANDOM_ID+"\"\n" +
				"}";
		Application parsed = stubResponseAndGet(200, responseJson, () -> client.updateApplication(request));
		assertEquals(appId, parsed.getApplicationId());
		assertEquals(accId, parsed.getAccountId());
		assertEquals(RANDOM_ID, parsed.getDefaultThemeId());

		stubResponseAndAssertThrows(200, responseJson,
			() -> client.updateApplication(null), NullPointerException.class
		);
	}
}
