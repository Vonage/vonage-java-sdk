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
package com.vonage.client.verify;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Locale;
import java.util.Map;

public class VerifyRequestTest {

	@Test
	public void testPinLengthBoundaries() {
		VerifyRequest.Builder builder = VerifyRequest.builder("4477990090090", "Brand.com")
				.senderId("Your friend").length(4).locale(new Locale("en", "gb"));

		assertEquals(4, builder.length(4).build().getLength().intValue());
		assertEquals(6, builder.length(6).build().getLength().intValue());
		assertThrows(IllegalArgumentException.class, () -> builder.length(5).build());
		builder.length(null);
		assertThrows(IllegalArgumentException.class, () -> builder.pinCode("123").build());
		assertThrows(IllegalArgumentException.class, () -> builder.pinCode("A1234567890").build());
		assertEquals(4, builder.pinCode("abcd").build().getPinCode().length());
		assertEquals(10, builder.pinCode("0123456789").build().getPinCode().length());
	}

	@Test
	public void testConstructVerifyParamsMissingValues() {
		VerifyRequest verifyRequest = VerifyRequest.builder("4477990090090", "Brand.com").build();

		Map<String, ?> params = verifyRequest.makeParams();
		assertEquals(2, params.size());
		assertEquals("4477990090090", params.get("number"));
		assertEquals("Brand.com", params.get("brand"));

		assertNull(verifyRequest.getLength());
		assertNull(verifyRequest.getLocale());
		assertNull(verifyRequest.getFrom());
		assertNull(verifyRequest.getCountry());
		assertNull(verifyRequest.getPinExpiry());
		assertNull(verifyRequest.getPinCode());
		assertNull(verifyRequest.getNextEventWait());
		assertNull(verifyRequest.getWorkflow());
	}

	@Test
	public void testInvalidBrand() {
		String number = "4477990090090";
		assertThrows(IllegalArgumentException.class, () ->
				VerifyRequest.builder(number, "A very looooooooooooooong brand name").build()
		);
		assertThrows(NullPointerException.class, () -> VerifyRequest.builder(number, null).build());
	}
}
