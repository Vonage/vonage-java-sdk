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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a list item in the Proactive Connect API.
 */
public class ListItem extends JsonableBaseObject {
	private Map<String, ?> data;
	private Instant createdAt, updatedAt;
	private UUID id, listId;

	protected ListItem() {
	}

	/**
	 * Custom data as key-value pairs for this list.
	 *
	 * @return The list data as a Map, or {@code null} if unset.
	 */
	@JsonProperty("data")
	public Map<String, ?> getData() {
		return data;
	}

	/**
	 * Time this item was created, in ISO 8601 format.
	 *
	 * @return The creation timestamp, or {@code null} if unknown.
	 */
	@JsonProperty("created_at")
	public Instant getCreatedAt() {
		return createdAt;
	}

	/**
	 * Time this item was last updated, in ISO 8601 format.
	 *
	 * @return The last update timestamp, or {@code null} if unknown.
	 */
	@JsonProperty("updated_at")
	public Instant getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * Unique identifier for this item.
	 *
	 * @return The item ID or {@code null} if unknown.
	 */
	@JsonProperty("id")
	public UUID getId() {
		return id;
	}

	/**
	 * Unique identifier for this list.
	 *
	 * @return The list ID or {@code null} if unknown.
	 */
	@JsonProperty("list_id")
	public UUID getListId() {
		return listId;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static ListItem fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
