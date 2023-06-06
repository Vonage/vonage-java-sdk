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
public class Account {
	private String apiKey;
	private String primaryAccountApiKey;
	private String name;
	private Boolean usePrimaryAccountBalance;
	private Boolean suspended;
	private Instant createdAt;
	private BigDecimal balance;
	private BigDecimal creditLimit;

	protected Account() {
	}

	/**
	 * Unique subaccount ID.
	 * 
	 * @return The subaccount API key.
	 */
	@JsonProperty("api_key")
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Unique primary account ID.
	 * 
	 * @return The primary account API key.
	 */
	@JsonProperty("primary_account_api_key")
	public String getPrimaryAccountApiKey() {
		return primaryAccountApiKey;
	}

	/**
	 * Name of the subaccount.
	 * 
	 * @return The subaccount name.
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * Flag showing if balance is shared with primary account.
	 * 
	 * @return Whether the balance is shared with the primary account.
	 */
	@JsonProperty("use_primary_account_balance")
	public Boolean getUsePrimaryAccountBalance() {
		return usePrimaryAccountBalance;
	}

	/**
	 * Subaccount suspension status.
	 * 
	 * @return Whether this subaccount has been suspended.
	 */
	@JsonProperty("suspended")
	public Boolean getSuspended() {
		return suspended;
	}

	/**
	 * Subaccount creation date and time.
	 * 
	 * @return The subaccount creation timestamp in ISO 8601 format.
	 */
	@JsonProperty("created_at")
	public Instant getCreatedAt() {
		return createdAt;
	}

	/**
	 * Balance of the subaccount.
	 * 
	 * @return The subaccount balance, or {@code null} if it is shared with primary account.
	 */
	@JsonProperty("balance")
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * Credit limit of the subaccount.
	 * 
	 * @return The subaccount credit limit, or {@code null} if it is shared with primary account.
	 */
	@JsonProperty("credit_limit")
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static Account fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, Account.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce Account from json.", ex);
		}
	}
}
