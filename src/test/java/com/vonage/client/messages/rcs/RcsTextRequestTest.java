/*
 *   Copyright 2024 Vonage
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
}
