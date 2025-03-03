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

import com.vonage.client.messages.MessageType;
import com.vonage.client.messages.TextMessageRequest;

/**
 * Represents an MMS request that sends a text message.
 *
 * @since 8.18.0
 */
public final class MmsTextRequest extends MmsRequest implements TextMessageRequest {

	MmsTextRequest(Builder builder) {
		super(builder, MessageType.TEXT);
	}

	@Override
	public String getText() {
		return super.getText();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends MmsRequest.Builder<MmsTextRequest, Builder> implements TextMessageRequest.Builder<Builder> {
		Builder() {}

		/**
		 * (REQUIRED)
		 * The text of the message to send.
		 *
		 * @param text The message text, may include Unicode.
		 * @return This builder.
		 */
		@Override
		public Builder text(String text) {
			return super.text(text);
		}

		@Override
		public MmsTextRequest build() {
			return new MmsTextRequest(this);
		}
	}
}
