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
package com.vonage.client.messages.viber;

import org.junit.Test;

import static org.junit.Assert.*;

public class ViberTextRequestTest {

	@Test
	public void testSerializeValid() {
		String from = "Thomas Shelby", to = "447900012345", txt = "By the order", cr = "79ac12f";
		String json = ViberTextRequest.builder()
				.from(from).to(to).text(txt)
				.clientRef(cr).build().toJson();
		assertTrue(json.contains("\"text\":\""+txt+"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"viber_service\""));
		assertTrue(json.contains("\"client_ref\":\""+cr+"\""));
		assertFalse(json.contains("\"viber_service\":"));
	}

	@Test
	public void testSerializeWithAllViberServiceFields() {
		ViberTextRequest msg = ViberTextRequest.builder()
				.viberType("template")
				.ttl(612)
				.from("Business")
				.category(Category.TRANSACTION)
				.to("447900012345")
				.text("CA$H£")
				.build();
		String json = msg.toJson();
		assertTrue(json.contains("\"text\":\""+msg.getText()+"\""));
		assertTrue(json.contains("\"from\":\""+msg.getFrom()+"\""));
		assertTrue(json.contains("\"to\":\""+msg.getTo()+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"viber_service\""));
		assertTrue(json.contains(
				"\"viber_service\":{\"category\":\"transaction\",\"ttl\":612,\"type\":\"template\"}"
		));
	}

	@Test
	public void testSerializeWithoutType() {
		ViberTextRequest msg = ViberTextRequest.builder()
				.ttl(9002)
				.from("Marketing")
				.category(Category.PROMOTION)
				.to("447900012345")
				.text("Hurry! Offer ends today")
				.build();
		String json = msg.toJson();
		assertTrue(json.contains("\"text\":\""+msg.getText()+"\""));
		assertTrue(json.contains("\"from\":\""+msg.getFrom()+"\""));
		assertTrue(json.contains("\"to\":\""+msg.getTo()+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"viber_service\""));
		assertTrue(json.contains("\"viber_service\":{\"category\":\"promotion\",\"ttl\":9002}"));
	}

	@Test
	public void testSerializeWithTtl() {
		ViberTextRequest msg = ViberTextRequest.builder()
				.ttl(2147)
				.from("Alice")
				.to("447900012345")
				.text("<3")
				.build();
		String json = msg.toJson();
		assertTrue(json.contains("\"text\":\""+msg.getText()+"\""));
		assertTrue(json.contains("\"from\":\""+msg.getFrom()+"\""));
		assertTrue(json.contains("\"to\":\""+msg.getTo()+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"viber_service\""));
		assertTrue(json.contains("\"viber_service\":{\"ttl\":2147}"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructLongSender() {
		StringBuilder from = new StringBuilder(51);
		for (int i = 0; i < from.capacity(); i++) {
			from.append('n');
		}
		assertEquals(51, from.length());
		ViberTextRequest.builder()
				.text("Bonjour")
				.from(from.toString())
				.to("447900000001")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptySender() {
		ViberTextRequest.builder()
				.text("Bonjour")
				.from("")
				.to("447900000001")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNoSender() {
		ViberTextRequest.builder()
				.text("Bonjour")
				.to("447900000001")
				.build();
	}

	@Test
	public void testConstructTtlBounds() {
		final String from = "Karin", to = "447900000001", text = "こんにちは";
		ViberTextRequest.Builder builder = ViberTextRequest.builder().from(from).text(text).to(to);

		ViberTextRequest msg = builder.ttl(30).build();
		assertEquals(30, msg.getViberService().getTtl().intValue());

		try {
			builder.ttl(29).build();
			fail("TTL shouldn't be below 30");
		}
		catch (IllegalArgumentException ex) {
			msg = builder.ttl(259200).build();
			assertEquals(259200, msg.getViberService().getTtl().intValue());
		}
		try {
			builder.ttl(259201).build();
			fail("TTL shouldn't be above 259200");
		}
		catch (IllegalArgumentException ex) {
			assertEquals(text, msg.getText());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructIDForRecipient() {
		ViberTextRequest.builder()
				.from("447900000001")
				.text("Bonjour")
				.to("Grace")
				.build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNullText() {
		ViberTextRequest.builder()
				.from("317900000002")
				.to("+447900000001")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptyText() {
		ViberTextRequest.builder()
				.from("447900000001")
				.to("317900000002")
				.text("")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructLongText() {
		StringBuilder text = new StringBuilder(1002);
		for (int i = 0; i < 999; i++) {
			text.append('*');
		}
		assertEquals(999, text.length());

		ViberTextRequest msg = ViberTextRequest.builder()
				.from("447900000001")
				.to("317900000002")
				.text(text.toString())
				.build();

		assertEquals(text.toString(), msg.getText());
		text.append("xy");
		assertEquals(1001, text.length());

		ViberTextRequest.builder()
				.from(msg.getFrom())
				.to(msg.getTo())
				.text(text.toString())
				.build();
	}
}
