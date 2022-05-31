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
package com.vonage.client.messages.viber;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.messages.internal.Channel;
import com.vonage.client.messages.internal.E164;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public abstract class ViberRequest extends MessageRequest {

	protected ViberService viberService;

	protected ViberRequest(Builder<?, ?> builder) {
		super(builder);
		viberService = ViberService.construct(builder.category, builder.ttl, builder.viberType);
	}

	@Override
	protected void validateSenderAndRecipient(String from, String to) throws IllegalArgumentException {
		if (from == null || from.isEmpty()) {
			throw new IllegalArgumentException("Sender ID cannot be empty");
		}
		if (from.length() > 50) {
			throw new IllegalArgumentException("Sender ID cannot be longer than 50 characters");
		}
		this.to = new E164(to).toString();
	}

	@JsonProperty("viber_service")
	public ViberService getViberService() {
		return viberService;
	}

	@SuppressWarnings("unchecked")
	public abstract static class Builder<M extends ViberRequest, B extends Builder<? extends M, ? extends B>> extends MessageRequest.Builder<M, B> {
		protected Category category;
		protected Integer ttl;
		protected String viberType;

		protected Builder() {
			channel = Channel.VIBER;
		}

		public B category(Category category) {
			this.category = category;
			return (B) this;
		}

		public B ttl(int ttl) {
			this.ttl = ttl;
			return (B) this;
		}

		public B viberType(String type) {
			this.viberType = type;
			return (B) this;
		}
	}
}
