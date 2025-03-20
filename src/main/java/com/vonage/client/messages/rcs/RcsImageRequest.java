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
package com.vonage.client.messages.rcs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.MediaMessageRequest;
import com.vonage.client.common.MessageType;
import com.vonage.client.messages.internal.MessagePayload;

/**
 * {@link com.vonage.client.messages.Channel#RCS}, {@link MessageType#IMAGE} request.
 *
 * @since 8.11.0
 */
public final class RcsImageRequest extends RcsRequest implements MediaMessageRequest {

	RcsImageRequest(Builder builder) {
		super(builder, MessageType.IMAGE);
		media.validateUrlExtension("jpg", "jpeg", "png");
	}

	@JsonProperty("image")
	public MessagePayload getImage() {
		return media;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends RcsRequest.Builder<RcsImageRequest, Builder> implements MediaMessageRequest.Builder<Builder> {
		Builder() {}

		/**
		 * (REQUIRED)
		 * The publicly accessible URL of the image attachment. Supported types are
		 * {@code .jpg}, {@code .jpeg}, and {@code .png}.
		 *
		 * @param url The URL as a string.
		 * @return This builder.
		 */
		@Override
		public Builder url(String url) {
			return super.url(url);
		}

		@Override
		public RcsImageRequest build() {
			return new RcsImageRequest(this);
		}
	}
}
