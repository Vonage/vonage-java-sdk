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
import com.vonage.client.Jsonable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a credit or balance transfer between accounts.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoneyTransfer extends AbstractTransfer {
	private UUID id;
	private Instant createdAt;
	private BigDecimal amount;
	private String reference;

	protected MoneyTransfer() {
	}

	protected MoneyTransfer(Builder builder) {
		super(builder);
		amount = Objects.requireNonNull(builder.amount, "Amount is required.");
		reference = builder.reference;
	}

	/**
	 * Unique ID of the transfer.
	 *
	 * @return The transfer ID.
	 */
	@JsonProperty("id")
	public UUID getId() {
		return id;
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
	 * Transfer amount.
	 * 
	 * @return The monetary amount to transfer between accounts.
	 */
	@JsonProperty("amount")
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Reference for the transfer.
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
	public static MoneyTransfer fromJson(String json) {
		return Jsonable.fromJson(json);
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends AbstractTransfer.Builder<MoneyTransfer, Builder> {
		private BigDecimal amount;
		private String from, to, reference;
	
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
		 * (REQUIRED) Transfer amount.
		 *
		 * @param amount The monetary amount to transfer between accounts.
		 *
		 * @return This builder.
		 */
		public Builder amount(double amount) {
			return amount(BigDecimal.valueOf(amount));
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
		 * Builds the {@linkplain MoneyTransfer}.
		 *
		 * @return An instance of MoneyTransfer, populated with all fields from this builder.
		 */
		public MoneyTransfer build() {
			return new MoneyTransfer(this);
		}
	}

}
