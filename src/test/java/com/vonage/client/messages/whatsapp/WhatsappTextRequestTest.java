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
package com.vonage.client.messages.whatsapp;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WhatsappTextRequestTest {

	@Test
	public void testSerializeValid() {
		String from = "447900000001", to = "317900000002", txt = "Hello, World!";
		String json = WhatsappTextRequest.builder().from(from).to(to).text(txt).build().toJson();
		assertTrue(json.contains("\"text\":\""+txt+"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptySender() {
		WhatsappTextRequest.builder()
				.from("")
				.to("317900000002")
				.text("Hello, World!")
				.build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoSender() {
		WhatsappTextRequest.builder()
				.to("317900000002")
				.text("Hello, World!")
				.build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNullText() {
		WhatsappTextRequest.builder()
				.from("447900000001")
				.to("317900000002")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptyText() {
		WhatsappTextRequest.builder()
				.from("447900000001")
				.to("317900000002")
				.text("")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructLongText() {
		StringBuilder text = new StringBuilder(1002);
		for (int i = 0; i < 4095; i++) {
			text.append('*');
		}
		assertEquals(4095, text.length());

		WhatsappTextRequest msg = WhatsappTextRequest.builder()
				.text(text.toString())
				.from("447900000001")
				.to("317900000002")
				.build();

		assertEquals(text.toString(), msg.getText());
		text.append("xy");
		assertEquals(4097, text.length());

		WhatsappTextRequest.builder()
				.from(msg.getFrom())
				.text(text.toString())
				.to(msg.getTo())
				.build();
	}
}
