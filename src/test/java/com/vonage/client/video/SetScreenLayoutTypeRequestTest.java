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
import static org.junit.Assert.assertThrows;
import org.junit.Test;

public class SetScreenLayoutTypeRequestTest {
	
	@Test
	public void testSerializeTypeOnly() {
		ArchiveLayout request = ArchiveLayout.builder().type(ScreenLayoutType.HORIZONTAL).build();
		assertEquals("{\"type\":\"horizontalPresentation\"}", request.toJson());
	}

	@Test
	public void testCustomTypeWithStylesheet() {
		String style = "layout.css";
		ArchiveLayout request = ArchiveLayout.builder()
				.type(ScreenLayoutType.CUSTOM).stylesheet(style).build();
		assertEquals("{\"type\":\"custom\",\"stylesheet\":\""+style+"\"}", request.toJson());
	}

	@Test
	public void testValidScreenshareType() {
		ArchiveLayout request = ArchiveLayout.builder()
				.type(ScreenLayoutType.BEST_FIT).screenshareType(ScreenLayoutType.PIP).build();
		assertEquals("{\"type\":\"bestFit\",\"screenshareType\":\"pip\"}", request.toJson());
	}

	@Test
	public void testConstructMissingType() {
		assertThrows(NullPointerException.class, () -> ArchiveLayout.builder().build());
		assertThrows(NullPointerException.class, () ->
				ArchiveLayout.builder().stylesheet("s.css").build()
		);
		assertThrows(NullPointerException.class, () ->
				ArchiveLayout.builder().screenshareType(ScreenLayoutType.BEST_FIT).build()
		);
	}

	@Test(expected = IllegalStateException.class)
	public void testInvalidScreenshareType() {
		ArchiveLayout.builder().type(ScreenLayoutType.VERTICAL).screenshareType(ScreenLayoutType.PIP).build();
	}

	@Test(expected = IllegalStateException.class)
	public void testMissingStylesheetWithCustomLayout() {
		ArchiveLayout.builder().type(ScreenLayoutType.CUSTOM).build();
	}

	@Test(expected = IllegalStateException.class)
	public void testRedundantStylesheet() {
		ArchiveLayout.builder().type(ScreenLayoutType.BEST_FIT).stylesheet("layout.css").build();
	}

	@Test(expected = IllegalStateException.class)
	public void testRedundantStylesheetWithScreenshareType() {
		ArchiveLayout.builder()
				.type(ScreenLayoutType.BEST_FIT)
				.stylesheet("layout.css")
				.screenshareType(ScreenLayoutType.CUSTOM)
				.build();
	}
}