/*
 *   Copyright 2024 Vonage
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
	/**
	 * Action call succeeded
	 */
	ACTION_CALL_SUCCEEDED,

	/**
	 * Action call failed
	 */
	ACTION_CALL_FAILED,

	/**
	 * Action call info
	 */
	ACTION_CALL_INFO,

	/**
	 * Recipient response
	 */
	RECIPIENT_RESPONSE,

	/**
	 * Run item skipped
	 */
	RUN_ITEM_SKIPPED,

	/**
	 * Run item failed
	 */
	RUN_ITEM_FAILED,

	/**
	 * Run item submitted
	 */
	RUN_ITEM_SUBMITTED,

	/**
	 * Run items total
	 */
	RUN_ITEMS_TOTAL,

	/**
	 * Run items ready
	 */
	RUN_ITEMS_READY,

	/**
	 * Run items excluded
	 */
	RUN_ITEMS_EXCLUDED;

	@JsonCreator
	public static EventType fromString(String value) {
		try {
			return valueOf(value.toUpperCase().replace('-', '_'));
		}
		catch (NullPointerException | IllegalArgumentException ex) {
			return null;
		}
	}

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase().replace('_', '-');
	}	
}
