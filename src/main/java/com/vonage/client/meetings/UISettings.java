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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

public class UISettings extends JsonableBaseObject {
	private RoomLanguage language;

	UISettings(Builder builder) {
		language = builder.language;
	}

	protected UISettings() {
	}

	/**
	 * Language setting.
	 *
	 * @return The language, as an enum.
	 */
	@JsonProperty("language")
	public RoomLanguage getLanguage() {
		return language;
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
		private RoomLanguage language;

		Builder() {}

		/**
		 *
		 * @param language The language setting.
		 *
		 * @return This builder.
		 */
		public Builder language(RoomLanguage language) {
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
