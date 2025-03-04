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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;

/**
 * Provides info on list changes compared to the latest sync.
 */
public class SyncStatus extends JsonableBaseObject {
	private SyncStatusValue value;
	private String details;
	private Boolean metadataModified, dataModified, dirty;

	protected SyncStatus() {
	}

	/**
	 * Synchronization state of the list.
	 *
	 * @return The sync status enum.
	 */
	@JsonProperty("value")
	public SyncStatusValue getValue() {
		return value;
	}

	/**
	 * Details on the sync status.
	 *
	 * @return Sync status details as a string.
	 */
	@JsonProperty("details")
	public String getDetails() {
		return details;
	}

	/**
	 * Whether the list definition has been modified since last sync.
	 *
	 * @return {@code true} if the list metadata was modified since last sync.
	 */
	@JsonProperty("metadata_modified")
	public Boolean getMetadataModified() {
		return metadataModified;
	}

	/**
	 * Whether one or more list items were added, removed and/or modified since last sync.
	 *
	 * @return {@code true} if the list data was modified since last sync.
	 */
	@JsonProperty("data_modified")
	public Boolean getDataModified() {
		return dataModified;
	}

	/**
	 * Whether the list content or metadata were modified since last sync.
	 *
	 * @return {@code true} if the list was modified since last sync.
	 */
	@JsonProperty("dirty")
	public Boolean getDirty() {
		return dirty;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static SyncStatus fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
