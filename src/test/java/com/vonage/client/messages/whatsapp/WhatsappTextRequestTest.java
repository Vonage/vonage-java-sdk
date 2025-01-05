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
package com.vonage.client.messages.whatsapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.UUID;

public class WhatsappTextRequestTest {
	String from = "447900000001", to = "317900000002", txt = "Hello, World!";

	@Test
	public void testSerializeValid() {
		String messageUuid = UUID.randomUUID().toString();
		String json = WhatsappTextRequest.builder().from(from).to(to).text(txt)
				.contextMessageId(messageUuid).build().toJson();

		assertTrue(json.contains("\"text\":\""+txt+"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"context\":{\"message_uuid\":\""+messageUuid+"\"}"));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testConstructEmptySender() {
		assertThrows(IllegalArgumentException.class, () -> WhatsappTextRequest.builder()
				.from("").to(to).text(txt).build()
		);
	}

	@Test
	public void testConstructNoSender() {
		assertThrows(NullPointerException.class, () -> WhatsappTextRequest.builder()
				.to(to).text(txt).build()
		);
	}

	@Test
	public void testConstructNullText() {
		assertThrows(NullPointerException.class, () -> WhatsappTextRequest.builder()
				.from(from).to(to).build()
		);
	}

	@Test
	public void testConstructEmptyText() {
		assertThrows(IllegalArgumentException.class, () -> WhatsappTextRequest.builder()
				.from(from).to(to).text("").build()
		);
	}

	@Test
	public void testConstructLongText() {
		StringBuilder text = new StringBuilder(1002);
        text.append("*".repeat(4095));
		assertEquals(4095, text.length());

		WhatsappTextRequest msg = WhatsappTextRequest.builder()
				.text(text.toString()).from(from).to(to).build();

		assertEquals(text.toString(), msg.getText());
		text.append("xy");
		assertEquals(4097, text.length());

		assertThrows(IllegalArgumentException.class, () -> WhatsappTextRequest.builder()
				.from(msg.getFrom()).text(text.toString()).to(msg.getTo()).build()
		);
	}
}
