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
package com.vonage.client.video;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetScreenLayoutTypeRequestTest {
	
	@Test
	public void testSerializeTypeOnly() {
		StreamCompositionLayout request = StreamCompositionLayout.builder(ScreenLayoutType.HORIZONTAL).build();
		Assertions.assertEquals("{\"type\":\"horizontalPresentation\"}", request.toJson());
	}

	@Test
	public void testCustomTypeWithStylesheet() {
		String style = "layout.css";
		StreamCompositionLayout request = StreamCompositionLayout.builder(ScreenLayoutType.CUSTOM).stylesheet(style).build();
		Assertions.assertEquals("{\"type\":\"custom\",\"stylesheet\":\""+style+"\"}", request.toJson());
	}

	@Test
	public void testValidScreenshareType() {
		StreamCompositionLayout request = StreamCompositionLayout.builder(ScreenLayoutType.BEST_FIT)
				.screenshareType(ScreenLayoutType.PIP).build();
		Assertions.assertEquals("{\"type\":\"bestFit\",\"screenshareType\":\"pip\"}", request.toJson());
	}

	@Test
	public void testConstructMissingType() {
		assertThrows(NullPointerException.class, () -> StreamCompositionLayout.builder(null).build());
		assertThrows(NullPointerException.class, () ->
				StreamCompositionLayout.builder(null).stylesheet("s.css").build()
		);
		assertThrows(NullPointerException.class, () ->
				StreamCompositionLayout.builder(null).screenshareType(ScreenLayoutType.BEST_FIT).build()
		);
	}

	@Test
	public void testInvalidInitialAndScreenshareType() {
		assertThrows(IllegalStateException.class, () ->
				StreamCompositionLayout.builder(ScreenLayoutType.VERTICAL)
						.screenshareType(ScreenLayoutType.PIP).build()
		);
	}

	@Test
	public void testCustomScreenshareType() {
		assertThrows(IllegalArgumentException.class, () ->
				StreamCompositionLayout.builder(ScreenLayoutType.BEST_FIT)
						.screenshareType(ScreenLayoutType.CUSTOM).build()
		);
	}

	@Test
	public void testMissingStylesheetWithCustomLayout() {
		assertThrows(IllegalStateException.class, () ->
				StreamCompositionLayout.builder(ScreenLayoutType.CUSTOM).build()
		);
	}

	@Test
	public void testRedundantStylesheet() {
		assertThrows(IllegalStateException.class, () ->
				StreamCompositionLayout.builder(ScreenLayoutType.BEST_FIT).stylesheet("layout.css").build()
		);
	}

	@Test
	public void testRedundantStylesheetWithScreenshareType() {
		assertThrows(IllegalStateException.class, () ->
				StreamCompositionLayout.builder(ScreenLayoutType.BEST_FIT)
					.stylesheet("layout.css").screenshareType(ScreenLayoutType.BEST_FIT).build()
		);
	}
}