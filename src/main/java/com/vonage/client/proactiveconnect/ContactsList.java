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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Proactive Connect list.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactsList {
	private UUID id;
	private String name, description;
	private List<String> tags;
	private List<ListAttribute> attributes;
	private Datasource datasource;
	private Instant createdAt, updatedAt;
	private Integer itemsCount;
	private SyncStatus syncStatus;

	ContactsList() {
	}

	ContactsList(Builder builder) {
		name = builder.name;
		description = builder.description;
		tags = builder.tags;
		attributes = builder.attributes;
		datasource = builder.datasource;
	}

	/**
	 * The name of the resource (max 255 characters).
	 *
	 * @return Resource name or {@code null} if unknown.
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * The description of the resource (max 1024 characters).
	 *
	 * @return Resource description or {@code null} if unknown.
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * Up to 10 custom strings assigned with a resource - each must be between 1 and 15 characters.
	 *
	 * @return The list of tags or {@code null} if unknown.
	 */
	@JsonProperty("tags")
	public List<String> getTags() {
		return tags;
	}

	/**
	 * The list attributes.
	 *
	 * @return The list's attributes or {@code null} if unknown.
	 */
	@JsonProperty("attributes")
	public List<ListAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * The list's datasource.
	 *
	 * @return The datasource or {@code null} if unknown.
	 */
	@JsonProperty("datasource")
	public Datasource getDatasource() {
		return datasource;
	}

	/**
	 * The creation timestamp in ISO 8601 format.
	 *
	 * @return The creation time or {@code null} if unknown.
	 */
	@JsonProperty("created_at")
	public Instant getCreatedAt() {
		return createdAt;
	}

	/**
	 * The last update timestamp in ISO 8601 format.
	 *
	 * @return The last updated time or {@code null} if unknown.
	 */
	@JsonProperty("updated_at")
	public Instant getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * The total number of list items.
	 *
	 * @return The number of items in the list or {@code null} if unknown.
	 */
	@JsonProperty("items_count")
	public Integer getItemsCount() {
		return itemsCount;
	}

	/**
	 * Unique identifier for this list.
	 *
	 * @return The list ID or {@code null} if unknown.
	 */
	@JsonProperty("id")
	public UUID getId() {
		return id;
	}

	/**
	 * Synchronization status between the list content (items) and its datasource.
	 *
	 * @return The synchronisation status, or {@code null} if unknown.
	 */
	@JsonProperty("sync_status")
	public SyncStatus getSyncStatus() {
		return syncStatus;
	}

	/**
	 * Updates (hydrates) this object's fields from additional JSON data.
	 *
	 * @param json The JSON payload.
	 */
	public void updateFromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.readerForUpdating(this).readValue(json, getClass());
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to update "+getClass().getSimpleName()+" from json.", ex);
		}
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static ContactsList fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.readValue(json, ContactsList.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce ContactsList from json.", ex);
		}
	}

	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this ContactsList object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}
	
	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String name, description;
		private List<String> tags;
		private List<ListAttribute> attributes;
		private Datasource datasource;
	
		Builder() {}
	
		/**
		 * Sets the resource name.
		 *
		 * @param name The name of the resource (max 255 characters).
		 *
		 * @return This builder.
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Sets the resource description.
		 *
		 * @param description The description of the resource (max 1024 characters).
		 *
		 * @return This builder.
		 */
		public Builder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * Sets the tags.
		 *
		 * @param tags Up to 10 custom strings assigned with a resource - each must be between 1 and 15 characters.
		 *
		 * @return This builder.
		 */
		public Builder tags(List<String> tags) {
			this.tags = tags;
			return this;
		}

		/**
		 * Sets the list attributes.
		 *
		 * @param attributes The list attributes as an array (or varargs) of {@code ListAttribute}s.
		 *
		 * @return This builder.
		 */
		public Builder attributes(ListAttribute... attributes) {
			return attributes(Arrays.asList(attributes));
		}

		/**
		 * Sets the list attributes.
		 *
		 * @param attributes The list of attributes.
		 *
		 * @return This builder.
		 */
		public Builder attributes(List<ListAttribute> attributes) {
			this.attributes = attributes;
			return this;
		}

		/**
		 * Sets the datasource.
		 *
		 * @param datasource The datasource type.
		 *
		 * @return This builder.
		 */
		public Builder datasource(Datasource datasource) {
			this.datasource = datasource;
			return this;
		}

	
		/**
		 * Builds the {@linkplain ContactsList}.
		 *
		 * @return An instance of ContactsList, populated with all fields from this builder.
		 */
		public ContactsList build() {
			return new ContactsList(this);
		}
	}
}
