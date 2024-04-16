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

import static com.vonage.client.TestUtils.testJsonableBaseObject;
import static com.vonage.client.video.VideoClientTest.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RenderRequestTest {

	@Test
	public void testSerializeAllParams() {
		String url = "http://example.org/render", name = "Experience Composer/Conductor/Pianist/Orchestra";
		var request = RenderRequest.builder()
				.token(token).url(url).name(name)
				.sessionId(sessionId).maxDuration(9000)
				.resolution(Resolution.SD_PORTRAIT).build();

		var expectedJson = "{\"sessionId\":\""+sessionId+"\",\"token\":\""+token+"\",\"url\":\""+url +
				"\",\"maxDuration\":9000,\"resolution\":\"480x640\",\"properties\":{\"name\":\""+name+"\"}}";

		assertEquals(expectedJson, request.toJson());
		testJsonableBaseObject(request);
	}

	@Test
	public void testSerializeRequiredParams() {
		var request = RenderRequest.builder().token(token)
				.sessionId(sessionId).url(renderUrl).name(renderName).build();

		var expectedJson = "{\"sessionId\":\""+sessionId+"\",\"token\":\""+token+"\"," +
				"\"url\":\""+renderUrl+"\",\"properties\":{\"name\":\""+renderName+"\"}}";

		assertEquals(expectedJson, request.toJson());
		testJsonableBaseObject(request);
	}

	@Test
	public void testConstructMissingSessionId() {
		assertThrows(NullPointerException.class, () -> RenderRequest.builder()
				.url(renderUrl).name(renderName).token(token).build()
		);
	}

	@Test
	public void testConstructMissingToken() {
		assertThrows(NullPointerException.class, () -> RenderRequest.builder()
				.url(renderUrl).name(renderName).sessionId(sessionId).build()
		);
	}

	@Test
	public void testConstructMissingName() {
		assertThrows(IllegalArgumentException.class, () -> RenderRequest.builder()
				.token(token).url(renderUrl).sessionId(sessionId).build()
		);
	}

	@Test
	public void testConstructMissingUrl() {
		assertThrows(NullPointerException.class, () -> RenderRequest.builder()
				.token(token).name(renderName).sessionId(sessionId).build()
		);
	}

	@Test
	public void testUrlLength() {
		var builder = RenderRequest.builder().sessionId(sessionId).token(token).name(renderName);
		String fourteen = "https://t.co/b";
		assertEquals(14, fourteen.length());
		assertThrows(IllegalArgumentException.class, () -> builder.url(fourteen).build());
		assertEquals(15, builder.url(fourteen + 'a').build().getUrl().toString().length());
		String twoThousandAndFourtySeven = fourteen + "lahb".repeat(508) + 'x';
		assertEquals(2047, twoThousandAndFourtySeven.length());
		assertEquals(2048, builder.url(twoThousandAndFourtySeven + 'y').build().getUrl().toString().length());
		assertThrows(IllegalArgumentException.class, () -> builder.url(twoThousandAndFourtySeven + "yz").build());
	}

	@Test
	public void testNameLength() {
		var builder = RenderRequest.builder().sessionId(sessionId).token(token).url(renderUrl);
		assertThrows(IllegalArgumentException.class, () -> builder.name("  ").build());
		assertEquals(1, builder.name("A").build().getProperties().getName().length());
		String twoHundred = "Felicity S".repeat(20);
		assertEquals(200, twoHundred.length());
		assertEquals(twoHundred, builder.name(twoHundred).build().getProperties().getName());
		assertThrows(IllegalArgumentException.class, () -> builder.name(twoHundred + 'A').build());
	}

	@Test
	public void testMaxDurationBounds() {
		var builder = RenderRequest.builder().name(renderName).sessionId(sessionId).token(token).url(renderUrl);
		int min = 60, max = 36_000;
		assertEquals(min, builder.maxDuration(min).build().getMaxDuration());
		assertEquals(max, builder.maxDuration(max).build().getMaxDuration());
		assertThrows(IllegalArgumentException.class, () -> builder.maxDuration(min-1).build());
		assertThrows(IllegalArgumentException.class, () -> builder.maxDuration(max+1).build());
	}

	@Test
	public void testSupportedResolutions() {
		var builder = RenderRequest.builder().sessionId(sessionId).token(token).name(renderName).url(renderUrl);
		for (var resolution : Resolution.values()) {
			builder.resolution(resolution);
			switch (resolution) {
				case HD_LANDSCAPE: case HD_PORTRAIT:
				case SD_LANDSCAPE: case SD_PORTRAIT:
					assertEquals(resolution, builder.build().getResolution());
					break;
				case FHD_LANDSCAPE: case FHD_PORTRAIT:
					assertThrows(IllegalArgumentException.class, builder::build);
					break;
			}
		}
	}

	@Test
	public void testUnknownRenderStatus() {
		assertNull(RenderStatus.fromString("rendering"));
	}
}
