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
package com.vonage.client.messages.sms;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmsTextRequestTest {
	final String
			from = "447900000001", to = "317900000002",  msg = "Hello, World!",
			contentId = "1107457532145798767", entityId = "1101456324675322134";

	@Test
	public void testSerializeAllParameters() {
		int ttl = 900000;
		SmsTextRequest sms = SmsTextRequest.builder()
				.from(from).to(to).text(msg).ttl(ttl).contentId(contentId)
				.encodingType(EncodingType.UNICODE).entityId(entityId).build();

		String json = sms.toJson();
		assertTrue(json.contains("\"text\":\""+msg+"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"sms\""));
		assertTrue(json.contains("\"ttl\":"+ttl));
		assertTrue(json.contains("\"sms\":{" +
				"\"encoding_type\":\"unicode\"," +
				"\"content_id\":\""+contentId+"\"," +
				"\"entity_id\":\""+entityId+"\"}"
		));
		assertEquals("SmsTextRequest "+json, sms.toString());
	}

	@Test
	public void testRequiredParametersOnly() {
		SmsTextRequest sms = SmsTextRequest.builder().from(from).to(to).text(msg).build();

		String json = sms.toJson();
		assertTrue(json.contains("\"text\":\""+msg+"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"sms\""));
		assertEquals("SmsTextRequest "+json, sms.toString());

		assertNull(sms.getMessageSettings());
		assertNull(sms.getTtl());
	}

	@Test
	public void testEncodingTypeSettingsOnly() {
		EncodingType encodingType = EncodingType.AUTO;
		SmsTextRequest sms = SmsTextRequest.builder()
				.from(from).to(to).text(msg).encodingType(encodingType).build();

		String json = sms.toJson();
		assertTrue(json.contains("\"text\":\""+msg+"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"sms\""));
		assertTrue(json.contains("\"sms\":{\"encoding_type\":\""+encodingType+"\"}"));
		assertEquals("SmsTextRequest "+json, sms.toString());

		assertNull(sms.getTtl());
		OutboundSettings settings = sms.getMessageSettings();
		assertNotNull(settings);
		assertEquals(encodingType, EncodingType.fromString(settings.getEncodingType().toString()));
		assertNull(settings.getEntityId());
		assertNull(settings.getContentId());
	}

	@Test
	public void testInvalidContentId() {
		assertThrows(IllegalArgumentException.class, () ->
				OutboundSettings.construct(EncodingType.TEXT, " ", entityId)
		);
	}

	@Test
	public void testInvalidEntityId() {
		assertThrows(IllegalArgumentException.class, () ->
				OutboundSettings.construct(EncodingType.TEXT, contentId, " ")
		);
	}

	@Test
	public void testTtlTooShort() {
		assertThrows(IllegalArgumentException.class, () ->
				SmsTextRequest.builder().from(from).to(to).text(msg).ttl(0).build()
		);
	}

	@Test
	public void testNullText() {
		assertThrows(NullPointerException.class, () ->
				SmsTextRequest.builder().from(from).to(to).build()
		);
	}

	@Test
	public void testEmptyText() {
		assertThrows(IllegalArgumentException.class, () ->
				SmsTextRequest.builder().from(from).to(to).text("").build()
		);
	}

	@Test
	public void testLongText() {
		StringBuilder text = new StringBuilder(1002);
		for (int i = 0; i < 999; i++) {
			text.append('*');
		}
		assertEquals(999, text.length());

		SmsTextRequest sms = SmsTextRequest.builder().text(text.toString()).from(from).to(to).build();

		assertEquals(text.toString(), sms.getText());
		text.append("xy");
		assertEquals(1001, text.length());

		assertThrows(IllegalArgumentException.class, () -> SmsTextRequest.builder()
				.from(sms.getFrom()).text(text.toString()).to(sms.getTo()).build()
		);
	}
}
