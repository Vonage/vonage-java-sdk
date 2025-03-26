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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.CaptionMediaMessageRequest;
import com.vonage.client.messages.internal.MessagePayload;
import com.vonage.client.common.MessageType;

public final class WhatsappImageRequest extends WhatsappRequest implements CaptionMediaMessageRequest {

	WhatsappImageRequest(Builder builder) {
		super(builder, MessageType.IMAGE);
		media.validateUrlExtension("jpg", "jpeg", "png");
		media.validateCaptionLength(3000);
	}

	@JsonProperty("image")
	public MessagePayload getImage() {
		return media;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends WhatsappRequest.Builder<WhatsappImageRequest, Builder> implements CaptionMediaMessageRequest.Builder<Builder> {

		Builder() {}

		/**
		 * (REQUIRED)
		 * Sets the URL of the image attachment. Supports only {@code .jpg},
		 * {@code .jpeg} and {@code .png} file extensions.
		 *
		 * @param url The URL as a string.
		 * @return This builder.
		 */
		@Override
		public Builder url(String url) {
			return super.url(url);
		}

		/**
		 * (OPTIONAL)
		 * Additional text to accompany the image. Must be between 1 and 3000 characters.
		 *
		 * @param caption The caption string.
		 * @return This builder.
		 */
		@Override
		public Builder caption(String caption) {
			return super.caption(caption);
		}

		@Override
		public WhatsappImageRequest build() {
			return new WhatsappImageRequest(this);
		}
	}

}
