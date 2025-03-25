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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.vonage.client.Jsonable;

/**
 * Represents the state of a conversation member.
 */
public enum MemberState {
	INVITED,
	JOINED,
	LEFT,
	@JsonEnumDefaultValue UNKNOWN;

	/**
	 * Convert a string to a MemberState enum.
	 *
	 * @param name The string to convert.
	 *
	 * @return The MemberState enum, or {@code null} if invalid.
	 */
	@JsonCreator
	public static MemberState fromString(String name) {
		return Jsonable.fromString(name, MemberState.class);
	}
}
