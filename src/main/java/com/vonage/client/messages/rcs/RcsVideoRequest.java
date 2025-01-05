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
import com.vonage.client.messages.MessageType;
import com.vonage.client.messages.internal.MessagePayload;

/**
 * {@link com.vonage.client.messages.Channel#RCS}, {@link MessageType#VIDEO} request.
 *
 * @since 8.11.0
 */
public final class RcsVideoRequest extends RcsRequest implements MediaMessageRequest {

	RcsVideoRequest(Builder builder) {
		super(builder, MessageType.VIDEO);
		media.validateUrlExtension("mp4", "3gpp");
	}

	@JsonProperty("video")
	public MessagePayload getVideo() {
		return media;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends RcsRequest.Builder<RcsVideoRequest, Builder> implements MediaMessageRequest.Builder<Builder> {
		Builder() {}

		/**
		 * (REQUIRED)
		 * Publicly accessible URL of the video attachment. Supported file types are
		 * {@code .mp4} and {@code .3gpp} with H.264 and AAC codecs only.
		 *
		 * @param url The URL as a string.
		 * @return This builder.
		 */
		@Override
		public Builder url(String url) {
			return super.url(url);
		}

		@Override
		public RcsVideoRequest build() {
			return new RcsVideoRequest(this);
		}
	}
}
