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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.E164;
import java.net.URI;
import java.util.Objects;

/**
 * Abstract base class of all Messages sent via the Messages v1 API. All subclasses follow a
 * builder pattern to enable easy construction. The design philosophy is "correct by construction":
 * that is, validation occurs when calling the {@link Builder#build()} method. It is still the
 * responsibility of the user to ensure all required parameters are set. The Javadoc for each parameter
 * in the builders indicates whether it is mandatory (REQUIRED) or not (OPTIONAL). Failure to specify
 * mandatory parameters will result in {@link NullPointerException} being thrown. Parameters which are
 * invalid (i.e. have malformed values, such as empty strings) will result in {@link IllegalArgumentException}
 * being thrown. The documentation on each parameter should provide clarity on parameter restrictions.
 */
public abstract class MessageRequest extends JsonableBaseObject {
	final MessageType messageType;
	final Channel channel;
	final String clientRef;
	final URI webhookUrl;
	final MessagesVersion webhookVersion;
	protected String from, to;

	/**
	 * Constructor where all of this class's fields should be set. This is protected
	 * to prevent users form explicitly calling it; this should only be called from the
	 * {@link Builder#build()} method. Subclasses should hide this constructor as well
	 * to avoid potentially confusing users on how to construct this object.
	 *
	 * @param builder The mutable builder object used to assign this MessageRequest's fields from.
	 * @param channel The service to send the message through.
	 * @param messageType The type of message to send.
	 */
	protected MessageRequest(Builder<?, ?> builder, Channel channel, MessageType messageType) {
		this.messageType = Objects.requireNonNull(messageType, "Message type cannot be null");
		this.channel = Objects.requireNonNull(channel, "Channel cannot be null");
		if (!this.channel.getSupportedOutboundMessageTypes().contains(this.messageType)) {
			throw new IllegalArgumentException(this.messageType +" cannot be sent via "+ this.channel);
		}
		clientRef = validateClientReference(builder.clientRef);
		from = builder.from;
		to = builder.to;
		webhookUrl = builder.webhookUrl;
		webhookVersion = builder.webhookVersion;
		validateSenderAndRecipient(from, to);
	}

	/**
	 * Validates and possibly sanitizes the client reference.
	 *
	 * @param clientRef The clientRef field passed in from the builder.
	 * @return The clientRef to use; usually the same as the argument.
	 */
	protected String validateClientReference(String clientRef) {
		int limit = 100;
		if (clientRef != null && clientRef.length() > limit) {
			throw new IllegalArgumentException("Client reference cannot be longer than "+limit+" characters");
		}
		return clientRef;
	}

	/**
	 * This method is used to validate the format of sender and recipient fields. By default,
	 * this method checks that the recipient is an E164-compliant number and that the sender is not blank.
	 * Subclasses may re-assign the sender and recipient fields to be well-formed / standardised / compliant.
	 *
	 * @param from The sender number or ID passed in from the builder.
	 * @param to The recipient number or ID passed in from the builder.
	 * @throws IllegalArgumentException If the sender or recipient are invalid / malformed.
	 */
	protected void validateSenderAndRecipient(String from, String to) throws IllegalArgumentException {
		if (from == null || from.isEmpty()) {
			throw new IllegalArgumentException("Sender cannot be empty");
		}
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

	@JsonProperty("webhook_url")
	public URI getWebhookUrl() {
		return webhookUrl;
	}

	@JsonProperty("webhook_version")
	public MessagesVersion getWebhookVersion() {
		return webhookVersion;
	}

	/**
	 * Mutable Builder class, designed to simulate named parameters to allow for convenient
	 * construction of MessageRequests. Subclasses should add their own mutable parameters
	 * and a method for setting them whilst returning the builder, to allow for chaining.
	 *
	 * @param <M> The type of MessageRequest that will be constructed when calling the {@link #build()} method.
	 * @param <B> The type of Builder that will be returned when chaining method calls. This is necessary
	 *           to enable the methods to be called in any order and still return the most specific
	 *           concrete subtype of builder, rather than this base class.
	 */
	@SuppressWarnings("unchecked")
	public abstract static class Builder<M extends MessageRequest, B extends Builder<? extends M, ? extends B>> {
		protected String from, to, clientRef;
		protected URI webhookUrl;
		protected MessagesVersion webhookVersion;

		/**
		 * Protected constructor to prevent users from explicitly creating this object.
		 * This should only be called by the static {@code builder()} method in
		 * the non-abstract subclasses of this builder's parent (declaring) class.
		 */
		protected Builder() {
		}

		/**
		 * (REQUIRED)
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
		 * (REQUIRED)
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
		 * (OPTIONAL)
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
		 * (OPTIONAL)
		 * Specifies the URL to which Status Webhook messages will be sent for this particular message.
		 * Overrides account-level and application-level Status Webhook url settings on a per-message basis.
		 *
		 * @param webhookUrl The status webhook URL as a string.
		 *
		 * @return This builder.
		 *
		 * @since 8.1.0
		 */
		public B webhookUrl(String webhookUrl) {
			return webhookUrl(URI.create(webhookUrl));
		}

		/**
		 * (OPTIONAL)
		 * Specifies the URL to which Status Webhook messages will be sent for this particular message.
		 * Overrides account-level and application-level Status Webhook url settings on a per-message basis.
		 *
		 * @param webhookUrl The status webhook URL.
		 *
		 * @return This builder.
		 *
		 * @since 8.1.0
		 */
		private B webhookUrl(URI webhookUrl) {
			this.webhookUrl = webhookUrl;
			return (B) this;
		}

		/**
		 * Specifies which version of the Messages API will be used to send Status Webhook messages for
		 * this particular message. For example, if {@linkplain MessagesVersion#V0_1} is set, then the
		 * JSON body of Status Webhook messages for this message will be sent in Messages v0.1 format.
		 * Over-rides account-level and application-level API version settings on a per-message basis.
		 *
		 * @param webhookVersion The messages API version enum.
		 *
		 * @return This builder.
		 *
		 * @since 8.1.0
		 */
		public B webhookVersion(MessagesVersion webhookVersion) {
			this.webhookVersion = webhookVersion;
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
