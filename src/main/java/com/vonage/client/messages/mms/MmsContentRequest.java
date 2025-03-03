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
package com.vonage.client.messages.mms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.CaptionMediaMessageRequest;
import com.vonage.client.messages.MessageType;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents an MMS request that sends multiple items in a single message.
 *
 * @since 8.18.0
 */
public final class MmsContentRequest extends MmsRequest implements CaptionMediaMessageRequest {
	private List<Content> content;

	MmsContentRequest(Builder builder) {
		super(builder, MessageType.CONTENT);
		if ((content = builder.content).isEmpty()) {
			throw new IllegalArgumentException("At least one content item is required.");
		}
	}

	@JsonProperty("content")
	public List<Content> getContent() {
		return content;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends MmsRequest.Builder<MmsContentRequest, Builder> {
		private List<Content> content = new ArrayList<>();

		Builder() {}

		/**
		 * Adds a content item to the request.
		 *
		 * @param type (REQUIRED) Type of content to include in the message.
		 * @param url (REQUIRED) URL of the content.
		 * @param caption (OPTIONAL) Caption for the content.
		 *
		 * @return This builder.
		 */
		public Builder addContent(MessageType type, URI url, String caption) {
			return addContent(new Content(type, url, caption));
		}

		/**
		 * Adds a content item to the request.
		 *
		 * @param contents The item(s) to include in the message.
		 * @return This builder.
		 */
		public Builder addContent(Content... contents) {
            content.addAll(Arrays.asList(contents));
			return this;
		}

		/**
		 * Sets the content items to send in the message. This will overwrite the current list of content items.
		 *
		 * @param content The list of content items to include in the message.
		 * @return This builder.
		 */
		public Builder contents(List<Content> content) {
			this.content = Objects.requireNonNull(content, "Content list cannot be null.");
			return this;
		}

		@Override
		public MmsContentRequest build() {
			return new MmsContentRequest(this);
		}
	}
}
