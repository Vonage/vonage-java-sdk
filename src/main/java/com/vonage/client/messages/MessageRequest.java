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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.E164;
import com.vonage.client.common.MessageType;
import com.vonage.client.messages.internal.MessagePayload;
import java.net.URI;
import java.util.*;

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
	protected String from, to;
	final String clientRef;
	final URI webhookUrl;
	final MessagesVersion webhookVersion;
	final List<MessageRequest> failover;
	protected final Integer ttl;
	final String text;
	protected final Map<String, Object> custom;
	@JsonIgnore protected final MessagePayload media;

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
		if ((ttl = builder.ttl) != null && ttl < 1) {
			throw new IllegalArgumentException("TTL must be positive.");
		}
		validateSenderAndRecipient(from = builder.from, to = builder.to);
		clientRef = validateClientReference(builder.clientRef);
		webhookUrl = builder.webhookUrl;
		webhookVersion = builder.webhookVersion;
		failover = builder.failover;

		MessagePayload media = null;
		Map<String, Object> custom = null;
		String text = null;

		switch (messageType) {
			case TEXT: {
				text = Objects.requireNonNull(builder.text, "Text message cannot be null.");
				if (text.isEmpty()) {
					throw new IllegalArgumentException("Text message cannot be blank.");
				}
				if (text.length() > maxTextLength()) {
					throw new IllegalArgumentException(
							"Text message cannot be longer than " + maxTextLength() + " characters."
					);
				}
				break;
			}
			case CUSTOM: {
				custom = builder.custom != null ? builder.custom : new LinkedHashMap<>(8);
				break;
			}
			case IMAGE: case AUDIO: case VIDEO: case FILE: case VCARD: {
				media = new MessagePayload(builder.url, builder.caption, builder.name);
				break;
			}
			default: break;
		}
		this.text = text;
		this.media = media;
		this.custom = custom;
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
			throw new IllegalArgumentException("Client reference cannot be longer than "+limit+" characters.");
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
			throw new IllegalArgumentException("Sender cannot be empty.");
		}
		this.to = new E164(to).toString();
	}

	/**
	 * Sets the maximum text length for text messages.
	 *
	 * @return The maximum text message string length.
	 * @since 8.11.0
	 */
	@JsonIgnore
	protected int maxTextLength() {
		return 1000;
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

	@JsonProperty("failover")
	public List<MessageRequest> getFailover() {
		return failover;
	}

	@JsonProperty("ttl")
	protected Integer getTtl() {
		return ttl;
	}

	@JsonProperty("text")
	protected String getText() {
		return text;
	}

	@JsonProperty("custom")
	protected Map<String, ?> getCustom() {
		return custom;
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
		private String from, to, clientRef, text, url, caption, name;
		private URI webhookUrl;
		private MessagesVersion webhookVersion;
		private List<MessageRequest> failover;
		private Integer ttl;
		private Map<String, Object> custom;

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
		 * Custom payload. The schema of a custom object can vary widely according to the channel.
		 * Please consult the relevant documentation for details.
		 *
		 * @param payload The custom payload properties to send as a Map.
		 * @return This builder.
		 */
		protected B custom(Map<String, ?> payload) {
			this.custom = new LinkedHashMap<>(payload);
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
		 * (OPTIONAL)
		 * The duration in milliseconds the delivery of a message will be attempted. By default, Vonage attempts
		 * delivery for 72 hours, however the maximum effective value depends on the operator and is typically
		 * 24 to 48 hours. We recommend this value should be kept at its default or at least 30 minutes.
		 *
		 * @param ttl The time-to-live for this message before abandoning delivery attempts, in milliseconds.
		 *
		 * @return This builder.
		 */
		protected B ttl(int ttl) {
			this.ttl = ttl;
			return (B) this;
		}

		/**
		 * (REQUIRED)
		 * Sets the text field.
		 *
		 * @param text The text string.
		 * @return This builder.
		 */
		protected B text(String text) {
			this.text = text;
			return (B) this;
		}

		/**
		 * (REQUIRED)
		 * Sets the media URL.
		 *
		 * @param url The URL as a string.
		 * @return This builder.
		 */
		protected B url(String url) {
			this.url = url;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Additional text to accompany the media. Must be between 1 and 2000 characters.
		 *
		 * @param caption The caption string.
		 * @return This builder.
		 */
		protected B caption(String caption) {
			this.caption = caption;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * The media name.
		 *
		 * @param name The name string.
		 * @return This builder.
		 */
		protected B name(String name) {
			this.name = name;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Sets the failover messages to be sent if the primary message fails.
		 *
		 * @param failover The failover message(s).
		 * @return This builder.
		 *
		 * @since 9.3.0
		 */
		public B failover(MessageRequest... failover) {
			return failover(Arrays.asList(failover));
		}

		/**
		 * (OPTIONAL)
		 * Sets the failover messages to be sent if the primary message fails.
		 *
		 * @param failover The list of failover messages.
		 * @return This builder.
		 *
		 * @since 9.3.0
		 */
		public B failover(List<MessageRequest> failover) {
			this.failover = failover;
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
