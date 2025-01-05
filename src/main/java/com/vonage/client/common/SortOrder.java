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
package com.vonage.client.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the sort order for resources returned.
 *
 * @since 8.4.0
 */
public enum SortOrder {
	/**
	 * Ascending (asc)
	 */
	ASCENDING,

	/**
	 * Descending (desc)
	 */
	DESCENDING;

	@JsonCreator
	public static SortOrder fromString(String value) {
		if (value == null || value.isEmpty()) return null;
		switch (value.toLowerCase()) {
			case "asc": case "ascending": return ASCENDING;
			case "desc": case "descending": return DESCENDING;
			default: throw new IllegalArgumentException("Unknown SortOrder: "+value);
		}
	}

	@JsonValue
	@Override
	public String toString() {
        return this == SortOrder.ASCENDING ? "asc" : "desc";
    }
}
