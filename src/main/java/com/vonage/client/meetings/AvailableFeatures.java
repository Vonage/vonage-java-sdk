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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

public class AvailableFeatures extends JsonableBaseObject {
	private Boolean isRecordingAvailable, isChatAvailable, isWhiteboardAvailable, isLocaleSwitcherAvailable;

	protected AvailableFeatures() {
	}

	AvailableFeatures(Builder builder) {
		isRecordingAvailable = builder.isRecordingAvailable;
		isChatAvailable = builder.isChatAvailable;
		isWhiteboardAvailable = builder.isWhiteboardAvailable;
		isLocaleSwitcherAvailable = builder.isLocaleSwitcherAvailable;
	}

	/**
	 * Determine if recording feature is available in the UI.
	 *
	 * @return {@code true} if the feature is available.
	 */
	@JsonProperty("is_recording_available")
	public Boolean getIsRecordingAvailable() {
		return isRecordingAvailable;
	}

	/**
	 * Determine if chat feature is available in the UI.
	 *
	 * @return {@code true} if the feature is available.
	 */
	@JsonProperty("is_chat_available")
	public Boolean getIsChatAvailable() {
		return isChatAvailable;
	}

	/**
	 * Determine if whiteboard feature is available in the UI.
	 *
	 * @return {@code true} if the feature is available.
	 */
	@JsonProperty("is_whiteboard_available")
	public Boolean getIsWhiteboardAvailable() {
		return isWhiteboardAvailable;
	}

	/**
	 * Determine if locale switcher feature is available in the UI.
	 *
	 * @return {@code true} if the feature is available.
	 */
	@JsonProperty("is_locale_switcher_available")
	public Boolean getIsLocaleSwitcherAvailable() {
		return isLocaleSwitcherAvailable;
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
		private Boolean isRecordingAvailable, isChatAvailable, isWhiteboardAvailable, isLocaleSwitcherAvailable;
	
		Builder() {}
	
		/**
		 *
		 * @param isRecordingAvailable Determine if recording feature is available in the UI.
		 *
		 * @return This builder.
		 */
		public Builder isRecordingAvailable(boolean isRecordingAvailable) {
			this.isRecordingAvailable = isRecordingAvailable;
			return this;
		}

		/**
		 *
		 * @param isChatAvailable Determine if chat feature is available in the UI.
		 *
		 * @return This builder.
		 */
		public Builder isChatAvailable(boolean isChatAvailable) {
			this.isChatAvailable = isChatAvailable;
			return this;
		}

		/**
		 *
		 * @param isWhiteboardAvailable Determine if whiteboard feature is available in the UI.
		 *
		 * @return This builder.
		 */
		public Builder isWhiteboardAvailable(boolean isWhiteboardAvailable) {
			this.isWhiteboardAvailable = isWhiteboardAvailable;
			return this;
		}

		/**
		 *
		 * @param isLocaleSwitcherAvailable Determine if locale switcher feature is available in the UI.
		 *
		 * @return This builder.
		 */
		public Builder isLocaleSwitcherAvailable(boolean isLocaleSwitcherAvailable) {
			this.isLocaleSwitcherAvailable = isLocaleSwitcherAvailable;
			return this;
		}
	
		/**
		 * Builds the {@linkplain AvailableFeatures}.
		 *
		 * @return An instance of AvailableFeatures, populated with all fields from this builder.
		 */
		public AvailableFeatures build() {
			return new AvailableFeatures(this);
		}
	}
}
