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
import com.vonage.client.VonageClient;
import java.util.List;

/**
 * A client for talking to the Vonage SubaccountsClient API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getSubaccountsClient()}.
 */
public class SubaccountsClient {
	final CreateSubaccountEndpoint createSubaccount;
	final UpdateSubaccountEndpoint updateSubaccount;
	final ListSubaccountsEndpoint listSubaccounts;
	final GetSubaccountEndpoint getSubaccount;
	final ListCreditTransfersEndpoint listCreditTransfers;
	final ListBalanceTransfersEndpoint listBalanceTransfers;
	final TransferCreditEndpoint transferCredit;
	final TransferBalanceEndpoint transferBalance;
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
		listCreditTransfers = new ListCreditTransfersEndpoint(httpWrapper);
		listBalanceTransfers = new ListBalanceTransfersEndpoint(httpWrapper);
		transferCredit = new TransferCreditEndpoint(httpWrapper);
		transferBalance = new TransferBalanceEndpoint(httpWrapper);
		transferNumber = new TransferNumberEndpoint(httpWrapper);
	}

	public Account createSubaccount(CreateSubaccountRequest request) {
		return createSubaccount.execute(request);
	}

	public Account updateSubaccount(UpdateSubaccountRequest request) {
		return updateSubaccount.execute(request);
	}

	public Account listSubaccounts() {
		return listSubaccounts.execute(null);
	}

	public Account getSubaccount(String request) {
		return getSubaccount.execute(request);
	}

	public List<CreditTransfer> listCreditTransfers() {
		return listCreditTransfers.execute(null);
	}

	public List<BalanceTransfer> listBalanceTransfers() {
		return listBalanceTransfers.execute(null);
	}

	public CreditTransfer transferCredit(CreditTransfer request) {
		return transferCredit.execute(request);
	}

	public BalanceTransfer transferBalance(BalanceTransfer request) {
		return transferBalance.execute(request);
	}

	public NumberTransfer transferNumber(NumberTransfer request) {
		return transferNumber.execute(request);
	}
}
