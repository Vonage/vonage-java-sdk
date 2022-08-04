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
package com.vonage.client.insight;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enum representing whether the user has changed carrier for the number.
 * The assumed status means that the information supplier has replied to the request but has not
 * explicitly reported that the number is ported. Note that this enum may be <code>null</code>.
 */
public enum PortedStatus {
	UNKNOWN, PORTED, NOT_PORTED, ASSUMED_NOT_PORTED, ASSUMED_PORTED;

	@JsonCreator
	public static PortedStatus fromString(String name) {
		if (name.equalsIgnoreCase("null")) {
			return null;
		}
		try {
			return PortedStatus.valueOf(name.toUpperCase());
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
