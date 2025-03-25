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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents the available layout types for archives in {@link StreamCompositionLayout}.
 */
public enum ScreenLayoutType {
	/**
	 * Best fit
	 */
	BEST_FIT("bestFit"),

	/**
	 * Custom (uses stylesheet)
	 */
	CUSTOM("custom"),

	/**
	 * Horizontal presentation
	 */
	HORIZONTAL("horizontalPresentation"),

	/**
	 * Vertical presentation
	 */
	VERTICAL("verticalPresentation"),

	/**
	 * Picture-in-picture
	 */
	PIP("pip");

	private static final Map<String, ScreenLayoutType> TYPE_INDEX =
		Arrays.stream(ScreenLayoutType.values()).collect(Collectors.toMap(
				ScreenLayoutType::toString, Function.identity()
		));

	private final String value;

	ScreenLayoutType(String value) {
		this.value = value;
	}

	/**
	 * Convert a string to a ScreenLayoutType enum.
	 *
	 * @param value The string to convert.
	 *
	 * @return The ScreenLayoutType as an enum, or {@code null} if the value is null.
	 * @throws IllegalArgumentException If the string is not a valid enum value.
	 */
	@JsonCreator
	public static ScreenLayoutType fromString(String value) {
		return TYPE_INDEX.getOrDefault(value, null);
	}

	@JsonValue
	@Override
	public String toString() {
		return value;
	}	
}
