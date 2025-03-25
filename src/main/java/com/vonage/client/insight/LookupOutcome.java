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
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

/**
 * Enum representing whether all information about a phone number has been returned.
 * <p>
 * {@code 0} is success,
 * {@code 1} is a partial success (some fields populated),
 * {@code 2} is failure.
 */
public enum LookupOutcome {
	SUCCESS(0),
	PARTIAL_SUCCESS(1),
	FAILED(2);

	private final int code;

	LookupOutcome(int code) {
		this.code = code;
	}

	/**
	 * Gets the enum code.
	 *
	 * @return The code used to create this enum.
	 */
	@JsonValue
	public int getCode() {
		return code;
	}

	/**
	 * Convert an integer value to a LookupOutcome enum.
	 *
	 * @param code The integer value to convert.
	 *
	 * @return The LookupOutcome enum, or {@code null} if unknown.
	 */
	@JsonCreator
	public static LookupOutcome fromInt(Integer code) {
		if (code == null) return null;
		return Arrays.stream(LookupOutcome.values())
				.filter(lo -> lo.code == code)
				.findFirst().orElse(null);
	}
}
