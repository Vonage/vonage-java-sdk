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

import com.vonage.client.AbstractClientTest;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.TestUtils;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.NoAuthMethod;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class SubaccountsClientTest extends AbstractClientTest<SubaccountsClient> {
	static final String FROM_API_KEY = "cde3214b", TO_API_KEY = "12c4d5e6", SUB_API_KEY = "def123ab",
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

	void assert403ResponseException(Executable invocation) throws Exception {
		String response = "{\n" +
				"   \"type\": \"https://developer.nexmo.com/api-errors#unprovisioned\",\n" +
				"   \"title\": \"Authorisation error\",\n" +
				"   \"detail\": \"Account acc6111f is not provisioned to access Subaccount Provisioning API\",\n" +
				"   \"instance\": \"158b8f199c45014ab7b08bfe9cc1c12c\"\n" +
				"}";
		assertApiResponseException(403, response, SubaccountsResponseException.class, invocation);
	}

	static void assertEqualsExpectedAccount(Account response) {
		testJsonableBaseObject(response);
		assertEquals(Account.fromJson(ACCOUNT_RESPONSE_JSON), response);
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

	static void assertEqualsExpectedMoneyTransfer(MoneyTransfer response) {
		testJsonableBaseObject(response);
		assertEquals(BigDecimal.valueOf(145.32), response.getAmount());
		assertEquals("7c9738e6", response.getFrom());
		assertEquals("ad6dc56f", response.getTo());
		assertEquals("Audit log ref", response.getReference());
		assertEquals(Instant.parse("2019-03-02T16:34:51Z"), response.getCreatedAt());
		assertEquals(UUID.fromString("ec48b760-8f26-40c6-9082-a122b6ca3640"), response.getId());
	}

	@Test
	public void testGetSubaccountWithoutApiKeyAuth() throws Exception {
		var sac = new SubaccountsClient(new HttpWrapper(new NoAuthMethod()));
		assertNotNull(sac);
		stubResponse(200, ACCOUNT_RESPONSE_JSON);
		var request = CreateSubaccountRequest.builder().name("S").build();
		assertThrows(IllegalStateException.class, () -> sac.createSubaccount(request));
	}

	@Test
	public void testCreateSubaccount() throws Exception {
		CreateSubaccountRequest request = CreateSubaccountRequest.builder()
				.name("Test sub").secret("16charactrSecr3T").build();
		assertEqualsExpectedAccount(stubResponseAndGet(ACCOUNT_RESPONSE_JSON, () -> client.createSubaccount(request)));
		stubResponseAndAssertThrows(200, () -> client.createSubaccount(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.createSubaccount(request), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.createSubaccount(request));

		new SubaccountsEndpointTestSpec<CreateSubaccountRequest, Account>() {
			@Override
			protected RestEndpoint<CreateSubaccountRequest, Account> endpoint() {
				return client.createSubaccount;
			}

			@Override
			protected Class<Account> expectedResponseType() {
				return Account.class;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(CreateSubaccountRequest request) {
				return "/accounts/"+request.getPrimaryAccountApiKey()+"/subaccounts";
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
			protected String sampleRequestBodyString() {
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
				testJsonableBaseObject(parsed);
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
				testJsonableBaseObject(parsed);
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
		.runTests();
	}

	@Test
	public void testUpdateSubaccount() throws Exception {
		UpdateSubaccountRequest request = UpdateSubaccountRequest.builder(SUB_API_KEY)
				.name("Renamed A").usePrimaryAccountBalance(false).suspended(true).build();
		assertEqualsExpectedAccount(stubResponseAndGet(ACCOUNT_RESPONSE_JSON, () -> client.updateSubaccount(request)));
		stubResponseAndAssertThrows(200, () -> client.updateSubaccount(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.updateSubaccount(request), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.updateSubaccount(request));

		new SubaccountsEndpointTestSpec<UpdateSubaccountRequest, Account>() {

			@Override
			protected RestEndpoint<UpdateSubaccountRequest, Account> endpoint() {
				return client.updateSubaccount;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PATCH;
			}

			@Override
			protected String expectedEndpointUri(UpdateSubaccountRequest request) {
				return "/accounts/"+ TestUtils.API_KEY +"/subaccounts/"+request.subaccountApiKey;
			}

			@Override
			protected UpdateSubaccountRequest sampleRequest() {
				return request;
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"name\":\""+request.getName() +
						"\",\"use_primary_account_balance\":" +
						request.getUsePrimaryAccountBalance()+",\"suspended\":true}";
			}
		}
		.runTests();
	}

	@Test
	public void testListSubaccounts() throws Exception {
		String expectedResponse = "{\"_embedded\":{\"primary_account\":" +
				ACCOUNT_RESPONSE_JSON + ",\"subaccounts\":[{}," + ACCOUNT_RESPONSE_JSON +",{}]}}";

		ListSubaccountsResponse response = stubResponseAndGet(expectedResponse, client::listSubaccounts);
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

		new SubaccountsEndpointTestSpec<Void, ListSubaccountsResponse>() {

			@Override
			protected RestEndpoint<Void, ListSubaccountsResponse> endpoint() {
				return client.listSubaccounts;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(Void request) {
				return "/accounts/"+ TestUtils.API_KEY +"/subaccounts";
			}

			@Override
			protected Void sampleRequest() {
				return null;
			}

		}
		.runTests();
	}

	@Test
	public void testGetSubaccount() throws Exception {
		assertEqualsExpectedAccount(stubResponseAndGet(ACCOUNT_RESPONSE_JSON, () -> client.getSubaccount(FROM_API_KEY)));
		stubResponseAndAssertThrows(200, () -> client.getSubaccount(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.getSubaccount(FROM_API_KEY), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.getSubaccount(FROM_API_KEY));

		new SubaccountsEndpointTestSpec<String, Account>() {

			@Override
			protected RestEndpoint<String, Account> endpoint() {
				return client.getSubaccount;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(String request) {
				return "/accounts/"+ TestUtils.API_KEY +"/subaccounts/"+request;
			}

			@Override
			protected String sampleRequest() {
				return SUB_API_KEY;
			}

		}
		.runTests();
	}

	@Test
	public void testListCreditTransfers() throws Exception {
		String startDate = "2022-06-01T08:00:00Z", endDate = "2023-06-08T09:01:40Z";
		ListTransfersFilter request = ListTransfersFilter.builder()
				.startDate(Instant.parse(startDate))
				.endDate(Instant.parse(endDate))
				.subaccount(SUB_API_KEY).build();

		String responseJson = "{\"_embedded\":{\"credit_transfers\":[{},"+MONEY_TRANSFER_RESPONSE_JSON+",{}]}}";
		List<MoneyTransfer> response = stubResponseAndGet(responseJson, () -> client.listCreditTransfers(request));
		assertNotNull(response);
		assertEquals(3, response.size());
		assertNotNull(response.get(0));
		assertNotNull(response.get(2));
		assertEqualsExpectedMoneyTransfer(response.get(1));
		assertNotNull(stubResponseAndGet(responseJson, client::listCreditTransfers));
		stubResponseAndAssertThrows(200, () -> client.listCreditTransfers(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.listCreditTransfers(request), SubaccountsResponseException.class);
		assert403ResponseException(client::listCreditTransfers);

		new SubaccountsEndpointTestSpec<ListTransfersFilter, ListTransfersResponseWrapper>() {

			@Override
			protected RestEndpoint<ListTransfersFilter, ListTransfersResponseWrapper> endpoint() {
				return client.listCreditTransfers;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListTransfersFilter request) {
				assertNotNull(request.getStartDate());
				assertNotNull(request.getEndDate());
				return "/accounts/"+ TestUtils.API_KEY +"/credit-transfers";
			}

			@Override
			protected ListTransfersFilter sampleRequest() {
				return request;
			}

		}
		.runTests();
	}

	@Test
	public void testListBalanceTransfers() throws Exception {
		ListTransfersFilter request = ListTransfersFilter.builder()
				.endDate(Instant.now()).subaccount(FROM_API_KEY).build();
		String responseJson = "{\"_embedded\":{\"balance_transfers\":[{},"+MONEY_TRANSFER_RESPONSE_JSON+",{}]}}";
		List<MoneyTransfer> response = stubResponseAndGet(responseJson, () -> client.listBalanceTransfers(request));
		assertNotNull(response);
		assertEquals(3, response.size());
		assertNotNull(response.get(0));
		assertNotNull(response.get(2));
		assertEqualsExpectedMoneyTransfer(response.get(1));
		assertNotNull(stubResponseAndGet(responseJson, client::listBalanceTransfers));
		stubResponseAndAssertThrows(200, () -> client.listBalanceTransfers(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.listBalanceTransfers(request), SubaccountsResponseException.class);
		assert403ResponseException(client::listBalanceTransfers);

		new SubaccountsEndpointTestSpec<ListTransfersFilter, ListTransfersResponseWrapper>() {

			@Override
			protected RestEndpoint<ListTransfersFilter, ListTransfersResponseWrapper> endpoint() {
				return client.listBalanceTransfers;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListTransfersFilter request) {
				assertNotNull(request.getStartDate());
				assertNull(request.getEndDate());
				assertNull(request.getSubaccount());
				return "/accounts/"+ TestUtils.API_KEY +"/balance-transfers";
			}

			@Override
			protected ListTransfersFilter sampleRequest() {
				return ListTransfersFilter.builder().build();
			}

		}
		.runTests();
	}

	abstract class MoneyTransferEndpointTestSpec extends SubaccountsEndpointTestSpec<MoneyTransfer, MoneyTransfer> {

		abstract String name();

		@Override
		protected HttpMethod expectedHttpMethod() {
			return HttpMethod.POST;
		}

		@Override
		protected String expectedEndpointUri(MoneyTransfer request) {
			return "/accounts/"+ TestUtils.API_KEY +"/"+name()+"-transfers";
		}

		@Override
		protected MoneyTransfer sampleRequest() {
			return MoneyTransfer.builder()
					.from("7c9738e6").to("ad6dc56f")
					.reference("This gets added to the audit log")
					.amount(BigDecimal.valueOf(123.45)).build();
		}

		@Override
		protected String sampleRequestBodyString() {
			MoneyTransfer request = sampleRequest();
			assertNotNull(request.toString());
			return "{\"from\":\""+request.getFrom()+"\",\"to\":\""+request.getTo() +
					"\",\"amount\":"+request.getAmount()+",\"reference\":\""+request.getReference()+"\"}";
		}
	}

	@Test
	public void testTransferCredit() throws Exception {
		MoneyTransfer request = MoneyTransfer.builder().amount(0.04).from(FROM_API_KEY).to(TO_API_KEY).build();
		assertEqualsExpectedMoneyTransfer(stubResponseAndGet(
				MONEY_TRANSFER_RESPONSE_JSON, () -> client.transferCredit(request)
		));
		stubResponseAndAssertThrows(200, () -> client.transferCredit(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.transferCredit(request), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.transferCredit(request));

		new MoneyTransferEndpointTestSpec() {

			@Override
			String name() {
				return "credit";
			}

			@Override
			protected RestEndpoint<MoneyTransfer, MoneyTransfer> endpoint() {
				return client.transferCredit;
			}
		}
		.runTests();
	}

	@Test
	public void testTransferBalance() throws Exception {
		MoneyTransfer request = MoneyTransfer.builder().amount(0.04).from(FROM_API_KEY).to(TO_API_KEY).build();
		String requestJson = request.toJson();
		assertEqualsExpectedMoneyTransfer(stubResponseAndGet(
				MONEY_TRANSFER_RESPONSE_JSON, () -> client.transferBalance(request)
		));
		stubResponseAndAssertThrows(200, () -> client.transferBalance(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.transferBalance(request), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.transferBalance(request));
		assertEquals(requestJson, MoneyTransfer.fromJson(requestJson).toJson());
		assertThrows(NullPointerException.class, () -> MoneyTransfer.builder()
				.from(FROM_API_KEY).to(TO_API_KEY).build()
		);

		new MoneyTransferEndpointTestSpec() {

			@Override
			String name() {
				return "balance";
			}

			@Override
			protected RestEndpoint<MoneyTransfer, MoneyTransfer> endpoint() {
				return client.transferBalance;
			}
		}
		.runTests();
	}

	@Test
	public void testTransferNumber() throws Exception {
		NumberTransfer request = NumberTransfer.builder()
				.from(FROM_API_KEY).to(TO_API_KEY).number("447900000001").country("GB").build();
		String requestJson = request.toJson(), responseJson = "{\n" +
				"   \"number\": \"235077036\",\n" +
				"   \"country\": \"DE\",\n" +
				"   \"from\": \"7c9738e6\",\n" +
				"   \"to\": \"ad6dc56f\"\n" +
				"}";
		NumberTransfer response = stubResponseAndGet(responseJson, () -> client.transferNumber(request));
		testJsonableBaseObject(response);
		assertEquals("235077036", response.getNumber());
		assertEquals("DE", response.getCountry());
		assertEquals("7c9738e6", response.getFrom());
		assertEquals("ad6dc56f", response.getTo());
		stubResponseAndAssertThrows(200, () -> client.transferNumber(null), NullPointerException.class);
		stubResponseAndAssertThrows(401, () -> client.transferNumber(request), SubaccountsResponseException.class);
		assert403ResponseException(() -> client.transferNumber(request));
		assertEquals(requestJson, NumberTransfer.fromJson(requestJson).toJson());
		assertThrows(IllegalArgumentException.class, () -> NumberTransfer.builder()
				.from(request.getFrom()).to(request.getTo()).number(request.getNumber())
				.country("United Kingdom").build()
		);

		new SubaccountsEndpointTestSpec<NumberTransfer, NumberTransfer>() {

			@Override
			protected RestEndpoint<NumberTransfer, NumberTransfer> endpoint() {
				return client.transferNumber;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(NumberTransfer request) {
				return "/accounts/"+ TestUtils.API_KEY +"/transfer-number";
			}

			@Override
			protected NumberTransfer sampleRequest() {
				return NumberTransfer.builder()
						.from("7c9738e6").to("ad6dc56f").country("GB").number("23507703696").build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"from\":\"7c9738e6\",\"to\":\"ad6dc56f\",\"number\":\"23507703696\",\"country\":\"GB\"}";
			}
		}
		.runTests();
	}
}