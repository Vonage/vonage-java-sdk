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
package com.vonage.client.video;

import com.vonage.client.VonageUnexpectedException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.UUID;

public class ArchiveTest {

	@Test
	public void testSerializeAllParams() {
		String sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN";
		String name = "Test archive", multiArchiveTag = "DemoArchive_TagName";
		StreamCompositionLayout layout = StreamCompositionLayout.builder(ScreenLayoutType.VERTICAL).build();

		Archive request = Archive.builder(sessionId)
				.name(name).hasAudio(true).hasVideo(true)
				.resolution(Resolution.HD_LANDSCAPE)
				.outputMode(OutputMode.COMPOSED)
				.streamMode(StreamMode.AUTO).layout(layout)
				.multiArchiveTag(multiArchiveTag).build();

		String expectedJson = "{\"name\":\""+name+"\",\"multiArchiveTag\":\""+multiArchiveTag +
				"\",\"outputMode\":\"composed\",\"sessionId\":\""+sessionId+"\",\"streamMode\":\"auto\"," +
				"\"resolution\":\"1280x720\",\"layout\":{\"type\":\"verticalPresentation\"}," +
				"\"hasVideo\":true,\"hasAudio\":true}";

		assertEquals(expectedJson, request.toJson());
	}

	@Test
	public void testSerializeCustomLayout() {
		String style = "stream.instructor {position: absolute; width: 100%;  height:50%;}";
		StreamCompositionLayout layout = StreamCompositionLayout.builder(ScreenLayoutType.CUSTOM).stylesheet(style).build();

		Archive request = Archive.builder("s1")
				.hasAudio(false).resolution(Resolution.SD_PORTRAIT)
				.streamMode(StreamMode.MANUAL).layout(layout)
				.outputMode(OutputMode.COMPOSED).build();

		String expectedJson = "{\"outputMode\":\"composed\",\"sessionId\":\"s1\",\"streamMode\":\"manual\"," +
				"\"resolution\":\"480x640\",\"layout\":{\"type\":\"custom\",\"stylesheet\":\""+style+"\"}," +
				"\"hasAudio\":false}";

		assertEquals(expectedJson, request.toJson());
	}

	@Test(expected = IllegalStateException.class)
	public void testConstructCustomLayoutOnNonComposedArchive() {
		Archive.builder("sessionId")
				.layout(StreamCompositionLayout.builder(ScreenLayoutType.BEST_FIT).build())
				.outputMode(OutputMode.INDIVIDUAL).build();
	}

	@Test
	public void testConstructSessionIdOnly() {
		String sessionId = UUID.randomUUID().toString();
		String expectedJson = "{\"sessionId\":\""+sessionId+"\"}";
		assertEquals(expectedJson, Archive.builder(sessionId).build().toJson());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullSessionId() {
		Archive.builder(null).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptySessionId() {
		Archive.builder("").build();
	}

	@Test(expected = VonageUnexpectedException.class)
	public void testFromJsonInvalid() {
		Archive.fromJson("{malformed]");
	}

	@Test(expected = VonageUnexpectedException.class)
	public void testUpdateFromJsonInvalid() {
		new Archive().updateFromJson("");
	}
}
