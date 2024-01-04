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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class ListTransfersResponseWrapper implements Jsonable {

	ListTransfersResponseWrapper() {}

	@JsonProperty("_embedded") private Embedded embedded;

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static final class Embedded {
		@JsonProperty("balance_transfers") private List<MoneyTransfer> balanceTransfers;
		@JsonProperty("credit_transfers") private List<MoneyTransfer> creditTransfers;
	}

	@JsonIgnore
	public List<MoneyTransfer> getBalanceTransfers() {
		return embedded != null ? embedded.balanceTransfers : null;
	}

	@JsonIgnore
	public List<MoneyTransfer> getCreditTransfers() {
		return embedded != null ? embedded.creditTransfers : null;
	}
}
