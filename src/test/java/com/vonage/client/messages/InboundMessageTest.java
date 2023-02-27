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

import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class InboundMessageTest {
	UUID messageUuid = UUID.randomUUID();
	String to = "447700900000", from = "447700900001";
	String timestamp = "2020-01-01T15:43:21Z";
	String clientRef = UUID.randomUUID().toString();
	String text = "Hello, world!";
	String price = "0.0333";
	String currency = "EUR";

	String getCommonPartialJsonStub(Channel channel, MessageType messageType) {
		return "{\n" +
			  "  \"message_uuid\": \""+messageUuid+"\",\n" +
			  "  \"to\": \""+to+"\",\n" +
			  "  \"from\": \""+from+"\",\n" +
			  "  \"timestamp\": \""+timestamp+"\",\n" +
			  "  \"channel\": \""+channel+"\",\n" +
			  "  \"message_type\": \""+messageType+"\",\n" +
			  "  \"client_ref\": \""+clientRef+"\"";
	}

	String getSmsPartialJsonStub() {
		return getCommonPartialJsonStub(Channel.SMS, MessageType.TEXT) +
			  ",\n  \"text\": \""+text+"\"";
	}

	String getSmsPartialStubWithUsage() {
		return getSmsPartialJsonStub() + ",\n  \"usage\": {\n" +
			  "    \"currency\": \""+currency+"\",\n" +
			  "    \"price\": \""+price+"\"\n"+
			  "  }";
	}

	void assertEqualsCommon(InboundMessage im) {
		assertEquals(messageUuid, im.getMessageUuid());
		assertEquals(to, im.getTo());
		assertEquals(from, im.getFrom());
		assertEquals(Instant.parse(timestamp), im.getTimestamp());
		assertEquals(clientRef, im.getClientRef());
	}

	void assertEqualsSms(InboundMessage im) {
		assertEqualsCommon(im);
		assertEquals(Channel.SMS, im.getChannel());
		assertEquals(MessageType.TEXT, im.getMessageType());
		assertEquals(text, im.getText());
	}

	void assertEqualsSmsWithUsage(InboundMessage im) {
		assertEqualsSms(im);
		MessageStatus.Usage usage = im.getSmsUsage();
		assertNotNull(usage);
		assertEquals(currency, usage.getCurrency().getCurrencyCode());
		assertEquals(price, ""+usage.getPrice());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUnknownProperty() {
		String fullJson = getSmsPartialStubWithUsage() + ",\n  \"sms\": {\n" +
			  "  \"num_messages\": \"2\",\n" +
			  "  \"keyword\": \"HELLO\"\n" +
			  "}\n}";

		InboundMessage im = InboundMessage.fromJson(fullJson);
		assertEqualsSmsWithUsage(im);

		assertNull(im.getAudioUrl());
		assertNull(im.getVcardUrl());
		assertNull(im.getVideoUrl());
		assertNull(im.getFileUrl());
		assertNull(im.getImageUrl());

		Map<String, String> smsMap = (Map<String, String>) im.getAdditionalProperties().get("sms");
		assertNotNull(smsMap);
		assertEquals("2", smsMap.get("num_messages"));
		assertEquals("HELLO", smsMap.get("keyword"));
	}

	@Test
	public void testMessengerUnsupportedType() {
		String fullJson = getCommonPartialJsonStub(Channel.MESSENGER, MessageType.UNSUPPORTED) + "\n}";
		InboundMessage im = InboundMessage.fromJson(fullJson);

		assertEqualsCommon(im);
		assertEquals(Channel.MESSENGER, im.getChannel());
		assertEquals(MessageType.UNSUPPORTED, im.getMessageType());
		assertNull(im.getAdditionalProperties());
		assertNull(im.getText());
		assertNull(im.getAudioUrl());
		assertNull(im.getVcardUrl());
		assertNull(im.getVideoUrl());
		assertNull(im.getFileUrl());
		assertNull(im.getImageUrl());
		assertThrows(IllegalStateException.class, im::getSmsUsage);
	}

	@Test
	public void testMmsVcard() {
		String vcard = "ftp://example.com/contact.vcf";
		String fullJson = getCommonPartialJsonStub(Channel.MMS, MessageType.VCARD) +
			  ",\n  \"vcard\": {\n" +
			  "    \"url\": \""+vcard+"\"\n" +
			  "}\n}";

		InboundMessage im = InboundMessage.fromJson(fullJson);
		assertEqualsCommon(im);
		assertEquals(Channel.MMS, im.getChannel());
		assertEquals(MessageType.VCARD, im.getMessageType());
		assertEquals(vcard, im.getVcardUrl().toString());

		assertNull(im.getAdditionalProperties());
		assertNull(im.getText());
		assertNull(im.getAudioUrl());
		assertNull(im.getVideoUrl());
		assertNull(im.getFileUrl());
		assertNull(im.getImageUrl());
	}

	@Test
	public void testImageOnly() {
		URI image = URI.create("https://www.example.org/path/to/image.png");
		String json = "{\"image\": {\"url\":\""+image+"\"}}";
		InboundMessage im = InboundMessage.fromJson(json);
		assertEquals(image, im.getImageUrl());
	}

	@Test
	public void testAudioOnly() {
		URI audio = URI.create("https://www.example.org/path/to/audio.mp3");
		String json = "{\"audio\": {\"url\":\""+audio+"\"}}";
		InboundMessage im = InboundMessage.fromJson(json);
		assertEquals(audio, im.getAudioUrl());
	}

	@Test
	public void testVideoOnly() {
		URI video = URI.create("https://www.example.org/path/to/video.mp4");
		String json = "{\"video\": {\"url\":\""+video+"\"}}";
		InboundMessage im = InboundMessage.fromJson(json);
		assertEquals(video, im.getVideoUrl());
	}

	@Test
	public void testFileOnly() {
		URI file = URI.create("https://www.example.org/path/to/file.zip");
		String json = "{\"file\": {\"url\":\""+file+"\"}}";
		InboundMessage im = InboundMessage.fromJson(json);
		assertEquals(file, im.getFileUrl());
	}
}
