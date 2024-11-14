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
package com.vonage.client.video;

import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.UUID;

public class ArchiveTest {

	@Test
	public void testSerializeAllParams() {
		String sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN";
		String name = "Test archive", multiArchiveTag = "DemoArchive_TagName";
		StreamCompositionLayout layout = StreamCompositionLayout.builder(ScreenLayoutType.VERTICAL).build();
		int maxBitrate = 321400;

		Archive request = Archive.builder(sessionId)
				.name(name).maxBitrate(maxBitrate)
				.hasAudio(true).hasVideo(true)
				.resolution(Resolution.HD_LANDSCAPE)
				.outputMode(OutputMode.COMPOSED)
				.streamMode(StreamMode.AUTO).layout(layout)
				.multiArchiveTag(multiArchiveTag).build();

		TestUtils.testJsonableBaseObject(request);
		String json = request.toJson();
		assertTrue(json.contains("\"name\":\""+name+"\""));
		assertTrue(json.contains("\"multiArchiveTag\":\""+multiArchiveTag+"\""));
		assertTrue(json.contains("\"sessionId\":\""+sessionId+"\""));
		assertTrue(json.contains("\"outputMode\":\"composed\""));
		assertTrue(json.contains("\"streamMode\":\"auto\""));
		assertTrue(json.contains("\"resolution\":\"1280x720\""));
		assertTrue(json.contains("\"layout\":{\"type\":\"verticalPresentation\"}"));
		assertTrue(json.contains("\"hasVideo\":true"));
		assertTrue(json.contains("\"hasAudio\":true"));
		assertTrue(json.contains("\"maxBitrate\":"+maxBitrate));
	}

	@Test
	public void testSerializeCustomLayout() {
		String style = "stream.instructor {position: absolute; width: 100%;  height:50%;}";
		StreamCompositionLayout layout = StreamCompositionLayout.builder(ScreenLayoutType.CUSTOM).stylesheet(style).build();

		Archive request = Archive.builder("s1")
				.hasAudio(false).resolution(Resolution.SD_PORTRAIT)
				.streamMode(StreamMode.MANUAL).layout(layout)
				.outputMode(OutputMode.COMPOSED).build();

		TestUtils.testJsonableBaseObject(request);
		String json = request.toJson();
		assertTrue(json.contains("\"outputMode\":\"composed\""));
		assertTrue(json.contains("\"streamMode\":\"manual\""));
		assertTrue(json.contains("\"sessionId\":\"s1\""));
		assertTrue(json.contains("\"resolution\":\"480x640\""));
		assertTrue(json.contains("\"hasAudio\":false"));
		assertTrue(json.contains("\"layout\":{\"type\":\"custom\",\"stylesheet\":\""+style+"\"}"));
	}

	@Test
	public void testConstructCustomLayoutOnNonComposedArchive() {
		assertThrows(IllegalStateException.class, () -> Archive.builder("sessionId")
				.layout(StreamCompositionLayout.builder(ScreenLayoutType.BEST_FIT).build())
				.outputMode(OutputMode.INDIVIDUAL).build()
		);
	}

	@Test
	public void testConstructSessionIdOnly() {
		String sessionId = UUID.randomUUID().toString();
		String expectedJson = "{\"sessionId\":\""+sessionId+"\"}";
		assertEquals(expectedJson, Archive.builder(sessionId).build().toJson());
	}

	@Test
	public void testConstructNullSessionId() {
		assertThrows(IllegalArgumentException.class, () -> Archive.builder(null).build());
	}

	@Test
	public void testConstructEmptySessionId() {
		assertThrows(IllegalArgumentException.class, () -> Archive.builder("").build());
	}

	@Test
	public void testMaxBitrateBounds() {
		var builder = Archive.builder("SESSION_ID");
		int min = 100000, max = 6000000;
		assertThrows(IllegalArgumentException.class, () -> builder.maxBitrate(min-1).build());
		assertEquals(min, builder.maxBitrate(min).build().getMaxBitrate());
		assertThrows(IllegalArgumentException.class, () -> builder.maxBitrate(max+1).build());
		assertEquals(max, builder.maxBitrate(max).build().getMaxBitrate());
	}

	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> Archive.fromJson("{malformed]"));
	}
}
