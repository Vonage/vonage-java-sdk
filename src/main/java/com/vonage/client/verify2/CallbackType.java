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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.Jsonable;

/**
 * Represents the type of webhook in {@link VerificationCallback#getType()}.
 */
public enum CallbackType {
	/**
	 * Used for miscellaneous event updates.
	 */
	EVENT,

	/**
	 * Used for inbound status updates for a particular request.
	 */
	SUMMARY;

	/**
	 * Convert a string to a CallbackType enum.
	 *
	 * @param name The string to convert.
	 *
	 * @return The CallbackType as an enum, or {@code null} if invalid.
	 */
	@JsonCreator
	public static CallbackType fromString(String name) {
		return Jsonable.fromString(name, CallbackType.class);
	}

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
