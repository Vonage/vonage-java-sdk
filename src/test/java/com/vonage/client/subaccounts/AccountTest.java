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

import com.vonage.client.VonageUnexpectedException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.math.BigDecimal;
import java.time.Instant;

public class AccountTest {
	
	@Test
	public void testFromJsonAllFields() {
		String apiKey = "bbe6222f";
		String primaryAccountApiKey = "acc6111f";
		String name = "Subaccount department A";
		Boolean usePrimaryAccountBalance = true;
		Boolean suspended = false;
		Instant createdAt = Instant.parse("2018-03-02T16:34:49Z");
		BigDecimal balance = BigDecimal.valueOf(99.25);
		BigDecimal creditLimit = BigDecimal.valueOf(-100.68);
	
		Account response = Account.fromJson("{\n" +
				"\"api_key\":\""+apiKey+"\",\n" +
				"\"primary_account_api_key\":\""+primaryAccountApiKey+"\",\n" +
				"\"name\":\""+name+"\",\n" +
				"\"use_primary_account_balance\":\""+usePrimaryAccountBalance+"\",\n" +
				"\"suspended\":\""+suspended+"\",\n" +
				"\"created_at\":\""+createdAt+"\",\n" +
				"\"balance\":\""+balance+"\",\n" +
				"\"credit_limit\":\""+creditLimit+"\"\n" +
		"}");
		
		assertEquals(apiKey, response.getApiKey());
		assertEquals(primaryAccountApiKey, response.getPrimaryAccountApiKey());
		assertEquals(name, response.getName());
		assertEquals(usePrimaryAccountBalance, response.getUsePrimaryAccountBalance());
		assertEquals(suspended, response.getSuspended());
		assertEquals(createdAt, response.getCreatedAt());
		assertEquals(balance, response.getBalance());
		assertEquals(creditLimit, response.getCreditLimit());
	}
	
	@Test(expected = VonageUnexpectedException.class)
	public void testFromJsonInvalid() {
		Account.fromJson("{malformed]");
	}

	@Test
	public void testFromJsonEmpty() {
		Account response = Account.fromJson("{}");
		assertNull(response.getApiKey());
		assertNull(response.getPrimaryAccountApiKey());
		assertNull(response.getName());
		assertNull(response.getUsePrimaryAccountBalance());
		assertNull(response.getSuspended());
		assertNull(response.getCreatedAt());
		assertNull(response.getBalance());
		assertNull(response.getCreditLimit());
	}
}