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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the level of approval needed to join the meeting in the room.
 */
public enum JoinApprovalLevel {
	/**
	 * Participants can join the meeting at any time without approval.
	 */
	NONE,

	/**
	 * Participants will join the meeting only after the host joined.
	 */
	AFTER_OWNER_ONLY,

	/**
	 * Participants will join the waiting room and the host will deny/approve them.
	 */
	EXPLICIT_APPROVAL;

	@JsonCreator
	public static JoinApprovalLevel fromString(String value) {
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
