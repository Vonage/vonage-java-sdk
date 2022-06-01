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

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public abstract class MessageRequest {
	MessageType messageType;
	Channel channel;
	protected String from, to, clientRef;

	/**
	 * This constructor is used for tests and is also needed to instantiate this
	 * object from a JSON string.
	 */
	MessageRequest() {
	}

	protected MessageRequest(Builder<?, ?> builder) {
		messageType = Objects.requireNonNull(builder.messageType, "Message type cannot be null");
		channel = Objects.requireNonNull(builder.channel, "Channel cannot be null");
		if (!channel.supportsMessageType(builder.messageType)) {
			throw new IllegalArgumentException(messageType+" cannot be sent via "+channel);
		}
		from = builder.from;
		to = builder.to;
		validateSenderAndRecipient(from, to);
		clientRef = builder.clientRef;
	}

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

	public void setClientRef(String clientRef) {
		this.clientRef = clientRef;
	}

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

		protected abstract MessageType getMessageType();

		protected abstract Channel getChannel();

		public B from(String from) {
			this.from = from;
			return (B) this;
		}

		public B to(String to) {
			this.to = to;
			return (B) this;
		}

		public B clientRef(String clientRef) {
			this.clientRef = clientRef;
			return (B) this;
		}

		public abstract M build();
	}
}
