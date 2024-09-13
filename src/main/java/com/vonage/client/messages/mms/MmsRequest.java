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
package com.vonage.client.messages.mms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.Channel;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.messages.MessageType;
import com.vonage.client.messages.internal.MessagePayload;

public abstract class MmsRequest extends MessageRequest {
	MessagePayload payload;

	protected MmsRequest(Builder<?, ?> builder, MessageType messageType) {
		super(builder, Channel.MMS, messageType);
		payload = new MessagePayload(builder.url, builder.caption);
		payload.validateCaptionLength(2000);
		int min = 300, max = 259200;
		if (ttl != null && (ttl < min || ttl > max)) {
			throw new IllegalArgumentException("TTL must be between "+min+" and "+max+" seconds.");
		}
	}

	@JsonProperty("ttl")
	public Integer getTtl() {
		return ttl;
	}

	@SuppressWarnings("unchecked")
	protected abstract static class Builder<M extends MmsRequest, B extends Builder<? extends M, ? extends B>> extends MessageRequest.Builder<M, B> {
		String url, caption;

		/**
		 * (REQUIRED)
		 * Sets the media URL.
		 *
		 * @param url The URL as a string.
		 * @return This builder.
		 */
		public B url(String url) {
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
	}
}
