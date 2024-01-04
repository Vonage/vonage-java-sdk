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
package com.vonage.client.messages.viber;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ViberVideoRequestTest {

	@Test
	public void testSerializeAllParams() {
		int duration = 91, fileSize = 67;
		String url = "file:///path/to/video.mp4", thumbUrl = "https://example.com/thumbnail.jpg";
		String caption = "Check this out!", json = ViberVideoRequest.builder()
				.from("Amy").to("447900000001")
				.duration(duration).fileSize(fileSize)
				.url(url).caption(caption).thumbUrl(thumbUrl)
				.build().toJson();

		assertTrue(json.contains("\"video\":{" +
				"\"url\":\""+url+"\",\"caption\":\""+caption+"\",\"thumb_url\":\""+thumbUrl+"\"}"
		));
		assertTrue(json.contains("\"message_type\":\"video\""));
		assertTrue(json.contains("\"channel\":\"viber_service\""));
		assertTrue(json.contains("\"from\":\"Amy\""));
		assertTrue(json.contains("\"to\":\"447900000001\""));
		assertTrue(json.contains("\"viber_service\":{\"duration\":"+duration+",\"file_size\":"+fileSize+"}"));
	}

	@Test
	public void testSerializeNoCaption() {
		String url = "ftp:///path/to/video.3gpp", thumbUrl = "http://example.com/thumbnail.jpeg";
		String json = ViberVideoRequest.builder()
				.from("Jo").to("447900000001")
				.duration(9).fileSize(20)
				.url(url).thumbUrl(thumbUrl)
				.build().toJson();
		assertTrue(json.contains("\"viber_service\":{\"duration\":9,\"file_size\":20}"));
		assertTrue(json.contains("\"video\":{\"url\":\""+url+"\",\"thumb_url\":\""+thumbUrl+"\"}"));
		assertTrue(json.contains("\"message_type\":\"video\""));
		assertTrue(json.contains("\"channel\":\"viber_service\""));
		assertTrue(json.contains("\"from\":\"Jo\""));
		assertTrue(json.contains("\"to\":\"447900000001\""));
	}

	@Test
	public void testConstructNoUrl() {
		assertThrows(NullPointerException.class, () -> ViberVideoRequest.builder()
				.from("447900000001").to("317900000002")
				.thumbUrl("https://example.com/thumb.jpg")
				.duration(10).fileSize(10).build()
		);
	}

	@Test
	public void testConstructNoThumbnailUrl() {
		assertThrows(NullPointerException.class, () -> ViberVideoRequest.builder()
				.from("447900000001").to("317900000002")
				.url("https://example.com/clip.mp4")
				.duration(10).fileSize(10).build()
		);
	}

	@Test
	public void testConstructInvalidUrlExtension() {
		assertThrows(IllegalArgumentException.class, () -> ViberVideoRequest.builder()
				.from("447900000001").thumbUrl("https://example.com/preview.jpg")
				.url("ftp://rel/path/to/clip.flv").to("317900000002")
				.duration(10).fileSize(10).build()
		);
	}

	@Test
	public void testConstructInvalidThumbnailExtension() {
		assertThrows(IllegalArgumentException.class, () -> ViberVideoRequest.builder()
				.from("447900000001").thumbUrl("https://example.com/preview.eps")
				.url("ftp://rel/path/to/clip.mp4").to("317900000002")
				.duration(10).fileSize(10).build()
		);
	}

	@Test
	public void testDurationBounds() {
		ViberVideoRequest.Builder builder = ViberVideoRequest.builder()
				.from("447900000001").thumbUrl("https://example.com/preview.jpg")
				.url("ftp://rel/path/to/clip.3gpp").to("317900000002").fileSize(10);

		assertThrows(NullPointerException.class, builder::build);
		assertThrows(IllegalArgumentException.class, () -> builder.duration(0).build());
		assertThrows(IllegalArgumentException.class, () -> builder.duration(601).build());
		assertEquals(1, builder.duration(1).build().getViberService().getDuration().intValue());
		assertEquals(600, builder.duration(600).build().getViberService().getDuration().intValue());
	}

	@Test
	public void testFileSizeBounds() {
		ViberVideoRequest.Builder builder = ViberVideoRequest.builder()
				.from("447900000001").thumbUrl("https://example.com/preview.jpg")
				.url("ftp://rel/path/to/clip.3gpp").to("317900000002").duration(10);

		assertThrows(NullPointerException.class, builder::build);
		assertThrows(IllegalArgumentException.class, () -> builder.fileSize(0).build());
		assertThrows(IllegalArgumentException.class, () -> builder.fileSize(201).build());
		assertEquals(1, builder.fileSize(1).build().getViberService().getFileSize().intValue());
		assertEquals(200, builder.fileSize(200).build().getViberService().getFileSize().intValue());
	}
}
