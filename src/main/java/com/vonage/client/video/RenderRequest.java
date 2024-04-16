/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.Objects;

/**
 * Defines the parameters for starting an Experience Composer session.
 *
 * @since 8.6.0
 */
public final class RenderRequest extends AbstractSessionTokenRequest {
	private URI url;
	private Integer maxDuration;
	private Resolution resolution;
	private Properties properties;

	private RenderRequest() {}

	private RenderRequest(Builder builder) {
		url = Objects.requireNonNull(builder.url, "URL is required.");
		properties = new Properties(builder.name);
		if ((maxDuration = builder.maxDuration) != null && (maxDuration < 60 || maxDuration > 36000)) {
			throw new IllegalArgumentException("Max duration must be between 60 and 36000 seconds.");
		}
		if ((resolution = builder.resolution) != null) switch (resolution) {
			default: throw new IllegalArgumentException("Unsupported resolution: "+resolution);
			case HD_PORTRAIT: case SD_PORTRAIT: case HD_LANDSCAPE: case SD_LANDSCAPE: break;
		}
	}

	/**
	 * Publisher initial configuration properties for the composed output stream.
	 */
	public static class Properties extends JsonableBaseObject {
		private final String name;

		public Properties(String name) {
			if ((this.name = name) == null || name.isEmpty()) {
				throw new IllegalArgumentException("Name is required.");
			}
			if (name.length() > 200) {
				throw new IllegalArgumentException("Name cannot exceed 200 characters.");
			}
		}

		/**
		 * Name of the composed output stream which is published to the session.
		 *
		 * @return The stream name.
		 */
		@JsonProperty("name")
		public String getName() {
			return name;
		}
	}

	/**
	 * A publicly reachable URL controlled by the customer and capable of generating the content
	 * to be rendered without user intervention.
	 *
	 * @return The URL, or {@code null} if unspecified.
	 */
	@JsonProperty("url")
	public URI getUrl() {
		return url;
	}

	/**
	 * Maximum time allowed for the Experience Composer, in seconds. After this time, it is stopped automatically,
	 * if it is still running. The maximum value is 36000 (10 hours), the minimum value is 60 (1 minute), and the
	 * default value is 7200 (2 hours). When the Experience Composer ends, its stream is unpublished and an event
	 * is posted to the callback URL, if configured in the Application Config.
	 *
	 * @return The maximum duration in seconds as an Integer, or {@code null} if unspecified.
	 */
	@JsonProperty("maxDuration")
	public Integer getMaxDuration() {
		return maxDuration;
	}

	/**
	 * Render resolution of the Experience Composer.
	 *
	 * @return The render resolution as an enum, or {@code null} if unspecified.
	 */
	@JsonProperty("resolution")
	public Resolution getResolution() {
		return resolution;
	}

	/**
	 * The initial configuration of Publisher properties for the composed output stream.
	 *
	 * @return The publisher properties object.
	 */
	@JsonProperty("properties")
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder instance.
	 */
	public static Builder Builder() {
		return new Builder();
	}

	/**
	 * Builder for defining the parameters of {@link RenderRequest}.
	 */
	public static class Builder extends AbstractSessionTokenRequest.Builder<RenderRequest, Builder> {
		private URI url;
		private Integer maxDuration;
		private Resolution resolution;
		private String name;

		/**
		 * (REQUIRED)
		 * Name of the composed output stream which is published to the session.
		 *
		 * @param name The stream name.
		 * @return This builder.
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * (REQUIRED)
		 * URL of the customer service where the callbacks will be received.
		 *
		 * @param url The URL as a string.
		 * @return This builder.
		 */
		public Builder url(String url) {
			return url(URI.create(url));
		}

		/**
		 * (REQUIRED)
		 * URL of the customer service where the callbacks will be received.
		 *
		 * @param url The URL as a URI.
		 * @return This builder.
		 */
		public Builder url(URI url) {
			this.url = url;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Maximum time allowed for the Experience Composer, in seconds. After this time, it is
		 * stopped automatically, if it is still running. The maximum value is 36000 (10 hours),
		 * the minimum value is 60 (1 minute), and the default value is 7200 (2 hours). When the
		 * Experience Composer ends, its stream is unpublished and an event is posted to the
		 * callback URL, if configured in the Application Config.
		 *
		 * @param maxDuration The maximum duration in seconds as an int.
		 * @return This builder.
		 */
		public Builder maxDuration(int maxDuration) {
			this.maxDuration = maxDuration;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Resolution of the display area for the composition. Must be either "640x480", "1280x720",
		 * "480x640", or "720x1280". 640x480 is the default. 1080p is not supported.
		 *
		 * @param resolution The resolution as an enum.
		 * @return This builder.
		 */
		public Builder resolution(Resolution resolution) {
			this.resolution = resolution;
			return this;
		}

		/**
		 * Builds the RenderRequest with this builder's properties.
		 *
		 * @return A new RenderRequest instance.
		 */
		public RenderRequest build() {
			return new RenderRequest(this);
		}
	}
}
