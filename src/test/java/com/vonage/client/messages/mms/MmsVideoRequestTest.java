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

import static org.junit.Assert.assertTrue;

public class MmsVideoRequestTest {

	@Test
	public void testSerializeValid() {
		String from = "447900000001", to = "317900000002",
				url = "https://foo.tld/path/to/video.mp4",
				caption = "Alt text";

		MmsVideoRequest mms = MmsVideoRequest.builder()
				.from(from).caption(caption).url(url).to(to)
				.build();

		String json = mms.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"video\""));
		assertTrue(json.contains("\"channel\":\"mms\""));
		assertTrue(json.contains("\"video\":{\"url\":\""+url+"\",\"caption\":\""+caption+"\"}"));
	}

	@Test
	public void testSerializeNoCaption() {
		String from = "447900000001", to = "317900000002",
				url = "https://foo.tld/path/to/video.flv";

		MmsVideoRequest mms = MmsVideoRequest.builder()
				.from(from).url(url).to(to).build();

		String json = mms.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"video\""));
		assertTrue(json.contains("\"channel\":\"mms\""));
		assertTrue(json.contains("\"video\":{\"url\":\""+url+"\"}"));
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoUrl() {
		MmsVideoRequest.builder()
				.from("447900000001")
				.to("317900000002")
				.build();
	}
}