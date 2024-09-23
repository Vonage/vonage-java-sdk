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
package com.vonage.client.messages.mms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.CaptionMediaMessageRequest;
import com.vonage.client.messages.MessageType;
import com.vonage.client.messages.internal.MessagePayload;

public final class MmsVideoRequest extends MmsRequest implements CaptionMediaMessageRequest {

	MmsVideoRequest(Builder builder) {
		super(builder, MessageType.VIDEO);
	}

	@JsonProperty("video")
	public MessagePayload getVideo() {
		return media;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends MmsRequest.Builder<MmsVideoRequest, Builder> implements CaptionMediaMessageRequest.Builder<Builder> {
		Builder() {}

		/**
		 * (REQUIRED)
		 * Sets the URL of the video attachment.
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
		 * Additional text to accompany the video. Must be between 1 and 2000 characters.
		 *
		 * @param caption The caption string.
		 * @return This builder.
		 */
		@Override
		public Builder caption(String caption) {
			return super.caption(caption);
		}

		@Override
		public MmsVideoRequest build() {
			return new MmsVideoRequest(this);
		}
	}
}
