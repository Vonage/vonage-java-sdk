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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents the audio options in {@link MemberMedia#getAudioSettings}.
 */
public class MediaAudioSettings extends JsonableBaseObject {
	Boolean enabled, earmuffed, muted;

	protected MediaAudioSettings() {}

	/**
	 * Whether audio is enabled.
	 * 
	 * @return {@code true} if enabled, or {@code null} if unspecified.
	 */
	@JsonProperty("enabled")
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * Whether hearing audio is disabled.
	 * 
	 * @return {@code true} if earmuffed, or {@code null} if unspecified.
	 */
	@JsonProperty("earmuffed")
	public Boolean getEarmuffed() {
		return earmuffed;
	}

	/**
	 * Whether producing audio is disabled.
	 * 
	 * @return {@code true} if muted, or {@code null} if unspecified.
	 */
	@JsonProperty("muted")
	public Boolean getMuted() {
		return muted;
	}
}
