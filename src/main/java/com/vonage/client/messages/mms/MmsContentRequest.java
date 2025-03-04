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
		 * Adds a vCard attachment to the request.
		 *
		 * @param url (REQUIRED) Publicly accessible URL of the vCard attachment.
		 * @param caption (OPTIONAL) Additional text to accompany the vCard (maximum 2000 characters).
		 *
		 * @return This builder.
		 */
		public Builder addVcard(String url, String caption) {
			content.add(new Content(MessageType.VCARD, url, caption));
			return this;
		}

		/**
		 * Adds a vCard attachment to the request.
		 *
		 * @param url Publicly accessible URL of the vCard attachment.
		 *
		 * @return This builder.
		 */
		public Builder addVcard(String url) {
			return addVcard(url, null);
		}

		/**
		 * Adds an audio attachment to the request.
		 *
		 * @param url (REQUIRED) Publicly accessible URL of the audio attachment.
		 * @param caption (OPTIONAL) Additional text to accompany the audio (maximum 2000 characters).
		 *
		 * @return This builder.
		 */
		public Builder addAudio(String url, String caption) {
			content.add(new Content(MessageType.AUDIO, url, caption));
			return this;
		}

		/**
		 * Adds an audio attachment to the request.
		 *
		 * @param url Publicly accessible URL of the audio attachment.
		 *
		 * @return This builder.
		 */
		public Builder addAudio(String url) {
			return addAudio(url, null);
		}

		/**
		 * Adds a video attachment to the request.
		 *
		 * @param url (REQUIRED) Publicly accessible URL of the video attachment.
		 * @param caption (OPTIONAL) Additional text to accompany the video (maximum 2000 characters).
		 *
		 * @return This builder.
		 */
		public Builder addVideo(String url, String caption) {
			content.add(new Content(MessageType.VIDEO, url, caption));
			return this;
		}

		/**
		 * Adds a video attachment to the request.
		 *
		 * @param url Publicly accessible URL of the video attachment.
		 *
		 * @return This builder.
		 */
		public Builder addVideo(String url) {
			return addVideo(url, null);
		}

		/**
		 * Adds an image attachment to the request.
		 *
		 * @param url (REQUIRED) Publicly accessible URL of the image attachment.
		 * @param caption (OPTIONAL) Additional text to accompany the image (maximum 2000 characters).
		 *
		 * @return This builder.
		 */
		public Builder addImage(String url, String caption) {
			content.add(new Content(MessageType.IMAGE, url, caption));
			return this;
		}

		/**
		 * Adds an image attachment to the request.
		 *
		 * @param url Publicly accessible URL of the image attachment.
		 *
		 * @return This builder.
		 */
		public Builder addImage(String url) {
			return addImage(url, null);
		}

		/**
		 * Adds a file attachment to the request.
		 *
		 * @param url (REQUIRED) Publicly accessible URL of the file attachment.
		 * @param caption (OPTIONAL) Additional text to accompany the file (maximum 2000 characters).
		 *
		 * @return This builder.
		 */
		public Builder addFile(String url, String caption) {
			content.add(new Content(MessageType.FILE, url, caption));
			return this;
		}

		/**
		 * Adds a file attachment to the request.
		 *
		 * @param url Publicly accessible URL of the file attachment.
		 *
		 * @return This builder.
		 */
		public Builder addFile(String url) {
			return addFile(url, null);
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
