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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class RcsTextRequestTest {
	final String from = "447900000001", to = "317900000002",
			message = "Hello, World!";

	@Test
	public void testSerializeAllParameters() {
		int ttl = 600;
		RcsTextRequest rcsText = RcsTextRequest.builder()
				.from(from).to(to).text(message).ttl(ttl).build();

		String json = rcsText.toJson();
		assertTrue(json.contains("\"text\":\""+ message +"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"rcs\""));
		assertTrue(json.contains("\"ttl\":"+ttl));
		assertEquals("RcsTextRequest "+json, rcsText.toString());
	}

	@Test
	public void testRequiredParametersOnly() {
		RcsTextRequest rcsText = RcsTextRequest.builder().from(from).to(to).text(message).build();

		String json = rcsText.toJson();
		assertTrue(json.contains("\"text\":\""+ message +"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"rcs\""));
		assertEquals("RcsTextRequest "+json, rcsText.toString());
		assertNull(rcsText.getTtl());
	}

	@Test
	public void testTtlTooShort() {
		assertThrows(IllegalArgumentException.class, () ->
				RcsTextRequest.builder().from(from).to(to).text(message).ttl(0).build()
		);
	}

	@Test
	public void testNullText() {
		assertThrows(NullPointerException.class, () ->
				RcsTextRequest.builder().from(from).to(to).build()
		);
	}

	@Test
	public void testEmptyText() {
		assertThrows(IllegalArgumentException.class, () ->
				RcsTextRequest.builder().from(from).to(to).text("").build()
		);
	}

	@Test
	public void testLongText() {
		StringBuilder text = new StringBuilder(3073);
        text.append("*".repeat(3071));
		assertEquals(3071, text.length());

		RcsTextRequest rcsText = RcsTextRequest.builder().text(text.toString()).from(from).to(to).build();

		assertEquals(text.toString(), rcsText.getText());
		text.append("xy");
		assertEquals(3073, text.length());

		assertThrows(IllegalArgumentException.class, () -> RcsTextRequest.builder()
				.from(rcsText.getFrom()).text(text.toString()).to(rcsText.getTo()).build()
		);
	}

	@Test
	public void testWithSuggestions() {
		RcsSuggestedReply reply = RcsSuggestedReply.builder()
				.text("Yes")
				.postbackData("yes")
				.build();

		RcsSuggestedActionDial dial = RcsSuggestedActionDial.builder()
				.text("Call Us")
				.postbackData("call")
				.phoneNumber("+14155550123")
				.build();

		RcsTextRequest rcsText = RcsTextRequest.builder()
				.from(from)
				.to(to)
				.text(message)
				.suggestions(reply, dial)
				.build();

		assertEquals(2, rcsText.getSuggestions().size());
		String json = rcsText.toJson();
		assertTrue(json.contains("\"suggestions\":["));
		assertTrue(json.contains("\"type\":\"reply\""));
		assertTrue(json.contains("\"type\":\"dial\""));
		assertTrue(json.contains("\"postback_data\":\"yes\""));
		assertTrue(json.contains("\"phone_number\":\"+14155550123\""));
	}

	@Test
	public void testAddSuggestion() {
		RcsTextRequest rcsText = RcsTextRequest.builder()
				.from(from)
				.to(to)
				.text(message)
				.addSuggestion(RcsSuggestedReply.builder()
						.text("Reply 1")
						.postbackData("r1")
						.build())
				.addSuggestion(RcsSuggestedReply.builder()
						.text("Reply 2")
						.postbackData("r2")
						.build())
				.build();

		assertEquals(2, rcsText.getSuggestions().size());
		String json = rcsText.toJson();
		assertTrue(json.contains("\"postback_data\":\"r1\""));
		assertTrue(json.contains("\"postback_data\":\"r2\""));
	}

	@Test
	public void testMaxSuggestions() {
		java.util.List<RcsSuggestion> suggestions = new java.util.ArrayList<>();
		for (int i = 1; i <= 11; i++) {
			suggestions.add(RcsSuggestedReply.builder()
					.text("Reply " + i)
					.postbackData("r" + i)
					.build());
		}

		RcsTextRequest rcsText = RcsTextRequest.builder()
				.from(from)
				.to(to)
				.text(message)
				.suggestions(suggestions)
				.build();

		assertEquals(11, rcsText.getSuggestions().size());
	}

	@Test
	public void testTooManySuggestions() {
		java.util.List<RcsSuggestion> suggestions = new java.util.ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			suggestions.add(RcsSuggestedReply.builder()
					.text("Reply " + i)
					.postbackData("r" + i)
					.build());
		}

		assertThrows(IllegalArgumentException.class, () ->
				RcsTextRequest.builder()
						.from(from)
						.to(to)
						.text(message)
						.suggestions(suggestions)
						.build()
		);
	}

	@Test
	public void testNoSuggestions() {
		RcsTextRequest rcsText = RcsTextRequest.builder()
				.from(from)
				.to(to)
				.text(message)
				.build();

		assertNull(rcsText.getSuggestions());
		String json = rcsText.toJson();
		assertFalse(json.contains("\"suggestions\""));
	}
}
