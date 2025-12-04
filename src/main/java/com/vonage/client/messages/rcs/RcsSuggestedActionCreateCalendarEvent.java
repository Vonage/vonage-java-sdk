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
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.time.Instant;
import java.util.Objects;

/**
 * Represents a suggested action to create a calendar event in an RCS message.
 *
 * @since 9.6.0
 */
public class RcsSuggestedActionCreateCalendarEvent extends JsonableBaseObject implements RcsSuggestion {
	private String text, postbackData, title, description;
	private Instant startTime, endTime;
	private URI fallbackUrl;

	protected RcsSuggestedActionCreateCalendarEvent() {
	}

	private RcsSuggestedActionCreateCalendarEvent(Builder builder) {
		this.text = Objects.requireNonNull(builder.text, "Text is required.");
		if (text.length() > 25) {
			throw new IllegalArgumentException("Text must be 25 characters or less.");
		}
		this.postbackData = Objects.requireNonNull(builder.postbackData, "Postback data is required.");
		this.startTime = Objects.requireNonNull(builder.startTime, "Start time is required.");
		this.endTime = Objects.requireNonNull(builder.endTime, "End time is required.");
		this.title = Objects.requireNonNull(builder.title, "Title is required.");
		this.description = builder.description;
		this.fallbackUrl = builder.fallbackUrl;
	}

	@Override
	@JsonProperty("type")
	public String getType() {
		return "create_calendar_event";
	}

	@Override
	@JsonProperty("text")
	public String getText() {
		return text;
	}

	@Override
	@JsonProperty("postback_data")
	public String getPostbackData() {
		return postbackData;
	}

	/**
	 * The start time of the calendar event in ISO 8601 format.
	 *
	 * @return The start time.
	 */
	@JsonProperty("start_time")
	public Instant getStartTime() {
		return startTime;
	}

	/**
	 * The end time of the calendar event in ISO 8601 format.
	 *
	 * @return The end time.
	 */
	@JsonProperty("end_time")
	public Instant getEndTime() {
		return endTime;
	}

	/**
	 * The title of the calendar event.
	 *
	 * @return The title.
	 */
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	/**
	 * A description of the calendar event.
	 *
	 * @return The description, or {@code null} if not set.
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * A URL to open if the device is unable to create a calendar event.
	 *
	 * @return The fallback URL, or {@code null} if not set.
	 */
	@JsonProperty("fallback_url")
	public URI getFallbackUrl() {
		return fallbackUrl;
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
		private String text, postbackData, title, description;
		private Instant startTime, endTime;
		private URI fallbackUrl;

		/**
		 * (REQUIRED)
		 * The text to display on the suggestion chip.
		 *
		 * @param text The suggestion text (max 25 characters).
		 * @return This builder.
		 */
		public Builder text(String text) {
			this.text = text;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The data that will be sent back in the {@code button.payload} property of a {@code button} message
		 * via the inbound message webhook when the user taps the suggestion chip.
		 *
		 * @param postbackData The postback data string.
		 * @return This builder.
		 */
		public Builder postbackData(String postbackData) {
			this.postbackData = postbackData;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The start time of the calendar event.
		 *
		 * @param startTime The start time.
		 * @return This builder.
		 */
		public Builder startTime(Instant startTime) {
			this.startTime = startTime;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The end time of the calendar event.
		 *
		 * @param endTime The end time.
		 * @return This builder.
		 */
		public Builder endTime(Instant endTime) {
			this.endTime = endTime;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The title of the calendar event.
		 *
		 * @param title The title.
		 * @return This builder.
		 */
		public Builder title(String title) {
			this.title = title;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * A description of the calendar event.
		 *
		 * @param description The description.
		 * @return This builder.
		 */
		public Builder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * A URL to open if the device is unable to create a calendar event.
		 *
		 * @param fallbackUrl The fallback URL as a string.
		 * @return This builder.
		 */
		public Builder fallbackUrl(String fallbackUrl) {
			return fallbackUrl(URI.create(fallbackUrl));
		}

		/**
		 * (OPTIONAL)
		 * A URL to open if the device is unable to create a calendar event.
		 *
		 * @param fallbackUrl The fallback URL.
		 * @return This builder.
		 */
		public Builder fallbackUrl(URI fallbackUrl) {
			this.fallbackUrl = fallbackUrl;
			return this;
		}

		/**
		 * Builds the RcsSuggestedActionCreateCalendarEvent object.
		 *
		 * @return A new {@linkplain RcsSuggestedActionCreateCalendarEvent}.
		 */
		public RcsSuggestedActionCreateCalendarEvent build() {
			return new RcsSuggestedActionCreateCalendarEvent(this);
		}
	}
}
