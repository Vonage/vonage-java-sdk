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

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.UUID;

public class CreateArchiveRequestTest {

	@Test
	public void testSerializeAllParams() {
		String sessionId = "flR1ZSBPY3QgMjkgMTI6MTM6MjMgUERUIDIwMTN";
		String name = "Test archive", multiArchiveTag = "DemoArchive_TagName";
		StreamCompositionLayout layout = StreamCompositionLayout.builder(ScreenLayoutType.VERTICAL).build();

		CreateArchiveRequest request = CreateArchiveRequest.builder(sessionId)
				.name(name).hasAudio(true).hasVideo(true)
				.resolution(Resolution.HD_LANDSCAPE)
				.outputMode(OutputMode.COMPOSED)
				.streamMode(StreamMode.AUTO).layout(layout)
				.multiArchiveTag(multiArchiveTag).build();

		String expectedJson = "{\"sessionId\":\""+sessionId+"\",\"name\":\""+name+"\",\"multiArchiveTag\":" +
			"\""+multiArchiveTag+"\",\"resolution\":\"1280x720\",\"outputMode\":\"composed\",\"streamMode\":" +
			"\"auto\",\"layout\":{\"type\":\"verticalPresentation\"},\"hasAudio\":true,\"hasVideo\":true}";

		assertEquals(expectedJson, request.toJson());
	}

	@Test
	public void testSerializeCustomLayout() {
		String style = "stream.instructor {position: absolute; width: 100%;  height:50%;}";
		StreamCompositionLayout layout = StreamCompositionLayout.builder(ScreenLayoutType.CUSTOM).stylesheet(style).build();

		CreateArchiveRequest request = CreateArchiveRequest.builder("s1")
				.hasAudio(false).resolution(Resolution.SD_LANDSCAPE)
				.streamMode(StreamMode.MANUAL).layout(layout)
				.outputMode(OutputMode.COMPOSED).build();

		String expectedJson = "{\"sessionId\":\"s1\",\"resolution\":\"640x480\",\"outputMode\":\"composed\"," +
				"\"streamMode\":\"manual\",\"layout\":{\"type\":\"custom\",\"stylesheet\":\""+style+"\"}," +
				"\"hasAudio\":false}";

		assertEquals(expectedJson, request.toJson());
	}

	@Test(expected = IllegalStateException.class)
	public void testConstructCustomLayoutOnNonComposedArchive() {
		CreateArchiveRequest.builder("sessionId")
				.layout(StreamCompositionLayout.builder(ScreenLayoutType.BEST_FIT).build())
				.outputMode(OutputMode.INDIVIDUAL).build();
	}

	@Test
	public void testConstructSessionIdOnly() {
		String sessionId = UUID.randomUUID().toString();
		String expectedJson = "{\"sessionId\":\""+sessionId+"\"}";
		assertEquals(expectedJson, CreateArchiveRequest.builder(sessionId).build().toJson());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNullSessionId() {
		CreateArchiveRequest.builder(null).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptySessionId() {
		CreateArchiveRequest.builder("").build();
	}
}
