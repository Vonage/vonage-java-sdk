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

import com.vonage.client.HttpWrapper;
import java.util.List;
import java.util.Objects;

public class SubaccountsClient {
	final CreateSubaccountEndpoint createSubaccount;
	final UpdateSubaccountEndpoint updateSubaccount;
	final ListSubaccountsEndpoint listSubaccounts;
	final GetSubaccountEndpoint getSubaccount;
	final ListMoneyTransfersEndpoint listBalanceTransfers, listCreditTransfers;
	final TransferMoneyEndpoint transferBalance, transferCredit;
	final TransferNumberEndpoint transferNumber;

	/**
	 * Constructor.
	 *
	 * @param httpWrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public SubaccountsClient(HttpWrapper httpWrapper) {
		createSubaccount = new CreateSubaccountEndpoint(httpWrapper);
		updateSubaccount = new UpdateSubaccountEndpoint(httpWrapper);
		listSubaccounts = new ListSubaccountsEndpoint(httpWrapper);
		getSubaccount = new GetSubaccountEndpoint(httpWrapper);
		listBalanceTransfers = new ListMoneyTransfersEndpoint(httpWrapper, "balance");
		listCreditTransfers = new ListMoneyTransfersEndpoint(httpWrapper, "credit");
		transferBalance = new TransferMoneyEndpoint(httpWrapper, "balance");
		transferCredit = new TransferMoneyEndpoint(httpWrapper, "credit");
		transferNumber = new TransferNumberEndpoint(httpWrapper);
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
	 */
	public Account updateSubaccount(UpdateSubaccountRequest request) {
		return updateSubaccount.execute(requireRequest(request));
	}

	/**
	 * Retrieve all subaccounts owned by the primary account.
	 *
	 * @return The primary account details and list of subaccounts associated with it.
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
	 */
	public Account getSubaccount(String subaccountApiKey) {
		return getSubaccount.execute(AbstractTransfer.validateAccountKey(subaccountApiKey, "Subaccount"));
	}

	/**
	 * Retrieve a list of credit transfers that have taken place for the primary account within a specified time period.
	 *
	 * @return The list of credit transfers.
	 * @see #listCreditTransfers(ListTransfersFilter)
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
	 */
	public List<MoneyTransfer> listCreditTransfers(ListTransfersFilter filter) {
		return listCreditTransfers.execute(requireRequest(filter)).getCreditTransfers();
	}

	/**
	 * Retrieve a list of balance transfers that have taken place for the primary account within a specified time period.
	 *
	 * @return The list of balance transfers.
	 * @see #listBalanceTransfers(ListTransfersFilter)
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
	 */
	public NumberTransfer transferNumber(NumberTransfer request) {
		return transferNumber.execute(requireRequest(request));
	}
}
