/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.messages.rcs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.Objects;

/**
 * Represents a suggested action to dial a phone number in an RCS message.
 *
 * @since 9.6.0
 */
public class RcsSuggestedActionDial extends JsonableBaseObject implements RcsSuggestion {
	private String text, postbackData, phoneNumber;
	private URI fallbackUrl;

	protected RcsSuggestedActionDial() {
	}

	private RcsSuggestedActionDial(Builder builder) {
		this.text = Objects.requireNonNull(builder.text, "Text is required.");
		if (text.length() > 25) {
			throw new IllegalArgumentException("Text must be 25 characters or less.");
		}
		this.postbackData = Objects.requireNonNull(builder.postbackData, "Postback data is required.");
		this.phoneNumber = Objects.requireNonNull(builder.phoneNumber, "Phone number is required.");
		this.fallbackUrl = builder.fallbackUrl;
	}

	@Override
	@JsonProperty("type")
	public String getType() {
		return "dial";
	}

	@Override
	@JsonProperty("text")
	public String getText() {
		return text;
	}

	@Override
	@JsonProperty("postback_data")
	public String getPostbackData() {
		return postbackData;
	}

	/**
	 * The phone number to dial in E.164 format.
	 *
	 * @return The phone number string.
	 */
	@JsonProperty("phone_number")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * A URL to open if the device is unable to place a call.
	 *
	 * @return The fallback URL, or {@code null} if not set.
	 */
	@JsonProperty("fallback_url")
	public URI getFallbackUrl() {
		return fallbackUrl;
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
		private String text, postbackData, phoneNumber;
		private URI fallbackUrl;

		/**
		 * (REQUIRED)
		 * The text to display on the suggestion chip.
		 *
		 * @param text The suggestion text (max 25 characters).
		 * @return This builder.
		 */
		public Builder text(String text) {
			this.text = text;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The data that will be sent back in the {@code button.payload} property of a {@code button} message
		 * via the inbound message webhook when the user taps the suggestion chip.
		 *
		 * @param postbackData The postback data string.
		 * @return This builder.
		 */
		public Builder postbackData(String postbackData) {
			this.postbackData = postbackData;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The phone number to dial in E.164 format.
		 *
		 * @param phoneNumber The phone number string.
		 * @return This builder.
		 */
		public Builder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * A URL to open if the device is unable to place a call.
		 *
		 * @param fallbackUrl The fallback URL as a string.
		 * @return This builder.
		 */
		public Builder fallbackUrl(String fallbackUrl) {
			return fallbackUrl(URI.create(fallbackUrl));
		}

		/**
		 * (OPTIONAL)
		 * A URL to open if the device is unable to place a call.
		 *
		 * @param fallbackUrl The fallback URL.
		 * @return This builder.
		 */
		public Builder fallbackUrl(URI fallbackUrl) {
			this.fallbackUrl = fallbackUrl;
			return this;
		}

		/**
		 * Builds the RcsSuggestedActionDial object.
		 *
		 * @return A new {@linkplain RcsSuggestedActionDial}.
		 */
		public RcsSuggestedActionDial build() {
			return new RcsSuggestedActionDial(this);
		}
	}
}
