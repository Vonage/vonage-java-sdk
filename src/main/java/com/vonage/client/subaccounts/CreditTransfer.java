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

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CreditTransfer extends AbstractMoneyTransfer {

	CreditTransfer() {
	}

	CreditTransfer(Builder builder) {
		super(builder);
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static CreditTransfer fromJson(String json) {
		CreditTransfer transfer = new CreditTransfer();
		transfer.updateFromJson(json);
		return transfer;
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
