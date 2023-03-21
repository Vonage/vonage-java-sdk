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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the status of an event or update in {@link VerificationCallback#getStatus()}.
 */
public enum VerificationStatus {
	/**
	 *
	 */
	COMPLETED,

	/**
	 *
	 */
	FAILED,

	/**
	 *
	 */
	EXPIRED,

	/**
	 *
	 */
	USER_REJECTED,

	/**
	 *
	 */
	ACTION_PENDING,

	/**
	 *
	 */
	UNUSED;

	@JsonCreator
	public static VerificationStatus fromString(String name) {
		try {
			return VerificationStatus.valueOf(name.toUpperCase());
		}
		catch (IllegalArgumentException ex) {
			return null;
		}
	}

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
