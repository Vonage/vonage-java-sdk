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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UpdateSubaccountRequest {
	@JsonIgnore final String subaccountApiKey;
	private final String name;
	private final Boolean usePrimaryAccountBalance, suspended;

	UpdateSubaccountRequest(Builder builder) {
		subaccountApiKey = AbstractTransfer.validateAccountKey(builder.subaccountApiKey, "Subaccount");
		name = builder.name;
		usePrimaryAccountBalance = builder.usePrimaryAccountBalance;
		suspended = builder.suspended;
	}

	/**
	 * (OPTIONAL) Name of the subaccount.
	 * 
	 * @return The updated subaccount name, or {@code null} if not set (the default).
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * (OPTIONAL) Whether to use the primary account's balance.
	 * 
	 * @return {@code true} if the balance is shared with the primary account or {@code null} if not set (the default).
	 */
	@JsonProperty("use_primary_account_balance")
	public Boolean getUsePrimaryAccountBalance() {
		return usePrimaryAccountBalance;
	}

	/**
	 * (OPTIONAL) Whether to suspend this subaccount.
	 * 
	 * @return Whether this subaccount should be suspended, or {@code null} if not set (the default).
	 */
	@JsonProperty("suspended")
	public Boolean getSuspended() {
		return suspended;
	}
	
	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this UpdateSubaccountRequest object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}
	
	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @param subaccountApiKey (REQUIRED) The subaccount's API key.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder(String subaccountApiKey) {
		return new Builder(subaccountApiKey);
	}
	
	public static class Builder {
		private final String subaccountApiKey;
		private String name;
		private Boolean usePrimaryAccountBalance, suspended;
	
		Builder(String subaccountApiKey) {
			this.subaccountApiKey = subaccountApiKey;
		}

		/**
		 * (OPTIONAL) Name of the subaccount.
		 *
		 * @param name The updated subaccount name, or {@code null} if not set (the default).
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
		 * @param usePrimaryAccountBalance {@code true} if the balance is shared with the primary account or {@code null} if not set (the default).
		 *
		 * @return This builder.
		 */
		public Builder usePrimaryAccountBalance(Boolean usePrimaryAccountBalance) {
			this.usePrimaryAccountBalance = usePrimaryAccountBalance;
			return this;
		}

		/**
		 * (OPTIONAL) Whether to suspend this subaccount.
		 *
		 * @param suspended Whether this subaccount should be suspended, or {@code null} if not set (the default).
		 *
		 * @return This builder.
		 */
		public Builder suspended(Boolean suspended) {
			this.suspended = suspended;
			return this;
		}

		/**
		 * Builds the {@linkplain UpdateSubaccountRequest}.
		 *
		 * @return An instance of UpdateSubaccountRequest, populated with all fields from this builder.
		 */
		public UpdateSubaccountRequest build() {
			return new UpdateSubaccountRequest(this);
		}
	}
}
