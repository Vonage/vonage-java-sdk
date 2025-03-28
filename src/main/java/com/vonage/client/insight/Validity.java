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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.vonage.client.Jsonable;

/**
 * Enum representing the existence of a number.
 * {@code UNKNOWN} means the number could not be validated. valid means the number is valid.
 * {@code NOT_VALID} means the number is not valid.
 * {@code INFERRED_NOT_VALID} means that the number could not be determined as valid or invalid
 * via an external system and the best guess is that the number is invalid.
 * This is applicable to mobile numbers only.
 */
public enum Validity {
	@JsonEnumDefaultValue UNKNOWN,
	VALID,
	NOT_VALID,
	INFERRED,
	INFERRED_NOT_VALID;

	/**
	 * Convert a string value to a Validity enum.
	 *
	 * @param name The string value to convert.
	 *
	 * @return The validity as an enum, or {@code null} if invalid.
	 */
	@JsonCreator
	public static Validity fromString(String name) {
		return Jsonable.fromString(name, Validity.class);
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
