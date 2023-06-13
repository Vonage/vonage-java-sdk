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

import com.vonage.client.ClientTest;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class SubaccountsClientTest extends ClientTest<SubaccountsClient> {
	static final String FROM_API_KEY = "cde3214b", TO_API_KEY = "12c4d5e6",
		ACCOUNT_RESPONSE_JSON = "{\n" +
				"   \"secret\": \"Password123\",\n" +
				"   \"api_key\": \"bbe6222f\",\n" +
				"   \"name\": \"Subaccount Department B\",\n" +
				"   \"primary_account_api_key\": \"acc6111f\",\n" +
				"   \"use_primary_account_balance\": true,\n" +
				"   \"created_at\": \"2018-03-02T16:34:49Z\",\n" +
				"   \"suspended\": false,\n" +
				"   \"balance\": 99.23,\n" +
				"   \"credit_limit\": -101.68\n}",
		MONEY_TRANSFER_RESPONSE_JSON = "{\n" +
				"   \"amount\": 145.32,\n" +
				"   \"from\": \"7c9738e6\",\n" +
				"   \"to\": \"ad6dc56f\",\n" +
				"   \"reference\": \"Audit log ref\",\n" +
				"   \"created_at\": \"2019-03-02T16:34:51Z\",\n" +
				"   \"id\": \"ec48b760-8f26-40c6-9082-a122b6ca3640\"\n}";

	public SubaccountsClientTest() {
		client = new SubaccountsClient(wrapper);
	}

	void assert403ResponseException(ThrowingRunnable invocation) throws Exception {
		int statusCode = 403;
		SubaccountsResponseException expectedResponse = SubaccountsResponseException.fromJson(
				"{\n" +
				"   \"type\": \"https://developer.nexmo.com/api-errors#unprovisioned\",\n" +
				"   \"title\": \"Authorisation error\",\n" +
				"   \"detail\": \"Account acc6111f is not provisioned to access Subaccount Provisioning API\",\n" +
				"   \"instance\": \"158b8f199c45014ab7b08bfe9cc1c12c\"\n" +
				"}"
		);

		String expectedJson = expectedResponse.toJson();
		assertEquals(224, expectedJson.length());
		wrapper.setHttpClient(stubHttpClient(statusCode, expectedJson));
		expectedResponse.setStatusCode(statusCode);
		String failPrefix = "Expected SubaccountsResponseException, but got ";

		try {
			invocation.run();
			fail(failPrefix + "nothing.");
		}
		catch (SubaccountsResponseException ex) {
			assertEquals(expectedResponse, ex);
			assertEquals(expectedJson, ex.toJson());
		}
		catch (Throwable ex) {
			fail(failPrefix + ex);
		}
	}

	static void assertEqualsExpectedAccount(Account response) {
		assertEquals("Password123", response.getSecret());
		assertEquals("bbe6222f", response.getApiKey());
		assertEquals("acc6111f", response.getPrimaryAccountApiKey());
		assertEquals("Subaccount Department B", response.getName());
		assertTrue(response.getUsePrimaryAccountBalance());
		assertFalse(response.getSuspended());
		assertEquals(Instant.parse("2018-03-02T16:34:49Z"), response.getCreatedAt());
		assertEquals(BigDecimal.valueOf(99.23), response.getBalance());
		assertEquals(BigDecimal.valueOf(-101.68), response.getCreditLimit());
	}

	static void assertEqualsExpectedMoneyTransfer(AbstractMoneyTransfer response) {
		assertNotNull(response);
		assertEquals(BigDecimal.valueOf(145.32), response.getAmount());
		assertEquals("7c9738e6", response.getFrom());
		assertEquals("ad6dc56f", response.getTo());
		assertEquals("Audit log ref", response.getReference());
		assertEquals(Instant.parse("2019-03-02T16:34:51Z"), response.getCreatedAt());
		assertEquals(UUID.fromString("ec48b760-8f26-40c6-9082-a122b6ca3640"), response.getId());
	}
	
	@Test
	public void testCreateSubaccount() throws Exception {
		CreateSubaccountRequest request = CreateSubaccountRequest.builder()
				.name("Test sub").secret("16charactrSecr3T").build();
		assertEqualsExpectedAccount(stubResponseAndGet(ACCOUNT_RESPONSE_JSON, () -> client.createSubaccount(request)));
		stubResponseAndAssertThrows(200, () -> client.createSubaccount(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.createSubaccount(request), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.createSubaccount(request));
	}

	@Test
	public void testUpdateSubaccount() throws Exception {
		UpdateSubaccountRequest request = UpdateSubaccountRequest.builder(FROM_API_KEY).build();
		assertEqualsExpectedAccount(stubResponseAndGet(ACCOUNT_RESPONSE_JSON, () -> client.updateSubaccount(request)));
		stubResponseAndAssertThrows(200, () -> client.updateSubaccount(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.updateSubaccount(request), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.updateSubaccount(request));
	}

	@Test
	public void testListSubaccounts() throws Exception {
		stubResponse("{\"_embedded\":{\"primary_account\":" +
				ACCOUNT_RESPONSE_JSON + ",\"subaccounts\":[{}," + ACCOUNT_RESPONSE_JSON +",{}]}}"
		);
		ListSubaccountsResponse response = client.listSubaccounts();
		assertNotNull(response);
		assertEqualsExpectedAccount(response.getPrimaryAccount());
		List<Account> subaccounts = response.getSubaccounts();
		assertNotNull(subaccounts);
		assertEquals(3, subaccounts.size());
		assertNotNull(subaccounts.get(0));
		assertEqualsExpectedAccount(subaccounts.get(1));
		assertNotNull(subaccounts.get(2));
		stubResponseAndAssertThrows(401, client::listSubaccounts, SubaccountsResponseException.class);
		assert403ResponseException(client::listSubaccounts);
	}

	@Test
	public void testGetSubaccount() throws Exception {
		assertEqualsExpectedAccount(stubResponseAndGet(ACCOUNT_RESPONSE_JSON, () -> client.getSubaccount(FROM_API_KEY)));
		stubResponseAndAssertThrows(200, () -> client.getSubaccount(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.getSubaccount(FROM_API_KEY), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.getSubaccount(FROM_API_KEY));
	}

	@Test
	public void testListCreditTransfers() throws Exception {
		ListTransfersFilter request = ListTransfersFilter.builder()
				.endDate(Instant.now()).subaccounts(FROM_API_KEY).build();
		String responseJson = "{\"_embedded\":{\"credit_transfers\":[{},"+MONEY_TRANSFER_RESPONSE_JSON+",{}]}}";
		List<CreditTransfer> response = stubResponseAndGet(responseJson, () -> client.listCreditTransfers(request));
		assertNotNull(response);
		assertEquals(3, response.size());
		assertNotNull(response.get(0));
		assertNotNull(response.get(2));
		assertEqualsExpectedMoneyTransfer(response.get(1));
		assertNotNull(stubResponseAndGet(responseJson, client::listCreditTransfers));
		stubResponseAndAssertThrows(200, () -> client.listCreditTransfers(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.listCreditTransfers(request), SubaccountsResponseException.class);
		assert403ResponseException(client::listCreditTransfers);
	}

	@Test
	public void testListBalanceTransfers() throws Exception {
		ListTransfersFilter request = ListTransfersFilter.builder()
				.endDate(Instant.now()).subaccounts(FROM_API_KEY).build();
		String responseJson = "{\"_embedded\":{\"balance_transfers\":[{},"+MONEY_TRANSFER_RESPONSE_JSON+",{}]}}";
		List<BalanceTransfer> response = stubResponseAndGet(responseJson, () -> client.listBalanceTransfers(request));
		assertNotNull(response);
		assertEquals(3, response.size());
		assertNotNull(response.get(0));
		assertNotNull(response.get(2));
		assertEqualsExpectedMoneyTransfer(response.get(1));
		assertNotNull(stubResponseAndGet(responseJson, client::listBalanceTransfers));
		stubResponseAndAssertThrows(200, () -> client.listBalanceTransfers(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.listBalanceTransfers(request), SubaccountsResponseException.class);
		assert403ResponseException(client::listBalanceTransfers);
	}

	@Test
	public void testTransferCredit() throws Exception {
		CreditTransfer request = CreditTransfer.builder().amount(0.04).from(FROM_API_KEY).to(TO_API_KEY).build();
		assertEqualsExpectedMoneyTransfer(stubResponseAndGet(
				MONEY_TRANSFER_RESPONSE_JSON, () -> client.transferCredit(request)
		));
		stubResponseAndAssertThrows(200, () -> client.transferCredit(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.transferCredit(request), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.transferCredit(request));
	}

	@Test
	public void testTransferBalance() throws Exception {
		BalanceTransfer request = BalanceTransfer.builder().amount(0.04).from(FROM_API_KEY).to(TO_API_KEY).build();
		assertEqualsExpectedMoneyTransfer(stubResponseAndGet(
				MONEY_TRANSFER_RESPONSE_JSON, () -> client.transferBalance(request)
		));
		stubResponseAndAssertThrows(200, () -> client.transferBalance(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.transferBalance(request), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.transferBalance(request));
	}

	@Test
	public void testTransferNumber() throws Exception {
		NumberTransfer request = NumberTransfer.builder()
				.from(FROM_API_KEY).to(TO_API_KEY).number("447900000001").country("GB").build();
		String responseJson = "{\n" +
				"   \"number\": \"235077036\",\n" +
				"   \"country\": \"DE\",\n" +
				"   \"from\": \"7c9738e6\",\n" +
				"   \"to\": \"ad6dc56f\"\n" +
				"}";
		NumberTransfer response = stubResponseAndGet(responseJson, () -> client.transferNumber(request));
		assertEquals("235077036", response.getNumber());
		assertEquals("DE", response.getCountry());
		assertEquals("7c9738e6", response.getFrom());
		assertEquals("ad6dc56f", response.getTo());
		stubResponseAndAssertThrows(200, () -> client.transferNumber(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.transferNumber(request), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.transferNumber(request));
	}
}