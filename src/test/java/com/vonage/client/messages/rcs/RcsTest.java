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
package com.vonage.client.messages.rcs;

import com.vonage.client.Jsonable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RcsTest {

	@Test
	public void testConstructorWithCategory() {
		String category = "transaction";
		Rcs rcs = new Rcs(category);
		assertEquals(category, rcs.getCategory());
		assertNull(rcs.getCardOrientation());
		assertNull(rcs.getImageAlignment());
		assertNull(rcs.getCardWidth());
	}

	@Test
	public void testBuilderAllFields() {
		String category = "authentication";
		CardOrientation orientation = CardOrientation.HORIZONTAL;
		ImageAlignment alignment = ImageAlignment.RIGHT;
		CardWidth width = CardWidth.MEDIUM;

		Rcs rcs = Rcs.builder()
			.category(category)
			.cardOrientation(orientation)
			.imageAlignment(alignment)
			.cardWidth(width)
			.build();

		assertEquals(category, rcs.getCategory());
		assertEquals(orientation, rcs.getCardOrientation());
		assertEquals(alignment, rcs.getImageAlignment());
		assertEquals(width, rcs.getCardWidth());
	}

	@Test
	public void testBuilderPartialFields() {
		CardOrientation orientation = CardOrientation.VERTICAL;
		CardWidth width = CardWidth.SMALL;

		Rcs rcs = Rcs.builder()
			.cardOrientation(orientation)
			.cardWidth(width)
			.build();

		assertNull(rcs.getCategory());
		assertEquals(orientation, rcs.getCardOrientation());
		assertNull(rcs.getImageAlignment());
		assertEquals(width, rcs.getCardWidth());
	}

	@Test
	public void testSerializeWithAllFields() {
		Rcs rcs = Rcs.builder()
			.category("promotion")
			.cardOrientation(CardOrientation.HORIZONTAL)
			.imageAlignment(ImageAlignment.LEFT)
			.cardWidth(CardWidth.SMALL)
			.build();

		String json = rcs.toJson();
		assertTrue(json.contains("\"category\":\"promotion\""));
		assertFalse(json.contains("trusted_recipient"));
		assertTrue(json.contains("\"card_orientation\":\"HORIZONTAL\""));
		assertTrue(json.contains("\"image_alignment\":\"LEFT\""));
		assertTrue(json.contains("\"card_width\":\"SMALL\""));
	}

	@Test
	public void testSerializeWithMinimalFields() {
		Rcs rcs = Rcs.builder()
			.cardOrientation(CardOrientation.VERTICAL)
			.build();

		String json = rcs.toJson();
		assertTrue(json.contains("\"card_orientation\":\"VERTICAL\""));
		assertFalse(json.contains("category"));
		assertFalse(json.contains("trusted_recipient"));
	}

	@Test
	public void testDeserializeWithAllFields() throws Exception {
		String json = "{\"category\":\"transaction\","
			+ "\"card_orientation\":\"HORIZONTAL\",\"image_alignment\":\"RIGHT\","
			+ "\"card_width\":\"MEDIUM\"}";

		Rcs rcs = Jsonable.fromJson(json, Rcs.class);
		assertNotNull(rcs);
		assertEquals("transaction", rcs.getCategory());
		assertEquals(CardOrientation.HORIZONTAL, rcs.getCardOrientation());
		assertEquals(ImageAlignment.RIGHT, rcs.getImageAlignment());
		assertEquals(CardWidth.MEDIUM, rcs.getCardWidth());
	}

	@Test
	public void testDeserializeWithPartialFields() throws Exception {
		String json = "{\"card_orientation\":\"VERTICAL\",\"card_width\":\"SMALL\"}";

		Rcs rcs = Jsonable.fromJson(json, Rcs.class);
		assertNotNull(rcs);
		assertNull(rcs.getCategory());
		assertEquals(CardOrientation.VERTICAL, rcs.getCardOrientation());
		assertNull(rcs.getImageAlignment());
		assertEquals(CardWidth.SMALL, rcs.getCardWidth());
	}

	@Test
	public void testEmptyRcs() {
		Rcs rcs = Rcs.builder().build();
		assertNull(rcs.getCategory());
		assertNull(rcs.getCardOrientation());
		assertNull(rcs.getImageAlignment());
		assertNull(rcs.getCardWidth());
	}
}
