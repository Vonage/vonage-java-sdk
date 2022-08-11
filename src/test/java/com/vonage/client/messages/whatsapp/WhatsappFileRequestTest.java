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
import static org.junit.Assert.assertTrue;

public class WhatsappFileRequestTest {

	@Test
	public void testSerializeValid() {
		String url = "file:///path/to/attachment.zip", caption = "Srs bzns";
		String json = WhatsappFileRequest.builder()
				.from("317900000002").to("447900000001")
				.url(url).caption(caption)
				.build().toJson();
		assertTrue(json.contains("\"file\":{\"url\":\""+url+"\",\"caption\":\""+caption+"\"}"));
		assertTrue(json.contains("\"message_type\":\"file\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testSerializeNoCaption() {
		String url = "file:///path/to/spec.pdf";
		String json = WhatsappFileRequest.builder()
				.url(url).from("447900000002").to("447900000001")
				.build().toJson();
		assertTrue(json.contains("\"file\":{\"url\":\""+url+"\"}"));
		assertTrue(json.contains("\"message_type\":\"file\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoUrl() {
		WhatsappFileRequest.builder()
				.caption("Description")
				.from("447900000001")
				.to("317900000002")
				.build();
	}
}
