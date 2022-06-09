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
import com.vonage.client.messages.internal.MessageType;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class MessengerVideoRequest extends MessengerRequest {
	MessagePayload video;

	MessengerVideoRequest(Builder builder) {
		super(builder);
		video = new MessagePayload(builder.url);
		video.validateExtension("mp4");
	}

	@JsonProperty("video")
	public MessagePayload getVideo() {
		return video;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends MessengerRequest.Builder<MessengerVideoRequest, Builder> {
		String url;

		Builder() {}

		@Override
		protected MessageType getMessageType() {
			return MessageType.VIDEO;
		}

		/**
		 * (REQUIRED)
		 * Sets the URL of the video attachment. Supports only <code>.mp4</code>
		 * file extension. Only H.264 video codec and AAC audio codec is supported.
		 *
		 * @param url The URL as a string.
		 * @return This builder.
		 */
		public Builder url(String url) {
			this.url = url;
			return this;
		}

		@Override
		public MessengerVideoRequest build() {
			return new MessengerVideoRequest(this);
		}
	}

}
