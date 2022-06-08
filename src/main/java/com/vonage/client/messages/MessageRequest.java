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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.messages.internal.Channel;
import com.vonage.client.messages.internal.E164;
import com.vonage.client.messages.internal.MessageType;

import java.util.Objects;

/**
 * Abstract base class of all Messages sent via the Messages v1 API.
 * All subclasses follow a builder pattern to enable easy construction.
 * The design philosophy is "correct by construction": that is, validation
 * occurs when calling the {@link Builder#build()} method. It is still the
 * responsibility of the user to ensure all required parameters are set.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public abstract class MessageRequest {
	final MessageType messageType;
	final Channel channel;
	final String clientRef;
	protected String from;
	protected String to;

	/**
	 * Constructor where all of this class's fields should be set.
	 *
	 * @param builder The mutable builder object used to assign this MessageRequest's fields from.
	 */
	protected MessageRequest(Builder<?, ?> builder) {
		messageType = Objects.requireNonNull(builder.messageType, "Message type cannot be null");
		channel = Objects.requireNonNull(builder.channel, "Channel cannot be null");
		if (!channel.getSupportedMessageTypes().contains(builder.messageType)) {
			throw new IllegalArgumentException(messageType+" cannot be sent via "+channel);
		}
		clientRef = validateClientReference(builder.clientRef);
		from = builder.from;
		to = builder.to;
		validateSenderAndRecipient(from, to);
	}

	/**
	 * Validates and possibly sanitizes the client reference.
	 *
	 * @param clientRef The clientRef field passed in from the builder.
	 * @return The clientRef to use; usually the same as the argument.
	 */
	protected String validateClientReference(String clientRef) {
		if (clientRef != null && clientRef.length() > 40) {
			throw new IllegalArgumentException("Client reference cannot be longer than 40 characters");
		}
		return clientRef;
	}

	/**
	 * This method is used to validate the format of sender and recipient fields. By default,
	 * this method checks that both the sender and recipient are E164 compliant numbers.
	 * Sublasses may override this method to change this behaviour, or to re-assign the
	 * sender and recipient fields to be well-formed / standardised / compliant.
	 *
	 * @param from The sender number or ID passed in from the builder.
	 * @param to The receipient number or ID passed in from the builder.
	 * @throws IllegalArgumentException If the sender or recipient are invalid / malformed.
	 */
	protected void validateSenderAndRecipient(String from, String to) throws IllegalArgumentException {
		this.from = new E164(from).toString();
		this.to = new E164(to).toString();
	}

	@JsonProperty("message_type")
	public MessageType getMessageType() {
		return messageType;
	}

	@JsonProperty("channel")
	public Channel getChannel() {
		return channel;
	}

	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	@JsonProperty("to")
	public String getTo() {
		return to;
	}

	@JsonProperty("client_ref")
	public String getClientRef() {
		return clientRef;
	}

	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this MessageRequest object.
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

	@SuppressWarnings("unchecked")
	public abstract static class Builder<M extends MessageRequest, B extends Builder<? extends M, ? extends B>> {
		final Channel channel;
		final MessageType messageType;
		protected String from, to, clientRef;

		protected Builder() {
			messageType = getMessageType();
			channel = getChannel();
		}

		/**
		 * The type of message to send.
		 *
		 * @return The MessageType.
		 */
		protected abstract MessageType getMessageType();

		/**
		 * The service to send the message through.
		 *
		 * @return The Channel.
		 */
		protected abstract Channel getChannel();

		/**
		 * Sets the sender number or ID.
		 *
		 * @param from The number or ID to send the message from.
		 * @return This builder.
		 */
		public B from(String from) {
			this.from = from;
			return (B) this;
		}

		/**
		 * Sets the recipient number or ID.
		 *
		 * @param to The number or ID to send the message to.
		 * @return This builder.
		 */
		public B to(String to) {
			this.to = to;
			return (B) this;
		}

		/**
		 * Sets the client reference, which will be present in every message status.
		 *
		 * @param clientRef Client reference of up to 40 characters.
		 * @return This builder.
		 */
		public B clientRef(String clientRef) {
			this.clientRef = clientRef;
			return (B) this;
		}

		/**
		 * Builds the MessageRequest.
		 *
		 * @return A MessageRequest, populated with all fields from this builder.
		 */
		public abstract M build();
	}
}
