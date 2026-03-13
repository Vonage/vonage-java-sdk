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
import com.vonage.client.common.MessageType;
import java.util.Objects;

/**
 * {@link com.vonage.client.messages.Channel#RCS}, {@link MessageType#CARD} request.
 *
 * @since 9.6.0
 */
public final class RcsCardRequest extends RcsRequest {
	private RcsCard card;

	RcsCardRequest(Builder builder) {
		super(builder, MessageType.CARD);
		this.card = Objects.requireNonNull(builder.card, "Card is required.");
	}

	/**
	 * The card to send.
	 *
	 * @return The RCS card object.
	 */
	@JsonProperty("card")
	public RcsCard getCard() {
		return card;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends RcsRequest.Builder<RcsCardRequest, Builder> {
		private RcsCard card;

		Builder() {}

		/**
		 * (REQUIRED)
		 * The card to send.
		 *
		 * @param card The RCS card object.
		 * @return This builder.
		 */
		public Builder card(RcsCard card) {
			this.card = card;
			return this;
		}

		@Override
		public RcsCardRequest build() {
			return new RcsCardRequest(this);
		}
	}
}
