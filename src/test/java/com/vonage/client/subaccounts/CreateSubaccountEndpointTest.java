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

import com.vonage.client.common.HttpMethod;
import static org.junit.Assert.*;

abstract class CreateSubaccountEndpointTest extends SubaccountsEndpointTestSpec<CreateSubaccountRequest, Account> {

	@Override
	protected Class<Account> expectedResponseType() {
		return Account.class;
	}

	@Override
	protected HttpMethod expectedHttpMethod() {
		return HttpMethod.POST;
	}

	@Override
	protected String expectedEndpointUri() {
		return "/accounts/"+endpoint().getApplicationIdOrApiKey()+"/subaccounts";
	}

	@Override
	protected CreateSubaccountRequest sampleRequest() {
		CreateSubaccountRequest request = CreateSubaccountRequest.builder()
				.name("Subaccount department A")
				.usePrimaryAccountBalance(false)
				.secret("Ab12cx340987ucvjklf").build();
		request.primaryAccountApiKey = "acc6111f";
		return request;
	}

	@Override
	protected String sampleRequestString() {
		return "{\"primary_account_api_key\":\"acc6111f\",\"name\":\"Subaccount department A\",\"secret\":" +
				"\"Ab12cx340987ucvjklf\",\"use_primary_account_balance\":false}";
	}

	@Override
	public void runTests() throws Exception {
		super.runTests();
		testFullResponse();
		testEmptyResponse();
	}

	void testFullResponse() throws Exception {
		String expectedResponse = "{\n" +
				"   \"api_key\": \"bbe6222f\",\n" +
				"   \"name\": \"Subaccount department A\",\n" +
				"   \"secret\": \"Password123\",\n" +
				"   \"primary_account_api_key\": \"acc6111f\",\n" +
				"   \"use_primary_account_balance\": true,\n" +
				"   \"created_at\": \"2018-03-02T16:34:49Z\",\n" +
				"   \"suspended\": false,\n" +
				"   \"balance\": 100.25,\n" +
				"   \"credit_limit\": -99.33\n" +
				"}";

		Account parsed = parseResponse(expectedResponse);
		assertNotNull(parsed);
		assertEquals("bbe6222f", parsed.getApiKey());
		assertEquals("Subaccount department A", parsed.getName());
		assertEquals("Password123", parsed.getSecret());
		assertTrue(parsed.getUsePrimaryAccountBalance());
		assertEquals(1520008489L, parsed.getCreatedAt().getEpochSecond());
		assertFalse(parsed.getSuspended());
		assertEquals(100.25, parsed.getBalance().doubleValue(), 0.001);
		assertEquals(-99.33, parsed.getCreditLimit().doubleValue(), 0.001);
	}

	void testEmptyResponse() throws Exception {
		Account parsed = parseResponse("{}");
		assertNotNull(parsed);
		assertNull(parsed.getApiKey());
		assertNull(parsed.getSecret());
		assertNull(parsed.getPrimaryAccountApiKey());
		assertNull(parsed.getName());
		assertNull(parsed.getUsePrimaryAccountBalance());
		assertNull(parsed.getSuspended());
		assertNull(parsed.getCreatedAt());
		assertNull(parsed.getBalance());
		assertNull(parsed.getCreditLimit());
	}
}