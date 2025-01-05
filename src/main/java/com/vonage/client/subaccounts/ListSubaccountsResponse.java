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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.util.List;

/**
 * Response container for {@link SubaccountsClient#listSubaccounts()}.
 */
public class ListSubaccountsResponse extends JsonableBaseObject {
	@JsonProperty("_embedded") private Embedded embedded;

	private static final class Embedded extends JsonableBaseObject {
		@JsonProperty("primary_account") private Account primaryAccount;
		@JsonProperty("subaccounts") private List<Account> subaccounts;
	}

	protected ListSubaccountsResponse() {
	}

	/**
	 * Parent account.
	 * 
	 * @return The primary account details.
	 */
	@JsonIgnore
	public Account getPrimaryAccount() {
		return embedded != null ? embedded.primaryAccount : null;
	}

	/**
	 * The subaccounts associated with the primary account.
	 * 
	 * @return List of subaccount details.
	 */
	@JsonIgnore
	public List<Account> getSubaccounts() {
		return embedded != null ? embedded.subaccounts : null;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static ListSubaccountsResponse fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
