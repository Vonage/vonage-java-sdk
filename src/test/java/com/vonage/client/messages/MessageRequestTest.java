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
package com.vonage.client.messages;

import org.junit.Test;

import static org.junit.Assert.*;

public class MessageRequestTest {

	static class ConcreteMessageRequest extends MessageRequest {

		static ConcreteMessageRequest.Builder builder(MessageType mt, Channel ct) {
			return new Builder() {
				@Override
				protected MessageType getMessageType() {
					return mt;
				}

				@Override
				protected Channel getChannel() {
					return ct;
				}
			};
		}

		static abstract class Builder extends MessageRequest.Builder<ConcreteMessageRequest, Builder> {

			@Override
			public ConcreteMessageRequest build() {
				return new ConcreteMessageRequest(this);
			}
		}

		private ConcreteMessageRequest(Builder builder) {
			super(builder);
		}
	}

	@Test
	public void testSerializeAllFields() {
		MessageRequest smr = ConcreteMessageRequest.builder(MessageType.VIDEO, Channel.MMS)
				.from("447900000009").to("12002009000")
				.clientRef("<40 character string").build();

		String generatedJson = smr.toJson();
		assertTrue(generatedJson.contains("\"client_ref\":\"<40 character string\""));
		assertTrue(generatedJson.contains("\"from\":\"447900000009\""));
		assertTrue(generatedJson.contains("\"to\":\"12002009000\""));
		assertTrue(generatedJson.contains("\"channel\":\"mms\""));
		assertTrue(generatedJson.contains("\"message_type\":\"video\""));
	}

	@Test
	public void testSerializeFieldsWithoutClientRef() {
		MessageRequest smr = ConcreteMessageRequest.builder(MessageType.IMAGE, Channel.VIBER)
				.from("447900000009").to("12002009000").build();

		String generatedJson = smr.toJson();
		assertFalse(generatedJson.contains("client_ref"));
		assertTrue(generatedJson.contains("\"from\":\"447900000009\""));
		assertTrue(generatedJson.contains("\"to\":\"12002009000\""));
		assertTrue(generatedJson.contains("\"channel\":\"viber_service\""));
		assertTrue(generatedJson.contains("\"message_type\":\"image\""));
	}

	@Test
	public void testSerializeNoNumbers() {
		MessageRequest smr = ConcreteMessageRequest.builder(MessageType.TEXT, Channel.SMS)
				.from("447900000009").to("447900000001").build();

		smr.from = null;
		smr.to = null;

		String generatedJson = smr.toJson();
		assertFalse(generatedJson.contains("from"));
		assertFalse(generatedJson.contains("to"));
		assertTrue(generatedJson.contains("text"));
		assertTrue(generatedJson.contains("sms"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidMessageType() {
		ConcreteMessageRequest.builder(MessageType.IMAGE, Channel.SMS)
				.from("447900000001").to("447900000009").build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptyNumber() {
		ConcreteMessageRequest.builder(MessageType.FILE, Channel.MESSENGER)
				.from("447900000009").to("").build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNullNumber() {
		ConcreteMessageRequest.builder(MessageType.CUSTOM, Channel.WHATSAPP)
				.from(null).to("447900000009").build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoNumber() {
		ConcreteMessageRequest.builder(MessageType.CUSTOM, Channel.WHATSAPP)
				.from("447900000009").build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidNumber() {
		ConcreteMessageRequest.builder(MessageType.FILE, Channel.MESSENGER)
				.to("447900000001").from("+0 NaN").build();
	}

	@Test
	public void testConstructMalformedButSalvagableNumbers() {
		String json = ConcreteMessageRequest.builder(MessageType.AUDIO, Channel.WHATSAPP)
				.from("+44 7900090000").to("+1 900-900-0000").build().toJson();

		assertTrue(json.contains("\"from\":\"447900090000\""));
		assertTrue(json.contains("\"to\":\"19009000000\""));
		assertTrue(json.contains("\"message_type\":\"audio\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructTooLongNumber() {
		ConcreteMessageRequest.builder(MessageType.TEXT, Channel.MESSENGER)
				.from("+447900090000").to("19009000000447900090000").build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoMessageType() {
		ConcreteMessageRequest.builder(null, Channel.MMS)
				.from("447900000001").to("447900000009").build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoChannel() {
		ConcreteMessageRequest.builder(MessageType.IMAGE, null)
				.from("447900000001").to("447900000009").build();
	}

	@Test
	public void testConstructLongClientRef() {
		StringBuilder clientRef = new StringBuilder(41);
		for (int i = 0; i < 39; i++) {
			clientRef.append('c');
		}

		ConcreteMessageRequest.Builder builder = ConcreteMessageRequest
				.builder(MessageType.TEXT, Channel.SMS)
				.from("447900000009").to("12002009000");

		assertEquals(39, builder.clientRef(clientRef.toString()).build().getClientRef().length());

		clientRef.append("0f");
		try {
			builder.clientRef(clientRef.toString()).build();
			fail("Expected exception for clientRef > 40 characters");
		}
		catch (IllegalArgumentException ex) {
			assertEquals(41, clientRef.length());
		}
	}

	@Test
	public void testToString() {
		MessageRequest request = ConcreteMessageRequest
				.builder(MessageType.CUSTOM, Channel.WHATSAPP)
				.from("447900000009").to("12002009000").build();

		String expected = "ConcreteMessageRequest {\"message_type\":\"custom\",\"channel\":\"whatsapp\",\"from\":\"447900000009\",\"to\":\"12002009000\"}";
		assertEquals(expected, request.toString());
	}
}
