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
package com.vonage.client.messages.whatsapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BsuidTest {

	@Test
	public void testValidBsuid() {
		String bsuid = "US.13491208655302741918";
		assertEquals(bsuid, new Bsuid(bsuid).toString());
		assertTrue(Bsuid.isValid(bsuid));
	}

	@Test
	public void testValidParentBsuid() {
		String parent = "US.ENT.11815799212886844830";
		assertEquals(parent, new Bsuid(parent).toString());
		assertTrue(Bsuid.isValid(parent));
	}

	@Test
	public void testAlphanumericIdentifier() {
		String bsuid = "GB.ab12CD34ef56";
		assertEquals(bsuid, new Bsuid(bsuid).toString());
		assertTrue(Bsuid.isValid(bsuid));
	}

	@Test
	public void testMaxLengthIdentifier() {
		String id = "A".repeat(128);
		String bsuid = "US." + id;
		assertEquals(bsuid, new Bsuid(bsuid).toString());
		assertTrue(Bsuid.isValid(bsuid));
	}

	@Test
	public void testTooLongIdentifier() {
		String bsuid = "US." + "A".repeat(129);
		assertFalse(Bsuid.isValid(bsuid));
		assertThrows(IllegalArgumentException.class, () -> new Bsuid(bsuid));
	}

	@Test
	public void testNullIsInvalid() {
		assertFalse(Bsuid.isValid(null));
		assertThrows(NullPointerException.class, () -> new Bsuid(null));
	}

	@Test
	public void testMalformedIdentifiers() {
		String[] invalid = {
				"",
				"13491208655302741918",          // missing country prefix
				"USA.13491208655302741918",      // 3-letter prefix
				"U.13491208655302741918",        // 1-letter prefix
				"US.",                            // missing identifier
				"US..123",                        // empty segment
				"US.abc-123",                     // non-alphanumeric
				"US.ENT.",                        // parent with empty identifier
				"447700900000"                    // phone number, not a BSUID
		};
		for (String candidate : invalid) {
			assertFalse(Bsuid.isValid(candidate), "Expected invalid: " + candidate);
			assertThrows(IllegalArgumentException.class, () -> new Bsuid(candidate),
					"Expected exception for: " + candidate);
		}
	}
}
