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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents a typing indicator to display when preparing a response on WhatsApp.
 * <p>
 * This feature allows you to show typing indicators (three successively blinking dots)
 * to let users know that a response is being prepared.
 *
 * @since 9.8.0
 */
public final class ReplyingIndicator extends JsonableBaseObject {
	private final Boolean show;
	private final String type;

	ReplyingIndicator() {
		this.show = null;
		this.type = null;
	}

	private ReplyingIndicator(Builder builder) {
		this.show = builder.show;
		this.type = builder.type;
	}

	/**
	 * Whether to show the typing indicator.
	 *
	 * @return {@code true} if the indicator should be shown, {@code false} otherwise, or {@code null} if not set.
	 */
	@JsonProperty("show")
	public Boolean getShow() {
		return show;
	}

	/**
	 * The type of indicator to display.
	 *
	 * @return The indicator type (currently only "text" is supported), or {@code null} if not set.
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * Creates a new builder for constructing a ReplyingIndicator.
	 *
	 * @return A new Builder instance.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for constructing ReplyingIndicator instances.
	 */
	public static final class Builder {
		private Boolean show;
		private String type;

		private Builder() {}

		/**
		 * Sets whether to show the typing indicator.
		 *
		 * @param show {@code true} to show the indicator, {@code false} to hide it.
		 * @return This builder instance for chaining.
		 */
		public Builder show(boolean show) {
			this.show = show;
			return this;
		}

		/**
		 * Sets the type of typing indicator to display.
		 *
		 * @param type The indicator type. Currently only "text" is supported.
		 * @return This builder instance for chaining.
		 */
		public Builder type(String type) {
			this.type = type;
			return this;
		}

		/**
		 * Builds the ReplyingIndicator instance.
		 *
		 * @return A new ReplyingIndicator instance.
		 */
		public ReplyingIndicator build() {
			return new ReplyingIndicator(this);
		}
	}
}
