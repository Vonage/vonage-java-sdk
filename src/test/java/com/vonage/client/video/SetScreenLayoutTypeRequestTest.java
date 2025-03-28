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
package com.vonage.client.video;

import com.vonage.client.TestUtils;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SetScreenLayoutTypeRequestTest {
	
	@Test
	public void testSerializeTypeOnly() {
		StreamCompositionLayout request = StreamCompositionLayout.standardLayout(ScreenLayoutType.HORIZONTAL);
		assertEquals("{\"type\":\"horizontalPresentation\"}", request.toJson());
		TestUtils.testJsonableBaseObject(request);
	}

	@Test
	public void testCustomTypeWithStylesheet() {
		String style = "layout.css";
		StreamCompositionLayout request = StreamCompositionLayout.customLayout(style);
		assertEquals("{\"type\":\"custom\",\"stylesheet\":\""+style+"\"}", request.toJson());
		TestUtils.testJsonableBaseObject(request);
	}

	@Test
	public void testValidScreenshareType() {
		StreamCompositionLayout request = StreamCompositionLayout.screenshareLayout(ScreenLayoutType.PIP);
		assertEquals("{\"type\":\"bestFit\",\"screenshareType\":\"pip\"}", request.toJson());
		TestUtils.testJsonableBaseObject(request);
	}

	@Test
	public void testConstructMissingType() {
		assertThrows(NullPointerException.class, () -> new StreamCompositionLayout.Builder(null).build());
		assertThrows(NullPointerException.class, () ->
				new StreamCompositionLayout.Builder(null).stylesheet("s.css").build()
		);
		assertThrows(NullPointerException.class, () ->
				new StreamCompositionLayout.Builder(null).screenshareType(ScreenLayoutType.BEST_FIT).build()
		);
	}

	@Test
	public void testInvalidInitialAndScreenshareType() {
		assertThrows(IllegalStateException.class, () ->
				new StreamCompositionLayout.Builder(ScreenLayoutType.VERTICAL)
						.screenshareType(ScreenLayoutType.PIP).build()
		);
	}

	@Test
	public void testCustomScreenshareType() {
		assertThrows(IllegalArgumentException.class, () ->
				new StreamCompositionLayout.Builder(ScreenLayoutType.BEST_FIT)
						.screenshareType(ScreenLayoutType.CUSTOM).build()
		);
	}

	@Test
	public void testMissingStylesheetWithCustomLayout() {
		assertThrows(IllegalStateException.class, () ->
				new StreamCompositionLayout.Builder(ScreenLayoutType.CUSTOM).build()
		);
	}

	@Test
	public void testRedundantStylesheet() {
		assertThrows(IllegalStateException.class, () ->
				new StreamCompositionLayout.Builder(ScreenLayoutType.BEST_FIT).stylesheet("layout.css").build()
		);
	}

	@Test
	public void testRedundantStylesheetWithScreenshareType() {
		assertThrows(IllegalStateException.class, () ->
				new StreamCompositionLayout.Builder(ScreenLayoutType.BEST_FIT)
					.stylesheet("layout.css").screenshareType(ScreenLayoutType.BEST_FIT).build()
		);
	}
}