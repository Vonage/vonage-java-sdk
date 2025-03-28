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
import com.vonage.client.Jsonable;

/**
 * Enum representing whether you can call number now. This is applicable to mobile numbers only.
 * Note that this enum may be {@code null}.
 */
public enum Reachability {
	UNKNOWN, REACHABLE, UNDELIVERABLE, ABSENT, BAD_NUMBER, BLACKLISTED;

	/**
	 * Convert a string value to a Reachability enum.
	 *
	 * @param name The string value to convert.
	 *
	 * @return The reachability as an enum, or {@code null} if invalid.
	 */
	@JsonCreator
	public static Reachability fromString(String name) {
		return Jsonable.fromString(name, Reachability.class);
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
