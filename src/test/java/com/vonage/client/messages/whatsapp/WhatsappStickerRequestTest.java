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

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.UUID;

public class WhatsappStickerRequestTest {

	@Test
	public void testSerializeValidUrl() {
		String url = "https://example.com/image.webp";
		String from = "317900000002", to = "447900000001";
		String json = WhatsappStickerRequest.builder()
				.from(from).to(to).url(url).build().toJson();
		assertTrue(json.contains("\"sticker\":{\"url\":\""+url+"\"}"));
		assertTrue(json.contains("\"message_type\":\"sticker\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
	}

	@Test
	public void testSerializeValidId() {
		String id = UUID.randomUUID().toString();
		String from = "317900000002", to = "447900000001";
		String json = WhatsappStickerRequest.builder()
				.from(from).to(to).id(id).build().toJson();
		assertTrue(json.contains("\"sticker\":{\"id\":\""+id+"\"}"));
		assertTrue(json.contains("\"message_type\":\"sticker\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
	}

	@Test(expected = IllegalStateException.class)
	public void testConstructNoUrlOrId() {
		WhatsappStickerRequest.builder()
				.from("447900000001").to("317900000002")
				.build();
	}

	@Test(expected = IllegalStateException.class)
	public void testConstructBothUrlAndId() {
		WhatsappStickerRequest.builder()
				.from("447900000001").to("317900000002")
				.url("https://example.com/image.webp")
				.id(UUID.randomUUID().toString()).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidId() {
		WhatsappStickerRequest.builder()
				.from("447900000001").to("317900000002")
				.id("not-a-guid").build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidUrlExtension() {
		WhatsappStickerRequest.builder()
				.from("447900000001").to("317900000002")
				.url("https://example.com/image.svg").build();
	}
}
