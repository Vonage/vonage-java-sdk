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

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageResponseParseException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListSubaccountsResponse {
	private Account primaryAccount;
	private List<Account> subaccounts;

	protected ListSubaccountsResponse() {
	}

	/**
	 * Parent account.
	 * 
	 * @return The primary account details.
	 */
	@JsonProperty("primary_account")
	public Account getPrimaryAccount() {
		return primaryAccount;
	}

	/**
	 * The subaccounts associated with the primary account.
	 * 
	 * @return List of subaccount details.
	 */
	@JsonProperty("subaccounts")
	public List<Account> getSubaccounts() {
		return subaccounts;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static ListSubaccountsResponse fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, ListSubaccountsResponse.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce ListSubaccountsResponse from json.", ex);
		}
	}
}
