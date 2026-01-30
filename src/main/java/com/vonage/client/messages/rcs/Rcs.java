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
package com.vonage.client.messages.rcs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * RCS-specific parameters.
 *
 * @since 9.6.0
 */
public final class Rcs extends JsonableBaseObject {
	private String category;
	Boolean trustedRecipient;

	Rcs() {}

	/**
	 * Creates an RCS options object with the specified category.
	 *
	 * @param category The RCS message category.
	 */
	public Rcs(String category) {
		this.category = category;
	}

	/**
	 * Creates an RCS options object with the specified category and trusted recipient flag.
	 *
	 * @param category The RCS message category.
	 * @param trustedRecipient Whether the recipient is trusted.
	 *
	 * @since 9.8.0
	 */
	public Rcs(String category, Boolean trustedRecipient) {
		this.category = category;
		this.trustedRecipient = trustedRecipient;
	}

	/**
	 * The RCS message category.
	 *
	 * @return The category as a string, or {@code null} if not set.
	 */
	@JsonProperty("category")
	public String getCategory() {
		return category;
	}

	/**
	 * Whether the recipient is trusted.
	 *
	 * @return The trusted recipient flag as a Boolean, or {@code null} if not set.
	 *
	 * @since 9.8.0
	 */
	@JsonProperty("trusted_recipient")
	public Boolean getTrustedRecipient() {
		return trustedRecipient;
	}
}
