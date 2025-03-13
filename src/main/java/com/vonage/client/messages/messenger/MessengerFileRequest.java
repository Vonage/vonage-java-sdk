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
package com.vonage.client.messages.messenger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.MediaMessageRequest;
import com.vonage.client.messages.internal.MessagePayload;
import com.vonage.client.common.MessageType;

public final class MessengerFileRequest extends MessengerRequest implements MediaMessageRequest {

	MessengerFileRequest(Builder builder) {
		super(builder, MessageType.FILE);
	}

	@JsonProperty("file")
	public MessagePayload getFile() {
		return media;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends MessengerRequest.Builder<MessengerFileRequest, Builder> implements MediaMessageRequest.Builder<Builder> {

		Builder() {}

		/**
		 * (REQUIRED)
		 * Sets the URL of the file attachment. Supports a wide range of attachments including
		 * {@code .zip}, {@code .csv} and {@code .pdf.}.
		 *
		 * @param url The URL as a string.
		 * @return This builder.
		 */
		@Override
		public Builder url(String url) {
			return super.url(url);
		}

		@Override
		public MessengerFileRequest build() {
			return new MessengerFileRequest(this);
		}
	}

}
