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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vonage.client.messages.Channel;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.messages.MessageType;
import com.vonage.client.common.E164;

public abstract class WhatsappRequest extends MessageRequest {

	protected WhatsappRequest(Builder<?, ?> builder, MessageType messageType) {
		super(builder, Channel.WHATSAPP, messageType);
	}

	@Override
	protected void validateSenderAndRecipient(String from, String to) throws IllegalArgumentException {
		this.from = new E164(from).toString();
		this.to = new E164(to).toString();
	}

	protected abstract static class Builder<M extends WhatsappRequest, B extends Builder<? extends M, ? extends B>> extends MessageRequest.Builder<M, B> {
	}
}
