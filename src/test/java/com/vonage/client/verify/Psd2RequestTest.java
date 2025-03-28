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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Locale;

public class Psd2RequestTest {

	@Test
	public void testToStringAllParameters() {
		Psd2Request request = Psd2Request.builder("447700900999", 10.31, "Ebony")
				.workflow(Workflow.SMS)
				.length(4)
				.locale(Locale.UK)
				.country("GB")
				.nextEventWait(60)
				.pinExpiry(1800)
				.build();

		String expected = "Psd2Request{number='447700900999', length=4, locale=en_GB, country='GB', pinExpiry=1800, " +
				"nextEventWait=60, amount=10.31, payee='Ebony', workflow=SMS}";

		assertEquals(expected, request.toString());
	}

	@Test
	public void testToStringRequiredParameters() {
		Psd2Request request = Psd2Request.builder("447700900999", 10.31, "Ebony").build();

		String expected = "Psd2Request{number='447700900999', length=null, locale=null, country='null', " +
				"pinExpiry=null, nextEventWait=null, amount=10.31, payee='Ebony', workflow=null}";

		assertEquals(expected, request.toString());
	}

	@Test
	public void testPinExpiryBounds() {
		Psd2Request.Builder builder = Psd2Request.builder("447700900999", 10.31, "Ebony");
		assertThrows(IllegalArgumentException.class, () -> builder.pinExpiry(59).build());
		assertThrows(IllegalArgumentException.class, () -> builder.pinExpiry(3601).build());
		builder.pinExpiry(3600).build();
		builder.pinExpiry(60).build();
	}

	@Test
	public void testNextEventWaitBounds() {
		Psd2Request.Builder builder = Psd2Request.builder("447700900999", 10.31, "Ebony");
		assertThrows(IllegalArgumentException.class, () -> builder.nextEventWait(59).build());
		assertThrows(IllegalArgumentException.class, () -> builder.nextEventWait(901).build());
		builder.nextEventWait(900).build();
		builder.nextEventWait(60).build();
	}

	@Test
	public void testConstructMissingRequiredParams() {
		var builder = Psd2Request.builder();
		assertThrows(NullPointerException.class, builder::build);
		assertThrows(IllegalArgumentException.class, () -> builder.number("447700900001").build());
		assertThrows(IllegalArgumentException.class, () -> builder.amount(0.87).build());
		assertThrows(IllegalArgumentException.class, () -> builder.payee("").build());
		assertNotNull(builder.payee("Myself").build());
	}

	@Test
	public void testConstructRequiredParams() {
		Psd2Request request = Psd2Request.builder().number("447700900001").amount(0.87).payee("Nexmo").build();
		assertNotNull(request);
		assertNull(request.getDashedLocale());
	}

	@Test
	public void testLongPayee() {
		assertThrows(IllegalArgumentException.class, () ->
				Psd2Request.builder("447700900001", 1.23, "1234567890abcedfghi").build()
		);
	}
}
