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
package com.vonage.client.messages.sms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.Channel;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.messages.MessageType;
import com.vonage.client.messages.internal.Text;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class SmsTextRequest extends MessageRequest {
	final String text;
	final Integer ttl;
	final OutboundSettings sms;

	SmsTextRequest(Builder builder) {
		super(builder, Channel.SMS, MessageType.TEXT);
		text = new Text(builder.text, 1000).toString();
		if ((ttl = builder.ttl) != null && ttl < 1) {
			throw new IllegalArgumentException("TTL must be positive.");
		}
		sms = OutboundSettings.construct(builder.encodingType, builder.contentId, builder.entityId);
	}

	@JsonProperty("text")
	public String getText() {
		return text;
	}

	@JsonProperty("ttl")
	public Integer getTtl() {
		return ttl;
	}

	@JsonProperty("sms")
	public OutboundSettings getMessageSettings() {
		return sms;
	}

	public static Builder builder() {
		return new Builder();
	}

	public final static class Builder extends MessageRequest.Builder<SmsTextRequest, Builder> {
		String text, contentId, entityId;
		Integer ttl;
		EncodingType encodingType;

		Builder() {}

		/**
		 * (REQUIRED)
		 * Sets the text field. Must be between 1 and 1000 characters. The Messages API automatically
		 * detects unicode characters when sending SMS and sends the message as a unicode SMS.
		 * <a href=developer.nexmo.com/messaging/sms/guides/concatenation-and-encoding>
		 *  Read more about concatenation and encoding</a>.
		 *
		 * @param text The text string.
		 * @return This builder.
		 */
		public Builder text(String text) {
			this.text = text;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The duration in milliseconds the delivery of an SMS will be attempted. By default, Vonage attempts
		 * delivery for 72 hours, however the maximum effective value depends on the operator and is typically
		 * 24 to 48 hours. We recommend this value should be kept at its default or at least 30 minutes.
		 *
		 * @param ttl The time-to-live for this message before abandoning delivery attempts, in milliseconds.
		 * @return This builder.
		 *
		 * @since 8.1.0
		 */
		public Builder ttl(int ttl) {
			this.ttl = ttl;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The encoding type to use for the message. If set to either {@linkplain EncodingType#TEXT} or
		 * {@linkplain EncodingType#UNICODE}, the specified type will be used. If set to
		 * {@linkplain EncodingType#AUTO} (the default), the Messages API will automatically set the type based
		 * on the content of text; i.e. if unicode characters are detected in text, then the message will be
		 * encoded as unicode, and otherwise as text.
		 *
		 * @param encodingType The message encoding type as an enum.
		 * @return This builder.
		 *
		 * @since 8.1.0
		 */
		public Builder encodingType(EncodingType encodingType) {
			this.encodingType = encodingType;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * A string parameter that satisfies regulatory requirements when sending an SMS to specific countries.
		 *
		 * @param contentId The content ID as a string.
		 * @return This builder.
		 *
		 * @since 8.1.0
		 */
		public Builder contentId(String contentId) {
			this.contentId = contentId;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * A string parameter that satisfies regulatory requirements when sending an SMS to specific countries.
		 *
		 * @param entityId The entity ID as a string.
		 * @return This builder.
		 *
		 * @since 8.1.0
		 */
		public Builder entityId(String entityId) {
			this.entityId = entityId;
			return this;
		}

		@Override
		public SmsTextRequest build() {
			return new SmsTextRequest(this);
		}
	}
}
