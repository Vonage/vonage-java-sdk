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
package com.vonage.client.messages.messenger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.messages.internal.Channel;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public abstract class MessengerRequest extends MessageRequest {

	protected Messenger messenger;

	protected MessengerRequest(Builder<?, ?> builder) {
		super(builder);
		messenger = Messenger.construct(builder.category, builder.tag);
	}

	@Override
	protected void validateSenderAndRecipient(String from, String to) {
		for (String id : new String[]{from, to}) {
			if (id == null || id.isEmpty()) {
				throw new IllegalArgumentException("ID cannot be empty");
			}
			if (id.length() > 50) {
				throw new IllegalArgumentException("ID cannot be longer than 50 characters");
			}
		}
	}

	@JsonProperty("messenger")
	public Messenger getMessenger() {
		return messenger;
	}

	@SuppressWarnings("unchecked")
	public abstract static class Builder<M extends MessengerRequest, B extends Builder<? extends M, ? extends B>> extends MessageRequest.Builder<M, B> {
		protected MessageTag tag;
		protected MessageCategory category;

		protected Builder() {
			channel = Channel.MESSENGER;
		}

		/**
		 * A tag describing the type and relevance of the 1:1 communication between your app and the end user.
		 *
		 * @param tag The tag.
		 * @return This builder.
		 */
		public B tag(MessageTag tag) {
			this.tag = tag;
			return (B) this;
		}

		/**
		 * The use of different category tags enables the business to send messages for different use cases.
		 * For Facebook Messenger they need to comply with their Messaging Types policy. Vonage maps our category to
		 * their messaging_type. If message_tag is used, then an additional tag for that type is mandatory. By
		 * default, Vonage sends the response category to Facebook Messenger.
		 *
		 * @param category The category.
		 * @return This builder.
		 */
		public B category(MessageCategory category) {
			this.category = category;
			return (B) this;
		}
	}
}
