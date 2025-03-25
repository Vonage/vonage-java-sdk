/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.Jsonable;

/**
 * Represents event substates for advanced machine detection in {@link EventWebhook#getMachineDetectionSubstate()}.
 *
 * @since 8.2.0
 */
public enum MachineDetectionStatus {

	/**
	 * The beginning of the voice mail beep.
	 */
	BEEP_START,

	/**
	 * Beep wasn't received after waiting for the designated period.
	 */
	BEEP_TIMEOUT;

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}

	/**
	 * Convert a string value to a MachineDetectionStatus enum.
	 *
	 * @param name The string value to convert.
	 *
	 * @return The machine detection status as an enum, or {@code null} if invalid.
	 */
	@JsonCreator
	public static MachineDetectionStatus fromString(String name) {
		return Jsonable.fromString(name, MachineDetectionStatus.class);
	}
}
