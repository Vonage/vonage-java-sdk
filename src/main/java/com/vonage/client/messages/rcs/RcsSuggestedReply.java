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
import java.util.Objects;

/**
 * Represents a suggested reply chip in an RCS message.
 *
 * @since 9.6.0
 */
public class RcsSuggestedReply extends JsonableBaseObject implements RcsSuggestion {
	private String text, postbackData;

	protected RcsSuggestedReply() {
	}

	private RcsSuggestedReply(Builder builder) {
		this.text = Objects.requireNonNull(builder.text, "Text is required.");
		if (text.length() > 25) {
			throw new IllegalArgumentException("Text must be 25 characters or less.");
		}
		this.postbackData = Objects.requireNonNull(builder.postbackData, "Postback data is required.");
	}

	@Override
	@JsonProperty("type")
	public String getType() {
		return "reply";
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
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String text, postbackData;

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
		 * The data that will be sent back in the {@code reply.id} property of a {@code reply} message
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
		 * Builds the RcsSuggestedReply object.
		 *
		 * @return A new {@linkplain RcsSuggestedReply}.
		 */
		public RcsSuggestedReply build() {
			return new RcsSuggestedReply(this);
		}
	}
}
