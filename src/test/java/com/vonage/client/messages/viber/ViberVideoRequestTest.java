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
package com.vonage.client.messages.viber;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ViberVideoRequestTest {

	@Test
	public void testSerializeAllParams() {
		String url = "file:///path/to/video.mp4", thumbUrl = "https://example.com/thumbnail.jpg";
		String caption = "Check this out!";
		String json = ViberVideoRequest.builder()
				.from("Amy").to("447900000001")
				.url(url).caption(caption).thumbUrl(thumbUrl)
				.build().toJson();
		assertTrue(json.contains("\"video\":{" +
				"\"url\":\""+url+"\",\"caption\":\""+caption+"\",\"thumb_url\":\""+thumbUrl+"\"}"
		));
		assertTrue(json.contains("\"message_type\":\"video\""));
		assertTrue(json.contains("\"channel\":\"viber_service\""));
		assertTrue(json.contains("\"from\":\"Amy\""));
		assertTrue(json.contains("\"to\":\"447900000001\""));
	}

	@Test
	public void testSerializeNoCaption() {
		String url = "ftp:///path/to/video.3gpp", thumbUrl = "http://example.com/thumbnail.jpeg";
		String json = ViberVideoRequest.builder()
				.from("Jo").to("447900000001")
				.url(url).thumbUrl(thumbUrl)
				.build().toJson();
		assertTrue(json.contains("\"video\":{\"url\":\""+url+"\",\"thumb_url\":\""+thumbUrl+"\"}"));
		assertTrue(json.contains("\"message_type\":\"video\""));
		assertTrue(json.contains("\"channel\":\"viber_service\""));
		assertTrue(json.contains("\"from\":\"Jo\""));
		assertTrue(json.contains("\"to\":\"447900000001\""));
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoUrl() {
		ViberVideoRequest.builder()
				.from("447900000001").to("317900000002")
				.thumbUrl("https://example.com/thumb.jpg")
				.build();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoThumbnailUrl() {
		ViberVideoRequest.builder()
				.from("447900000001").to("317900000002")
				.url("https://example.com/clip.mp4")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidUrlExtension() {
		ViberVideoRequest.builder()
				.from("447900000001")
				.thumbUrl("https://example.com/preview.jpg")
				.url("ftp://rel/path/to/clip.flv")
				.to("317900000002")
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidThumbnailExtension() {
		ViberVideoRequest.builder()
				.from("447900000001")
				.thumbUrl("https://example.com/preview.eps")
				.url("ftp://rel/path/to/clip.mp4")
				.to("317900000002")
				.build();
	}
}
