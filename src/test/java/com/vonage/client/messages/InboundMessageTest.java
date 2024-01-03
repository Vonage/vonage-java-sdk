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
package com.vonage.client.messages;

import com.vonage.client.VonageResponseParseException;
import com.vonage.client.messages.sms.SmsInboundMetadata;
import com.vonage.client.messages.whatsapp.Order;
import com.vonage.client.messages.whatsapp.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InboundMessageTest {
	final UUID messageUuid = UUID.randomUUID();
	final String to = "447700900000";
    final String from = "447700900001";
	final String timestamp = "2020-01-01T15:43:21Z";
	final String clientRef = UUID.randomUUID().toString();
	final String text = "Hello, world!";
	final String price = "0.0333";
	final String currency = "EUR";

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

	String getSmsPartialStubWithUsageAndMetadata() {
		return getSmsPartialJsonStub() + ",\n  \"usage\": {\n" +
			  "    \"currency\": \""+currency+"\",\n" +
			  "    \"price\": \""+price+"\"\n"+
			  "  },\n  \"sms\": {\n" +
				"  \"num_messages\": \"2\",\n" +
				"  \"total_count\": \"3\",\n" +
				"  \"keyword\": \"HELLO\"\n  }";
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

	void assertEqualsSmsWithUsageAndMetadata(InboundMessage im) {
		assertEqualsSms(im);
		MessageStatus.Usage usage = im.getUsage();
		assertNotNull(usage);
		assertEquals(currency, usage.getCurrency().getCurrencyCode());
		assertEquals(price, String.valueOf(usage.getPrice()));
		SmsInboundMetadata metadata = im.getSmsMetadata();
		assertNotNull(metadata);
		assertEquals(2, metadata.getNumMessages().intValue());
		assertEquals(3, metadata.getTotalCount().intValue());
		assertEquals("HELLO", metadata.getKeyword());
	}

	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> InboundMessage.fromJson("{malformed]"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUnknownProperty() {
		String fullJson = getSmsPartialStubWithUsageAndMetadata() +
				",\n\"someRandomProp\": {\"int_field\": 19, \"str_field\": \"A value\", \"col\":[]}}";

		InboundMessage im = InboundMessage.fromJson(fullJson);
		assertEqualsSmsWithUsageAndMetadata(im);

		Map<String, Object> randomProp = (Map<String, Object>) im.getUnmappedProperties().get("someRandomProp");
		assertNotNull(randomProp);
		assertEquals(3, randomProp.size());
		assertEquals(19, randomProp.get("int_field"));
		assertEquals("A value", randomProp.get("str_field"));
		assertTrue(((List<?>) randomProp.get("col")).isEmpty());

		assertNull(im.getAudioUrl());
		assertNull(im.getVcardUrl());
		assertNull(im.getVideoUrl());
		assertNull(im.getFileUrl());
		assertNull(im.getImageUrl());
		assertNull(im.getWhatsappContext());
		assertNull(im.getWhatsappReply());
		assertNull(im.getWhatsappLocation());
		assertNull(im.getWhatsappOrder());
		assertNull(im.getProviderMessage());
	}

	@Test
	public void testMessengerUnsupportedType() {
		String fullJson = getCommonPartialJsonStub(Channel.MESSENGER, MessageType.UNSUPPORTED) + "\n}";
		InboundMessage im = InboundMessage.fromJson(fullJson);

		assertEqualsCommon(im);
		assertEquals(Channel.MESSENGER, im.getChannel());
		assertEquals(MessageType.UNSUPPORTED, im.getMessageType());
		assertNull(im.getUnmappedProperties());
		assertNull(im.getText());
		assertNull(im.getAudioUrl());
		assertNull(im.getVcardUrl());
		assertNull(im.getVideoUrl());
		assertNull(im.getFileUrl());
		assertNull(im.getImageUrl());
		assertNull(im.getWhatsappContext());
		assertNull(im.getWhatsappReply());
		assertNull(im.getWhatsappLocation());
		assertNull(im.getWhatsappOrder());
		assertNull(im.getProviderMessage());
		assertNull(im.getUsage());
		assertNull(im.getSmsMetadata());
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

		assertNull(im.getUnmappedProperties());
		assertNull(im.getText());
		assertNull(im.getAudioUrl());
		assertNull(im.getVideoUrl());
		assertNull(im.getFileUrl());
		assertNull(im.getImageUrl());
		assertNull(im.getWhatsappContext());
		assertNull(im.getWhatsappReply());
		assertNull(im.getWhatsappLocation());
		assertNull(im.getWhatsappOrder());
		assertNull(im.getProviderMessage());
		assertNull(im.getUsage());
		assertNull(im.getSmsMetadata());
	}

	@Test
	public void testImageOnly() {
		URI image = URI.create("https://www.example.org/path/to/image.png");
		String caption = "Alt text accompanying the image";
		String json = "{\"image\": {\"url\":\""+image+"\",\"caption\":\""+caption+"\"}}";
		InboundMessage im = InboundMessage.fromJson(json);
		assertEquals(image, im.getImageUrl());
		assertEquals(caption, im.getImageCaption());
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

	@Test
	public void testStickerOnly() {
		URI sticker = URI.create("https://www.example.org/path/to/sticker.webp");
		String json = "{\"sticker\": {\"url\":\""+sticker+"\"}}";
		InboundMessage im = InboundMessage.fromJson(json);
		assertEquals(sticker, im.getStickerUrl());
	}

	@Test
	public void testWhatsappLocationOnly() {
		double latitude = 40.34772, longitude = 74.18847;
		String name = "Vonage", address = "23 Main St, Holmdel, NJ 07733, USA";
		String json = "{\"location\":{\"lat\":"+latitude+",\"long\":"+longitude +
				",\"name\":\""+name+"\",\"address\":\""+address+"\"}}";
		InboundMessage im = InboundMessage.fromJson(json);
		Location location = im.getWhatsappLocation();
		assertNotNull(location);
		assertEquals(longitude, location.getLongitude(), 0.000001);
		assertEquals(latitude, location.getLatitude(), 0.000001);
		assertEquals(name, location.getName());
		assertEquals(address, location.getAddress());
	}

	@Test
	public void testWhatsappReplyOnly() {
		String id = "row1", title = "9am", description = "Select 9am appointment time";
		String json = "{\"reply\":{\"id\":\""+id+"\",\"title\":\""+title+"\",\"description\":\""+description+"\"}}";
		InboundMessage im = InboundMessage.fromJson(json);
		Reply reply = im.getWhatsappReply();
		assertNotNull(reply);
		assertEquals(id, reply.getId());
		assertEquals(title, reply.getTitle());
		assertEquals(description, reply.getDescription());
	}

	@Test
	public void testWhatsappOrderOnly() {
		Integer quantity = 3;
		Double price = 9.99;
		String cid = "2806150799683508", pid = "pk1v7rudbg", currency = "USD",
				json = "{\"order\":{\"catalog_id\":\""+cid+"\",\"product_items\":[{},{\n"+
						"    \"product_retailer_id\": \""+pid+"\",\n" +
						"    \"quantity\": \""+quantity+"\",\n" +
						"    \"item_price\": \""+price+"\",\n" +
						"    \"currency\": \""+currency+"\"\n" +
						"}]}}";
		InboundMessage im = InboundMessage.fromJson(json);
		Order order = im.getWhatsappOrder();
		assertNotNull(order);
		assertEquals(cid, order.getCatalogId());
		List<ProductItem> items = order.getProductItems();
		assertNotNull(items);
		assertEquals(2, items.size());
		ProductItem emptyItem = items.get(0), mainItem = items.get(1);
		assertNotNull(emptyItem);
		assertNull(emptyItem.getProductRetailerId());
		assertNull(emptyItem.getQuantity());
		assertNull(emptyItem.getItemPrice());
		assertNull(emptyItem.getCurrency());
		assertNotNull(mainItem);
		assertEquals(pid, mainItem.getProductRetailerId());
		assertEquals(quantity, mainItem.getQuantity());
		assertEquals(price, mainItem.getItemPrice());
		assertEquals(Currency.getInstance(currency), mainItem.getCurrency());
	}

	@Test
	public void testWhatsappContextForOrderOnly() {
		UUID messageId = UUID.randomUUID();
		String cid = "1267260820787549", pid = "r07qei73l7", from = "447700900001", json = "{\n" +
				"  \"context_status\": \"available\",\n" +
				"  \"context\": {\n" +
				"      \"whatsapp_referred_product\": {\n" +
				"         \"catalog_id\": \""+cid+"\",\n" +
				"         \"product_retailer_id\": \""+pid+"\"\n" +
				"      },\n"+
				"      \"message_uuid\": \""+messageId+"\",\n" +
				"      \"message_from\": \""+from+"\"\n" +
				"   }" +
				"}";
		InboundMessage im = InboundMessage.fromJson(json);
		assertEquals(ContextStatus.AVAILABLE, im.getWhatsappContextStatus());
		Context context = im.getWhatsappContext();
		assertNotNull(context);
		assertEquals(messageId, context.getMessageUuid());
		assertEquals(from, context.getMessageFrom());
		ReferredProduct wrp = context.getReferredProduct();
		assertNotNull(wrp);
		assertEquals(cid, wrp.getCatalogId());
		assertEquals(pid, wrp.getProductRetailerId());
	}

	@Test
	public void testWhatsappStatusContextAndProfileOnly() {
		UUID messageId = UUID.randomUUID();
		String name = "Jane Smith", from = "447700900000", json = "{\n" +
				"  \"profile\":{\"name\": \""+name+"\"},\n" +
				"  \"context_status\": \"unavailable\",\n" +
				"   \"context\": {\n" +
				"      \"message_uuid\": \""+messageId+"\",\n" +
				"      \"message_from\": \""+from+"\"\n" +
				"   }" +
				"}";
		InboundMessage im = InboundMessage.fromJson(json);
		Profile profile = im.getWhatsappProfile();
		assertNotNull(profile);
		assertEquals(name, profile.getName());
		assertEquals(ContextStatus.UNAVAILABLE.toString(), im.getWhatsappContextStatus().toString());
		Context context = im.getWhatsappContext();
		assertNotNull(context);
		assertEquals(messageId, context.getMessageUuid());
		assertEquals(from, context.getMessageFrom());
	}

	@Test
	public void testWhatsappReferralOnly() {
		String body = "Check out our new product offering",
				headline = "New Products!", sourceId = "212731241638144",
				sourceType = "post", sourceUrl = "https://fb.me/2ZulEu42P",
				json = "{\n" +
				"  \"whatsapp\": {\n" +
				"      \"referral\": {\n" +
				"         \"body\": \""+body+"\",\n" +
				"         \"headline\": \""+headline+"\",\n" +
				"         \"source_id\": \""+sourceId+"\",\n" +
				"         \"source_type\": \""+sourceType+"\",\n" +
				"         \"source_url\": \""+sourceUrl+"\"\n" +
				"      }\n" +
				"   }\n" +
				"}";

		InboundMessage im = InboundMessage.fromJson(json);
		Referral referral = im.getWhatsappReferral();
		assertNotNull(referral);
		assertEquals(body, referral.getBody());
		assertEquals(headline, referral.getHeadline());
		assertEquals(sourceId, referral.getSourceId());
		assertEquals(sourceType, referral.getSourceType());
		assertEquals(URI.create(sourceUrl), referral.getSourceUrl());
	}
}
