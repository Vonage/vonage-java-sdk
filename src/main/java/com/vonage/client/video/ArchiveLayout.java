/*
 *   Copyright 2022 Vonage
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.util.Objects;

/**
 * Defines the properties used for {@link VideoClient#setArchiveLayout(String, ArchiveLayout)}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ArchiveLayout {
	private final ScreenLayoutType type, screenshareType;
	private final String stylesheet;

	ArchiveLayout(Builder builder) {
		type = Objects.requireNonNull(builder.type, "Initial layout type is required.");
		if ((screenshareType = builder.screenshareType) != null && type != ScreenLayoutType.BEST_FIT) {
			throw new IllegalStateException(
				"Layout type must be set to '"+ScreenLayoutType.BEST_FIT+"' when setting the screen share layout."
			);
		}
		if (screenshareType == ScreenLayoutType.CUSTOM) {
			throw new IllegalArgumentException("Screen share type cannot be '"+ScreenLayoutType.CUSTOM+"'.");
		}
		if ((stylesheet = builder.stylesheet) != null && type != ScreenLayoutType.CUSTOM) {
			throw new IllegalStateException(
				"Layout type must be set to '"+ ScreenLayoutType.CUSTOM+"' when setting the stylesheet."
			);
		}
		if (type == ScreenLayoutType.CUSTOM && stylesheet == null) {
			throw new IllegalStateException(
				"Stylesheet must be set when initial layout type is set to '"+ ScreenLayoutType.CUSTOM+"'."
			);
		}
	}

	/**
	 * @return The initial layout.
	 */
	public ScreenLayoutType getType() {
		return type;
	}

	/**
	 * @return The layout when screen sharing.
	 */
	public ScreenLayoutType getScreenshareType() {
		return screenshareType;
	}

	/**
	 * @return The stylesheet if using the {@link ScreenLayoutType#CUSTOM} layout.
	 */
	public String getStylesheet() {
		return stylesheet;
	}
	
	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this SetArchiveLayoutRequest object.
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
	 * Entry point for constructing an  object.
	 *
	 * @param initialLayout Initial layout for the composed archive.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder(ScreenLayoutType initialLayout) {
		return new Builder(initialLayout);
	}
	
	public static class Builder {
		private final ScreenLayoutType type;
		private ScreenLayoutType screenshareType;
		private String stylesheet;
	
		Builder(ScreenLayoutType type) {
			this.type = type;
		}

		/**
		 * (OPTIONAL) The layout type to use when there is a screen-sharing stream in the session.
		 * If you set this property, the initial layout must be set to {@link ScreenLayoutType#BEST_FIT}
		 * and the {@linkplain #stylesheet(String)} property left unset.
		 *
		 * @param screenshareType The screen share layout type.
		 * @return This builder.
		 */
		public Builder screenshareType(ScreenLayoutType screenshareType) {
			this.screenshareType = screenshareType;
			return this;
		}

		/**
		 * Used for the {@linkplain ScreenLayoutType#CUSTOM} layout to define the visual layout.
		 * For other layout types, do not set this property.
		 *
		 * @param stylesheet The CSS code to use for the layout. For example: <br>
		 * {@code stream.instructor {position: absolute; width: 100%;  height:50%;}}
		 *
		 * @return This builder.
		 */
		public Builder stylesheet(String stylesheet) {
			this.stylesheet = stylesheet;
			return this;
		}

	
		/**
		 * Builds the {@linkplain ArchiveLayout}.
		 *
		 * @return An instance of ArchiveLayout, populated with all fields from this builder.
		 */
		public ArchiveLayout build() {
			return new ArchiveLayout(this);
		}
	}
}
