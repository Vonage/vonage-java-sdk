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
package com.vonage.client.messages.viber;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.internal.MessagePayload;
import com.vonage.client.messages.MessageType;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class ViberImageRequest extends ViberRequest {
	final MessagePayload image;

	ViberImageRequest(Builder builder) {
		super(builder, MessageType.IMAGE);
		image = new MessagePayload(builder.url);
		image.validateUrlExtension("jpg", "jpeg", "png");
	}

	@JsonProperty("image")
	public MessagePayload getImage() {
		return image;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends ViberRequest.Builder<ViberImageRequest, Builder> {
		String url;

		Builder() {}

		/**
		 * (REQUIRED)
		 * Sets the URL of the image attachment. Supports only
		 * <code>.jpg</code>, <code>.jpeg</code> and <code>.png</code>
		 * file extensions.
		 *
		 * @param url The URL as a string.
		 * @return This builder.
		 */
		public Builder url(String url) {
			this.url = url;
			return this;
		}

		@Override
		public Builder actionUrl(String actionUrl) {
			return super.actionUrl(actionUrl);
		}

		@Override
		public Builder actionText(String actionText) {
			return super.actionText(actionText);
		}

		@Override
		public ViberImageRequest build() {
			return new ViberImageRequest(this);
		}
	}
}
