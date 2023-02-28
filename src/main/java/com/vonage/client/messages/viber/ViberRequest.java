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
import com.vonage.client.messages.Channel;
import com.vonage.client.messages.MessageType;
import com.vonage.client.messages.internal.E164;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public abstract class ViberRequest extends MessageRequest {
	protected final ViberService viberService;

	protected ViberRequest(Builder<?, ?> builder, MessageType messageType) {
		super(builder, Channel.VIBER, messageType);
		viberService = ViberService.construct(
				builder.category,
				builder.ttl,
				builder.viberType,
				Action.construct(builder.actionUrl, builder.actionText)
		);
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
	protected abstract static class Builder<M extends ViberRequest, B extends Builder<? extends M, ? extends B>> extends MessageRequest.Builder<M, B> {
		protected Category category;
		protected Integer ttl;
		protected String viberType, actionUrl, actionText;

		/**
		 * (OPTIONAL)
		 * Sets the category tag of the message.
		 *
		 * @param category The Viber message category.
		 * @return This builder.
		 */
		public B category(Category category) {
			this.category = category;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Sets the time-to-live of message to be delivered in seconds. If the message is not
		 * delivered within this time, it will be deleted. The TTL must be between 30 and
		 * 259200 seconds (i.e. 3 days), inclusive.
		 *
		 * @param ttl The number of seconds the message can live undelivered before being discarded.
		 * @return This builder.
		 */
		public B ttl(int ttl) {
			this.ttl = ttl;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Viber-specific type definition. To use "template", please contact your Vonage Account Manager
		 * to set up your templates. To find out more please visit the
		 * <a href=https://www.vonage.com/communications-apis/messages/>product page</a>.
		 *
		 * @param type The Viber type.
		 * @return This builder.
		 */
		public B viberType(String type) {
			this.viberType = type;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * A URL which is requested when the action button is clicked.
		 *
		 * @param actionUrl The URL as a string.
		 * @return This builder.
		 */
		protected B actionUrl(String actionUrl) {
			this.actionUrl = actionUrl;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Text which is rendered on the action button.
		 *
		 * @param actionText The action button description.
		 * @return This builder.
		 */
		protected B actionText(String actionText) {
			this.actionText = actionText;
			return (B) this;
		}
	}
}
