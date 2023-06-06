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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditTransfer {
	private UUID creditTransferId;
	private Instant createdAt;
	private BigDecimal amount;
	private String from;
	private String to;
	private String reference;

	protected CreditTransfer() {
	}

	CreditTransfer(Builder builder) {
		amount = Objects.requireNonNull(builder.amount);
		from = Objects.requireNonNull(builder.from);
		to = Objects.requireNonNull(builder.to);
		reference = builder.reference;
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
	 * The date and time when the transfer was executed.
	 * 
	 * @return The transfer timestamp in ISO 8601 format.
	 */
	@JsonProperty("created_at")
	public Instant getCreatedAt() {
		return createdAt;
	}

	/**
	 * (REQUIRED) Transfer amount.
	 * 
	 * @return The monetary amount to transfer between accounts.
	 */
	@JsonProperty("amount")
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Account to transfer from.
	 * 
	 * @return The transferer account ID.
	 */
	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	/**
	 * (REQUIRED) Account to transfer to.
	 * 
	 * @return The transferee account ID.
	 */
	@JsonProperty("to")
	public String getTo() {
		return to;
	}

	/**
	 * (OPTIONAL) Reference for the transfer.
	 * 
	 * @return The transfer reference message, or {@code null} if not set (the default).
	 */
	@JsonProperty("reference")
	public String getReference() {
		return reference;
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
			return mapper.readValue(json, CreditTransfer.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce CreditTransfer from json.", ex);
		}
	}

	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this CreditTransfer object.
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
		private BigDecimal amount;
		private String from;
		private String to;
		private String reference;
	
		Builder() {}
	
		/**
		 * (REQUIRED) Transfer amount.
		 *
		 * @param amount The monetary amount to transfer between accounts.
		 *
		 * @return This builder.
		 */
		public Builder amount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		/**
		 * Account to transfer from.
		 *
		 * @param from The transferer account ID.
		 *
		 * @return This builder.
		 */
		public Builder from(String from) {
			this.from = from;
			return this;
		}

		/**
		 * (REQUIRED) Account to transfer to.
		 *
		 * @param to The transferee account ID.
		 *
		 * @return This builder.
		 */
		public Builder to(String to) {
			this.to = to;
			return this;
		}

		/**
		 * (OPTIONAL) Reference for the transfer.
		 *
		 * @param reference The transfer reference message, or {@code null} if not set (the default).
		 *
		 * @return This builder.
		 */
		public Builder reference(String reference) {
			this.reference = reference;
			return this;
		}

	
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
