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
package com.vonage.client.messages.messenger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessengerVideoRequestTest {

	@Test
	public void testSerializeValid() {
		String url = "file:///path/to/video.mp4";
		String json = MessengerVideoRequest.builder()
				.from("alice").to("robert").url(url)
				.build().toJson();
		assertTrue(json.contains("\"video\":{\"url\":\""+url+"\"}"));
		assertTrue(json.contains("\"message_type\":\"video\""));
		assertTrue(json.contains("\"channel\":\"messenger\""));
	}

	@Test
	public void testConstructNoUrl() {
		assertThrows(NullPointerException.class, () -> MessengerVideoRequest.builder()
				.from("447900000001").to("317900000002").build()
		);
	}

	@Test
	public void testConstructInvalidExtension() {
		assertThrows(IllegalArgumentException.class, () -> MessengerVideoRequest.builder()
				.from("447900000001").url("ftp://rel/path/to/trailer.bik").to("317900000002").build()
		);
	}
}
