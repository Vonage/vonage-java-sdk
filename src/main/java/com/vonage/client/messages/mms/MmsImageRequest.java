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
package com.vonage.client.messages.mms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.internal.MessagePayload;
import com.vonage.client.messages.internal.MessageType;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class MmsImageRequest extends MmsRequest {

	MmsImageRequest(Builder builder) {
		super(builder);
		payload = new MessagePayload(builder.url, builder.caption);
		payload.validateExtension("jpg", "jpeg", "png", "gif");
	}

	@JsonProperty("image")
	public MessagePayload getImage() {
		return payload;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends MmsRequest.Builder<MmsImageRequest, Builder> {
		String caption;

		Builder() {}

		@Override
		protected MessageType getMessageType() {
			return MessageType.IMAGE;
		}

		/**
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

		/**
		 * Additional text to accompany the image. Must be between 1 and 2000 characters.
		 *
		 * @param caption The caption string.
		 * @return This builder.
		 */
		public Builder caption(String caption) {
			this.caption = caption;
			return this;
		}

		@Override
		public MmsImageRequest build() {
			return new MmsImageRequest(this);
		}
	}

}
