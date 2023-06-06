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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.util.UUID;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceTransfer extends AbstractMoneyTransfer {
	private UUID balanceTransferId;

	protected BalanceTransfer() {
	}

	BalanceTransfer(Builder builder) {
		super(builder);
	}

	/**
	 * Unique balance transfer ID.
	 * 
	 * @return The UUID of the balance transfer.
	 */
	@JsonProperty("balance_transfer_id")
	public UUID getBalanceTransferId() {
		return balanceTransferId;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static BalanceTransfer fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, BalanceTransfer.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce BalanceTransfer from json.", ex);
		}
	}

	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends AbstractMoneyTransfer.Builder<BalanceTransfer, Builder> {
		Builder() {}
	
		/**
		 * Builds the {@linkplain BalanceTransfer}.
		 *
		 * @return An instance of BalanceTransfer, populated with all fields from this builder.
		 */
		public BalanceTransfer build() {
			return new BalanceTransfer(this);
		}
	}
}
