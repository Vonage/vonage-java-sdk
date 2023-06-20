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

import org.junit.Test;
import static org.junit.Assert.*;

public class WhatsappImageRequestTest {

	@Test
	public void testSerializeValid() {
		String url = "file:///path/to/picture.jpg", caption = "Cute kittens";
		String json = WhatsappImageRequest.builder()
				.from("317900000002").to("447900000001")
				.url(url).caption(caption)
				.build().toJson();
		assertTrue(json.contains("\"image\":{\"url\":\""+url+"\",\"caption\":\""+caption+"\"}"));
		assertTrue(json.contains("\"message_type\":\"image\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testSerializeNoCaption() {
		String url = "file:///path/to/picture.jpg";
		String json = WhatsappImageRequest.builder()
				.url(url).from("447900000002").to("447900000001")
				.build().toJson();
		assertTrue(json.contains("\"image\":{\"url\":\""+url+"\"}"));
		assertTrue(json.contains("\"message_type\":\"image\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testConstructCaptionLength() {
		WhatsappImageRequest.Builder builder = WhatsappImageRequest.builder()
				.url("file:///path/to/picture.png").from("317900000002").to("447900000001");

		assertEquals(12, builder.build().getTo().length());

		try {
			builder.caption("").build();
			fail("Expected exception for empty caption");
		}
		catch (IllegalArgumentException ex) {
			assertEquals("V", builder.caption("V").build().getImage().getCaption());
		}

		StringBuilder sb = new StringBuilder(3001);
		for (int i = 0; i < 2999; i++) {
			sb.append('*');
		}
		assertEquals(2999, sb.length());

		assertEquals(sb.toString(), builder.caption(sb.toString()).build().getImage().getCaption());
		try {
			builder.caption(sb.append("xy").toString()).build();
			fail("Expected exception for caption length");
		}
		catch (IllegalArgumentException ex) {
			assertEquals(3001, sb.length());
		}
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoUrl() {
		WhatsappImageRequest.builder()
				.caption("Description")
				.from("447900000001")
				.to("317900000002")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidExtension() {
		WhatsappImageRequest.builder()
				.from("447900000001")
				.url("ftp://rel/path/to/photo.bmp")
				.to("317900000002")
				.build();
	}

	@Test
	public void testValidExtensions() {
		WhatsappImageRequest.Builder builder = WhatsappImageRequest.builder()
				.from("447900000001")
				.to("317900000002");

		String baseUrl = "file:///path/to/resource", url;
		for (String imageType : new String[]{"jpeg", "jpg", "png"}) {
			url = baseUrl+'.'+imageType;
			builder.url(url);
			assertEquals(url, builder.build().getImage().getUrl().toString());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptyCaption() {
		WhatsappVideoRequest.builder()
				.from("447900000001")
				.url("ftp://rel/path/to/picture.jpeg")
				.to("317900000002")
				.caption("")
				.build();
	}
}
