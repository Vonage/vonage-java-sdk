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
package com.vonage.client.messages.mms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.Channel;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.common.MessageType;

public abstract class MmsRequest extends MessageRequest {
	protected final Boolean trustedRecipient;

	protected MmsRequest(Builder<?, ?> builder, MessageType messageType) {
		super(builder, Channel.MMS, messageType);
		this.trustedRecipient = builder.trustedRecipient;
		if (media != null) {
			media.validateCaptionLength(3000);
		}
		int min = 300, max = 259200;
		if (ttl != null && (ttl < min || ttl > max)) {
			throw new IllegalArgumentException("TTL must be between "+min+" and "+max+" seconds.");
		}
	}

	@JsonProperty("ttl")
	public Integer getTtl() {
		return ttl;
	}

	@JsonProperty("trusted_recipient")
	public Boolean getTrustedRecipient() {
		return trustedRecipient;
	}

	protected abstract static class Builder<M extends MmsRequest, B extends Builder<? extends M, ? extends B>> extends MessageRequest.Builder<M, B> {
		protected Boolean trustedRecipient;

		/**
		 * (OPTIONAL)
		 * Time-To-Live (how long a message should exist before it is delivered successfully) in seconds.
		 * If a message is not delivered successfully within the TTL time, the message is considered expired
		 * and will be rejected if TTL is supported. Must be between 300 and 259200. Default is 600.
		 *
		 * @param ttl The message time-to-live in seconds.
		 *
		 * @return This builder.
		 * @since 8.7.0
		 */
		@Override
		public B ttl(int ttl) {
			return super.ttl(ttl);
		}

		/**
		 * (OPTIONAL)
		 * Indicates if the recipient is trusted.
		 *
		 * @param trustedRecipient Whether the recipient is trusted (true or false).
		 * @return This builder.
		 *
		 * @since 9.8.0
		 */
		@SuppressWarnings("unchecked")
		public B trustedRecipient(Boolean trustedRecipient) {
			this.trustedRecipient = trustedRecipient;
			return (B) this;
		}
	}
}
