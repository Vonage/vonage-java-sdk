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
package com.vonage.client.messages.whatsapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class WhatsappAudioRequestTest {

	@Test
	public void testSerializeValid() {
		String url = "file:///path/to/song.mp3";
		String json = WhatsappAudioRequest.builder()
				.from("Acme Corp").to("447900000001")
				.url(url).build().toJson();
		assertTrue(json.contains("\"audio\":{\"url\":\""+url+"\"}"));
		assertTrue(json.contains("\"message_type\":\"audio\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoUrl() {
		WhatsappAudioRequest.builder()
				.from("447900000001")
				.to("317900000002")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidExtension() {
		WhatsappAudioRequest.builder()
				.from("447900000001")
				.url("ftp://rel/path/to/track.wav")
				.to("317900000002")
				.build();
	}

	@Test
	public void testValidExtensions() {
		WhatsappAudioRequest.Builder builder = WhatsappAudioRequest.builder()
				.from("447900000001")
				.to("317900000002");

		String baseUrl = "file:///path/to/resource", url;
		for (String imageType : new String[]{"aac", "m4a", "amr", "mp3", "opus"}) {
			url = baseUrl + imageType;
			builder.url(url);
			assertEquals(url, builder.build().getAudio().getUrl());
		}
	}

	@Test
	public void testConstructUrlLength() {
		int limit = 2000;
		String baseUrl = "file:///path/to/folder", file = "song.mp3";
		WhatsappAudioRequest.Builder builder = WhatsappAudioRequest.builder()
				.from("317900000002").to("447900000001");

		try {
			builder.url("f:///a").build();
			fail("Expected exception for short URL");
		}
		catch (IllegalArgumentException ex) {
			assertEquals(22, builder.url(baseUrl).build().getAudio().getUrl().length());
		}

		StringBuilder sb = new StringBuilder(limit + 1);
		int paddingLength = (limit - 1) - (baseUrl.length() + file.length());
		sb.append(baseUrl);
		for (int i = 0; i < paddingLength; i++) {
			sb.append('x');
		}
		sb.append(file);
		assertEquals(limit - 1, sb.length());

		String overflow = sb.substring(0, sb.length() - file.length()) + "xy"+file;
		try {
			builder.url(overflow).build();
			fail("Expected exception for URL length");
		}
		catch (IllegalArgumentException ex) {
			assertEquals(limit + 1, overflow.length());
		}
	}
}
