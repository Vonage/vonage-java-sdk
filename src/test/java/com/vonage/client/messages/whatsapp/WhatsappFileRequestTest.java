/*
 *   Copyright 2024 Vonage
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
import java.util.UUID;

public class WhatsappFileRequestTest {
	String from = "447900000001", to = "317900000002";

	@Test
	public void testSerializeValid() {
		String messageUuid = UUID.randomUUID().toString();
		String url = "file:///path/to/attachment.zip", caption = "Srs bzns", name = "Stuff";
		String json = WhatsappFileRequest.builder()
				.from(from).to(to).url(url).contextMessageId(messageUuid)
				.caption(caption).name(name).build().toJson();

		assertTrue(json.contains(
				"\"file\":{\"url\":\""+url+"\",\"caption\":\"" + caption + "\",\"name\":\""+name+"\"}"
		));
		assertTrue(json.contains("\"context\":{\"message_uuid\":\""+messageUuid+"\"}"));
		assertTrue(json.contains("\"message_type\":\"file\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testSerializeNoCaptionOrName() {
		String url = "file:///path/to/spec.pdf";
		WhatsappFileRequest req = WhatsappFileRequest.builder().url(url).from(from).to(to).build();
		assertNull(req.getFile().getName());
		String json = req.toJson();
		assertTrue(json.contains("\"file\":{\"url\":\""+url+"\"}"));
		assertTrue(json.contains("\"message_type\":\"file\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testConstructNoUrl() {
		assertThrows(NullPointerException.class, () -> WhatsappFileRequest.builder()
				.caption("Description").from(from).to(to).build()
		);
	}
}
