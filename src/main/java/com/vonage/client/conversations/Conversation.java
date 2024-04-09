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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.users.channels.Channel;
import com.vonage.client.users.channels.Pstn;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a Conversation (request and response).
 */
public class Conversation extends BaseConversation {
	private ConversationStatus state;
	private Integer sequenceNumber;
	private ConversationProperties properties;
	private Collection<? extends Channel> numbers;
	private Callback callback;

	protected Conversation() {
	}

	Conversation(Builder builder) {
		if ((name = builder.name) != null && (name.length() > 100 || name.trim().isEmpty())) {
			throw new IllegalArgumentException("Name must be between 1 and 100 characters.");
		}
		if ((displayName = builder.displayName) != null && (displayName.length() > 50 || displayName.trim().isEmpty())) {
			throw new IllegalArgumentException("Display name must be between 1 and 50 characters.");
		}
		imageUrl = builder.imageUrl;
		properties = builder.properties;
		callback = builder.callback;
		if ((numbers = builder.numbers) != null) {
			numbers.forEach(Channel::setTypeField);
		}
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
	 * Currently, only {@link Pstn} (Phone) type is supported.
	 *
	 * @return The channels associated with this conversation, or {@code null} if unspecified.
	 */
	@JsonProperty("numbers")
	public Collection<? extends Channel> getNumbers() {
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
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for creating or updating a Conversation. All fields are optional.
	 */
	public static class Builder {
		private String name, displayName;
		private URI imageUrl;
		private ConversationProperties properties;
		private Collection<? extends Channel> numbers;
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
		 * @param imageUrl The image URL as a string.
		 *
		 * @return This builder.
		 */
		public Builder imageUrl(String imageUrl) {
			this.imageUrl = URI.create(imageUrl);
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
		 * Sets the PSTN numbers for this conversation.
		 *
		 * @param phoneNumber The telephone or mobile number(s) for this conversation in E.164 format.
		 *
		 * @return This builder.
		 */
		public Builder phone(String... phoneNumber) {
			return numbers(Arrays.stream(phoneNumber).map(Pstn::new).toArray(Channel[]::new));
		}

		/**
		 * Channels containing the contact numbers for this conversation.
		 *
		 * @param numbers The channels associated with this conversation.
		 *
		 * @return This builder.
		 */
		Builder numbers(Channel... numbers) {
			return numbers(Arrays.asList(numbers));
		}

		/**
		 * Channels containing the contact numbers for this conversation.
		 *
		 * @param numbers The channels associated with this conversation.
		 *
		 * @return This builder.
		 */
		Builder numbers(Collection<? extends Channel> numbers) {
			this.numbers = new ArrayList<>(numbers);
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
