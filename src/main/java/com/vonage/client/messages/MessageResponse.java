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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.util.UUID;

/**
 * Response returned when sending a message. Regardless of the channel or message type,
 * the returned response (HTTP 202 payload) is always the same format.
 */
public class MessageResponse extends JsonableBaseObject {
	protected UUID messageUuid;

	/**
	 * Protected to prevent users from explicitly creating this object.
	 */
	protected MessageResponse() {
	}

	/**
	 * Returns the UUID of the message that was sent.
	 *
	 * @return The unique message ID.
	 */
	@JsonProperty("message_uuid")
	public UUID getMessageUuid() {
		return messageUuid;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static MessageResponse fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
