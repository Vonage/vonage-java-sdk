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
package com.vonage.client.subaccounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
import java.util.List;

public class ListSubaccountsResponseTest {
	
	@Test
	public void testFromJsonAllFields() throws Exception {
		ListSubaccountsResponse parsed = ListSubaccountsResponse.fromJson("{\n" +
				"   \"_embedded\": {\n" +
				"      \"primary_account\": {\n" +
				"         \"api_key\": \"bbe6222f\",\n" +
				"         \"name\": \"Subaccount department A\",\n" +
				"         \"primary_account_api_key\": \"acc6111f\",\n" +
				"         \"use_primary_account_balance\": true,\n" +
				"         \"created_at\": \"2018-03-02T16:34:49Z\",\n" +
				"         \"suspended\": false,\n" +
				"         \"balance\": 100.25,\n" +
				"         \"credit_limit\": -100.25\n" +
				"      },\n" +
				"      \"subaccounts\": [{},\n" +
				"         {\n" +
				"            \"api_key\": \"bbe6222f\",\n" +
				"            \"name\": \"Subaccount department A\",\n" +
				"            \"primary_account_api_key\": \"acc6111f\",\n" +
				"            \"use_primary_account_balance\": true,\n" +
				"            \"created_at\": \"2018-03-02T16:34:49Z\",\n" +
				"            \"suspended\": false,\n" +
				"            \"balance\": 100.25,\n" +
				"            \"credit_limit\": -100.25\n" +
				"         },\n" +
				"         {\n" +
				"            \"name\": \"Test\"\n" +
				"         }" +
				"      ]\n" +
				"   }\n" +
				"}");

		TestUtils.testJsonableBaseObject(parsed);
		Account primary = parsed.getPrimaryAccount();
		assertNotNull(primary);
		assertEquals("bbe6222f", primary.getApiKey());
		assertEquals("Subaccount department A", primary.getName());
		assertEquals("acc6111f", primary.getPrimaryAccountApiKey());
		assertTrue(primary.getUsePrimaryAccountBalance());
		assertEquals(Instant.parse("2018-03-02T16:34:49Z"), primary.getCreatedAt());
		assertFalse(primary.getSuspended());
		assertEquals(100.25, primary.getBalance().doubleValue(), 0.001);
		assertEquals(-100.25, primary.getCreditLimit().doubleValue(), 0.001);
		List<Account> subaccounts = parsed.getSubaccounts();
		assertNotNull(subaccounts);
		assertEquals(3, subaccounts.size());
		assertNotNull(subaccounts.get(0));
		assertNotNull(subaccounts.get(1));
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		String secondSubJson = mapper.writeValueAsString(subaccounts.get(1));
		String primaryJson = mapper.writeValueAsString(primary);
		assertEquals(primaryJson, secondSubJson);
		Account thirdSub = subaccounts.get(2);
		assertNotNull(thirdSub);
		assertEquals("Test", thirdSub.getName());
		assertNull(thirdSub.getApiKey());
		assertNull(thirdSub.getPrimaryAccountApiKey());
		assertNull(thirdSub.getSuspended());
		assertNull(thirdSub.getUsePrimaryAccountBalance());
		assertNull(thirdSub.getCreatedAt());
		assertNull(thirdSub.getBalance());
		assertNull(thirdSub.getCreditLimit());
	}
	
	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> ListSubaccountsResponse.fromJson("{malformed]"));
	}

	@Test
	public void testFromJsonEmpty() {
		ListSubaccountsResponse response = ListSubaccountsResponse.fromJson("{}");
		assertNull(response.getPrimaryAccount());
		assertNull(response.getSubaccounts());

		response = ListSubaccountsResponse.fromJson("{\"_embedded\": {}}");
		assertNull(response.getPrimaryAccount());
		assertNull(response.getSubaccounts());

		response = ListSubaccountsResponse.fromJson("{\"_embedded\":{\"primary_account\":{},\"subaccounts\":[]}}");
		assertNotNull(response.getPrimaryAccount());
		assertNotNull(response.getSubaccounts());
		assertEquals(0, response.getSubaccounts().size());
	}
}