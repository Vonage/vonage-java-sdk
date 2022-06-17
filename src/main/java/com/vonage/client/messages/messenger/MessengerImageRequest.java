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
import com.vonage.client.messages.internal.MessagePayload;
import com.vonage.client.messages.MessageType;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class MessengerImageRequest extends MessengerRequest {
	MessagePayload image;

	MessengerImageRequest(Builder builder) {
		super(builder);
		image = new MessagePayload(builder.url);
		image.validateUrlExtension("jpg", "jpeg", "png", "gif");
	}

	@JsonProperty("image")
	public MessagePayload getImage() {
		return image;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends MessengerRequest.Builder<MessengerImageRequest, Builder> {
		String url;

		Builder() {}

		@Override
		protected MessageType getMessageType() {
			return MessageType.IMAGE;
		}

		/**
		 * (REQUIRED)
		 * Sets the URL of the image attachment. Supports only <code>.jpg</code>,
		 * <code>.jpeg</code>, <code>.png</code> and <code>.gif</code> file extensions.
		 *
		 * @param url The URL as a string.
		 * @return This builder.
		 */
		public Builder url(String url) {
			this.url = url;
			return this;
		}

		@Override
		public MessengerImageRequest build() {
			return new MessengerImageRequest(this);
		}
	}

}
