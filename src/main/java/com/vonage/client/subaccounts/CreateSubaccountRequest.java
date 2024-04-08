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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

public class CreateSubaccountRequest extends JsonableBaseObject {
	String primaryAccountApiKey;
	private final String name, secret;
	private final Boolean usePrimaryAccountBalance;

	CreateSubaccountRequest(Builder builder) {
		if ((name = builder.name) == null || (name.trim().isEmpty() || name.length() > 80) ){
			throw new IllegalArgumentException("Name must be between 1 and 80 characters long.");
		}
		if ((secret = builder.secret) != null && (secret.trim().length() < 8 || secret.length() > 25)) {
			throw new IllegalArgumentException("Secret must be between 8 and 25 characters long.");
		}
		usePrimaryAccountBalance = builder.usePrimaryAccountBalance;
	}

	/**
	 * The primary account's API key.
	 *
	 * @return The primary account API key.
	 */
	@JsonProperty("primary_account_api_key")
	String getPrimaryAccountApiKey() {
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
	 * Whether to use the primary account's balance.
	 *
	 * @return {@code true} if the balance is shared with the primary account or {@code null} if unspecified (the default).
	 */
	@JsonProperty("use_primary_account_balance")
	public Boolean getUsePrimaryAccountBalance() {
		return usePrimaryAccountBalance;
	}

	/**
	 * Subaccount API secret.
	 *
	 * @return The secret to associate with this API key.
	 */
	@JsonProperty("secret")
	public String getSecret() {
		return secret;
	}
	
	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String name, secret;
		private Boolean usePrimaryAccountBalance;
	
		Builder() {}

		/**
		 * (REQUIRED) Name of the subaccount.
		 *
		 * @param name Subaccount display name.
		 *
		 * @return This builder.
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * (OPTIONAL) Whether to use the primary account's balance.
		 *
		 * @param usePrimaryAccountBalance {@code true} if the balance should be shared with the primary account.
		 *
		 * @return This builder.
		 */
		public Builder usePrimaryAccountBalance(boolean usePrimaryAccountBalance) {
			this.usePrimaryAccountBalance = usePrimaryAccountBalance;
			return this;
		}

		/**
		 * (OPTIONAL) Secret for the subaccount. The requirements are as follows:
		 * <ul>
		 *   <li>Between 8 and 25 characters long</li>
		 *   <li>1 lowercase letter</li>
		 *   <li>1 capital letter</li>
		 *   <li>1 digit</li>
		 *   <li>Must be unique.</li>
		 * </ul>
		 *
		 * @param secret Subaccount API secret.
		 *
		 * @return This builder.
		 */
		public Builder secret(String secret) {
			this.secret = secret;
			return this;
		}
	
		/**
		 * Builds the {@linkplain CreateSubaccountRequest}.
		 *
		 * @return An instance of CreateSubaccountRequest, populated with all fields from this builder.
		 */
		public CreateSubaccountRequest build() {
			return new CreateSubaccountRequest(this);
		}
	}
}
