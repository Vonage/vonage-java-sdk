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
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents an RCS rich card with media, text, and suggestion actions.
 *
 * @since 9.6.0
 */
public class RcsCard extends JsonableBaseObject {
	private String title, text, mediaDescription;
	private URI mediaUrl, thumbnailUrl;
	private MediaHeight mediaHeight;
	private Boolean mediaForceRefresh;
	private List<RcsSuggestion> suggestions;

	/**
	 * The height of the media element.
	 */
	public enum MediaHeight {
		/**
		 * Short media height.
		 */
		SHORT,

		/**
		 * Medium media height.
		 */
		MEDIUM,

		/**
		 * Tall media height (full screen).
		 */
		TALL;

		@JsonValue
		@Override
		public String toString() {
			return name();
		}

		/**
		 * Parse a media height from a string.
		 *
		 * @param value The media height as a string.
		 * @return The MediaHeight enum value.
		 */
		public static MediaHeight fromString(String value) {
			return Jsonable.fromString(value, MediaHeight.class);
		}
	}

	protected RcsCard() {
	}

	private RcsCard(Builder builder) {
		this.title = Objects.requireNonNull(builder.title, "Title is required.");
		if (title.length() > 200) {
			throw new IllegalArgumentException("Title must be 200 characters or less.");
		}
		this.text = Objects.requireNonNull(builder.text, "Text is required.");
		if (text.length() > 2000) {
			throw new IllegalArgumentException("Text must be 2000 characters or less.");
		}
		this.mediaUrl = Objects.requireNonNull(builder.mediaUrl, "Media URL is required.");
		this.mediaDescription = builder.mediaDescription;
		this.mediaHeight = builder.mediaHeight;
		this.thumbnailUrl = builder.thumbnailUrl;
		this.mediaForceRefresh = builder.mediaForceRefresh;
		if (builder.suggestions != null && !builder.suggestions.isEmpty()) {
			if (builder.suggestions.size() > 4) {
				throw new IllegalArgumentException("A card can have a maximum of 4 suggestions.");
			}
			this.suggestions = builder.suggestions;
		}
	}

	/**
	 * The title of the card.
	 *
	 * @return The card title (max 200 characters).
	 */
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	/**
	 * The text to display on the card.
	 *
	 * @return The card text (max 2000 characters).
	 */
	@JsonProperty("text")
	public String getText() {
		return text;
	}

	/**
	 * The publicly accessible URL of the media attachment for the card.
	 * Supported media include images, videos, and PDFs.
	 *
	 * @return The media URL.
	 */
	@JsonProperty("media_url")
	public URI getMediaUrl() {
		return mediaUrl;
	}

	/**
	 * A short description of the media for accessibility purposes.
	 *
	 * @return The media description, or {@code null} if not set.
	 */
	@JsonProperty("media_description")
	public String getMediaDescription() {
		return mediaDescription;
	}

	/**
	 * The height of the media element. If not specified, defaults to {@code SHORT}.
	 *
	 * @return The media height, or {@code null} if not set.
	 */
	@JsonProperty("media_height")
	public MediaHeight getMediaHeight() {
		return mediaHeight;
	}

	/**
	 * The publicly accessible URL of the thumbnail image for the card.
	 *
	 * @return The thumbnail URL, or {@code null} if not set.
	 */
	@JsonProperty("thumbnail_url")
	public URI getThumbnailUrl() {
		return thumbnailUrl;
	}

	/**
	 * By default, media URLs are cached on the RCS platform for up to 60 days.
	 * Set this property to {@code true} to force the platform to fetch a fresh copy
	 * of the media from the provided URL.
	 *
	 * @return {@code true} if media should be force-refreshed, {@code null} otherwise.
	 */
	@JsonProperty("media_force_refresh")
	public Boolean getMediaForceRefresh() {
		return mediaForceRefresh;
	}

	/**
	 * An array of suggestion objects to include with the card. You can include up to 4 suggestions per card.
	 *
	 * @return The list of suggestions, or {@code null} if none are set.
	 */
	@JsonProperty("suggestions")
	public List<RcsSuggestion> getSuggestions() {
		return suggestions;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String title, text, mediaDescription;
		private URI mediaUrl, thumbnailUrl;
		private MediaHeight mediaHeight;
		private Boolean mediaForceRefresh;
		private List<RcsSuggestion> suggestions;

		/**
		 * (REQUIRED)
		 * The title of the card.
		 *
		 * @param title The card title (max 200 characters).
		 * @return This builder.
		 */
		public Builder title(String title) {
			this.title = title;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The text to display on the card.
		 *
		 * @param text The card text (max 2000 characters).
		 * @return This builder.
		 */
		public Builder text(String text) {
			this.text = text;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The publicly accessible URL of the media attachment for the card.
		 * Supported media include images, videos, and PDFs.
		 *
		 * @param mediaUrl The media URL as a string.
		 * @return This builder.
		 */
		public Builder mediaUrl(String mediaUrl) {
			return mediaUrl(URI.create(mediaUrl));
		}

		/**
		 * (REQUIRED)
		 * The publicly accessible URL of the media attachment for the card.
		 * Supported media include images, videos, and PDFs.
		 *
		 * @param mediaUrl The media URL.
		 * @return This builder.
		 */
		public Builder mediaUrl(URI mediaUrl) {
			this.mediaUrl = mediaUrl;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * A short description of the media for accessibility purposes.
		 *
		 * @param mediaDescription The media description.
		 * @return This builder.
		 */
		public Builder mediaDescription(String mediaDescription) {
			this.mediaDescription = mediaDescription;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The height of the media element. If not specified, defaults to {@code SHORT}.
		 *
		 * @param mediaHeight The media height.
		 * @return This builder.
		 */
		public Builder mediaHeight(MediaHeight mediaHeight) {
			this.mediaHeight = mediaHeight;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The publicly accessible URL of the thumbnail image for the card.
		 *
		 * @param thumbnailUrl The thumbnail URL as a string.
		 * @return This builder.
		 */
		public Builder thumbnailUrl(String thumbnailUrl) {
			return thumbnailUrl(URI.create(thumbnailUrl));
		}

		/**
		 * (OPTIONAL)
		 * The publicly accessible URL of the thumbnail image for the card.
		 *
		 * @param thumbnailUrl The thumbnail URL.
		 * @return This builder.
		 */
		public Builder thumbnailUrl(URI thumbnailUrl) {
			this.thumbnailUrl = thumbnailUrl;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * By default, media URLs are cached on the RCS platform for up to 60 days.
		 * Set this property to {@code true} to force the platform to fetch a fresh copy
		 * of the media from the provided URL.
		 *
		 * @param mediaForceRefresh Whether to force-refresh the media.
		 * @return This builder.
		 */
		public Builder mediaForceRefresh(boolean mediaForceRefresh) {
			this.mediaForceRefresh = mediaForceRefresh;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * An array of suggestion objects to include with the card. You can include up to 4 suggestions per card.
		 *
		 * @param suggestions The list of suggestions.
		 * @return This builder.
		 */
		public Builder suggestions(List<RcsSuggestion> suggestions) {
			this.suggestions = suggestions;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * An array of suggestion objects to include with the card. You can include up to 4 suggestions per card.
		 *
		 * @param suggestions The suggestions as varargs.
		 * @return This builder.
		 */
		public Builder suggestions(RcsSuggestion... suggestions) {
			return suggestions(Arrays.asList(suggestions));
		}

		/**
		 * (OPTIONAL)
		 * Add a single suggestion to the card. You can include up to 4 suggestions per card.
		 *
		 * @param suggestion The suggestion to add.
		 * @return This builder.
		 */
		public Builder addSuggestion(RcsSuggestion suggestion) {
			if (this.suggestions == null) {
				this.suggestions = new ArrayList<>(4);
			}
			this.suggestions.add(suggestion);
			return this;
		}

		/**
		 * Builds the RcsCard object.
		 *
		 * @return A new {@linkplain RcsCard}.
		 */
		public RcsCard build() {
			return new RcsCard(this);
		}
	}
}
