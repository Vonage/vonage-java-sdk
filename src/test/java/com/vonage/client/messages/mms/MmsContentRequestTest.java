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
package com.vonage.client.messages.mms;

import com.vonage.client.messages.MessageType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.util.List;

public class MmsContentRequestTest {
	private final String from = "447900000001", to = "317900000002",
			fileUrl = "https://foo.tld/path/to/file.pdf",
			imageUrl = "https://foo.tld/path/to/image.jpg",
			videoUrl = "https://foo.tld/path/to/video.mp4",
			audioUrl = "https://foo.tld/path/to/audio.mp3",
			vcardUrl = "https://foo.tld/path/to/vcard.vcf",
			caption = "Please see the attached ";

	@Test
	public void testSerializeAllMessageTypes() {
		var mms = MmsContentRequest.builder()
				.from(from).to(to).addContent(
						new Content(MessageType.FILE, fileUrl, caption + "file."),
						new Content(MessageType.IMAGE, imageUrl, caption + "image."),
						new Content(MessageType.VIDEO, videoUrl, caption + "video."),
						new Content(MessageType.AUDIO, audioUrl, caption + "audio."),
						new Content(MessageType.VCARD, vcardUrl)
				)
				.build();

		String json = mms.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"content\""));
		assertTrue(json.contains("\"channel\":\"mms\""));
		assertTrue(json.contains("\"content\":[" +
				"{\"type\":\"file\",\"url\":\""+fileUrl+"\",\"caption\":\""+caption+"file.\"}," +
				"{\"type\":\"image\",\"url\":\""+imageUrl+"\",\"caption\":\""+caption+"image.\"}," +
				"{\"type\":\"video\",\"url\":\""+videoUrl+"\",\"caption\":\""+caption+"video.\"}," +
				"{\"type\":\"audio\",\"url\":\""+audioUrl+"\",\"caption\":\""+caption+"audio.\"}," +
				"{\"type\":\"vcard\",\"url\":\""+vcardUrl+"\"}]"
		));
	}

	@Test
	public void testContentsReplacedIfListIsProvided() {
		var mms = MmsContentRequest.builder()
				.from(from).to(to).addContent(
						new Content(MessageType.FILE, fileUrl, caption + "file."),
						new Content(MessageType.IMAGE, imageUrl, caption + "image.")
				)
				.contents(List.of(
						new Content(MessageType.VIDEO, videoUrl, caption + "video."),
						new Content(MessageType.AUDIO, audioUrl, caption + "audio."),
						new Content(MessageType.VCARD, vcardUrl)
				))
				.build();

		String json = mms.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"content\""));
		assertTrue(json.contains("\"channel\":\"mms\""));
		assertTrue(json.contains("\"content\":[" +
				"{\"type\":\"video\",\"url\":\""+videoUrl+"\",\"caption\":\""+caption+"video.\"}," +
				"{\"type\":\"audio\",\"url\":\""+audioUrl+"\",\"caption\":\""+caption+"audio.\"}," +
				"{\"type\":\"vcard\",\"url\":\""+vcardUrl+"\"}]"
		));
	}

	@Test
	public void testConstructNoContent() {
		assertThrows(IllegalArgumentException.class, () -> MmsContentRequest.builder().from(from).to(to).build());
	}

	@Test
	public void testConstructEmptyContent() {
		assertThrows(IllegalArgumentException.class, () -> MmsContentRequest.builder()
				.from(from).to(to).addContent().build());
	}

	@Test
	public void testBuilderListReferenceIsReplace() {
		assertThrows(UnsupportedOperationException.class, () -> MmsContentRequest.builder()
				.from(from).to(to)
				.contents(List.of(new Content(MessageType.VCARD, URI.create(vcardUrl))))
				.addContent(new Content(MessageType.IMAGE, URI.create(imageUrl), caption)).build()
		);
	}
}
