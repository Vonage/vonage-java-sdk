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

import com.vonage.client.common.MessageType;
import com.vonage.client.messages.TextMessageRequest;

public final class WhatsappTextRequest extends WhatsappRequest implements TextMessageRequest {

	WhatsappTextRequest(Builder builder) {
		super(builder, MessageType.TEXT);
	}

	@Override
	protected int maxTextLength() {
		return 4096;
	}

	@Override
	public String getText() {
		return super.getText();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends WhatsappRequest.Builder<WhatsappTextRequest, Builder> implements TextMessageRequest.Builder<Builder> {

		Builder() {}

		/**
		 * (REQUIRED)
		 * Sets the text field. Must be between 1 and 4096 characters, including unicode.
		 *
		 * @param text The text string.
		 * @return This builder.
		 */
		@Override
		public Builder text(String text) {
			return super.text(text);
		}

		public WhatsappTextRequest build() {
			return new WhatsappTextRequest(this);
		}
	}
}
