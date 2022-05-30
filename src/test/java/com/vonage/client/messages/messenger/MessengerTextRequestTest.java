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
		String from = "ali", to = "bob", txt = "Hello, World!", cr = "79ac12f";
		String json = MessengerTextRequest.builder()
				.from(from).to(to).text(txt)
				.clientRef(cr)
				.build().toJson();
		assertTrue(json.contains("\"text\":\""+txt+"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"messenger\""));
		assertTrue(json.contains("\"client_ref\":\""+cr+"\""));
	}

	@Test
	public void testSerializeWithCategoryAndTag() {
		MessengerTextRequest msg = MessengerTextRequest.builder()
				.category(Category.UPDATE)
				.tag(Tag.CONFIRMED_EVENT_UPDATE)
				.from("Sara Lance")
				.to("Ava Sharpe")
				.text("I love you!!")
				.build();
		String json = msg.toJson();
		assertTrue(json.contains("\"text\":\""+msg.getText()+"\""));
		assertTrue(json.contains("\"from\":\""+msg.getFrom()+"\""));
		assertTrue(json.contains("\"to\":\""+msg.getTo()+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"messenger\""));
		assertTrue(json.contains(
				"\"messenger\":{\"category\":\""+ msg.getMessenger().getCategory() +
				"\",\"tag\":\""+msg.getMessenger().getTag()+"\"}"
		));
	}

	@Test
	public void testSerializeWithoutTag() {
		MessengerTextRequest msg = MessengerTextRequest.builder()
				.category(Category.RESPONSE)
				.from("Sara Lance")
				.to("Ava Sharpe")
				.text("I love you!!")
				.build();
		String json = msg.toJson();
		assertTrue(json.contains("\"text\":\""+msg.getText()+"\""));
		assertTrue(json.contains("\"from\":\""+msg.getFrom()+"\""));
		assertTrue(json.contains("\"to\":\""+msg.getTo()+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"messenger\""));
		assertTrue(json.contains("\"messenger\":{\"category\":\""+msg.getMessenger().getCategory()+"\"}"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructMessageTagCategoryWithoutTag() {
		MessengerTextRequest.builder()
				.category(Category.MESSAGE_TAG)
				.from("Alice")
				.to("Bob")
				.text("Hello :wave:")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructLongRecipient() {
		StringBuilder from = new StringBuilder(51);
		for (int i = 0; i < from.capacity(); i++) {
			from.append('n');
		}
		assertEquals(51, from.length());
		MessengerTextRequest.builder()
				.from(from.toString())
				.to("bob")
				.build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNullText() {
		MessengerTextRequest.builder()
				.from("ali")
				.to("bob")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptyText() {
		MessengerTextRequest.builder()
				.from("447900000001")
				.to("317900000002")
				.text("")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructLongText() {
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
