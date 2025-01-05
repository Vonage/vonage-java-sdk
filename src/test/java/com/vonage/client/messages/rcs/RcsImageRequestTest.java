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
package com.vonage.client.messages.rcs;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class RcsImageRequestTest {

	@Test
	public void testSerializeValid() {
		String from = "447900000001", to = "317900000002",
				url = "https://foo.tld/path/to/image.jpeg";

		var rcs = RcsImageRequest.builder().from(from).url(url).to(to).build();

		String json = rcs.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"image\""));
		assertTrue(json.contains("\"channel\":\"rcs\""));
		assertTrue(json.contains("\"image\":{\"url\":\""+url+"\"}"));
	}

	@Test
	public void testConstructNoUrl() {
		assertThrows(NullPointerException.class, () -> RcsImageRequest.builder()
				.from("447900000001").to("317900000002").build()
		);
	}

	@Test
	public void testConstructInvalidExtension() {
		assertThrows(IllegalArgumentException.class, () -> RcsImageRequest.builder()
				.from("447900000001").to("317900000002").url("http://foo.tld/path/to/image.bmp").build()
		);
	}

	@Test
	public void testValidExtensions() {
		var builder = RcsImageRequest.builder().from("447900000001").to("317900000002");
		String baseUrl = "file:///path/to/resource", url;
		for (String imageType : new String[]{".jpeg", ".jpg", ".png"}) {
			url = baseUrl + imageType;
			builder.url(url);
			assertEquals(url, builder.build().getImage().getUrl().toString());
		}
	}
}
