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
package com.vonage.client.subaccounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.VonageUnexpectedException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreateSubaccountRequestTest {
	
	@Test
	public void testSerializeValid() {
		String primaryAccountApiKey = "acc6111f";
		String name = "Subaccount department A";
		String secret = "1234567890abcdef";
		CreateSubaccountRequest request = CreateSubaccountRequest.builder()
				.name(name).usePrimaryAccountBalance(false).secret(secret).build();
		request.primaryAccountApiKey = primaryAccountApiKey;

		String json = request.toJson();
		assertTrue(json.contains("\"use_primary_account_balance\":false"));
		assertTrue(json.contains("\"name\":\""+name+"\""));
		assertTrue(json.contains("\"secret\":\""+secret+"\""));
		assertTrue(json.contains("\"primary_account_api_key\":\""+primaryAccountApiKey+"\""));
	}

	@Test
	public void testConstructRequiredParameters() {
		CreateSubaccountRequest request = CreateSubaccountRequest.builder().name("Sub dept B").build();
		assertEquals("{\"name\":\""+request.getName()+"\"}", request.toJson());
		assertNull(request.getPrimaryAccountApiKey());
		assertNull(request.getUsePrimaryAccountBalance());
	}

	@Test
	public void testConstructNoName() {
		CreateSubaccountRequest.Builder builder = CreateSubaccountRequest.builder();
		assertThrows(IllegalArgumentException.class, builder::build);
		assertThrows(IllegalArgumentException.class, () -> builder.name("  ").build());
	}

	@Test
	public void testConstructSecretLength() {
		CreateSubaccountRequest.Builder builder = CreateSubaccountRequest.builder().name("Department C");
		String min = "*A1b23c5", max = "A1b2*".repeat(5);
		assertEquals(min, builder.secret(min).build().getSecret());
		assertEquals(max, builder.secret(max).build().getSecret());
		assertThrows(IllegalArgumentException.class, () -> builder.secret(min.substring(1)).build());
		assertThrows(IllegalArgumentException.class, () -> builder.secret(max + "T").build());
	}

	@Test
	public void testNameLength() {
		CreateSubaccountRequest.Builder builder = CreateSubaccountRequest.builder();
		assertEquals(1, builder.name("N").build().getName().length());
		assertThrows(IllegalArgumentException.class, () -> builder.name(" \t \n  ").build());
		int limit = 80;
		StringBuilder sb = new StringBuilder(limit);
		for (int i = 0; i < limit; sb.append((char) ((i++ % 26) + 65)));
		assertEquals(limit, builder.name(sb.toString()).build().getName().length());
		assertThrows(IllegalArgumentException.class, () -> builder.name(sb.append("0").toString()).build());
	}

	@Test
	public void triggerJsonProcessingException() {
		class SelfRefrencing extends CreateSubaccountRequest {
			@JsonProperty("self") final SelfRefrencing self = this;

			SelfRefrencing(Builder builder) {
				super(builder);
			}
		}
		assertThrows(VonageUnexpectedException.class, () ->
				new SelfRefrencing(CreateSubaccountRequest.builder().name("Name")).toJson()
		);
	}
}