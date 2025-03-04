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
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
				.from(from).to(to)
				.addFile(fileUrl, caption + "file.")
				.addImage(imageUrl, caption + "image.")
				.addVideo(videoUrl, caption + "video.")
				.addAudio(audioUrl, caption + "audio.")
				.addVcard(vcardUrl, caption + "vCard.").build();

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
				"{\"type\":\"vcard\",\"url\":\""+vcardUrl+"\",\"caption\":\""+caption+"vCard.\"}]"
		));
	}

	@Test
	public void testContentsReplacedIfListIsProvided() {
		var mms = MmsContentRequest.builder()
				.from(from).to(to)
				.addImage(imageUrl)
				.addFile(fileUrl)
				.addVideo(videoUrl)
				.addAudio(audioUrl)
				.contents(List.of(
						new Content(MessageType.VIDEO, videoUrl),
						new Content(MessageType.AUDIO, audioUrl),
						new Content(MessageType.VCARD, vcardUrl)
				))
				.build();

		String json = mms.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"content\""));
		assertTrue(json.contains("\"channel\":\"mms\""));
		assertTrue(json.contains("\"content\":[" +
				"{\"type\":\"video\",\"url\":\""+videoUrl+"\"}," +
				"{\"type\":\"audio\",\"url\":\""+audioUrl+"\"}," +
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
				.contents(List.of(new Content(MessageType.VCARD, vcardUrl)))
				.addContent(new Content(MessageType.IMAGE, imageUrl, caption)).build()
		);
	}

	@Test
	public void testInvalidMediaType() {
		assertThrows(IllegalArgumentException.class, () -> MmsContentRequest.builder()
				.addContent(new Content(MessageType.TEXT, videoUrl, caption))
				.from(from).to(to).build()
		);
	}

	@Test
	public void testContentInvalidUrl() {
		assertThrows(IllegalArgumentException.class, () -> new Content(MessageType.VIDEO, "", caption));
		assertThrows(IllegalArgumentException.class, () ->
				new Content(MessageType.VCARD, "foo://-==;]{=.tld/contact.vcf", caption)
		);
		assertThrows(NullPointerException.class, () -> new Content(MessageType.IMAGE, null));
		assertThrows(NullPointerException.class, () -> new Content(MessageType.FILE, null, caption));
	}

	@Test
	public void testContentCaptionLength() {
		var content = new Content(MessageType.IMAGE, imageUrl, caption);
		assertEquals(caption, content.getCaption());
		assertThrows(IllegalArgumentException.class, () -> new Content(MessageType.IMAGE, imageUrl, ""));

		StringBuilder sb = new StringBuilder(2001);
		sb.append("*".repeat(1999));
		assertEquals(1999, sb.length());

		var sbStr = sb.toString();
		assertEquals(sbStr, new Content(MessageType.IMAGE, imageUrl, sbStr).getCaption());
		try {
			new Content(MessageType.IMAGE, imageUrl, sb.append("xy").toString());
			fail("Expected exception for caption length");
		}
		catch (IllegalArgumentException ex) {
			assertEquals(2001, sb.length());
		}
	}
}
