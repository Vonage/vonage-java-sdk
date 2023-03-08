/*
 *   Copyright 2023 Vonage
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
import com.vonage.client.messages.MessageType;
import com.vonage.client.messages.internal.Text;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class MessengerTextRequest extends MessengerRequest {
	final String text;

	MessengerTextRequest(Builder builder) {
		super(builder, MessageType.TEXT);
		text = new Text(builder.text, 640).toString();
	}

	@JsonProperty("text")
	public String getText() {
		return text;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends MessengerRequest.Builder<MessengerTextRequest, Builder> {
		String text;

		Builder() {}

		/**
		 * (REQUIRED)
		 * Sets the text field. Must be between 1 and 640 characters, including unicode.
		 *
		 * @param text The text string.
		 * @return This builder.
		 */
		public Builder text(String text) {
			this.text = text;
			return this;
		}

		public MessengerTextRequest build() {
			return new MessengerTextRequest(this);
		}
	}
}
