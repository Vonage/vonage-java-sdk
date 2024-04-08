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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.internal.MessagePayload;
import com.vonage.client.messages.MessageType;

public final class WhatsappFileRequest extends WhatsappRequest {
	final MessagePayload file;

	WhatsappFileRequest(Builder builder) {
		super(builder, MessageType.FILE);
		file = new MessagePayload(builder.url, builder.caption, builder.name);
	}

	@JsonProperty("file")
	public MessagePayload getFile() {
		return file;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends WhatsappRequest.Builder<WhatsappFileRequest, Builder> {
		String url, caption, name;

		Builder() {}

		/**
		 * (REQUIRED)
		 * Sets the URL of the file attachment. Supports a wide range of attachments including
		 * {@code .zip}, {@code .csv} and {@code .pdf.}.
		 *
		 * @param url The URL as a string.
		 * @return This builder.
		 */
		public Builder url(String url) {
			this.url = url;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Additional text to accompany the file.
		 *
		 * @param caption The caption string.
		 * @return This builder.
		 */
		public Builder caption(String caption) {
			this.caption = caption;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Specifies the name of the file being sent. If not included, the value for caption will be used as
		 * the file name. If neither name nor caption are included, the file name will be parsed from the url.
		 *
		 * @param name The file name.
		 * @return This builder.
		 *
		 * @since 8.1.0
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		@Override
		public WhatsappFileRequest build() {
			return new WhatsappFileRequest(this);
		}
	}
}
