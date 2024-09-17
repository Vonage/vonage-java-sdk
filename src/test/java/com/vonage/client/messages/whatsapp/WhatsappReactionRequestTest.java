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
package com.vonage.client.messages.whatsapp;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;
import java.util.UUID;

public class WhatsappReactionRequestTest {

	@Test
	public void testSerializeReact() {
		String emoji = "😀";
		String from = "317900000002", to = "447900000001";
		String json = WhatsappReactionRequest.builder()
				.from(from).to(to).reaction(emoji).build().toJson();
		assertTrue(json.contains("\"reaction\":{\"action\":\"react\",\"emoji\":\""+emoji+"\"}"));
		assertTrue(json.contains("\"message_type\":\"reaction\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
	}

	@Test
	public void testSerializeUnreact() {
		String from = "317900000002", to = "447900000001";
		String json = WhatsappReactionRequest.builder()
				.from(from).to(to).unreact().build().toJson();
		assertTrue(json.contains("\"reaction\":{\"action\":\"unreact\"}"));
		assertTrue(json.contains("\"message_type\":\"reaction\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
	}

	@Test
	public void testConstructNoAction() {
		assertThrows(NullPointerException.class, () -> WhatsappReactionRequest.builder()
				.from("447900000001").to("317900000002").build()
		);
	}
}
