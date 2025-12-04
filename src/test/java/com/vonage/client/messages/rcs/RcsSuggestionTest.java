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
package com.vonage.client.messages.rcs;

import org.junit.jupiter.api.*;
import java.net.URI;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

public class RcsSuggestionTest {

	@Test
	public void testSuggestedReply() {
		RcsSuggestedReply reply = RcsSuggestedReply.builder()
				.text("Yes")
				.postbackData("question_1_yes")
				.build();

		assertEquals("reply", reply.getType());
		assertEquals("Yes", reply.getText());
		assertEquals("question_1_yes", reply.getPostbackData());

		String json = reply.toJson();
		assertTrue(json.contains("\"type\":\"reply\""));
		assertTrue(json.contains("\"text\":\"Yes\""));
		assertTrue(json.contains("\"postback_data\":\"question_1_yes\""));
	}

	@Test
	public void testSuggestedReplyTextTooLong() {
		assertThrows(IllegalArgumentException.class, () ->
				RcsSuggestedReply.builder()
						.text("This text is way too long to fit in a suggestion chip")
						.postbackData("test")
						.build()
		);
	}

	@Test
	public void testSuggestedReplyMissingRequired() {
		assertThrows(NullPointerException.class, () ->
				RcsSuggestedReply.builder().text("Yes").build()
		);
		assertThrows(NullPointerException.class, () ->
				RcsSuggestedReply.builder().postbackData("test").build()
		);
	}

	@Test
	public void testSuggestedActionDial() {
		RcsSuggestedActionDial dial = RcsSuggestedActionDial.builder()
				.text("Call Us")
				.postbackData("action_1")
				.phoneNumber("+14155550100")
				.fallbackUrl("https://example.com/contact")
				.build();

		assertEquals("dial", dial.getType());
		assertEquals("Call Us", dial.getText());
		assertEquals("action_1", dial.getPostbackData());
		assertEquals("+14155550100", dial.getPhoneNumber());
		assertEquals(URI.create("https://example.com/contact"), dial.getFallbackUrl());

		String json = dial.toJson();
		assertTrue(json.contains("\"type\":\"dial\""));
		assertTrue(json.contains("\"phone_number\":\"+14155550100\""));
		assertTrue(json.contains("\"fallback_url\":\"https://example.com/contact\""));
	}

	@Test
	public void testSuggestedActionViewLocation() {
		RcsSuggestedActionViewLocation viewLocation = RcsSuggestedActionViewLocation.builder()
				.text("View Location")
				.postbackData("action_2")
				.latitude(37.7749)
				.longitude(-122.4194)
				.pinLabel("Vonage")
				.build();

		assertEquals("view_location", viewLocation.getType());
		assertEquals("37.7749", viewLocation.getLatitude());
		assertEquals("-122.4194", viewLocation.getLongitude());
		assertEquals("Vonage", viewLocation.getPinLabel());

		String json = viewLocation.toJson();
		assertTrue(json.contains("\"type\":\"view_location\""));
		assertTrue(json.contains("\"latitude\":\"37.7749\""));
		assertTrue(json.contains("\"longitude\":\"-122.4194\""));
		assertTrue(json.contains("\"pin_label\":\"Vonage\""));
	}

	@Test
	public void testSuggestedActionShareLocation() {
		RcsSuggestedActionShareLocation shareLocation = RcsSuggestedActionShareLocation.builder()
				.text("Share Location")
				.postbackData("action_3")
				.build();

		assertEquals("share_location", shareLocation.getType());
		assertEquals("Share Location", shareLocation.getText());
		assertEquals("action_3", shareLocation.getPostbackData());

		String json = shareLocation.toJson();
		assertTrue(json.contains("\"type\":\"share_location\""));
	}

	@Test
	public void testSuggestedActionOpenUrl() {
		RcsSuggestedActionOpenUrl openUrl = RcsSuggestedActionOpenUrl.builder()
				.text("Visit Site")
				.postbackData("action_4")
				.url("https://example.com")
				.description("Example website")
				.build();

		assertEquals("open_url", openUrl.getType());
		assertEquals(URI.create("https://example.com"), openUrl.getUrl());
		assertEquals("Example website", openUrl.getDescription());

		String json = openUrl.toJson();
		assertTrue(json.contains("\"type\":\"open_url\""));
		assertTrue(json.contains("\"url\":\"https://example.com\""));
		assertTrue(json.contains("\"description\":\"Example website\""));
	}

	@Test
	public void testSuggestedActionOpenUrlWebview() {
		RcsSuggestedActionOpenUrlWebview openUrlWebview = RcsSuggestedActionOpenUrlWebview.builder()
				.text("Open in App")
				.postbackData("action_5")
				.url("https://example.com")
				.description("Example website")
				.viewMode(RcsSuggestedActionOpenUrlWebview.ViewMode.FULL)
				.build();

		assertEquals("open_url_in_webview", openUrlWebview.getType());
		assertEquals(RcsSuggestedActionOpenUrlWebview.ViewMode.FULL, openUrlWebview.getViewMode());

		String json = openUrlWebview.toJson();
		assertTrue(json.contains("\"type\":\"open_url_in_webview\""));
		assertTrue(json.contains("\"view_mode\":\"FULL\""));
	}

	@Test
	public void testSuggestedActionCreateCalendarEvent() {
		Instant startTime = Instant.parse("2023-01-01T10:00:00Z");
		Instant endTime = Instant.parse("2023-01-01T11:00:00Z");

		RcsSuggestedActionCreateCalendarEvent calendarEvent = RcsSuggestedActionCreateCalendarEvent.builder()
				.text("Add to Calendar")
				.postbackData("action_6")
				.startTime(startTime)
				.endTime(endTime)
				.title("New Year Party")
				.description("Join us to celebrate!")
				.fallbackUrl("https://www.google.com/calendar")
				.build();

		assertEquals("create_calendar_event", calendarEvent.getType());
		assertEquals(startTime, calendarEvent.getStartTime());
		assertEquals(endTime, calendarEvent.getEndTime());
		assertEquals("New Year Party", calendarEvent.getTitle());
		assertEquals("Join us to celebrate!", calendarEvent.getDescription());

		String json = calendarEvent.toJson();
		assertTrue(json.contains("\"type\":\"create_calendar_event\""));
		assertTrue(json.contains("\"title\":\"New Year Party\""));
		assertTrue(json.contains("\"start_time\":\"2023-01-01T10:00:00Z\""));
	}

	@Test
	public void testCalendarEventMissingRequired() {
		Instant startTime = Instant.parse("2023-01-01T10:00:00Z");
		Instant endTime = Instant.parse("2023-01-01T11:00:00Z");

		assertThrows(NullPointerException.class, () ->
				RcsSuggestedActionCreateCalendarEvent.builder()
						.text("Add")
						.postbackData("test")
						.startTime(startTime)
						.title("Event")
						.build()
		);

		assertThrows(NullPointerException.class, () ->
				RcsSuggestedActionCreateCalendarEvent.builder()
						.text("Add")
						.postbackData("test")
						.endTime(endTime)
						.title("Event")
						.build()
		);

		assertThrows(NullPointerException.class, () ->
				RcsSuggestedActionCreateCalendarEvent.builder()
						.text("Add")
						.postbackData("test")
						.startTime(startTime)
						.endTime(endTime)
						.build()
		);
	}
}
