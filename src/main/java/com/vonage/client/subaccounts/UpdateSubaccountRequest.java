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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UpdateSubaccountRequest {
	private final String primaryAccountApiKey;
	private final String name;
	private final Boolean usePrimaryAccountBalance;
	private final String subaccountApiKey;
	private final Boolean suspended;

	UpdateSubaccountRequest(Builder builder) {
		primaryAccountApiKey = builder.primaryAccountApiKey;
		name = builder.name;
		usePrimaryAccountBalance = builder.usePrimaryAccountBalance;
		subaccountApiKey = builder.subaccountApiKey;
		suspended = builder.suspended;
	}

	/**
	 * (REQUIRED) The primary account's API key.
	 * 
	 * @return The primary account API key.
	 */
	@JsonProperty("primary_account_api_key")
	public String getPrimaryAccountApiKey() {
		return primaryAccountApiKey;
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
	public Boolean getUsePrimaryAccountBalance() {
		return usePrimaryAccountBalance;
	}

	/**
	 * (REQUIRED) The subaccount's API key.
	 * 
	 * @return The subaccount API key.
	 */
	public String getSubaccountApiKey() {
		return subaccountApiKey;
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
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+' '+toJson();
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
		private String primaryAccountApiKey;
		private String name;
		private Boolean usePrimaryAccountBalance;
		private String subaccountApiKey;
		private Boolean suspended;
	
		Builder() {}
	
		/**
		 * (REQUIRED) The primary account's API key.
		 *
		 * @param primaryAccountApiKey The primary account API key.
		 *
		 * @return This builder.
		 */
		public Builder primaryAccountApiKey(String primaryAccountApiKey) {
			this.primaryAccountApiKey = primaryAccountApiKey;
			return this;
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
		 * (REQUIRED) The subaccount's API key.
		 *
		 * @param subaccountApiKey The subaccount API key.
		 *
		 * @return This builder.
		 */
		public Builder subaccountApiKey(String subaccountApiKey) {
			this.subaccountApiKey = subaccountApiKey;
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
