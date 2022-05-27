/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.messages.messenger;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessengerTextRequestTest {

	@Test
	public void testSerializeValid() {
		String from = "447900000001", to = "317900000002", txt = "Hello, World!";
		MessengerTextRequest msg = MessengerTextRequest.builder().from(from).to(to).text(txt).build();
		String json = msg.toJson();
		assertTrue(json.contains("\"text\":\""+txt+"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"messenger\""));
	}

	@Test(expected = NullPointerException.class)
	public void testNullText() {
		MessengerTextRequest.builder()
				.from("447900000001")
				.to("317900000002")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyText() {
		MessengerTextRequest.builder()
				.from("447900000001")
				.to("317900000002")
				.text("")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLongText() {
		StringBuilder text = new StringBuilder(1002);
		for (int i = 0; i < 639; i++) {
			text.append('*');
		}
		assertEquals(639, text.length());

		MessengerTextRequest msg = MessengerTextRequest.builder()
				.from("447900000001")
				.to("317900000002")
				.text(text.toString())
				.build();

		assertEquals(text.toString(), msg.getText());
		text.append("tt");
		assertEquals(641, text.length());

		MessengerTextRequest.builder()
				.from(msg.getFrom())
				.to(msg.getTo())
				.text(text.toString())
				.build();
	}
}
