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
package com.vonage.client.subaccounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.VonageUnexpectedException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UpdateSubaccountRequestTest {
	
	@Test
	public void testSerializeAllParameters() {
		String name = "Subaccount department A";
		boolean usePrimaryAccountBalance = false, suspended = true;
		UpdateSubaccountRequest request = UpdateSubaccountRequest.builder("acc6111f")
				.name(name).usePrimaryAccountBalance(usePrimaryAccountBalance)
				.suspended(suspended).build();

		String expectedJson = "{\"name\":\""+name+"\",\"" +
				"use_primary_account_balance\":"+usePrimaryAccountBalance + ",\"suspended\":"+suspended+"}";
		assertEquals(expectedJson, request.toJson());
	}

	@Test
	public void testSerializeRequiredParameters() {
		UpdateSubaccountRequest.Builder builder  = UpdateSubaccountRequest.builder("abc6123f");
		assertThrows(IllegalStateException.class, builder::build);
		assertEquals("{\"suspended\":false}", builder.suspended(false).build().toJson());
	}

	@Test
	public void testInvalidApiKey() {
		assertThrows(NullPointerException.class, () ->
				UpdateSubaccountRequest.builder(null).build()
		);
		assertThrows(IllegalArgumentException.class, () ->
				UpdateSubaccountRequest.builder("acc611f").build()
		);
		assertThrows(IllegalArgumentException.class, () ->
				UpdateSubaccountRequest.builder("        ").build()
		);
	}

	@Test
	public void testNameLength() {
		UpdateSubaccountRequest.Builder builder = UpdateSubaccountRequest.builder("abc123d4");
		assertEquals(8, builder.name("N").build().subaccountApiKey.length());
		assertThrows(IllegalArgumentException.class, () -> builder.name(" \t \n  ").build());
		int limit = 80;
		StringBuilder sb = new StringBuilder(limit);
		for (int i = 0; i < limit; sb.append((char) ((i++ % 26) + 65)));
		assertEquals(limit, builder.name(sb.toString()).build().getName().length());
		assertThrows(IllegalArgumentException.class, () -> builder.name(sb.append("0").toString()).build());
	}

	@Test(expected = VonageUnexpectedException.class)
	public void triggerJsonProcessingException() {
		class SelfRefrencing extends UpdateSubaccountRequest {
			@JsonProperty("self") SelfRefrencing self = this;

			SelfRefrencing(Builder builder) {
				super(builder);
			}
		}
		new SelfRefrencing(UpdateSubaccountRequest.builder("a1b2c3d4").suspended(true)).toJson();
	}
}