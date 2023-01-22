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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UISettings {
	private String language;

	UISettings(Builder builder) {
		language = builder.language;
	}

	protected UISettings() {
	}

	/**
	 * Language setting.
	 *
	 * @return The language, as a string.
	 */
	@JsonProperty("language")
	public String getLanguage() {
		return language;
	}

	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this UISettings object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
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
		private String language;

		Builder() {}

		/**
		 *
		 * @param language The language setting.
		 *
		 * @return This builder.
		 */
		public Builder language(String language) {
			this.language = language;
			return this;
		}

		/**
		 * Builds the {@linkplain UISettings}.
		 *
		 * @return An instance of UISettings, populated with all fields from this builder.
		 */
		public UISettings build() {
			return new UISettings(this);
		}
	}
}
