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

public class WhatsappFileRequestTest {

	@Test
	public void testSerializeValid() {
		String url = "file:///path/to/attachment.zip", caption = "Srs bzns", name = "Stuff";
		String json = WhatsappFileRequest.builder()
				.from("317900000002").to("447900000001")
				.url(url).caption(caption).name(name)
				.build().toJson();
		assertTrue(json.contains(
				"\"file\":{\"url\":\""+url+ "\",\"caption\":\""+caption+"\",\"name\":\""+name+"\"}"
		));
		assertTrue(json.contains("\"message_type\":\"file\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testSerializeNoCaptionOrName() {
		String url = "file:///path/to/spec.pdf";
		String json = WhatsappFileRequest.builder()
				.url(url).from("447900000002").to("447900000001").build().toJson();
		assertTrue(json.contains("\"file\":{\"url\":\""+url+"\"}"));
		assertTrue(json.contains("\"message_type\":\"file\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testConstructNoUrl() {
		assertThrows(NullPointerException.class, () -> WhatsappFileRequest.builder()
				.caption("Description").from("447900000001").to("317900000002").build()
		);
	}
}
