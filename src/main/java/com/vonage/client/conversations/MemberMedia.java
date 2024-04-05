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

/**
 * Contains the media properties for {@link Member#getMedia()}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class MemberMedia extends JsonableBaseObject {
	private Boolean audio;
	private MediaAudioSettings audioSettings;

	MemberMedia(Builder builder) {
		audio = builder.audio;
		audioSettings = builder.audioSettings;
	}

	MemberMedia() {
	}

	/**
	 * Whether the media has audio.
	 * 
	 * @return {@code true} if audio is present, or {@code null} if unspecified.
	 */
	@JsonProperty("audio")
	public Boolean getAudio() {
		return audio;
	}

	/**
	 * Audio-related properties.
	 * 
	 * @return The audio settings object, or {@code null} if not applicable.
	 */
	@JsonProperty("audio_settings")
	public MediaAudioSettings getAudioSettings() {
		return audioSettings;
	}


	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for constructing MemberMedia. All fields are optional.
	 */
	public static final class Builder {
		private Boolean audio;
		private MediaAudioSettings audioSettings;

		private Builder() {}

		private MediaAudioSettings initAudioSettings() {
			if (audioSettings == null) {
				audioSettings = new MediaAudioSettings();
			}
			return audioSettings;
		}

		/**
		 *
		 * @param audio
		 *
		 * @return This builder.
		 */
		public Builder audio(boolean audio) {
			this.audio = audio;
			return this;
		}

		/**
		 *
		 * @param enabled
		 *
		 * @return This builder.
		 */
		public Builder audioEnabled(boolean enabled) {
			initAudioSettings().enabled = enabled;
			return this;
		}

		/**
		 *
		 * @param earmuffed
		 *
		 * @return This builder.
		 */
		public Builder earmuffed(boolean earmuffed) {
			initAudioSettings().earmuffed = earmuffed;
			return this;
		}

		/**
		 *
		 * @param muted
		 *
		 * @return This builder.
		 */
		public Builder muted(boolean muted) {
			initAudioSettings().muted = muted;
			return this;
		}

		/**
		 * Builds the {@linkplain MemberMedia}.
		 *
		 * @return An instance of MemberMedia, populated with all fields from this builder.
		 */
		public MemberMedia build() {
			return new MemberMedia(this);
		}
	}
}
