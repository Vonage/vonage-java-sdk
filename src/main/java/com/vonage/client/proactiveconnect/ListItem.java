/*
 *   Copyright 2023 Vonage
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

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListItem {
	@JsonProperty("data") private DataWrapper data;
	private Instant createdAt, updatedAt;
	private UUID id, listId;

	protected ListItem() {
	}

	/**
	 * Custom data as key-value pairs for this list.
	 *
	 * @return The list data as a Map, or {@code null} if unset.
	 */
	@JsonIgnore
	public Map<String, ?> getData() {
		return data != null ? data.getData() : null;
	}

	@JsonGetter("data")
	private DataWrapper getDataRaw() {
		return data;
	}

	@JsonSetter("data")
	private void setDataRaw(DataWrapper data) {
		this.data = data;
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
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, ListItem.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce ListItem from json.", ex);
		}
	}
}
