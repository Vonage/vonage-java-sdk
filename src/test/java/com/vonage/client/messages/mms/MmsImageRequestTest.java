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
package com.vonage.client.messages.mms;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class MmsImageRequestTest {

	@Test
	public void testSerializeValid() {
		String from = "447900000001", to = "317900000002",
				url = "https://foo.tld/path/to/image.jpeg",
				caption = "Alt text";

		MmsImageRequest mms = MmsImageRequest.builder()
				.from(from).caption(caption).url(url).to(to)
				.build();

		String json = mms.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"image\""));
		assertTrue(json.contains("\"channel\":\"mms\""));
		assertTrue(json.contains("\"image\":{\"url\":\""+url+"\",\"caption\":\""+caption+"\"}"));
	}

	@Test
	public void testSerializeNoCaption() {
		String from = "447900000001", to = "317900000002",
				url = "https://foo.tld/path/to/image.jpeg";

		MmsImageRequest mms = MmsImageRequest.builder()
				.from(from).url(url).to(to).build();

		String json = mms.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"image\""));
		assertTrue(json.contains("\"channel\":\"mms\""));
		assertTrue(json.contains("\"image\":{\"url\":\""+url+"\"}"));
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoUrl() {
		MmsImageRequest.builder()
				.caption("Description")
				.from("447900000001")
				.to("317900000002")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidExtension() {
		MmsImageRequest.builder()
				.from("447900000001")
				.to("317900000002")
				.url("http://foo.tld/path/to/image.bmp")
				.build();
	}

	@Test
	public void testValidExtensions() {
		MmsImageRequest.Builder builder = MmsImageRequest.builder()
				.from("447900000001")
				.to("317900000002");

		String baseUrl = "file:///path/to/resource", url;
		for (String imageType : new String[]{"jpeg", "jpg", "png", "gif"}) {
			url = baseUrl + imageType;
			builder.url(url);
			assertEquals(url, builder.build().getImage().getUrl());
		}
	}

	@Test
	public void testConstructCaptionLength() {
		MmsImageRequest.Builder builder = MmsImageRequest.builder()
				.url("file:///path/to/picture.png").from("317900000002").to("447900000001");

		assertEquals(12, builder.build().getTo().length());

		try {
			builder.caption("").build();
			fail("Expected exception for empty caption");
		}
		catch (IllegalArgumentException ex) {
			assertEquals("V", builder.caption("V").build().getImage().getCaption());
		}

		StringBuilder sb = new StringBuilder(2001);
		for (int i = 0; i < 1999; i++) {
			sb.append('*');
		}
		assertEquals(1999, sb.length());

		assertEquals(sb.toString(), builder.caption(sb.toString()).build().getImage().getCaption());
		try {
			builder.caption(sb.append("xy").toString()).build();
			fail("Expected exception for caption length");
		}
		catch (IllegalArgumentException ex) {
			assertEquals(2001, sb.length());
		}
	}
}
