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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.util.Objects;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberTransfer {
	private String from;
	private String to;
	private String country;
	private String number;

	protected NumberTransfer() {
	}

	NumberTransfer(Builder builder) {
		from = Objects.requireNonNull(builder.from);
		to = Objects.requireNonNull(builder.to);
		country = builder.country;
		number = builder.number;
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
	 * (REQUIRED) Country the number is registered in.
	 * 
	 * @return The two letter country code for the number.
	 */
	@JsonProperty("country")
	public String getCountry() {
		return country;
	}

	/**
	 * (REQUIRED) The number being transferred between accounts.
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
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, NumberTransfer.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce NumberTransfer from json.", ex);
		}
	}

	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this NumberTransfer object.
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
		private String from;
		private String to;
		private String country;
		private String number;
	
		Builder() {}
	
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
		 * (REQUIRED) Country the number is registered in.
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
