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

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Utility class for validating and sanitising phone numbers to be compliant with the E164 format.
 */
public class E164 {
	static final Pattern PATTERN = Pattern.compile("[1-9]\\d{6,14}");

	private final String number;

	public E164(String number) {
		Objects.requireNonNull(number, "Number cannot be null");
		String sanitized = number.replace(" ", "").replace("-","");
		if (sanitized.startsWith("+")) {
			sanitized = sanitized.substring(1);
		}
		if (PATTERN.matcher(sanitized).matches()) {
			this.number = sanitized;
		}
		else {
			throw new IllegalArgumentException("Malformed E.164 number: "+number);
		}
	}

	@JsonValue
	@Override
	public String toString() {
		return number;
	}
}
