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

import java.util.Objects;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public abstract class MessageRequest {

	/**
	 * This constructor is used for tests and is also needed to instantiate this
	 * object from a JSON string.
	 */
	MessageRequest() {
	}

	protected MessageRequest(Builder<?> builder) {
		from = new E164(builder.from);
		to = new E164(builder.to);
		messageType = Objects.requireNonNull(builder.messageType, "Message type cannot be null");
		channel = Objects.requireNonNull(builder.channel, "Channel cannot be null");
		if (!channel.supportsMessageType(builder.messageType)) {
			throw new IllegalArgumentException(messageType+" cannot be sent via "+channel);
		}
		clientRef = builder.clientRef;
	}

	MessageType messageType;
	Channel channel;
	E164 from;
	E164 to;
	String clientRef;

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
		return from.toString();
	}

	@JsonProperty("to")
	public String getTo() {
		return to.toString();
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
	public abstract static class Builder<B extends Builder<B>> {
		final Channel channel;
		MessageType messageType;
		String from, to, clientRef;

		public Builder(Channel channel) {
			this.channel = channel;
		}

		public B messageType(MessageType messageType) {
			this.messageType = messageType;
			return (B) this;
		}

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

		public abstract MessageRequest build();
	}
}
