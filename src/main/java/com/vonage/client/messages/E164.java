/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.messages;

import java.util.Objects;
import java.util.regex.Pattern;

final class E164 {

	static final Pattern PATTERN = Pattern.compile("[1-9]\\d{6,14}");

	private final String number;

	public E164(String number) {
		Objects.requireNonNull(number, "Number cannot be null");
		String sanitized = number
				.replace(" ", "")
				.replace("-","");
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

	@Override
	public String toString() {
		return number;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof E164)) return false;
		E164 e164 = (E164) o;
		return Objects.equals(number, e164.number);
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}
}
