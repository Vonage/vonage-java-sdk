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
package com.vonage.client.messages.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.Channel;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.common.MessageType;
import com.vonage.client.messages.TextMessageRequest;

public final class SmsTextRequest extends MessageRequest implements TextMessageRequest {
	final OutboundSettings sms;

	SmsTextRequest(Builder builder) {
		super(builder, Channel.SMS, MessageType.TEXT);
		sms = OutboundSettings.construct(builder.encodingType, builder.contentId, builder.entityId, builder.poolId, builder.trustedRecipient);
	}

	@Override
	public String getText() {
		return super.getText();
	}

	@JsonProperty("sms")
	public OutboundSettings getMessageSettings() {
		return sms;
	}

	@JsonProperty("ttl")
	public Integer getTtl() {
		return ttl;
	}

	public static Builder builder() {
		return new Builder();
	}

	public final static class Builder extends MessageRequest.Builder<SmsTextRequest, Builder> implements TextMessageRequest.Builder<Builder> {
		String contentId, entityId, poolId;
		EncodingType encodingType;
		Boolean trustedRecipient;

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
		@Override
		public Builder text(String text) {
			return super.text(text);
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
		public Builder ttl(int ttl) {
			return super.ttl(ttl);
		}

		@Override
		public SmsTextRequest build() {
			return new SmsTextRequest(this);
		}

		/**
		 * (OPTIONAL)
		 * The ID of the Number Pool to use as the sender of this message. If specified, a number from the
		 * pool will be used as the from number. The from number is still required even when specifying a
		 * pool_id and will be used as a fall-back if the number pool cannot be used. See the Number Pools
		 * documentation for more information.
		 *
		 * @param poolId The pool ID as a string.
		 * @return This builder.
		 *
		 * @since 9.9.0
		 */
		public Builder poolId(String poolId) {
			this.poolId = poolId;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Indicates whether the recipient is a trusted recipient.
		 *
		 * @param trustedRecipient
		 * @return
		 * 
		 * @since 9.8.0
		 */
		public Builder trustedRecipient(Boolean trustedRecipient) {
			this.trustedRecipient = trustedRecipient;
			return this;
		}
	}
}
