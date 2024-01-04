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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents a subaccount.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Jsonable {
	private String apiKey, primaryAccountApiKey, name, secret;
	private Boolean usePrimaryAccountBalance, suspended;
	private Instant createdAt;
	private BigDecimal balance, creditLimit;

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
	 * API secret of the subaccount.
	 *
	 * @return The subaccount secret if available, or {@code null} if redacted for this response.
	 */
	@JsonProperty("secret")
	public String getSecret() {
		return secret;
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
		return Jsonable.fromJson(json);
	}
}
