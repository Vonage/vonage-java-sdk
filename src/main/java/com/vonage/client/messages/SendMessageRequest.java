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
public abstract class SendMessageRequest {

	/**
	 * This constructor is used for tests and is also needed to instantiate this
	 * object from a JSON string.
	 */
	SendMessageRequest() {
	}

	protected SendMessageRequest(String from, String to, MessageType messageType, Channel channel) {
		this.from = new E164(from);
		this.to = new E164(to);
		this.messageType = Objects.requireNonNull(messageType, "Message type cannot be null");
		this.channel = Objects.requireNonNull(channel, "Channel cannot be null");
	}

	protected MessageType messageType;
	protected Channel channel;
	protected E164 from;
	protected E164 to;
	protected String messageUuid;
	protected String clientRef;

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

	@JsonProperty("message_uuid")
	public String getMessageUuid() {
		return messageUuid;
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
}
