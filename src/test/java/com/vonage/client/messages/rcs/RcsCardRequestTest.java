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

public class RcsCardRequestTest {
	private final String from = "447900000001", to = "317900000002";

	private RcsCard createTestCard(String title) {
		return RcsCard.builder()
				.title(title)
				.text("Test card description")
				.mediaUrl("https://example.com/image.jpg")
				.build();
	}

	@Test
	public void testSerializeAllParameters() {
		int ttl = 600;
		RcsCard card = createTestCard("Test Card");

		RcsCardRequest request = RcsCardRequest.builder()
				.from(from)
				.to(to)
				.card(card)
				.ttl(ttl)
				.build();

		assertEquals(card, request.getCard());
		assertEquals(from, request.getFrom());
		assertEquals(to, request.getTo());
		assertEquals(ttl, request.getTtl());

		String json = request.toJson();
		assertTrue(json.contains("\"from\":\"" + from + "\""));
		assertTrue(json.contains("\"to\":\"" + to + "\""));
		assertTrue(json.contains("\"message_type\":\"card\""));
		assertTrue(json.contains("\"channel\":\"rcs\""));
		assertTrue(json.contains("\"ttl\":" + ttl));
		assertTrue(json.contains("\"card\":{"));
		assertTrue(json.contains("\"title\":\"Test Card\""));
	}

	@Test
	public void testRequiredParametersOnly() {
		RcsCard card = createTestCard("Test Card");

		RcsCardRequest request = RcsCardRequest.builder()
				.from(from)
				.to(to)
				.card(card)
				.build();

		assertNotNull(request.getCard());
		assertEquals(from, request.getFrom());
		assertEquals(to, request.getTo());
		assertNull(request.getTtl());

		String json = request.toJson();
		assertTrue(json.contains("\"message_type\":\"card\""));
		assertFalse(json.contains("\"ttl\""));
	}

	@Test
	public void testMissingCard() {
		assertThrows(NullPointerException.class, () ->
				RcsCardRequest.builder()
						.from(from)
						.to(to)
						.build()
		);
	}

	@Test
	public void testMissingFromTo() {
		RcsCard card = createTestCard("Test Card");

		assertThrows(IllegalArgumentException.class, () ->
				RcsCardRequest.builder()
						.to(to)
						.card(card)
						.build()
		);

		assertThrows(NullPointerException.class, () ->
				RcsCardRequest.builder()
						.from(from)
						.card(card)
						.build()
		);
	}

	@Test
	public void testWithSuggestions() {
		RcsCard card = RcsCard.builder()
				.title("Product Card")
				.text("Check out our amazing product!")
				.mediaUrl("https://example.com/product.jpg")
				.addSuggestion(RcsSuggestedReply.builder()
						.text("Buy Now")
						.postbackData("buy_product_123")
						.build())
				.addSuggestion(RcsSuggestedActionOpenUrl.builder()
						.text("Learn More")
						.postbackData("learn_more")
						.url("https://example.com/product")
						.build())
				.build();

		RcsCardRequest request = RcsCardRequest.builder()
				.from(from)
				.to(to)
				.card(card)
				.build();

		String json = request.toJson();
		assertTrue(json.contains("\"suggestions\":["));
		assertTrue(json.contains("\"type\":\"reply\""));
		assertTrue(json.contains("\"type\":\"open_url\""));
	}
}
