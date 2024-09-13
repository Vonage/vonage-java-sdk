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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.VonageUnexpectedException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.internal.matchers.Null;

public class MessageRequestTest {

	static class ConcreteMessageRequest extends MessageRequest {

		static ConcreteMessageRequest.Builder builder(MessageType mt, Channel ct) {
			return new Builder(mt, ct);
		}

		static class Builder extends MessageRequest.Builder<ConcreteMessageRequest, Builder> {
			final MessageType messageType;
			final Channel channel;

			Builder(MessageType messageType, Channel channel) {
				this.messageType = messageType;
				this.channel = channel;
			}

			@Override
			public ConcreteMessageRequest build() {
				return new ConcreteMessageRequest(this);
			}
		}

		private ConcreteMessageRequest(Builder builder) {
			super(builder, builder.channel, builder.messageType);
		}
	}

	@Test
	public void testSerializeAllFields() {
		MessageRequest smr = ConcreteMessageRequest.builder(MessageType.VIDEO, Channel.MMS)
				.from("447900000009").to("12002009000")
				.url("https://example.com/video.mp4")
				.clientRef("<40 character string")
				.webhookUrl("https://example.com/status")
				.webhookVersion(MessagesVersion.V1).build();

		String generatedJson = smr.toJson();
		assertTrue(generatedJson.contains("\"client_ref\":\"<40 character string\""));
		assertTrue(generatedJson.contains("\"webhook_url\":\"https://example.com/status\""));
		assertTrue(generatedJson.contains("\"webhook_version\":\"v1\""));
		assertTrue(generatedJson.contains("\"from\":\"447900000009\""));
		assertTrue(generatedJson.contains("\"to\":\"12002009000\""));
		assertTrue(generatedJson.contains("\"channel\":\"mms\""));
		assertTrue(generatedJson.contains("\"message_type\":\"video\""));
		assertFalse(generatedJson.contains("https://example.com/video.mp4"));
	}

	@Test
	public void testSerializeFieldsRequiredOnly() {
		MessageRequest smr = ConcreteMessageRequest.builder(MessageType.IMAGE, Channel.VIBER)
				.url("https://example.org/image.jpg").from("447900000009").to("12002009000").build();

		String generatedJson = smr.toJson();
		assertFalse(generatedJson.contains("client_ref"));
		assertFalse(generatedJson.contains("webhook_url"));
		assertFalse(generatedJson.contains("webhook_version"));
		assertFalse(generatedJson.contains("text"));
		assertFalse(generatedJson.contains("ttl"));
		assertTrue(generatedJson.contains("\"from\":\"447900000009\""));
		assertTrue(generatedJson.contains("\"to\":\"12002009000\""));
		assertTrue(generatedJson.contains("\"channel\":\"viber_service\""));
		assertTrue(generatedJson.contains("\"message_type\":\"image\""));
	}

	@Test
	public void testSerializeNoNumbers() {
		MessageRequest smr = ConcreteMessageRequest.builder(MessageType.CUSTOM, Channel.RCS)
				.from("447900000009").to("447900000001").build();

		smr.from = null;
		smr.to = null;

		String generatedJson = smr.toJson();
		assertFalse(generatedJson.contains("to\""));
		assertFalse(generatedJson.contains("from"));
		assertTrue(generatedJson.contains("\"channel\":\"rcs\""));
		assertTrue(generatedJson.contains("\"message_type\":\"custom\""));
	}

	@Test
	public void testConstructInvalidMessageType() {
		assertThrows(IllegalArgumentException.class, () ->
				ConcreteMessageRequest.builder(MessageType.IMAGE, Channel.SMS)
						.from("447900000001").to("447900000009").build()
		);
	}

	@Test
	public void testConstructNoTextIfTextMessage() {
		assertThrows(NullPointerException.class, () ->
				ConcreteMessageRequest.builder(MessageType.TEXT, Channel.SMS)
					.to("447900000009").from("447900000001").build()
		);
	}

	@Test
	public void testConstructNegativeTtl() {
		assertThrows(IllegalArgumentException.class, () ->
				ConcreteMessageRequest.builder(MessageType.CUSTOM, Channel.WHATSAPP)
					.from("447900000001").to("447900000009").ttl(-1).build()
		);
	}

	@Test
	public void testConstructEmptyNumber() {
		assertThrows(IllegalArgumentException.class, () ->
				ConcreteMessageRequest.builder(MessageType.FILE, Channel.MESSENGER)
					.from("447900000009").to("").url("https://example.org/file.pdf").build()
		);
	}

	@Test
	public void testConstructNullNumber() {
		assertThrows(IllegalArgumentException.class, () ->
				ConcreteMessageRequest.builder(MessageType.CUSTOM, Channel.WHATSAPP)
					.to("447900000009").from(null).build()
		);
	}

	@Test
	public void testConstructNoNumber() {
		assertThrows(NullPointerException.class, () ->
				ConcreteMessageRequest.builder(MessageType.CUSTOM, Channel.WHATSAPP).from("447900000009").build()
		);
	}

	@Test
	public void testConstructInvalidNumber() {
		assertThrows(IllegalArgumentException.class, () ->
				ConcreteMessageRequest.builder(MessageType.TEMPLATE, Channel.WHATSAPP)
					.from("447900000001").to("+0 NaN").build()
		);
	}

	@Test
	public void testConstructMalformedButSalvagableNumbers() {
		String json = ConcreteMessageRequest.builder(MessageType.CUSTOM, Channel.WHATSAPP)
				.from("+44 7900090000").to("+1 900-900-0000").build().toJson();

		assertTrue(json.contains("\"from\":\"+44 7900090000\""));
		assertTrue(json.contains("\"to\":\"19009000000\""));
		assertTrue(json.contains("\"message_type\":\"custom\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testConstructTooLongNumber() {
		assertThrows(IllegalArgumentException.class, () ->
				ConcreteMessageRequest.builder(MessageType.STICKER, Channel.WHATSAPP)
					.from("+447900090000").to("19009000000447900090000").build()
		);
	}

	@Test
	public void testConstructNoMessageType() {
		assertThrows(NullPointerException.class, () ->
				ConcreteMessageRequest.builder(null, Channel.MMS)
					.from("447900000001").to("447900000009").build()
		);
	}

	@Test
	public void testConstructNoChannel() {
		assertThrows(NullPointerException.class, () ->
				ConcreteMessageRequest.builder(MessageType.IMAGE, null)
					.from("447900000001").to("447900000009").build()
		);
	}

	@Test
	public void testConstructLongClientRef() {
		StringBuilder clientRef = new StringBuilder(41);
		for (int i = 0; i < 99; i++) {
			clientRef.append('c');
		}

		ConcreteMessageRequest.Builder builder = ConcreteMessageRequest
				.builder(MessageType.FILE, Channel.RCS)
				.url("https://example.com/file.zip")
				.from("447900000009").to("12002009000");

		assertEquals(99, builder.clientRef(clientRef.toString()).build().getClientRef().length());

		clientRef.append("0f");
		try {
			builder.clientRef(clientRef.toString()).build();
			fail("Expected exception for clientRef > 100 characters");
		}
		catch (IllegalArgumentException ex) {
			assertEquals(101, clientRef.length());
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
