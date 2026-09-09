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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents a WhatsApp user's identifiers as returned in inbound message webhooks
 * (the {@code whatsapp.sender} object) and status callbacks (the {@code whatsapp.recipient} object).
 * <p>
 * Depending on whether the user has adopted a WhatsApp username, and on Meta's phone-number visibility
 * rules, some of these fields may be absent. The business-scoped user ID ({@link #getUserId()}) is always
 * present for WhatsApp consumers.
 *
 * @since 9.13.0
 */
public final class WhatsappUser extends JsonableBaseObject {
	private String userId, parentUserId, waId;

	WhatsappUser() {}

	/**
	 * The user's business-scoped user ID (BSUID).
	 *
	 * @return The BSUID, or {@code null} if not present.
	 */
	@JsonProperty("user_id")
	public String getUserId() {
		return userId;
	}

	/**
	 * The user's parent BSUID. Only present if your business has enabled parent BSUIDs.
	 *
	 * @return The parent BSUID, or {@code null} if not present.
	 */
	@JsonProperty("parent_user_id")
	public String getParentUserId() {
		return parentUserId;
	}

	/**
	 * The user's phone number (WhatsApp ID). Omitted if the message was to / from a BSUID and the
	 * phone number cannot be included per Meta's visibility rules.
	 *
	 * @return The phone number, or {@code null} if not present.
	 */
	@JsonProperty("wa_id")
	public String getWaId() {
		return waId;
	}
}
