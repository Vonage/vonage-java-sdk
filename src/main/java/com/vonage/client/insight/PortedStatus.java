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
 * Enum representing whether the user has changed carrier for the number.
 * The assumed status means that the information supplier has replied to the request but has not
 * explicitly reported that the number is ported. Note that this enum may be {@code null}.
 */
public enum PortedStatus {
	@JsonEnumDefaultValue UNKNOWN,
	PORTED,
	NOT_PORTED,
	ASSUMED_NOT_PORTED,
	ASSUMED_PORTED;

	/**
	 * Convert a string value to a PortedStatus enum.
	 *
	 * @param name The string value to convert.
	 *
	 * @return The ported status as an enum, or {@code null} if invalid.
	 */
	@JsonCreator
	public static PortedStatus fromString(String name) {
		return Jsonable.fromString(name, PortedStatus.class);
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
