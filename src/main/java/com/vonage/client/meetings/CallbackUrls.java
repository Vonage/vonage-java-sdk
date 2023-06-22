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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallbackUrls {
	private URI roomsCallbackUrl, sessionsCallbackUrl, recordingsCallbackUrl;

	protected CallbackUrls() {
	}

	CallbackUrls(Builder builder) {
		if (builder.roomsCallbackUrl != null) {
			roomsCallbackUrl = URI.create(builder.roomsCallbackUrl);
		}
		if (builder.sessionsCallbackUrl != null) {
			sessionsCallbackUrl = URI.create(builder.sessionsCallbackUrl);
		}
		if (builder.recordingsCallbackUrl != null) {
			recordingsCallbackUrl = URI.create(builder.recordingsCallbackUrl);
		}
	}

	/**
	 * Callback url for rooms events, overrides application level rooms callback URL.
	 *
	 * @return The rooms callback URL.
	 */
	@JsonProperty("rooms_callback_url")
	public URI getRoomsCallbackUrl() {
		return roomsCallbackUrl;
	}

	/**
	 * Callback url for sessions events, overrides application level sessions callback URL.
	 *
	 * @return The sessions callback URL.
	 */
	@JsonProperty("sessions_callback_url")
	public URI getSessionsCallbackUrl() {
		return sessionsCallbackUrl;
	}

	/**
	 * Callback url for recordings events, overrides application level recordings callback URL.
	 *
	 * @return The recordings callback URL.
	 */
	@JsonProperty("recordings_callback_url")
	public URI getRecordingsCallbackUrl() {
		return recordingsCallbackUrl;
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
		private String roomsCallbackUrl, sessionsCallbackUrl, recordingsCallbackUrl;
	
		Builder() {}
	
		/**
		 *
		 * @param roomsCallbackUrl Callback url for rooms events, overrides application level rooms callback url.
		 *
		 * @return This builder.
		 */
		public Builder roomsCallbackUrl(String roomsCallbackUrl) {
			this.roomsCallbackUrl = roomsCallbackUrl;
			return this;
		}

		/**
		 *
		 * @param sessionsCallbackUrl Callback url for sessions events, overrides application level sessions callback url.
		 *
		 * @return This builder.
		 */
		public Builder sessionsCallbackUrl(String sessionsCallbackUrl) {
			this.sessionsCallbackUrl = sessionsCallbackUrl;
			return this;
		}

		/**
		 *
		 * @param recordingsCallbackUrl Callback url for recordings events, overrides application level recordings callback url.
		 *
		 * @return This builder.
		 */
		public Builder recordingsCallbackUrl(String recordingsCallbackUrl) {
			this.recordingsCallbackUrl = recordingsCallbackUrl;
			return this;
		}

	
		/**
		 * Builds the {@linkplain CallbackUrls}.
		 *
		 * @return An instance of CallbackUrls, populated with all fields from this builder.
		 */
		public CallbackUrls build() {
			return new CallbackUrls(this);
		}
	}
}
