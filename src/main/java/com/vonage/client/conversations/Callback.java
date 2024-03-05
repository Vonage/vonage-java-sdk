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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.HttpMethod;
import java.net.URI;
import java.util.UUID;

/**
 * Callback properties for a {@link Conversation}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Callback extends JsonableBaseObject {
	private URI url;
	private String eventMask;
	private Params params;
	private HttpMethod method;

	Callback() {}

	Callback(Builder builder) {
		url = builder.url;
		if ((eventMask = builder.eventMask) != null && (eventMask.length() > 200 || eventMask.trim().isEmpty())) {
			throw new IllegalArgumentException("Event mask must be between 1 and 200 characters");
		}
		if ((method = builder.method) != null && !(method == HttpMethod.POST || method == HttpMethod.GET)) {
			throw new IllegalArgumentException("Callback HTTP method must be either POST or GET, not "+method);
		}
		params = builder.params;
	}

	/**
	 * Event URL for the callback.
	 * 
	 * @return The callback URL, or {@code null} if unspecified.
	 */
	@JsonProperty("url")
	public URI getUrl() {
		return url;
	}

	@JsonProperty("event_mask")
	public String getEventMask() {
		return eventMask;
	}

	/**
	 * Additional parameters.
	 * 
	 * @return The callback parameters, or {@code null} if unspecified.
	 */
	@JsonProperty("params")
	public Params getParams() {
		return params;
	}

	/**
	 * Method to use for the callback, either {@linkplain HttpMethod#GET} or {@linkplain HttpMethod#POST}.
	 * 
	 * @return The HTTP method as an enum, or {@code null} if unspecified.
	 */
	@JsonProperty("method")
	public HttpMethod getMethod() {
		return method;
	}

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Params extends JsonableBaseObject {
		private UUID applicationId;
		private URI nccoUrl;

		protected Params() {}

		/**
		 * Vonage Application ID.
		 *
		 * @return The application ID, or {@code null} if unspecified.
		 */
		@JsonProperty("applicationId")
		public UUID getApplicationId() {
			return applicationId;
		}

		/**
		 * Call Control Object URL to use for the callback.
		 *
		 * @return The NCCO URL, or {@code null} if unspecified.
		 */
		@JsonProperty("ncco_url")
		public URI getNccoUrl() {
			return nccoUrl;
		}
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
		private URI url;
		private String eventMask;
		private HttpMethod method;
		private Params params;

		private Builder() {}

		private Params initParams() {
			if (params == null) {
				params = new Params();
			}
			return params;
		}

		/**
		 * Event URL for the callback.
		 *
		 * @param url The callback URL as a string.
		 *
		 * @return This builder.
		 */
		public Builder url(String url) {
			this.url = URI.create(url);
			return this;
		}

		/**
		 *
		 *
		 * @param eventMask
		 *
		 * @return This builder.
		 */
		public Builder eventMask(String eventMask) {
			this.eventMask = eventMask;
			return this;
		}

		/**
		 * HTTP method to use for the callback.
		 * Must be either {@linkplain HttpMethod#GET} or {@linkplain HttpMethod#POST}.
		 *
		 * @param method The HTTP method as an enum, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder method(HttpMethod method) {
			this.method = method;
			return this;
		}

		/**
		 * Vonage Application ID.
		 *
		 * @param applicationId The application ID as a string.
		 *
		 * @return This builder.
		 */
		public Builder applicationId(String applicationId) {
			initParams().applicationId = UUID.fromString(applicationId);
			return this;
		}

		/**
		 * Call Control Object URL to use for the callback.
		 *
		 * @param nccoUrl The NCCO URL as a string.
		 *
		 * @return This builder.
		 */
		public Builder nccoUrl(String nccoUrl) {
			initParams().nccoUrl = URI.create(nccoUrl);
			return this;
		}

		/**
		 * Builds the {@linkplain Callback}.
		 *
		 * @return An instance of Callback, populated with all fields from this builder.
		 */
		public Callback build() {
			return new Callback(this);
		}
	}
}
