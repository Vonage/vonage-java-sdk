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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Utility class for validating a WhatsApp business-scoped user ID (BSUID).
 * <p>
 * A BSUID is a unique identifier assigned by Meta to each WhatsApp user, tied to a specific business portfolio.
 * It is the primary way to identify and message a user when their phone number is not available (for example,
 * when the user has adopted a WhatsApp username). A BSUID consists of an
 * <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166 alpha-2</a> country code prefix, followed
 * by a period and up to 128 alphanumeric characters, e.g. {@code US.13491208655302741918}.
 * <p>
 * A <em>parent BSUID</em> is a special variant available to managed businesses with multiple linked Meta business
 * portfolios, allowing a single identifier to be used across all enrolled portfolios. It has the form
 * {@code {COUNTRY_CODE}.ENT.{ALPHANUMERIC_ID}}, e.g. {@code US.ENT.11815799212886844830}.
 *
 * @since 9.13.0
 */
public final class Bsuid {
	static final Pattern PATTERN = Pattern.compile("[A-Za-z]{2}\\.(ENT\\.)?[A-Za-z0-9]{1,128}");

	private final String id;

	/**
	 * Validates the provided business-scoped user ID.
	 *
	 * @param id The BSUID (or parent BSUID) to validate.
	 *
	 * @throws IllegalArgumentException If the identifier is not a well-formed BSUID.
	 */
	public Bsuid(String id) {
		Objects.requireNonNull(id, "BSUID cannot be null");
		if (PATTERN.matcher(id).matches()) {
			this.id = id;
		}
		else {
			throw new IllegalArgumentException("Malformed BSUID: " + id);
		}
	}

	/**
	 * Convenience method for checking whether a string is a well-formed BSUID without throwing.
	 *
	 * @param id The candidate identifier to check (may be {@code null}).
	 *
	 * @return {@code true} if the argument is a valid BSUID or parent BSUID, {@code false} otherwise.
	 */
	public static boolean isValid(String id) {
		return id != null && PATTERN.matcher(id).matches();
	}

	@JsonValue
	@Override
	public String toString() {
		return id;
	}
}
