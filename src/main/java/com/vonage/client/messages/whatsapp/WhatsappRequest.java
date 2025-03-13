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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.common.E164;
import com.vonage.client.messages.Channel;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.common.MessageType;
import java.util.UUID;

public abstract class WhatsappRequest extends MessageRequest {
	final Context context;

	protected WhatsappRequest(Builder<?, ?> builder, MessageType messageType) {
		super(builder, Channel.WHATSAPP, messageType);
        context = builder.messageUuid != null ? new Context(builder.messageUuid) : null;
	}

	@Override
	protected void validateSenderAndRecipient(String from, String to) throws IllegalArgumentException {
		this.from = new E164(from).toString();
		this.to = new E164(to).toString();
	}

	@JsonProperty("context")
	public Context getContext() {
		return context;
	}

	@SuppressWarnings("unchecked")
	protected abstract static class Builder<M extends WhatsappRequest, B extends Builder<? extends M, ? extends B>> extends MessageRequest.Builder<M, B> {
		UUID messageUuid;

		/**
		 * (REQUIRED for replies and reaction messages)
		 * An optional context used for quoting/replying to a specific message in a conversation. When used,
		 * the WhatsApp UI will display the new message along with a contextual bubble that displays the
		 * quoted/replied to message's content.<br>
		 * This field is the UUID of the message being replied to or quoted.
		 *
		 * @param messageUuid The context's message UUID as a string.
		 *
		 * @return This builder.
		 * @since 8.7.0
		 */
		public B contextMessageId(String messageUuid) {
			this.messageUuid = UUID.fromString(messageUuid);
			return (B) this;
		}
	}
}
