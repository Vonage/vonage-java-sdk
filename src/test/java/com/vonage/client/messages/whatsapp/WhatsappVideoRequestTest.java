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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WhatsappVideoRequestTest {

	@Test
	public void testSerializeValid() {
		String url = "file:///path/to/clip.mp4", caption = "Cute kittens";
		String json = WhatsappVideoRequest.builder()
				.from("317900000002").to("447900000001").url(url).caption(caption).build().toJson();
		assertTrue(json.contains("\"video\":{\"url\":\""+url+"\",\"caption\":\""+caption+"\"}"));
		assertTrue(json.contains("\"message_type\":\"video\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testSerializeNoCaption() {
		String url = "file:///path/to/clip.mp4";
		String json = WhatsappVideoRequest.builder()
				.url(url).from("447900000002").to("447900000001")
				.build().toJson();
		assertTrue(json.contains("\"video\":{\"url\":\""+url+"\"}"));
		assertTrue(json.contains("\"message_type\":\"video\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testConstructNoUrl() {
		assertThrows(NullPointerException.class, () -> WhatsappVideoRequest.builder()
				.caption("Description").from("447900000001").to("317900000002").build()
		);
	}

	@Test
	public void testConstructInvalidExtension() {
		assertThrows(IllegalArgumentException.class, () -> WhatsappVideoRequest.builder()
				.from("447900000001").url("ftp://rel/path/to/video.mov").to("317900000002").build()
		);
	}

	@Test
	public void testConstructEmptyCaption() {
		assertThrows(IllegalArgumentException.class, () -> WhatsappVideoRequest.builder()
				.caption("").from("447900000001").url("ftp://rel/path/to/video.mp4").to("317900000002").build()
		);
	}

	@Test
	public void testValidExtensions() {
		WhatsappVideoRequest.Builder builder = WhatsappVideoRequest.builder()
				.from("447900000001").to("317900000002");

		String baseUrl = "file:///path/to/resource", url;
		for (String imageType : new String[]{"mp4", "3gpp"}) {
			url = baseUrl+'.'+imageType;
			builder.url(url);
			assertEquals(url, builder.build().getVideo().getUrl().toString());
		}
	}
}
