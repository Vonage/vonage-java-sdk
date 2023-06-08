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
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AbstractMoneyTransfer extends AbstractTransfer {
	private Instant createdAt;
	private BigDecimal amount;
	private String reference;

	protected AbstractMoneyTransfer() {
	}

	AbstractMoneyTransfer(Builder<?, ?> builder) {
		super(builder);
		amount = Objects.requireNonNull(builder.amount, "Amount is required.");
		reference = builder.reference;
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

	@SuppressWarnings("unchecked")
	protected static abstract class Builder<T extends AbstractMoneyTransfer, B extends Builder<? extends T, ? extends B>> extends AbstractTransfer.Builder<T, B> {
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
		public B amount(BigDecimal amount) {
			this.amount = amount;
			return (B) this;
		}

		/**
		 * (REQUIRED) Transfer amount.
		 *
		 * @param amount The monetary amount to transfer between accounts.
		 *
		 * @return This builder.
		 */
		public B amount(double amount) {
			return amount(BigDecimal.valueOf(amount));
		}

		/**
		 * (OPTIONAL) Reference for the transfer.
		 *
		 * @param reference The transfer reference message, or {@code null} if not set (the default).
		 *
		 * @return This builder.
		 */
		public B reference(String reference) {
			this.reference = reference;
			return (B) this;
		}
	}

}
