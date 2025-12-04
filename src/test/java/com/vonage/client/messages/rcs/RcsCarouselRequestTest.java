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
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RcsCarouselRequestTest {
	private final String from = "447900000001", to = "317900000002";

	private RcsCard createTestCard(String title) {
		return RcsCard.builder()
				.title(title)
				.text("Description for " + title)
				.mediaUrl("https://example.com/" + title.toLowerCase().replace(" ", "_") + ".jpg")
				.build();
	}

	@Test
	public void testSerializeAllParameters() {
		int ttl = 600;
		List<RcsCard> cards = new ArrayList<>();
		cards.add(createTestCard("Card 1"));
		cards.add(createTestCard("Card 2"));
		cards.add(createTestCard("Card 3"));

		RcsCarouselRequest request = RcsCarouselRequest.builder()
				.from(from)
				.to(to)
				.carousel(cards)
				.ttl(ttl)
				.build();

		assertEquals(3, request.getCarousel().size());
		assertEquals(from, request.getFrom());
		assertEquals(to, request.getTo());
		assertEquals(ttl, request.getTtl());

		String json = request.toJson();
		assertTrue(json.contains("\"from\":\"" + from + "\""));
		assertTrue(json.contains("\"to\":\"" + to + "\""));
		assertTrue(json.contains("\"message_type\":\"carousel\""));
		assertTrue(json.contains("\"channel\":\"rcs\""));
		assertTrue(json.contains("\"ttl\":" + ttl));
		assertTrue(json.contains("\"carousel\":["));
		assertTrue(json.contains("\"title\":\"Card 1\""));
		assertTrue(json.contains("\"title\":\"Card 2\""));
		assertTrue(json.contains("\"title\":\"Card 3\""));
	}

	@Test
	public void testRequiredParametersOnly() {
		RcsCarouselRequest request = RcsCarouselRequest.builder()
				.from(from)
				.to(to)
				.carousel(
						createTestCard("Card 1"),
						createTestCard("Card 2")
				)
				.build();

		assertEquals(2, request.getCarousel().size());
		assertNull(request.getTtl());

		String json = request.toJson();
		assertTrue(json.contains("\"message_type\":\"carousel\""));
		assertFalse(json.contains("\"ttl\""));
	}

	@Test
	public void testMissingCarousel() {
		assertThrows(NullPointerException.class, () ->
				RcsCarouselRequest.builder()
						.from(from)
						.to(to)
						.build()
		);
	}

	@Test
	public void testTooFewCards() {
		assertThrows(IllegalArgumentException.class, () ->
				RcsCarouselRequest.builder()
						.from(from)
						.to(to)
						.carousel(createTestCard("Card 1"))
						.build()
		);
	}

	@Test
	public void testTooManyCards() {
		List<RcsCard> cards = new ArrayList<>();
		for (int i = 1; i <= 11; i++) {
			cards.add(createTestCard("Card " + i));
		}

		assertThrows(IllegalArgumentException.class, () ->
				RcsCarouselRequest.builder()
						.from(from)
						.to(to)
						.carousel(cards)
						.build()
		);
	}

	@Test
	public void testMinimumCards() {
		RcsCarouselRequest request = RcsCarouselRequest.builder()
				.from(from)
				.to(to)
				.carousel(
						createTestCard("Card 1"),
						createTestCard("Card 2")
				)
				.build();

		assertEquals(2, request.getCarousel().size());
	}

	@Test
	public void testMaximumCards() {
		List<RcsCard> cards = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			cards.add(createTestCard("Card " + i));
		}

		RcsCarouselRequest request = RcsCarouselRequest.builder()
				.from(from)
				.to(to)
				.carousel(cards)
				.build();

		assertEquals(10, request.getCarousel().size());
	}

	@Test
	public void testCarouselWithSuggestions() {
		RcsCard card1 = RcsCard.builder()
				.title("Product 1")
				.text("Amazing product 1")
				.mediaUrl("https://example.com/product1.jpg")
				.addSuggestion(RcsSuggestedReply.builder()
						.text("Buy Now")
						.postbackData("buy_1")
						.build())
				.build();

		RcsCard card2 = RcsCard.builder()
				.title("Product 2")
				.text("Amazing product 2")
				.mediaUrl("https://example.com/product2.jpg")
				.addSuggestion(RcsSuggestedReply.builder()
						.text("Buy Now")
						.postbackData("buy_2")
						.build())
				.build();

		RcsCarouselRequest request = RcsCarouselRequest.builder()
				.from(from)
				.to(to)
				.carousel(card1, card2)
				.build();

		String json = request.toJson();
		assertTrue(json.contains("\"suggestions\":["));
		assertTrue(json.contains("\"postback_data\":\"buy_1\""));
		assertTrue(json.contains("\"postback_data\":\"buy_2\""));
	}
}
