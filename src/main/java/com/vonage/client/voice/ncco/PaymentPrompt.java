/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Class representing prompt settings for {@link PayAction}. Refer to
 * <a href=https://developer.vonage.com/voice/voice-api/ncco-reference#prompts-text-settings>
 * the documentation</a> for details.
 *
 * @since 7.0.0
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PaymentPrompt {
	private final Type type;
	private final String text;
	private final Errors errors;

	private PaymentPrompt(Builder builder) {
		type = builder.type;
		text = builder.text;
		errors = builder.errors;
	}

	public Type getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	public Errors getErrors() {
		return errors;
	}

	/**
	 * Enum representing possible values for payment prompt types.
	 */
	public enum Type {
		CARD_NUMBER,
		EXPIRATION_DATE,
		SECURITY_CODE;

		@JsonValue
		@Override
		public String toString() {
			String[] parts = name().split("_");
			StringBuilder jsonValue = new StringBuilder();
			for (String part : parts) {
				jsonValue.append(part.charAt(0)).append(part.substring(1).toLowerCase());
			}
			return jsonValue.toString();
		}
	}

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	public static class Errors {
		@JsonProperty("Timeout") Timeout timeout;
		@JsonProperty("InvalidCardNumber") InvalidCardNumber invalidCardNumber;
		@JsonProperty("InvalidCardType") InvalidCardType invalidCardType;
		@JsonProperty("InvalidExpirationDate") InvalidExpirationDate invalidExpirationDate;
		@JsonProperty("InvalidSecurityCode") InvalidSecurityCode invalidSecurityCode;

		Errors() {}

		public Timeout getTimeout() {
			return timeout;
		}

		public InvalidCardNumber getInvalidCardNumber() {
			return invalidCardNumber;
		}

		public InvalidCardType getInvalidCardType() {
			return invalidCardType;
		}

		public InvalidExpirationDate getInvalidExpirationDate() {
			return invalidExpirationDate;
		}

		public InvalidSecurityCode getInvalidSecurityCode() {
			return invalidSecurityCode;
		}

		public static abstract class Error {
			private final String text;

			protected Error(String text) {
				this.text = text;
			}

			public String getText() {
				return text;
			}
		}

		public static class Timeout extends Error {
			public Timeout(String text) {
				super(text);
			}
		}

		public static class InvalidCardNumber extends Error {
			public InvalidCardNumber(String text) {
				super(text);
			}
		}

		public static class InvalidCardType extends Error {
			public InvalidCardType(String text) {
				super(text);
			}
		}

		public static class InvalidExpirationDate extends Error {
			public InvalidExpirationDate(String text) {
				super(text);
			}
		}

		public static class InvalidSecurityCode extends Error {
			public InvalidSecurityCode(String text) {
				super(text);
			}
		}
	}

	/**
	 * Entry point for constructing a {@linkplain PaymentPrompt}.
	 *
	 * @param type The prompt type.
	 * @return A new {@linkplain Builder} instance.
	 */
	public static Builder builder(Type type) {
		return new Builder(type);
	}

	public static class Builder {
		private final Type type;
		private String text;
		private Errors errors;

		Builder(Type type) {
			this.type = type;
		}

		/**
		 * @param text The prompt message.
		 * @return This builder.
		 */
		public Builder text(String text) {
			this.text = text;
			return this;
		}

		private Errors initErrors() {
			if (errors == null) {
				errors = new Errors();
			}
			return errors;
		}

		/**
		 * Adds a Timeout error.
		 *
		 * @param errorText The text for the error.
		 * @return This builder.
		 */
		public Builder timeout(String errorText) {
			initErrors().timeout = new Errors.Timeout(errorText);
			return this;
		}

		/**
		 * Adds a Timeout error.
		 *
		 * @param errorText The text for the error.
		 * @return This builder.
		 */
		public Builder invalidCardNumber(String errorText) {
			initErrors().invalidCardNumber = new Errors.InvalidCardNumber(errorText);
			return this;
		}

		/**
		 * Adds an InvalidCardType error.
		 *
		 * @param errorText The text for the error.
		 * @return This builder.
		 */
		public Builder invalidCardType(String errorText) {
			initErrors().invalidCardType = new Errors.InvalidCardType(errorText);
			return this;
		}

		/**
		 * Adds an InvalidExpirationDate error.
		 *
		 * @param errorText The text for the error.
		 * @return This builder.
		 */
		public Builder invalidExpirationDate(String errorText) {
			initErrors().invalidExpirationDate = new Errors.InvalidExpirationDate(errorText);
			return this;
		}

		/**
		 * Adds an InvalidSecurityCode error.
		 *
		 * @param errorText The text for the error.
		 * @return This builder.
		 */
		public Builder invalidSecurityCode(String errorText) {
			initErrors().invalidSecurityCode = new Errors.InvalidSecurityCode(errorText);
			return this;
		}

		/**
		 * Builds the {@link PaymentPrompt} object with this builder's fields..
		 * @return A new {@link PaymentPrompt} instance.
		 */
		public PaymentPrompt build() {
			return new PaymentPrompt(this);
		}
	}
}
