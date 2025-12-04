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
import com.vonage.client.messages.TextMessageRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link com.vonage.client.messages.Channel#RCS}, {@link MessageType#TEXT} request.
 *
 * @since 8.11.0
 */
public final class RcsTextRequest extends RcsRequest implements TextMessageRequest {
	private List<RcsSuggestion> suggestions;

	RcsTextRequest(Builder builder) {
		super(builder, MessageType.TEXT);
		if (builder.suggestions != null && !builder.suggestions.isEmpty()) {
			if (builder.suggestions.size() > 11) {
				throw new IllegalArgumentException("A text message can have a maximum of 11 suggestions.");
			}
			this.suggestions = builder.suggestions;
		}
	}

	@Override
	protected int maxTextLength() {
		return 3072;
	}

	@Override
	public String getText() {
		return super.getText();
	}

	/**
	 * An array of suggestion objects to include with the message. You can include up to 11 suggestions per message.
	 *
	 * @return The list of suggestions, or {@code null} if none are set.
	 */
	@JsonProperty("suggestions")
	public List<RcsSuggestion> getSuggestions() {
		return suggestions;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends RcsRequest.Builder<RcsTextRequest, Builder> implements TextMessageRequest.Builder<Builder> {
		private List<RcsSuggestion> suggestions;

		Builder() {}

		/**
		 * (REQUIRED)
		 * The text of the message to send. Limited to 3072 characters, including unicode.
		 *
		 * @param text The text string.
		 * @return This builder.
		 */
		@Override
		public Builder text(String text) {
			return super.text(text);
		}

		/**
		 * (OPTIONAL)
		 * An array of suggestion objects to include with the message. You can include up to 11 suggestions per message.
		 *
		 * @param suggestions The list of suggestions.
		 * @return This builder.
		 */
		public Builder suggestions(List<RcsSuggestion> suggestions) {
			this.suggestions = suggestions;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * An array of suggestion objects to include with the message. You can include up to 11 suggestions per message.
		 *
		 * @param suggestions The suggestions as varargs.
		 * @return This builder.
		 */
		public Builder suggestions(RcsSuggestion... suggestions) {
			return suggestions(Arrays.asList(suggestions));
		}

		/**
		 * (OPTIONAL)
		 * Add a single suggestion to the message. You can include up to 11 suggestions per message.
		 *
		 * @param suggestion The suggestion to add.
		 * @return This builder.
		 */
		public Builder addSuggestion(RcsSuggestion suggestion) {
			if (this.suggestions == null) {
				this.suggestions = new ArrayList<>(11);
			}
			this.suggestions.add(suggestion);
			return this;
		}

		@Override
		public RcsTextRequest build() {
			return new RcsTextRequest(this);
		}
	}
}
