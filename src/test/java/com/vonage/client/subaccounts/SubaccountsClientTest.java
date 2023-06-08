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

public class SubaccountsClientTest extends ClientTest<SubaccountsClient> {
	
	public SubaccountsClientTest() {
		client = new SubaccountsClient(wrapper);
	}

	
	/*@Test
	public void testCreateSubaccount() throws Exception {
		CreateSubaccountRequest request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.createSubaccount(request));
		stubResponseAndAssertThrows(200, () -> client.createSubaccount(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testUpdateSubaccount() throws Exception {
		UpdateSubaccountRequest request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.updateSubaccount(request));
		stubResponseAndAssertThrows(200, () -> client.updateSubaccount(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testListSubaccounts() throws Exception {
		Void request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.listSubaccounts(request));
		stubResponseAndAssertThrows(200, () -> client.listSubaccounts(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testGetSubaccount() throws Exception {
		String request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.getSubaccount(request));
		stubResponseAndAssertThrows(200, () -> client.getSubaccount(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testListCreditTransfers() throws Exception {
		ListTransfersRequest request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.listCreditTransfers(request));
		stubResponseAndAssertThrows(200, () -> client.listCreditTransfers(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testListBalanceTransfers() throws Exception {
		ListTransfersRequest request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.listBalanceTransfers(request));
		stubResponseAndAssertThrows(200, () -> client.listBalanceTransfers(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testTransferCredit() throws Exception {
		CreditTransfer request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.transferCredit(request));
		stubResponseAndAssertThrows(200, () -> client.transferCredit(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testTransferBalance() throws Exception {
		BalanceTransfer request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.transferBalance(request));
		stubResponseAndAssertThrows(200, () -> client.transferBalance(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testTransferNumber() throws Exception {
		NumberTransfer request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.transferNumber(request));
		stubResponseAndAssertThrows(200, () -> client.transferNumber(null), IllegalArgumentException.class);
	}*/
}