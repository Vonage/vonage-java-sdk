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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.time.Instant;

/**
 * Represents the timestamps in {@link BaseConversation#getTimestamp()}.
 */
public class ConversationTimestamp extends JsonableBaseObject {
	private Instant created, updated, destroyed;

	protected ConversationTimestamp() {}

	/**
	 * Time that the conversation was created.
	 * 
	 * @return The conversation creation time as an Instant, or {@code null} if unknown.
	 */
	@JsonProperty("created")
	public Instant getCreated() {
		return created;
	}

	/**
	 * Time that the conversation was update.
	 * 
	 * @return The conversation update time as an Instant, or {@code null} if unknown.
	 */
	@JsonProperty("updated")
	public Instant getUpdated() {
		return updated;
	}

	/**
	 * Time that the conversation was destroyed.
	 * 
	 * @return The conversation deletion time as an Instant, or {@code null} if unknown.
	 */
	@JsonProperty("destroyed")
	public Instant getDestroyed() {
		return destroyed;
	}
}
