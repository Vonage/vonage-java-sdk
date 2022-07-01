/*
 *   Copyright 2020 Vonage
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

/**
 * Enum representing the existence of a number.
 * <code>UNKNOWN</code> means the number could not be validated. valid means the number is valid.
 * <code>NOT_VALID</code> means the number is not valid.
 * <code>INFERRED_NOT_VALID</code> means that the number could not be determined as valid or invalid
 * via an external system and the best guess is that the number is invalid.
 * This is applicable to mobile numbers only.
 */
public enum Validity {
	UNKNOWN, VALID, NOT_VALID, INFERRED, INFERRED_NOT_VALID;

	@JsonCreator
	public static Validity fromString(String name) {
		try {
			return Validity.valueOf(name.toUpperCase());
		}
		catch (IllegalArgumentException ex) {
			return UNKNOWN;
		}
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
