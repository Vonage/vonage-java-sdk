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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.util.UUID;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UpdateApplicationRequest {
	private final UUID defaultThemeId;

	UpdateApplicationRequest(Builder builder) {
		defaultThemeId = builder.defaultThemeId;
	}

	/**
	 * @return The theme ID to set as application default theme.
	 */
	@JsonProperty("default_theme_id")
	public UUID getDefaultThemeId() {
		return defaultThemeId;
	}
	
	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this UpdateApplicationRequest object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException(
				  "Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe
			);
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
		private UUID defaultThemeId;
	
		Builder() {}
	
		/**
		 *
		 * @param defaultThemeId The theme ID to set as application default theme.
		 *
		 * @return This builder.
		 */
		public Builder defaultThemeId(UUID defaultThemeId) {
			this.defaultThemeId = defaultThemeId;
			return this;
		}

		/**
		 *
		 * @param defaultThemeId The theme ID to set as application default theme.
		 *
		 * @return This builder.
		 */
		public Builder defaultThemeId(String defaultThemeId) {
			return defaultThemeId(UUID.fromString(defaultThemeId));
		}

	
		/**
		 * Builds the {@linkplain UpdateApplicationRequest}.
		 *
		 * @return An instance of UpdateApplicationRequest, populated with all fields from this builder.
		 */
		public UpdateApplicationRequest build() {
			return new UpdateApplicationRequest(this);
		}
	}
}