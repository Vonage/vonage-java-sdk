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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vonage.client.VonageResponseParseException;
import java.io.IOException;
import java.util.UUID;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditTransfer extends AbstractMoneyTransfer {
	private UUID creditTransferId;

	protected CreditTransfer() {
	}

	CreditTransfer(Builder builder) {
		super(builder);
	}

	/**
	 * Unique credit transfer ID.
	 * 
	 * @return The UUID of the credit transfer.
	 */
	@JsonProperty("credit_transfer_id")
	public UUID getCreditTransferId() {
		return creditTransferId;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static CreditTransfer fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.readValue(json, CreditTransfer.class);
		}
		catch (IOException ex) {
			throw new VonageResponseParseException("Failed to produce CreditTransfer from json.", ex);
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
	
	public static class Builder extends AbstractMoneyTransfer.Builder<CreditTransfer, Builder> {
		Builder() {}

		/**
		 * Builds the {@linkplain CreditTransfer}.
		 *
		 * @return An instance of CreditTransfer, populated with all fields from this builder.
		 */
		public CreditTransfer build() {
			return new CreditTransfer(this);
		}
	}
}
