/*
 *   Copyright 2024 Vonage
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
import com.vonage.client.messages.MessageType;
import java.util.Objects;

/**
 * {@link com.vonage.client.messages.Channel#WHATSAPP}, {@link MessageType#REACTION} request.
 *
 * @since 8.11.0
 */
public final class WhatsappReactionRequest extends WhatsappRequest {
	final Reaction reaction;

	WhatsappReactionRequest(Builder builder) {
		super(builder, MessageType.REACTION);
		reaction = Objects.requireNonNull(builder.reaction, "Reaction is required.");
	}

	@JsonProperty("reaction")
	public Reaction getReaction() {
		return reaction;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends WhatsappRequest.Builder<WhatsappReactionRequest, Builder> {
		private Reaction reaction;

		Builder() {}

		/**
		 * (REQUIRED unless {@linkplain #unreact()} is called)
		 * Set the reaction to the message. This must be a single emoji character in UTF-8 encoding.
		 *
		 * @param emoji The emoji character to use as a string.
		 * @return This builder.
		 */
		public Builder reaction(String emoji) {
			reaction = new Reaction(emoji);
			return this;
		}

		/**
		 * (REQUIRED unless {@linkplain #reaction(String)} is called)
		 * Unreact to the message.
		 *
		 * @return This builder.
		 */
		public Builder unreact() {
			reaction = new Reaction();
			return this;
		}

		@Override
		public WhatsappReactionRequest build() {
			return new WhatsappReactionRequest(this);
		}
	}
}
