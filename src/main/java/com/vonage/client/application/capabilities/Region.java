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
package com.vonage.client.application.capabilities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents a Vonage region.
 *
 * @since 7.7.0
 */
public enum Region {
	/**
	 * api-us-3.vonage.com (Virginia)
	 */
	NA_EAST,

	/**
	 * api-us-4.vonage.com (Oregon)
	 */
	NA_WEST,

	/**
	 * api-eu-3.vonage.com (Dublin)
	 */
	EU_WEST,

	/**
	 * api-eu-4.vonage.com (Frankfurt)
	 */
	EU_EAST,

	/**
	 * api-ap-3.vonage.com (Singapore)
	 */
	APAC_SNG,

	/**
	 * api-ap-4.vonage.com (Sydney)
	 */
	APAC_AUSTRALIA;

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase().replace("_", "-");
	}

	@JsonCreator
	public static Region fromString(String value) {
		try {
			return Region.valueOf(value.toUpperCase().replace("-", "_"));
		}
		catch (NullPointerException | IllegalArgumentException ex) {
			return null;
		}
	}
}
