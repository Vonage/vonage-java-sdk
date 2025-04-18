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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;

/**
 * Represents the main attributes of a conversation.
 */
public class BaseConversation extends JsonableBaseObject {
	String id, name, displayName;
	URI imageUrl;
	ConversationTimestamp timestamp;

	protected BaseConversation() {
	}

	/**
	 * Unique identifier for this conversation.
	 * 
	 * @return The conversation ID as a string.
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * Internal conversation name. Must be unique.
	 * 
	 * @return The conversation name, or {@code null} if unespecified.
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * The public facing name of the conversation.
	 * 
	 * @return The display name, or {@code null} if unespecified.
	 */
	@JsonProperty("display_name")
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * An image URL that you associate with the conversation.
	 * 
	 * @return The image URL, or {@code null} if unspecified.
	 */
	@JsonProperty("image_url")
	public URI getImageUrl() {
		return imageUrl;
	}

	/**
	 * Timestamps for this conversation.
	 * 
	 * @return The timestamps object, or {@code null} if unknown.
	 */
	@JsonProperty("timestamp")
	public ConversationTimestamp getTimestamp() {
		return timestamp;
	}
}
