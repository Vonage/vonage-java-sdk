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
import com.vonage.client.Jsonable;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UpdateApplicationRequest implements Jsonable {
	private final UUID defaultThemeId;

	UpdateApplicationRequest(Builder builder) {
		defaultThemeId = Objects.requireNonNull(builder.defaultThemeId, "Default theme ID is required.");
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
		return "{\"update_details\": "+ Jsonable.super.toJson() + "}";
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
		 * Builds the {@linkplain UpdateApplicationRequest}.
		 *
		 * @return An instance of UpdateApplicationRequest, populated with all fields from this builder.
		 */
		public UpdateApplicationRequest build() {
			return new UpdateApplicationRequest(this);
		}
	}
}
