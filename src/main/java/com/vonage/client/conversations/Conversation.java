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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.users.channels.Channel;
import java.net.URI;
import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversation extends BaseConversation {
	private ConversationStatus state;
	private Integer sequenceNumber;
	private ConversationProperties properties;
	private List<Channel> numbers;
	private Callback callback;

	protected Conversation() {
	}

	Conversation(Builder builder) {
		name = builder.name;
		displayName = builder.displayName;
		imageUrl = builder.imageUrl;
		properties = builder.properties;
		numbers = builder.numbers;
		callback = builder.callback;
	}

	/**
	 * The state the conversation is in.
	 *
	 * @return The conversation state as an enum, or {@code null} if unknown.
	 */
	@JsonProperty("state")
	public ConversationStatus getState() {
		return state;
	}

	/**
	 * The last Event ID in this conversation. This ID can be used to retrieve a specific event.
	 *
	 * @return The last event ID as an integer, or {@code null} if unknown.
	 */
	@JsonProperty("sequence_number")
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Properties for this conversation.
	 *
	 * @return The conversation properties object, or {@code null} if not applicable.
	 */
	@JsonProperty("properties")
	public ConversationProperties getProperties() {
		return properties;
	}

	/**
	 * Channels containing the contact numbers for this conversation.
	 *
	 * @return The list of channels associated with this conversation, or {@code null} if unspecified.
	 */
	@JsonProperty("numbers")
	public List<Channel> getNumbers() {
		return numbers;
	}

	/**
	 * Specifies callback parameters for webhooks.
	 *
	 * @return The callback properties, or {@code null} if unspecified.
	 */
	@JsonProperty("callback")
	public Callback getCallback() {
		return callback;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static Conversation fromJson(String json) {
		return Jsonable.fromJson(json);
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
		private String name, displayName;
		private URI imageUrl;
		private ConversationProperties properties;
		private List<Channel> numbers;
		private Callback callback;
	
		Builder() {}

		/**
		 * Internal conversation name. Must be unique.
		 *
		 * @param name The conversation name.
		 *
		 * @return This builder.
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * The public facing name of the conversation.
		 *
		 * @param displayName The display name.
		 *
		 * @return This builder.
		 */
		public Builder displayName(String displayName) {
			this.displayName = displayName;
			return this;
		}

		/**
		 * An image URL that you associate with the conversation.
		 *
		 * @param imageUrl The image URL.
		 *
		 * @return This builder.
		 */
		public Builder imageUrl(URI imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}

		/**
		 * Properties for this conversation.
		 *
		 * @param properties The conversation properties object.
		 *
		 * @return This builder.
		 */
		public Builder properties(ConversationProperties properties) {
			this.properties = properties;
			return this;
		}

		/**
		 * Channels containing the contact numbers for this conversation.
		 *
		 * @param numbers The list of channels associated with this conversation.
		 *
		 * @return This builder.
		 */
		public Builder numbers(List<Channel> numbers) {
			this.numbers = numbers;
			return this;
		}

		/**
		 * Specifies callback parameters for webhooks.
		 *
		 * @param callback The callback properties, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder callback(Callback callback) {
			this.callback = callback;
			return this;
		}

	
		/**
		 * Builds the {@linkplain Conversation}.
		 *
		 * @return An instance of Conversation, populated with all fields from this builder.
		 */
		public Conversation build() {
			return new Conversation(this);
		}
	}
}
