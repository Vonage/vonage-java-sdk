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

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.RestEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class SubaccountsClient {
	final RestEndpoint<CreateSubaccountRequest, Account> createSubaccount;
	final RestEndpoint<UpdateSubaccountRequest, Account> updateSubaccount;
	final RestEndpoint<Void, ListSubaccountsResponse> listSubaccounts;
	final RestEndpoint<String, Account> getSubaccount;
	final RestEndpoint<ListTransfersFilter, ListTransfersResponseWrapper> listBalanceTransfers, listCreditTransfers;
	final RestEndpoint<MoneyTransfer, MoneyTransfer> transferBalance, transferCredit;
	final RestEndpoint<NumberTransfer, NumberTransfer> transferNumber;

	/**
	 * Constructor.
	 *
	 * @param wrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public SubaccountsClient(HttpWrapper wrapper) {
		@SuppressWarnings("unchecked")
		final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
			Endpoint(Function<T, String> pathGetter, HttpMethod method, R... type) {
				super(DynamicEndpoint.<T, R> builder(type)
						.responseExceptionType(SubaccountsResponseException.class)
						.wrapper(wrapper).requestMethod(method).authMethod(TokenAuthMethod.class)
						.pathGetter((de, req) -> {
								if (req instanceof CreateSubaccountRequest) {
									CreateSubaccountRequest csr = (CreateSubaccountRequest) req;
									csr.primaryAccountApiKey = de.getApplicationIdOrApiKey();
								}
								return String.format(
										de.getHttpWrapper().getHttpConfig().getApiBaseUri()
										+ "/accounts/%s/", de.getApplicationIdOrApiKey()
								) + pathGetter.apply(req);
						})
				);
			}
		}

		createSubaccount = new Endpoint<>(req -> "subaccounts", HttpMethod.POST);
		updateSubaccount = new Endpoint<>(req -> "subaccounts/"+req.subaccountApiKey, HttpMethod.PATCH);
		listSubaccounts = new Endpoint<>(req -> "subaccounts", HttpMethod.GET);
		getSubaccount = new Endpoint<>(req -> "subaccounts/"+req, HttpMethod.GET);
		listBalanceTransfers = new Endpoint<>(req -> "balance-transfers", HttpMethod.GET);
		listCreditTransfers = new Endpoint<>(req -> "credit-transfers", HttpMethod.GET);
		transferBalance = new Endpoint<>(req -> "balance-transfers", HttpMethod.POST);
		transferCredit = new Endpoint<>(req -> "credit-transfers", HttpMethod.POST);
		transferNumber = new Endpoint<>(req -> "transfer-number", HttpMethod.POST);
	}

	private <T> T requireRequest(T request) {
		return Objects.requireNonNull(request, "Request is required.");
	}

	/**
	 * Create a new subaccount under this API primary account.
	 *
	 * @param request Properties for the new subaccount.
	 *
	 * @return Details of the created subaccount.
	 *
	 * @throws SubaccountsResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *   <li><b>401</b>: Credential is missing or invalid.</li>
	 * 	 <li><b>403</b>: Action is forbidden.</li>
	 * 	 <li><b>404</b>: The account ID provided does not exist in our system or you do not have access.</li>
	 * 	 <li><b>422</b>: Validation error.</li>
	 * </ul>
	 */
	public Account createSubaccount(CreateSubaccountRequest request) {
		return createSubaccount.execute(requireRequest(request));
	}

	/**
	 * Change one or more properties of a subaccount.
	 *
	 * @param request Properties of the subaccount to update.
	 *
	 * @return Details of the updated subaccount.
	 *
	 * @throws SubaccountsResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *   <li><b>401</b>: Credential is missing or invalid.</li>
	 * 	 <li><b>403</b>: Action is forbidden.</li>
	 * 	 <li><b>404</b>: The account ID provided does not exist in our system or you do not have access.</li>
	 * 	 <li><b>422</b>: Validation error.</li>
	 * </ul>
	 */
	public Account updateSubaccount(UpdateSubaccountRequest request) {
		return updateSubaccount.execute(requireRequest(request));
	}

	/**
	 * Retrieve all subaccounts owned by the primary account.
	 *
	 * @return The primary account details and list of subaccounts associated with it.
	 *
	 * @throws SubaccountsResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *   <li><b>401</b>: Credential is missing or invalid.</li>
	 * 	 <li><b>403</b>: Action is forbidden.</li>
	 * 	 <li><b>404</b>: The account ID provided does not exist in our system or you do not have access.</li>
	 * </ul>
	 */
	public ListSubaccountsResponse listSubaccounts() {
		return listSubaccounts.execute(null);
	}

	/**
	 * Get information about a specific subaccount.
	 *
	 * @param subaccountApiKey Unique ID of the subaccount to retrieve.
	 *
	 * @return Details of the requested subaccount.
	 *
	 * @throws SubaccountsResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *   <li><b>401</b>: Credential is missing or invalid.</li>
	 * 	 <li><b>403</b>: Action is forbidden.</li>
	 * 	 <li><b>404</b>: The account ID provided does not exist in our system or you do not have access.</li>
	 * </ul>
	 */
	public Account getSubaccount(String subaccountApiKey) {
		return getSubaccount.execute(AbstractTransfer.validateAccountKey(subaccountApiKey, "Subaccount"));
	}

	/**
	 * Retrieve a list of credit transfers that have taken place for the primary account within a specified time period.
	 *
	 * @return The list of credit transfers.
	 * @see #listCreditTransfers(ListTransfersFilter)
	 *
	 * @throws SubaccountsResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *   <li><b>401</b>: Credential is missing or invalid.</li>
	 * 	 <li><b>403</b>: Action is forbidden.</li>
	 * 	 <li><b>404</b>: The account ID provided does not exist in our system or you do not have access.</li>
	 * </ul>
	 */
	public List<MoneyTransfer> listCreditTransfers() {
		return listCreditTransfers(ListTransfersFilter.builder().build());
	}

	/**
	 * Retrieve a list of credit transfers that have taken place for the primary account within a specified time period.
	 *
	 * @param filter Additional parameters to narrow down the returned list by.
	 *
	 * @return The list of credit transfers.
	 * @see #listCreditTransfers()
	 *
	 * @throws SubaccountsResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *   <li><b>401</b>: Credential is missing or invalid.</li>
	 * 	 <li><b>403</b>: Action is forbidden.</li>
	 * 	 <li><b>404</b>: The account ID provided does not exist in our system or you do not have access.</li>
	 * </ul>
	 */
	public List<MoneyTransfer> listCreditTransfers(ListTransfersFilter filter) {
		return listCreditTransfers.execute(requireRequest(filter)).getCreditTransfers();
	}

	/**
	 * Retrieve a list of balance transfers that have taken place for the primary account within a specified time period.
	 *
	 * @return The list of balance transfers.
	 * @see #listBalanceTransfers(ListTransfersFilter)
	 *
	 * @throws SubaccountsResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *   <li><b>401</b>: Credential is missing or invalid.</li>
	 * 	 <li><b>403</b>: Action is forbidden.</li>
	 * 	 <li><b>404</b>: The account ID provided does not exist in our system or you do not have access.</li>
	 * </ul>
	 */
	public List<MoneyTransfer> listBalanceTransfers() {
		return listBalanceTransfers(ListTransfersFilter.builder().build());
	}

	/**
	 * Retrieve a list of balance transfers that have taken place for the primary account within a specified time period.
	 *
	 * @param filter Additional parameters to narrow down the returned list by.
	 *
	 * @return The list of balance transfers.
	 * @see #listBalanceTransfers()
	 *
	 * @throws SubaccountsResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *   <li><b>401</b>: Credential is missing or invalid.</li>
	 * 	 <li><b>403</b>: Action is forbidden.</li>
	 * 	 <li><b>404</b>: The account ID provided does not exist in our system or you do not have access.</li>
	 * </ul>
	 */
	public List<MoneyTransfer> listBalanceTransfers(ListTransfersFilter filter) {
		return listBalanceTransfers.execute(requireRequest(filter)).getBalanceTransfers();
	}

	/**
	 * Transfer credit limit between the primary account and one of its subaccounts.
	 *
	 * @param request Properties of the credit transfer.
	 *
	 * @return Details of the transfer if successful.
	 *
	 * @throws SubaccountsResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *   <li><b>401</b>: Credential is missing or invalid.</li>
	 * 	 <li><b>403</b>: Action is forbidden.</li>
	 * 	 <li><b>404</b>: The account ID provided does not exist in our system or you do not have access.</li>
	 * 	 <li><b>422</b>: Validation error.</li>
	 * </ul>
	 */
	public MoneyTransfer transferCredit(MoneyTransfer request) {
		return transferCredit.execute(requireRequest(request));
	}

	/**
	 * Transfer balance between the primary account and one of its subaccounts.
	 *
	 * @param request Properties of the balance transfer.
	 *
	 * @return Details of the transfer if successful.
	 *
	 * @throws SubaccountsResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *   <li><b>401</b>: Credential is missing or invalid.</li>
	 * 	 <li><b>403</b>: Action is forbidden.</li>
	 * 	 <li><b>404</b>: The account ID provided does not exist in our system or you do not have access.</li>
	 * 	 <li><b>422</b>: Validation error.</li>
	 * </ul>
	 */
	public MoneyTransfer transferBalance(MoneyTransfer request) {
		return transferBalance.execute(requireRequest(request));
	}

	/**
	 * Transfer a number between subaccounts.
	 *
	 * @param request Properties of the number transfer.
	 *
	 * @return Details of the transfer if successful.
	 *
	 * @throws SubaccountsResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *   <li><b>401</b>: Credential is missing or invalid.</li>
	 * 	 <li><b>403</b>: Action is forbidden.</li>
	 * 	 <li><b>404</b>: The account ID provided does not exist in our system or you do not have access.</li>
	 * 	 <li><b>409</b>: Transfer conflict.</li>
	 * 	 <li><b>422</b>: Validation error.</li>
	 * </ul>
	 */
	public NumberTransfer transferNumber(NumberTransfer request) {
		return transferNumber.execute(requireRequest(request));
	}
}
