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
import static org.junit.jupiter.api.Assertions.*;

public class RcsCardTest {
	private final String title = "Card Title", text = "This is some text to display on the card.",
			mediaUrl = "https://example.com/image.jpg", mediaDescription = "Image description",
			thumbnailUrl = "https://example.com/thumbnail.jpg";

	private RcsSuggestion createTestSuggestion(String label) {
		return RcsSuggestedReply.builder()
				.text(label)
				.postbackData("test_" + label)
				.build();
	}

	@Test
	public void testSerializeAllParameters() {
		RcsCard card = RcsCard.builder()
				.title(title)
				.text(text)
				.mediaUrl(mediaUrl)
				.mediaDescription(mediaDescription)
				.mediaHeight(RcsCard.MediaHeight.TALL)
				.thumbnailUrl(thumbnailUrl)
				.mediaForceRefresh(true)
				.addSuggestion(createTestSuggestion("Option 1"))
				.addSuggestion(createTestSuggestion("Option 2"))
				.build();

		assertEquals(title, card.getTitle());
		assertEquals(text, card.getText());
		assertEquals(mediaUrl, card.getMediaUrl().toString());
		assertEquals(mediaDescription, card.getMediaDescription());
		assertEquals(RcsCard.MediaHeight.TALL, card.getMediaHeight());
		assertEquals(thumbnailUrl, card.getThumbnailUrl().toString());
		assertTrue(card.getMediaForceRefresh());
		assertEquals(2, card.getSuggestions().size());

		String json = card.toJson();
		assertTrue(json.contains("\"title\":\"" + title + "\""));
		assertTrue(json.contains("\"text\":\"" + text + "\""));
		assertTrue(json.contains("\"media_url\":\"" + mediaUrl + "\""));
		assertTrue(json.contains("\"media_height\":\"TALL\""));
		assertTrue(json.contains("\"media_force_refresh\":true"));
		assertTrue(json.contains("\"suggestions\":["));
	}

	@Test
	public void testRequiredParametersOnly() {
		RcsCard card = RcsCard.builder()
				.title(title)
				.text(text)
				.mediaUrl(mediaUrl)
				.build();

		assertEquals(title, card.getTitle());
		assertEquals(text, card.getText());
		assertEquals(mediaUrl, card.getMediaUrl().toString());
		assertNull(card.getMediaDescription());
		assertNull(card.getMediaHeight());
		assertNull(card.getThumbnailUrl());
		assertNull(card.getMediaForceRefresh());
		assertNull(card.getSuggestions());

		String json = card.toJson();
		assertTrue(json.contains("\"title\":\"" + title + "\""));
		assertFalse(json.contains("\"media_height\""));
		assertFalse(json.contains("\"suggestions\""));
	}

	@Test
	public void testMissingRequiredFields() {
		assertThrows(NullPointerException.class, () ->
				RcsCard.builder().text(text).mediaUrl(mediaUrl).build()
		);
		assertThrows(NullPointerException.class, () ->
				RcsCard.builder().title(title).mediaUrl(mediaUrl).build()
		);
		assertThrows(NullPointerException.class, () ->
				RcsCard.builder().title(title).text(text).build()
		);
	}

	@Test
	public void testTitleTooLong() {
		String longTitle = "a".repeat(201);
		assertThrows(IllegalArgumentException.class, () ->
				RcsCard.builder().title(longTitle).text(text).mediaUrl(mediaUrl).build()
		);
	}

	@Test
	public void testTextTooLong() {
		String longText = "a".repeat(2001);
		assertThrows(IllegalArgumentException.class, () ->
				RcsCard.builder().title(title).text(longText).mediaUrl(mediaUrl).build()
		);
	}

	@Test
	public void testTooManySuggestions() {
		RcsCard.Builder builder = RcsCard.builder()
				.title(title)
				.text(text)
				.mediaUrl(mediaUrl);

		for (int i = 1; i <= 5; i++) {
			builder.addSuggestion(createTestSuggestion("Option " + i));
		}

		assertThrows(IllegalArgumentException.class, builder::build);
	}

	@Test
	public void testMaxSuggestions() {
		RcsCard.Builder builder = RcsCard.builder()
				.title(title)
				.text(text)
				.mediaUrl(mediaUrl);

		for (int i = 1; i <= 4; i++) {
			builder.addSuggestion(createTestSuggestion("Option " + i));
		}

		RcsCard card = builder.build();
		assertEquals(4, card.getSuggestions().size());
	}

	@Test
	public void testSuggestionsVarargs() {
		RcsCard card = RcsCard.builder()
				.title(title)
				.text(text)
				.mediaUrl(mediaUrl)
				.suggestions(
						createTestSuggestion("Option 1"),
						createTestSuggestion("Option 2"),
						createTestSuggestion("Option 3")
				)
				.build();

		assertEquals(3, card.getSuggestions().size());
	}

	@Test
	public void testMediaHeightEnum() {
		assertEquals("SHORT", RcsCard.MediaHeight.SHORT.toString());
		assertEquals("MEDIUM", RcsCard.MediaHeight.MEDIUM.toString());
		assertEquals("TALL", RcsCard.MediaHeight.TALL.toString());

		assertEquals(RcsCard.MediaHeight.SHORT, RcsCard.MediaHeight.fromString("SHORT"));
		assertEquals(RcsCard.MediaHeight.MEDIUM, RcsCard.MediaHeight.fromString("MEDIUM"));
		assertEquals(RcsCard.MediaHeight.TALL, RcsCard.MediaHeight.fromString("TALL"));
	}
}
