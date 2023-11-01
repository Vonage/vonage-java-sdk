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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.ClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.common.HalLinks;
import com.vonage.client.common.HttpMethod;
import org.apache.http.client.HttpClient;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
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
			SAMPLE_ROOM_RESPONSE = "        {\n" +
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
			"            \"join_approval_level\": \"none\",\n" +
			"            \"callback_urls\": {\n" +
			"                \"rooms_callback_url\": \"https://example.com/rooms\",\n" +
			"                \"sessions_callback_url\": \"https://example.com/sessions\",\n" +
			"                \"recordings_callback_url\": \"https://example.com/recordings\"\n" +
			"            },\n" +
			"            \"ui_settings\": {\n" +
			"                \"language\": \"de\"\n" +
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
			"    \"total_items\": 3,\n" +
			"    \"_embedded\": [\n" + SAMPLE_ROOM_RESPONSE +",\n" +
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
			"            \"join_approval_level\": \"none\",\n" +
			"            \"ui_settings\": {\n" +
			"                \"language\": \"de\"\n" +
			"            },\n" +
			"            \"available_features\": {\n" +
			"                \"is_recording_available\": true,\n" +
			"                \"is_chat_available\": true,\n" +
			"                \"is_whiteboard_available\": true,\n" +
			"                \"is_locale_switcher_available\": false\n" +
			"            }\n" +
			"        },\n" +
			"        {}\n" +
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
			"    }\n" +
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
					"   \"ended_at\": \"2019-11-30T00:59:06Z\",\n" +
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
		Instant expiresAt = ZonedDateTime.of(
				3000, 1, 17,
				15, 53, 3,
				377_000_000, ZoneId.of("UTC")
		).truncatedTo(ChronoUnit.MILLIS).toInstant();
		assertEquals(expiresAt, parsed.getExpiresAt());
		assertEquals(Instant.parse("3000-01-17T15:53:03.377Z"), parsed.getExpiresAt());
		RecordingOptions recordingOptions = parsed.getRecordingOptions();
		assertNotNull(recordingOptions);
		assertFalse(recordingOptions.getAutoRecord());
		assertFalse(recordingOptions.getRecordOnlyOwner());
		assertEquals("365658578", parsed.getMeetingCode());
		RoomLinks roomLinks = parsed.getLinks();
		assertNotNull(roomLinks);
		assertTrue(roomLinks.getHostUrl().toString().startsWith("https://meetings.vonage.com/?room_token=365658578&participant_token=ey"));
		assertEquals("https://meetings.vonage.com/365658578", roomLinks.getGuestUrl().toString());
		Instant createdAt = ZonedDateTime.of(
				2023, 1, 17,
				16, 19, 13,
				518_000_000, ZoneId.of("UTC")
		).truncatedTo(ChronoUnit.MILLIS).toInstant();
		assertEquals(createdAt, parsed.getCreatedAt());
		assertEquals(Instant.parse("2023-01-17T16:19:13.518Z"), parsed.getCreatedAt());
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
		assertEquals(RoomLanguage.DE, uiSettings.getLanguage());
		AvailableFeatures availableFeatures = parsed.getAvailableFeatures();
		assertNotNull(availableFeatures);
		assertTrue(availableFeatures.getIsRecordingAvailable());
		assertTrue(availableFeatures.getIsChatAvailable());
		assertTrue(availableFeatures.getIsWhiteboardAvailable());
		assertTrue(availableFeatures.getIsLocaleSwitcherAvailable());
	}

	void stubResponseAndAssertEqualsSampleRoom(Supplier<? extends MeetingRoom> call) throws Exception {
		assertEqualsSampleRoom(stubResponseAndGet(200, SAMPLE_ROOM_RESPONSE, call));
	}

	static void assertEqualsAvailableRooms(List<MeetingRoom> rooms) {
		assertEquals(3, rooms.size());
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
		assertEquals(RoomLanguage.DE, otherRoom.getUiSettings().getLanguage());
	}

	static void assertEqualsAvailableRooms(ListRoomsResponse parsed) {
		assertEqualsAvailableRooms(parsed.getMeetingRooms());
		HalLinks links = parsed.getLinks();
		assertEquals("api-us.vonage.com/meetings/rooms?page_size=3", links.getFirstUrl().toString());
		assertEquals("api-us.vonage.com/meetings/rooms?page_size=3&start_id=1991085", links.getSelfUrl().toString());
		assertEquals("api-us.vonage.com/meetings/rooms?page_size=3&end_id=1991084", links.getPrevUrl().toString());
		assertEquals("api-us.vonage.com/meetings/rooms?page_size=3&start_id=1994609", links.getNextUrl().toString());
	}

	static void assertEqualsSampleTheme(Theme parsed) {
		assertNotNull(parsed);
		assertEquals("d03e71e5-9336-49c1-8adc-12fb3fa98110", parsed.getThemeId().toString());
		assertEquals("Theme1", parsed.getThemeName());
		assertEquals(ThemeDomain.VBC, parsed.getDomain());
		assertEquals("94a99d02-24b2-445a-9526-2846aa5f846", parsed.getAccountId());
		assertEquals("862f8c7b-d203-4729-68a3-7eded210c9ca", parsed.getApplicationId().toString());
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
		assertEquals(UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08"), parsed.getId());
		assertEquals("2_MX40NjMwODczMn5-MTU3NTgyODEwNzQ2MH5OZDJrVmdBRUNDbG5MUzNqNXgya20yQ1Z-fg", parsed.getSessionId());
		assertEquals(RecordingStatus.STARTED, parsed.getStatus());
		assertEquals(URI.create("http://example.com/recording.mp4"), parsed.getUrl());

		assertEquals(Instant.parse("2019-08-24T14:15:22Z"), parsed.getStartedAt());
		Instant startedAt = ZonedDateTime.of(
				2019, 8, 24,
				14, 15, 22,
				0, ZoneId.of("UTC")
		).truncatedTo(ChronoUnit.MILLIS).toInstant();
		assertEquals(startedAt, parsed.getStartedAt());

		assertEquals(Instant.parse("2019-11-30T00:59:06Z"), parsed.getEndedAt());
		Instant endedAt = ZonedDateTime.of(
				2019, 11, 30,
				0, 59, 6,
				0, ZoneId.of("UTC")
		).truncatedTo(ChronoUnit.MILLIS).toInstant();
		assertEquals(endedAt, parsed.getEndedAt());
	}

	void stubResponseAndAssertEqualsSampleRecording(Supplier<? extends Recording> call) throws Exception {
		assertEqualsSampleRecording(stubResponseAndGet(200, SAMPLE_RECORDING_RESPONSE, call));
	}

	void testPaginatedMeetingRoomsResponse(Supplier<List<MeetingRoom>> call) throws Exception {
		String firstJson = "{\n" +
				"   \"page_size\": 1000,\n" +
				"   \"total_items\": 1080,\n" +
				"   \"_embedded\": [{\"theme_id\":\""+RANDOM_ID+"\"},{}],\n" +
				"   \"_links\": {\n" +
				"      \"first\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/meetings/rooms?page_size=50\"\n" +
				"      },\n" +
				"      \"self\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/meetings/rooms?page_size=50&start_id=2293905\"\n" +
				"      },\n" +
				"      \"next\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/meetings/rooms?page_size=50&start_id=2293906\"\n" +
				"      },\n" +
				"      \"prev\": {\n" +
				"         \"href\": \"https://api-eu.vonage.com/meetings/rooms?page_size=50&start_id=2293904\"\n" +
				"      }\n" +
				"   }\n" +
				"}",
				secondJson = "{\n" +
						"   \"page_size\": 80,\n" +
						"   \"total_items\": 1080,\n" +
						"   \"_embedded\": [{},{},{},{},{},{},{\"id\":\""+ROOM_ID+"\"},{}],\n" +
						"   \"_links\": {\n" +
						"      \"next\": {\n" +
						"         \"href\": \"https://api-eu.vonage.com/meetings/rooms?page_size=50&start_id=2235997\"\n" +
						"      }\n" +
						"   }\n" +
						"}";

		HttpClient httpClient = stubHttpClient(200, firstJson, secondJson);
		wrapper.setHttpClient(httpClient);
		List<MeetingRoom> rooms = call.get();
		assertEquals(10, rooms.size());
		assertEquals(RANDOM_ID, rooms.get(0).getThemeId());
		assertEquals(ROOM_ID, rooms.get(8).getId());
	}

	void assert401ResponseException(Executable invocation) throws Exception {
		int statusCode = 401;
		MeetingsResponseException expectedResponse = MeetingsResponseException.fromJson(
				"{\n" +
				"   \"title\": \"Missing Auth\",\n" +
				"   \"detail\": \"Auth header is required\"\n" +
				"}"
		);

		expectedResponse.setStatusCode(statusCode);
		String expectedJson = expectedResponse.toJson();
		stubResponse(statusCode, expectedJson);
		String failPrefix = "Expected "+expectedResponse.getClass().getSimpleName()+", but got ";

		try {
			invocation.execute();
			fail(failPrefix + "nothing.");
		}
		catch (MeetingsResponseException ex) {
			assertEquals(expectedResponse, ex);
			assertEquals(expectedJson, ex.toJson());
			assertEquals("Missing Auth", ex.getTitle());
			assertEquals("Auth header is required", ex.getDetail());
			assertNull(ex.getType());
			assertNull(ex.getInstance());
			assertNull(ex.getName());
			assertNotNull(ex.getMessage());
		}
		catch (Throwable ex) {
			fail(failPrefix + ex);
		}
	}

	void assert400ResponseException(Executable invocation) throws Exception {
		int statusCode = 400;
		MeetingsResponseException expectedResponse = MeetingsResponseException.fromJson(
				"{\n" +
				"   \"message\": \"Explanation about why validation failed.\",\n" +
				"   \"name\": \"InputValidationError\",\n" +
				"   \"status\": "+statusCode+"\n" +
				"}"
		);

		stubResponse(statusCode, expectedResponse.toJson());
		String failPrefix = "Expected "+expectedResponse.getClass().getSimpleName()+", but got ";

		try {
			invocation.execute();
			fail(failPrefix + "nothing.");
		}
		catch (MeetingsResponseException ex) {
			assertEquals(statusCode, ex.getStatusCode());
			assertEquals(statusCode, expectedResponse.getStatusCode());
			assertEquals("InputValidationError", ex.getName());
			assertEquals("Explanation about why validation failed.", ex.getMessage());
			assertEquals(expectedResponse.getName(), ex.getName());
			assertEquals(expectedResponse.getMessage(), ex.getMessage());
			assertNull(ex.getType());
			assertNull(ex.getInstance());
			assertNull(expectedResponse.getTitle());
			assertEquals("Test reason", ex.getTitle());
			assertNull(ex.getDetail());
		}
		catch (Throwable ex) {
			fail(failPrefix + ex);
		}
	}

	@Test
	public void testListRooms() throws Exception {
		assertEqualsAvailableRooms(stubResponseAndGet(200, LIST_ROOMS_RESPONSE, client::listRooms));
		assert401ResponseException(client::listRooms);
		testPaginatedMeetingRoomsResponse(client::listRooms);
	}

	@Test
	public void testGetRoom() throws Exception {
		stubResponseAndAssertEqualsSampleRoom(() -> client.getRoom(ROOM_ID));

		stubResponseAndAssertThrows(200, SAMPLE_ROOM_RESPONSE,
			() -> client.getRoom(null), NullPointerException.class
		);
		assert401ResponseException(() -> client.getRoom(ROOM_ID));
	}

	@Test
	public void testUpdateRoom() throws Exception {
		UpdateRoomRequest request = UpdateRoomRequest.builder().build();
		stubResponseAndAssertEqualsSampleRoom(() -> client.updateRoom(ROOM_ID, request));

		stubResponseAndAssertThrows(200, SAMPLE_ROOM_RESPONSE,
			() -> client.updateRoom(ROOM_ID, null), NullPointerException.class
		);
		stubResponseAndAssertThrows(200, SAMPLE_ROOM_RESPONSE,
			() -> client.updateRoom(null, request), NullPointerException.class
		);
		assert401ResponseException(() -> client.updateRoom(ROOM_ID, request));
	}

	@Test
	public void testCreateRoom() throws Exception {
		MeetingRoom request = MeetingRoom.builder("Sample mr").build();
		stubResponseAndAssertEqualsSampleRoom(() -> client.createRoom(request));

		stubResponseAndAssertThrows(201, SAMPLE_ROOM_RESPONSE,
			() -> client.createRoom(null), NullPointerException.class
		);
		assert400ResponseException(() -> client.createRoom(request));
	}

	@Test
	public void testSearchRoomsByTheme() throws Exception {
		assertEqualsAvailableRooms(stubResponseAndGet(200, LIST_ROOMS_RESPONSE,
				() -> client.searchRoomsByTheme(RANDOM_ID))
		);
		stubResponseAndAssertThrows(200, LIST_ROOMS_RESPONSE,
			() -> client.searchRoomsByTheme(null), NullPointerException.class
		);
		assert401ResponseException(() -> client.searchRoomsByTheme(RANDOM_ID));
		testPaginatedMeetingRoomsResponse(() -> client.searchRoomsByTheme(RANDOM_ID));
	}

	@Test
	public void testListThemes() throws Exception {
		String responseJson = "["+SAMPLE_THEME_RESPONSE+",{\"theme_id\":\""+RANDOM_ID+"\"}]";
		List<Theme> parsed = stubResponseAndGet(200, responseJson, client::listThemes);
		assertEquals(2, parsed.size());
		assertEqualsSampleTheme(parsed.get(0));
		assertEquals(RANDOM_ID, parsed.get(1).getThemeId());
		assert401ResponseException(client::listThemes);
	}

	@Test
	public void testGetTheme() throws Exception {
		stubResponseAndAssertEqualsSampleTheme(() -> client.getTheme(RANDOM_ID));

		stubResponseAndAssertThrows(200, SAMPLE_THEME_RESPONSE,
			() -> client.getTheme(null), NullPointerException.class
		);
		assert401ResponseException(() -> client.getTheme(RANDOM_ID));
	}

	@Test
	public void testCreateTheme() throws Exception {
		Theme request = Theme.builder().brandText("My Company").mainColor("#fff000").build();
		stubResponseAndAssertEqualsSampleTheme(() -> client.createTheme(request));

		stubResponseAndAssertThrows(200, SAMPLE_THEME_RESPONSE,
			() -> client.createTheme(null), NullPointerException.class
		);
		assert400ResponseException(() -> client.createTheme(request));
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
		assert400ResponseException(() -> client.updateTheme(RANDOM_ID, request));
	}

	@Test
	public void testDeleteTheme() throws Exception {
		stubResponseAndRun(204, () -> client.deleteTheme(RANDOM_ID, false));

		stubResponseAndAssertThrows(204,
			() -> client.deleteTheme(null, true), NullPointerException.class
		);
		assert401ResponseException(() -> client.deleteTheme(RANDOM_ID, true));
	}

	@Test
	public void testListRecordings() throws Exception {
		String sessionId = "session_id123";
		String responseJson = "{\"_embedded\":{\"recordings\":[" + SAMPLE_RECORDING_RESPONSE + ",{}]}}";
		stubResponse(200, responseJson);
		List<Recording> recordings = client.listRecordings(sessionId);
		assertEquals(2, recordings.size());
		assertEqualsSampleRecording(recordings.get(0));
		assertNotNull(recordings.get(1));

		responseJson = "{\"_embedded\":{}}";
		stubResponse(200, responseJson);
		recordings = client.listRecordings(sessionId);
		assertNotNull(recordings);
		assertEquals(0, recordings.size());

		responseJson = "{\"_embedded\":{\"recordings\":[]}}";
		stubResponse(200, responseJson);
		recordings = client.listRecordings(sessionId);
		assertNotNull(recordings);
		assertEquals(0, recordings.size());

		stubResponseAndAssertThrows(200,
			() -> client.listRecordings(null), IllegalArgumentException.class
		);
		assert401ResponseException(() -> client.listRecordings(sessionId));
	}

	@Test
	public void testGetRecording() throws Exception {
		stubResponseAndAssertEqualsSampleRecording(() -> client.getRecording(RANDOM_ID));

		stubResponseAndAssertThrows(200,
			() -> client.getRecording(null), NullPointerException.class
		);
		assert401ResponseException(() -> client.getRecording(RANDOM_ID));
	}

	@Test
	public void testDeleteRecording() throws Exception {
		stubResponseAndRun(204, () -> client.deleteRecording(RANDOM_ID));

		stubResponseAndAssertThrows(204,
			() -> client.deleteRecording(null), NullPointerException.class
		);
		assert401ResponseException(() -> client.deleteRecording(RANDOM_ID));
	}

	@Test
	public void testListDialNumbers() throws Exception {
		String responseJson = "[\n" +
				"{\"number\":\"17329672755\",\"display_name\":\"United States\",\"locale\":\"en_US\"},\n" +
				"{\"number\":\"48123964788\",\"display_name\":\"Poland\",\"locale\":\"pl_PL\"},\n" +
				"{\"number\":\"827047844377\",\"display_name\":\"South Korea\",\"locale\":\"ko_KR\"}\n" +
			"]";
		List<DialInNumber> parsed = stubResponseAndGet(200, responseJson, client::listDialNumbers);
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
		assert401ResponseException(client::listDialNumbers);
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
		assert400ResponseException(() -> client.updateApplication(request));
	}

	@Test
	public void testFinalizeLogos() throws Exception {
		final String logoKey = "greyscale-logo-key0";
		stubResponseAndRun(200,() -> client.finalizeLogos(RANDOM_ID, Arrays.asList("key1", "l-k-2", "k3")));
		stubResponseAndRun(200, () -> client.finalizeLogos(RANDOM_ID, Collections.singletonList("a")));

		stubResponseAndAssertThrows(200,
			() -> client.finalizeLogos(null, Arrays.asList(logoKey)), NullPointerException.class
		);
		stubResponseAndAssertThrows(200,
			() -> client.finalizeLogos(RANDOM_ID, null), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200,
			() -> client.finalizeLogos(RANDOM_ID, Collections.emptyList()), IllegalArgumentException.class
		);

		MeetingsResponseException expectedResponse = MeetingsResponseException.fromJson(
				"{\n" +
				"   \"status\": 400,\n" +
				"   \"name\": \"BadRequestError\",\n" +
				"   \"message\": \"could not finalize logos\",\n" +
				"   \"errors\": [\n" +
				"      {\n" +
				"         \"logoKey\": \""+logoKey+"\",\n" +
				"         \"code\": \"invalid_logo_properties\",\n" +
				"         \"invalidProperty\": \"exceeds_size\"\n" +
				"      }\n" +
				"   ]\n" +
				"}"
		);

		wrapper.setHttpClient(stubHttpClient(400, expectedResponse.toJson()));

		try {
			client.finalizeLogos(RANDOM_ID, Collections.singletonList(logoKey));
			fail("Expected "+expectedResponse.getClass().getSimpleName());
		}
		catch (MeetingsResponseException ex) {
			assertEquals(400, ex.getStatusCode());
			assertEquals(ex.getStatusCode(), expectedResponse.getStatusCode());
			assertEquals("BadRequestError", ex.getName());
			assertEquals("could not finalize logos", ex.getMessage());
			assertEquals(expectedResponse.getName(), ex.getName());
			assertEquals(expectedResponse.getMessage(), ex.getMessage());
			assertNull(ex.getType());
			assertNull(ex.getInstance());
			assertNull(expectedResponse.getTitle());
			assertEquals("Test reason", ex.getTitle());
			assertNull(ex.getDetail());

			List<?> errors = ex.getErrors();
			assertNotNull(errors);
			assertEquals(1, errors.size());
			@SuppressWarnings("unchecked")
			Map<String, String> firstError = (Map<String, String>) errors.get(0);
			assertNotNull(firstError);
			assertEquals(3, firstError.size());
			assertEquals(logoKey, firstError.get("logoKey"));
			assertEquals("invalid_logo_properties", firstError.get("code"));
			assertEquals("exceeds_size", firstError.get("invalidProperty"));
			assertEquals(expectedResponse.getErrors(), errors);
		}
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
		assert401ResponseException(client::listLogoUploadUrls);
	}

	@Test
	public void testGetUploadDetailsForLogoType() throws Exception {
		String whiteKey = "blanc", colourKey = "colour", faviconKey = "favourite";
		String responseJson = "[\n" +
				"{\"fields\":{\"key\": \""+whiteKey+"\",\"logoType\": \"white\"}},\n" +
				"{\"fields\":{\"key\": \""+colourKey+"\",\"logoType\": \"colored\"}},\n" +
				"{\"fields\":{\"key\": \""+faviconKey+"\",\"logoType\": \"favicon\"}}]";

		stubResponse(200, responseJson);
		LogoUploadsUrlResponse colour = client.getUploadDetailsForLogoType(LogoType.COLORED);
		assertEquals(LogoType.COLORED, colour.getFields().getLogoType());
		assertEquals(colourKey, colour.getFields().getKey());

		stubResponse(200, responseJson);
		LogoUploadsUrlResponse favicon = client.getUploadDetailsForLogoType(LogoType.FAVICON);
		assertEquals(LogoType.FAVICON, favicon.getFields().getLogoType());
		assertEquals(faviconKey, favicon.getFields().getKey());

		stubResponse(200, responseJson);
		LogoUploadsUrlResponse white = client.getUploadDetailsForLogoType(LogoType.WHITE);
		assertEquals(LogoType.WHITE, white.getFields().getLogoType());
		assertEquals(whiteKey, white.getFields().getKey());
	}

	@Test
	public void testUploadLogo() throws Exception {
		LogoUploadsUrlResponse details = new ObjectMapper().readerFor(LogoUploadsUrlResponse.class).readValue((
				"{\"url\":\"https://s3.amazonaws.com/roomservice-whitelabel-logos-prod\",\"fields\":{" +
				"\"Content-Type\":\"image/png\",\"key\":\"auto-expiring-temp/logos/colored/REDACTED\"," +
				"\"logoType\":\"colored\",\"bucket\":\"roomservice-whitelabel-logos-prod\"," +
				"\"X-Amz-Algorithm\":\"AWS4-HMAC-SHA256\",\"X-Amz-Credential\":" +
				"\"REDACTED/20230130/us-east-1/s3/aws4_request\",\"X-Amz-Date\":\"20230130T113037Z\"," +
				"\"X-Amz-Security-Token\":\"REDACTED\",\"Policy\":\"REDACTED\",\"X-Amz-Signature\":\"REDACTED\"}}"
		));
		Path image = Paths.get("/path/to/logo.png");

		client.httpClient = stubHttpClient(204);
		client.uploadLogo(image, details);

		client.httpClient = stubHttpClient(400);
		assertThrows(MeetingsResponseException.class, () -> client.uploadLogo(image, details));

		client.httpClient = stubHttpClient(200);
		when(client.httpClient.execute(any())).thenThrow(IOException.class);
		assertThrows(VonageUnexpectedException.class, () -> client.uploadLogo(image, details));
	}

	@Test
	public void testUpdateThemeLogo() throws Exception {
		String responseJson = "[{\"url\":\"" +
				"https://s3.amazonaws.com/roomservice-whitelabel-logos-prod\",\"fields\":{" +
				"\"Content-Type\":\"image/png\",\"key\":\"auto-expiring-temp/logos/favicon/REDACTED\"," +
				"\"logoType\":\"favicon\",\"bucket\":\"roomservice-whitelabel-logos-prod\"," +
				"\"X-Amz-Algorithm\":\"AWS4-HMAC-SHA256\",\"X-Amz-Credential\":" +
				"\"REDACTED/20230130/us-east-1/s3/aws4_request\",\"X-Amz-Date\":\"20230130T113037Z\"," +
				"\"X-Amz-Security-Token\":\"REDACTED\",\"Policy\":\"REDACTED\",\"X-Amz-Signature\":\"REDACTED\"}}]";

		Path image = Paths.get("/path/to/logo.png");
		stubResponse(200, responseJson);
		client.httpClient = stubHttpClient(204);
		client.updateThemeLogo(RANDOM_ID, LogoType.FAVICON, image);
	}

	// ENDPOINT TESTS

	@Test
	public void testCreateRoomEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<MeetingRoom, MeetingRoom>() {

			@Override
			protected RestEndpoint<MeetingRoom, MeetingRoom> endpoint() {
				return client.createRoom;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(MeetingRoom request) {
				return "/v1/meetings/rooms";
			}

			@Override
			protected MeetingRoom sampleRequest() {
				return MeetingRoom.builder("Srs bzns meeting")
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
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"expires_at\":\"+1000000000-12-31T23:59:59.999Z\"," +
						"\"theme_id\":\"ef2b46f3-8ebb-437e-a671-272e4990fbc8\"," +
						"\"display_name\":\"Srs bzns meeting\",\"metadata\":\"code=1123\"," +
						"\"is_available\":false,\"expire_after_use\":true,\"type\":\"long_term\"," +
						"\"join_approval_level\":\"explicit_approval\",\"recording_options\":" +
						"{\"auto_record\":true,\"record_only_owner\":false}," +
						"\"initial_join_options\":{\"microphone_state\":\"off\"}," +
						"\"ui_settings\":{\"language\":\"IT\"},\"callback_urls\":" +
						"{\"rooms_callback_url\":\"example.com/ro\"," +
						"\"sessions_callback_url\":\"example.com/se\"," +
						"\"recordings_callback_url\":\"example.com/re\"}," +
						"\"available_features\":{\"is_recording_available\":true," +
						"\"is_chat_available\":false,\"is_whiteboard_available\":false," +
						"\"is_locale_switcher_available\":false}}";
			}
		}
		.runTests();
	}

	@Test
	public void testCreateThemeEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<Theme, Theme>() {

			@Override
			protected RestEndpoint<Theme, Theme> endpoint() {
				return client.createTheme;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(Theme request) {
				return "/v1/meetings/themes";
			}

			@Override
			protected Theme sampleRequest() {
				return Theme.builder()
						.themeName("Test theme").shortCompanyUrl("https://developer.vonage.com")
						.mainColor("#AB34C1").brandText("Vonage").build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"theme_name\":\"Test theme\"," +
						"\"main_color\":\"#AB34C1\"," +
						"\"short_company_url\":\"https://developer.vonage.com\"," +
						"\"brand_text\":\"Vonage\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testDeleteRecordingEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<UUID, Void>() {

			@Override
			protected RestEndpoint<UUID, Void> endpoint() {
				return client.deleteRecording;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.DELETE;
			}

			@Override
			protected String expectedEndpointUri(UUID request) {
				return "/v1/meetings/recordings/" + request;
			}

			@Override
			protected UUID sampleRequest() {
				return RANDOM_ID;
			}
		}
		.runTests();
	}

	@Test
	public void testDeleteThemeEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<DeleteThemeRequest, Void>() {

			@Override
			protected RestEndpoint<DeleteThemeRequest, Void> endpoint() {
				return client.deleteTheme;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.DELETE;
			}

			@Override
			protected String expectedEndpointUri(DeleteThemeRequest request) {
				return "/v1/meetings/themes/" + request.themeId;
			}

			@Override
			protected DeleteThemeRequest sampleRequest() {
				return new DeleteThemeRequest(RANDOM_ID, false);
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				return Collections.singletonMap("force", String.valueOf(sampleRequest().force));
			}
		}
		.runTests();
	}

	@Test
	public void testFinalizeLogosEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<FinalizeLogosRequest, Void>() {

			@Override
			protected RestEndpoint<FinalizeLogosRequest, Void> endpoint() {
				return client.finalizeLogos;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PUT;
			}

			@Override
			protected String expectedEndpointUri(FinalizeLogosRequest request) {
				return "/v1/meetings/themes/" + request.themeId + "/finalizeLogos";
			}

			@Override
			protected FinalizeLogosRequest sampleRequest() {
				return new FinalizeLogosRequest(RANDOM_ID, Arrays.asList("col", "fff"));
			}
		}
		.runTests();
	}

	@Test
	public void testGetLogoUploadUrlsEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<Void, GetLogoUploadUrlsResponse>() {

			@Override
			protected RestEndpoint<Void, GetLogoUploadUrlsResponse> endpoint() {
				return client.getLogoUploadUrls;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(Void request) {
				return "/v1/meetings/themes/logos-upload-urls";
			}

			@Override
			protected Void sampleRequest() {
				return null;
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testUnsuccessfulResponse();
				testEmptyEdgeCases();
				testParseMalformedResponse();
				testInvalidContentType();
				testInvalidLogoType();
			}

			private void testUnsuccessfulResponse() throws Exception {
				stubResponse(400, "{}");
				assertThrows(MeetingsResponseException.class, this::executeEndpoint);
			}

			private void testEmptyEdgeCases() throws Exception {
				stubResponse(200, "[{\"nonsense\":true}]");
				List<LogoUploadsUrlResponse> parsed = executeEndpoint();
				assertNotNull(parsed);
				assertEquals(1, parsed.size());
				assertNotNull(parsed.get(0));

				stubResponse(200, "[]");
				parsed = endpoint().execute(sampleRequest());
				assertNotNull(parsed);
				assertEquals(0, parsed.size());
			}

			private void testParseMalformedResponse() throws Exception {
				stubResponse(200, "{malformed]");
				assertThrows(VonageResponseParseException.class, this::executeEndpoint);
			}

			private void testInvalidContentType() throws Exception {
				stubResponse(200, "[{\"fields\":{\"Content-Type\":\"not-a-mime\"}}]");
				assertThrows(VonageResponseParseException.class, this::executeEndpoint);
			}

			private void testInvalidLogoType() throws Exception {
				stubResponse(200, "[{\"fields\":{\"logoType\":\"smoll\"}}]");
				List<LogoUploadsUrlResponse> parsed = executeEndpoint();
				assertNotNull(parsed);
				assertEquals(1, parsed.size());
				assertNull(parsed.get(0).getFields().getLogoType());
			}
		}
		.runTests();
	}

	@Test
	public void testGetRecordingEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<UUID, Recording>() {

			@Override
			protected RestEndpoint<UUID, Recording> endpoint() {
				return client.getRecording;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(UUID request) {
				return "/v1/meetings/recordings/" + request;
			}

			@Override
			protected UUID sampleRequest() {
				return RANDOM_ID;
			}
		}
		.runTests();
	}

	@Test
	public void testGetRoomEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<UUID, MeetingRoom>() {

			@Override
			protected RestEndpoint<UUID, MeetingRoom> endpoint() {
				return client.getRoom;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(UUID request) {
				return "/v1/meetings/rooms/" + request;
			}

			@Override
			protected UUID sampleRequest() {
				return ROOM_ID;
			}
		}
		.runTests();
	}

	@Test
	public void testGetThemeEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<UUID, Theme>() {

			@Override
			protected RestEndpoint<UUID, Theme> endpoint() {
				return client.getTheme;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(UUID request) {
				return "/v1/meetings/themes/" + request;
			}

			@Override
			protected UUID sampleRequest() {
				return RANDOM_ID;
			}
		}
		.runTests();
	}

	@Test
	public void testListDialNumbersEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<Void, ListDialNumbersResponse>() {

			@Override
			protected RestEndpoint<Void, ListDialNumbersResponse> endpoint() {
				return client.listDialNumbers;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(Void request) {
				return "/v1/meetings/dial-in-numbers";
			}

			@Override
			protected Void sampleRequest() {
				return null;
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testParseResponse();
			}

			private void testParseResponse() throws Exception {
				String expectedPayload = "[\n" +
						"{},   {\n" +
						"      \"number\": \"17323338801\",\n" +
						"      \"locale\": \"en-US\",\n" +
						"      \"display_name\": \"United States\"\n" +
						"   }," +
						"  {\"locale\": \"de-DE\",\"unknown property\":0}\n" +
						"]";
				stubResponse(200, expectedPayload);
				List<DialInNumber> parsed = executeEndpoint();
				assertEquals(3, parsed.size());

				DialInNumber empty = parsed.get(0);
				assertNotNull(empty);
				assertNull(empty.getNumber());
				assertNull(empty.getDisplayName());
				assertNull(empty.getLocale());

				DialInNumber usa = parsed.get(1);
				assertEquals("17323338801", usa.getNumber());
				assertEquals("United States", usa.getDisplayName());
				assertEquals(Locale.US, usa.getLocale());

				DialInNumber germany = parsed.get(2);
				assertEquals(Locale.GERMANY, germany.getLocale());
				assertNull(germany.getDisplayName());
				assertNull(germany.getNumber());
			}
		}
		.runTests();
	}

	@Test
	public void testListRecordingsEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<String, ListRecordingsResponse>() {

			@Override
			protected RestEndpoint<String, ListRecordingsResponse> endpoint() {
				return client.listRecordings;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/v1/meetings/sessions/" + request + "/recordings";
			}

			@Override
			protected String sampleRequest() {
				return "2_MX40NjMwODczMn5-MTU3NTgyODEwNzQ2Jk..";
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testBadResponse();
				testEmptyEdgeCases();
				testInvalidRecordingStatus();
			}

			private void testBadResponse() throws Exception {
				stubResponse(400, "{}");
				assertThrows(MeetingsResponseException.class, this::executeEndpoint);
				stubResponse(200, "{malformed]");
				assertThrows(VonageResponseParseException.class, this::executeEndpoint);
			}

			private void testEmptyEdgeCases() throws Exception {
				stubResponse(200, "");
				ListRecordingsResponse parsed = executeEndpoint();
				assertNotNull(parsed);
				assertNull(parsed.getRecordings());

				stubResponse(200, "{}");
				parsed = executeEndpoint();
				assertNotNull(parsed);
				assertNull(parsed.getRecordings());

				stubResponse(200, "{\"_embedded\":{}}");
				parsed = executeEndpoint();
				assertNotNull(parsed);
				assertNull(parsed.getRecordings());

				stubResponse(200, "{\"_embedded\":{\"recordings\":[]}}");
				parsed = executeEndpoint();
				assertNotNull(parsed);
				assertNotNull(parsed.getRecordings());
				assertEquals(0, parsed.getRecordings().size());

				stubResponse(200, "{\"_embedded\":{\"recordings\":[{}]}}");
				parsed = executeEndpoint();
				assertNotNull(parsed);
				assertNotNull(parsed.getRecordings());
				assertEquals(1, parsed.getRecordings().size());
				assertNotNull(parsed.getRecordings().get(0));

				String expectedPayload = "{\"_embedded\":{\"recordings\":[{},{\"_links\":{},\"status\":\"uploaded\"}]}}";
				stubResponse(200, expectedPayload);
				parsed = executeEndpoint();
				assertEquals(2, parsed.getRecordings().size());
				assertNull(parsed.getRecordings().get(1).getUrl());
				assertEquals(RecordingStatus.UPLOADED, parsed.getRecordings().get(1).getStatus());
				assertEquals("uploaded", RecordingStatus.UPLOADED.toString());
				Recording empty = parsed.getRecordings().get(0);
				assertNotNull(empty);
				assertNull(empty.getId());
				assertNull(empty.getUrl());
				assertNull(empty.getEndedAt());
				assertNull(empty.getStartedAt());
				assertNull(empty.getStatus());
				assertNull(empty.getSessionId());
			}

			private void testInvalidRecordingStatus() throws Exception {
				String expectedPayload = "{\"_embedded\":{\"recordings\":[{\"status\":\"You're on camera!\"}]}}";
				stubResponse(200, expectedPayload);
				ListRecordingsResponse parsed = executeEndpoint();;
				assertNotNull(parsed);
				assertEquals(1, parsed.getRecordings().size());
				assertNull(parsed.getRecordings().get(0).getStatus());
			}
		}
		.runTests();
	}

	@Test
	public void testListRoomsEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<ListRoomsRequest, ListRoomsResponse>() {

			@Override
			protected RestEndpoint<ListRoomsRequest, ListRoomsResponse> endpoint() {
				return client.listRooms;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListRoomsRequest request) {
				return "/v1/meetings/rooms";
			}

			@Override
			protected ListRoomsRequest sampleRequest() {
				return new ListRoomsRequest(null, null, null, null);
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				return Collections.emptyMap();
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testAllQueryParams();
				testParseResponse();
			}

			private void testAllQueryParams() throws Exception {
				ListRoomsRequest request = new ListRoomsRequest(51, 150, 25, null);
				Map<String, String> params = new LinkedHashMap<>();
				params.put("start_id", "51");
				params.put("end_id", "150");
				params.put("page_size", "25");
				assertRequestUriAndBody(request, params);
			}

			private void testParseResponse() throws Exception {
				String expectedResponse = "{\"page_size\":null,\"_embedded\":[{}],\"total_items\":3}";
				stubResponse(200, expectedResponse);
				ListRoomsResponse parsed = executeEndpoint();
				assertNull(parsed.getLinks());
				assertEquals(1, parsed.getMeetingRooms().size());
				assertNotNull(parsed.getMeetingRooms().get(0));
				assertEquals(3, parsed.getTotalItems().intValue());

				stubResponse(200, "{malformed]");
				assertThrows(VonageResponseParseException.class, this::executeEndpoint);

				stubResponse(400, "{}");
				assertThrows(MeetingsResponseException.class, this::executeEndpoint);
			}
		}
		.runTests();
	}

	@Test
	public void testListThemesEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<Void, ListThemesResponse>() {

			@Override
			protected RestEndpoint<Void, ListThemesResponse> endpoint() {
				return client.listThemes;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(Void request) {
				return "/v1/meetings/themes";
			}

			@Override
			protected Void sampleRequest() {
				return null;
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testEmptyEdgeCases();
				stubResponse(400, "{}");
				assertThrows(MeetingsResponseException.class, this::executeEndpoint);
			}

			private void testEmptyEdgeCases() throws Exception {
				stubResponse(200, "[{}]");
				List<Theme> parsed = executeEndpoint();
				assertEquals(1, parsed.size());
				assertNotNull(parsed.get(0));

				stubResponse(200, "[]");
				parsed = executeEndpoint();
				assertNotNull(parsed);
				assertEquals(0, parsed.size());
			}
		}
		.runTests();
	}

	@Test
	public void testSearchThemeRoomsEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<ListRoomsRequest, ListRoomsResponse>() {

			@Override
			protected RestEndpoint<ListRoomsRequest, ListRoomsResponse> endpoint() {
				return client.searchThemeRooms;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListRoomsRequest request) {
				return "/v1/meetings/themes/" + request.themeId + "/rooms";
			}

			@Override
			protected ListRoomsRequest sampleRequest() {
				return new ListRoomsRequest(21, 33, 50, RANDOM_ID);
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				ListRoomsRequest request = sampleRequest();
				Map<String, String> params = new LinkedHashMap<>();
				params.put("start_id", String.valueOf(request.startId));
				params.put("end_id", String.valueOf(request.endId));
				params.put("page_size", String.valueOf(request.pageSize));
				return params;
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testStartIdOnly();
				testParseResponse();
			}

			private void testStartIdOnly() throws Exception {
				ListRoomsRequest request = new ListRoomsRequest(1, null, null, RANDOM_ID);
				Map<String, String> params = Collections.singletonMap("start_id", "1");
				assertRequestUriAndBody(request, params);
			}

			private void testParseResponse() throws Exception {
				ListRoomsRequest request = sampleRequest();
				String expectedResponse = "{\n" +
						"\"page_size\":"+request.pageSize+",\"_embedded\":[{},{},{}," +
						"{\"type\":\"instant\"},{\"nonsense\":0}],\"_links\":{\n" +
						"\"self\":{\"href\":\"https://vonage.com\"},\"prev\":{},\"next\":{\"href\":null,\"no\":1}}}";
				stubResponse(200, expectedResponse);
				ListRoomsResponse parsed = endpoint().execute(request);
				assertEquals(request.pageSize, parsed.getPageSize());
				assertEquals(5, parsed.getMeetingRooms().size());
				assertEquals(RoomType.INSTANT, parsed.getMeetingRooms().get(3).getType());
				assertNull(parsed.getLinks().getNextUrl());
				assertNull(parsed.getLinks().getPrevUrl());
				assertNull(parsed.getLinks().getFirstUrl());
				assertEquals("https://vonage.com", parsed.getLinks().getSelfUrl().toString());
			}
		}
		.runTests();
	}

	@Test
	public void testUpdateApplicationEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<UpdateApplicationRequest, Application>() {

			@Override
			protected RestEndpoint<UpdateApplicationRequest, Application> endpoint() {
				return client.updateApplication;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PATCH;
			}

			@Override
			protected String expectedEndpointUri(UpdateApplicationRequest request) {
				return "/v1/meetings/applications";
			}

			@Override
			protected UpdateApplicationRequest sampleRequest() {
				return UpdateApplicationRequest.builder().defaultThemeId(RANDOM_ID).build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"update_details\":{\"default_theme_id\":\""+sampleRequest().getDefaultThemeId()+"\"}}";
			}
		}
		.runTests();
	}

	@Test
	public void testUpdateRoomEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<UpdateRoomRequest, MeetingRoom>() {

			@Override
			protected RestEndpoint<UpdateRoomRequest, MeetingRoom> endpoint() {
				return client.updateRoom;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PATCH;
			}

			@Override
			protected String expectedEndpointUri(UpdateRoomRequest request) {
				return "/v1/meetings/rooms/" + request.roomId;
			}

			@Override
			protected UpdateRoomRequest sampleRequest() {
				UpdateRoomRequest request = UpdateRoomRequest.builder()
						.initialJoinOptions(InitialJoinOptions.builder().microphoneState(MicrophoneState.ON).build())
						.expireAfterUse(false).callbackUrls(CallbackUrls.builder().build())
						.availableFeatures(AvailableFeatures.builder().build())
						.themeId(RANDOM_ID).joinApprovalLevel(JoinApprovalLevel.AFTER_OWNER_ONLY)
						.expiresAt(Instant.MAX.minusSeconds(86400)).build();
				request.roomId = ROOM_ID;
				return request;
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"update_details\":{\"expire_after_use\":false,\"initial_join_options\":" +
						"{\"microphone_state\":\"on\"},\"callback_urls\":{},\"available_features\":{}," +
						"\"join_approval_level\":\"after_owner_only\",\"theme_id\":\""+RANDOM_ID+"\"," +
						"\"expires_at\":\"+1000000000-12-30T23:59:59.999Z\"}}";
			}
		}
		.runTests();
	}

	@Test
	public void testUpdateThemeEndpoint() throws Exception {
		new MeetingsEndpointTestSpec<Theme, Theme>() {

			@Override
			protected RestEndpoint<Theme, Theme> endpoint() {
				return client.updateTheme;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PATCH;
			}

			@Override
			protected String expectedEndpointUri(Theme request) {
				return "/v1/meetings/themes/" + request.getThemeId();
			}

			@Override
			protected Theme sampleRequest() {
				Theme request = Theme.builder()
						.shortCompanyUrl("developer.vonage.com").brandText("Vonage (purple)")
						.themeName("Vonage theme").mainColor("#8a1278").build();
				request.setThemeIdAndFlagUpdate(RANDOM_ID);
				return request;
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"update_details\":{\"theme_name\":\"Vonage theme\",\"main_color\":\"#8a1278\"," +
						"\"short_company_url\":\"developer.vonage.com\",\"brand_text\":\"Vonage (purple)\"}}";
			}
		}
		.runTests();
	}
}
