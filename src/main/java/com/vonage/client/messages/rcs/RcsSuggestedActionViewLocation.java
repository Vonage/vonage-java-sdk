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
import java.util.Objects;

/**
 * Represents a suggested action to view a location on a map in an RCS message.
 *
 * @since 9.6.0
 */
public class RcsSuggestedActionViewLocation extends JsonableBaseObject implements RcsSuggestion {
	private String text, postbackData, latitude, longitude, pinLabel;
	private URI fallbackUrl;

	protected RcsSuggestedActionViewLocation() {
	}

	private RcsSuggestedActionViewLocation(Builder builder) {
		this.text = Objects.requireNonNull(builder.text, "Text is required.");
		if (text.length() > 25) {
			throw new IllegalArgumentException("Text must be 25 characters or less.");
		}
		this.postbackData = Objects.requireNonNull(builder.postbackData, "Postback data is required.");
		this.latitude = Objects.requireNonNull(builder.latitude, "Latitude is required.");
		this.longitude = Objects.requireNonNull(builder.longitude, "Longitude is required.");
		this.pinLabel = Objects.requireNonNull(builder.pinLabel, "Pin label is required.");
		this.fallbackUrl = builder.fallbackUrl;
	}

	@Override
	@JsonProperty("type")
	public String getType() {
		return "view_location";
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
	 * The latitude of the location to view.
	 *
	 * @return The latitude string.
	 */
	@JsonProperty("latitude")
	public String getLatitude() {
		return latitude;
	}

	/**
	 * The longitude of the location to view.
	 *
	 * @return The longitude string.
	 */
	@JsonProperty("longitude")
	public String getLongitude() {
		return longitude;
	}

	/**
	 * A label to display on the location pin.
	 *
	 * @return The pin label.
	 */
	@JsonProperty("pin_label")
	public String getPinLabel() {
		return pinLabel;
	}

	/**
	 * A URL to open if the device is unable to display a map.
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
		private String text, postbackData, latitude, longitude, pinLabel;
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
		 * The latitude of the location to view.
		 *
		 * @param latitude The latitude as a string.
		 * @return This builder.
		 */
		public Builder latitude(String latitude) {
			this.latitude = latitude;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The latitude of the location to view.
		 *
		 * @param latitude The latitude as a double.
		 * @return This builder.
		 */
		public Builder latitude(double latitude) {
			return latitude(String.valueOf(latitude));
		}

		/**
		 * (REQUIRED)
		 * The longitude of the location to view.
		 *
		 * @param longitude The longitude as a string.
		 * @return This builder.
		 */
		public Builder longitude(String longitude) {
			this.longitude = longitude;
			return this;
		}

		/**
		 * (REQUIRED)
		 * The longitude of the location to view.
		 *
		 * @param longitude The longitude as a double.
		 * @return This builder.
		 */
		public Builder longitude(double longitude) {
			return longitude(String.valueOf(longitude));
		}

		/**
		 * (REQUIRED)
		 * A label to display on the location pin.
		 *
		 * @param pinLabel The pin label.
		 * @return This builder.
		 */
		public Builder pinLabel(String pinLabel) {
			this.pinLabel = pinLabel;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * A URL to open if the device is unable to display a map.
		 *
		 * @param fallbackUrl The fallback URL as a string.
		 * @return This builder.
		 */
		public Builder fallbackUrl(String fallbackUrl) {
			return fallbackUrl(URI.create(fallbackUrl));
		}

		/**
		 * (OPTIONAL)
		 * A URL to open if the device is unable to display a map.
		 *
		 * @param fallbackUrl The fallback URL.
		 * @return This builder.
		 */
		public Builder fallbackUrl(URI fallbackUrl) {
			this.fallbackUrl = fallbackUrl;
			return this;
		}

		/**
		 * Builds the RcsSuggestedActionViewLocation object.
		 *
		 * @return A new {@linkplain RcsSuggestedActionViewLocation}.
		 */
		public RcsSuggestedActionViewLocation build() {
			return new RcsSuggestedActionViewLocation(this);
		}
	}
}
