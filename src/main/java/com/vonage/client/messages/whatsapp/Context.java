/*
 *   Copyright 2024 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.UUID;

/**
 * This is only present for the Inbound Message where the user is quoting another message.
 * It provides information about the quoted message and/or the product message being responded to.
 *
 * @since 7.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Context extends JsonableBaseObject {
	private String messageFrom;
	private UUID messageUuid;
	private ReferredProduct referredProduct;

	Context() {}

	/**
	 * The phone number of the original sender of the message being quoted.
	 *
	 * @return The original sender's phone number in E.164 format.
	 */
	@JsonProperty("message_from")
	public String getMessageFrom() {
		return messageFrom;
	}

	/**
	 * The UUID of the message being quoted.
	 *
	 * @return The message ID.
	 */
	@JsonProperty("message_uuid")
	public UUID getMessageUuid() {
		return messageUuid;
	}

	/**
	 * Only applies to Order messages.
	 *
	 * @return The referred product details, or {@code null} if not applicable.
	 *
	 * @deprecated This will be moved in a future release.
	 */
	@Deprecated
	@JsonProperty("whatsapp_referred_product")
	public ReferredProduct getReferredProduct() {
		return referredProduct;
	}
}
