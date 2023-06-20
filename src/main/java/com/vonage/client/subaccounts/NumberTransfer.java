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
import com.vonage.client.common.E164;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberTransfer extends AbstractTransfer {
	private String number, country;

	protected NumberTransfer() {
	}

	NumberTransfer(Builder builder) {
		super(builder);
		number = new E164(builder.number).toString();
		if ((country = builder.country) == null || country.trim().length() != 2) {
			throw new IllegalArgumentException("ISO 3166-1 alpha-2 country code is required.");
		}
	}

	/**
	 * Country the number is registered in (ISO 3166-1 alpha-2 format).
	 * 
	 * @return The two-letter country code for the number.
	 */
	@JsonProperty("country")
	public String getCountry() {
		return country;
	}

	/**
	 * The number being transferred between accounts.
	 * 
	 * @return The transfer phone number in E164 format.
	 */
	@JsonProperty("number")
	public String getNumber() {
		return number;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static NumberTransfer fromJson(String json) {
		NumberTransfer transfer = new NumberTransfer();
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
	
	public static class Builder extends AbstractTransfer.Builder<NumberTransfer, Builder> {
		private String number, country;
	
		Builder() {}

		/**
		 * (REQUIRED) Country the number is registered in, in ISO 3166-1 alpha-2 format.
		 *
		 * @param country The two letter country code for the number.
		 *
		 * @return This builder.
		 */
		public Builder country(String country) {
			this.country = country;
			return this;
		}

		/**
		 * (REQUIRED) The number being transferred between accounts.
		 *
		 * @param number The transfer phone number in E164 format.
		 *
		 * @return This builder.
		 */
		public Builder number(String number) {
			this.number = number;
			return this;
		}

	
		/**
		 * Builds the {@linkplain NumberTransfer}.
		 *
		 * @return An instance of NumberTransfer, populated with all fields from this builder.
		 */
		public NumberTransfer build() {
			return new NumberTransfer(this);
		}
	}
}
