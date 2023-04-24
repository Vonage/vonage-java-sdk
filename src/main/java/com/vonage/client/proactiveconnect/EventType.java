/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents values for {@link Event#getType()}.
 */
public enum EventType {
	ACTION_CALL_SUCCEEDED,
	ACTION_CALL_FAILED,
	ACTION_CALL_INFO,
	RECIPIENT_RESPONSE,
	RUN_ITEM_SKIPPED,
	RUN_ITEM_FAILED,
	RUN_ITEM_SUBMITTED,
	RUN_ITEMS_TOTAL,
	RUN_ITEMS_READY,
	RUN_ITEMS_EXCLUDED;

	@JsonCreator
	public static EventType fromString(String value) {
		try {
			return valueOf(value.toUpperCase());
		}
		catch (NullPointerException | IllegalArgumentException ex) {
			return null;
		}
	}

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}	
}
