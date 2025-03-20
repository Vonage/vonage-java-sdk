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
package com.vonage.client.messages.viber;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.CaptionMediaMessageRequest;
import com.vonage.client.common.MessageType;
import java.util.Objects;

/**
 * @since 7.2.0
 */
public final class ViberVideoRequest extends ViberRequest implements CaptionMediaMessageRequest {
	final Video video;

	ViberVideoRequest(Builder builder) {
		super(builder, MessageType.VIDEO);
		Objects.requireNonNull(builder.duration, "Duration is required.");
		Objects.requireNonNull(builder.fileSize, "File size is required.");
		video = new Video(media.getUrl().toString(), builder.thumbUrl, media.getCaption());
	}

	@JsonProperty("video")
	public Video getVideo() {
		return video;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends ViberRequest.Builder<ViberVideoRequest, Builder> implements CaptionMediaMessageRequest.Builder<Builder> {
		String thumbUrl;

		Builder() {}

		/**
		 * (REQUIRED)
		 * Sets the URL of the video attachment. Supports only {@code .mp4} and {@code .3gpp} file extensions.
		 * Note: Video codec must be H.264 and audio codec AAC.
		 *
		 * @param url The video URL as a string.
		 * @return This builder.
		 */
		@Override
		public Builder url(String url) {
			return super.url(url);
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
		 * (REQUIRED)
		 * Length of the video in seconds. Must be between 1 and 600.
		 *
		 * @param duration The video duration as an integer.
		 * @return This builder.
		 */
		public Builder duration(int duration) {
			this.duration = duration;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The video file size in megabytes. Must be between 1 and 200.
		 *
		 * @param fileSize The file size as an integer.
		 * @return This builder.
		 */
		public Builder fileSize(int fileSize) {
			this.fileSize = fileSize;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Additional text to accompany the video.
		 *
		 * @param caption The caption string.
		 * @return This builder.
		 */
		@Override
		public Builder caption(String caption) {
			return super.caption(caption);
		}

		@Override
		public ViberVideoRequest build() {
			return new ViberVideoRequest(this);
		}
	}
}
