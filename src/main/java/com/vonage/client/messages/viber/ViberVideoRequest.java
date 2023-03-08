/*
 *   Copyright 2023 Vonage
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
import com.vonage.client.messages.MessageType;

/**
 * @since 7.2.0
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class ViberVideoRequest extends ViberRequest {
	final Video video;

	ViberVideoRequest(Builder builder) {
		super(builder, MessageType.VIDEO);
		video = new Video(builder.url, builder.thumbUrl, builder.caption);
	}

	@JsonProperty("video")
	public Video getVideo() {
		return video;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends ViberRequest.Builder<ViberVideoRequest, Builder> {
		String url, thumbUrl, caption;

		Builder() {}

		/**
		 * (REQUIRED)
		 * Sets the URL of the video attachment. Supports only {@code .mp4} and {@code .3gpp} file extensions.
		 * Note: Video codec must be H.264 and audio codec AAC.
		 *
		 * @param url The video URL as a string.
		 * @return This builder.
		 */
		public Builder url(String url) {
			this.url = url;
			return this;
		}

		/**
		 * (REQUIRED)
		 * URL to an image file (.jpg) for a thumbnail preview of the video.
		 *
		 * @param thumbUrl The thumbnail image URL as a string.
		 * @return This builder.
		 */
		public Builder thumbUrl(String thumbUrl) {
			this.thumbUrl = thumbUrl;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Additional text to accompany the video.
		 *
		 * @param caption The caption string.
		 * @return This builder.
		 */
		public Builder caption(String caption) {
			this.caption = caption;
			return this;
		}

		@Override
		public ViberVideoRequest build() {
			return new ViberVideoRequest(this);
		}
	}
}
