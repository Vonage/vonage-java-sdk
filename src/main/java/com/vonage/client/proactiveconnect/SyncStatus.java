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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncStatus {
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
	 * @return Details on the sync status.
	 */
	@JsonProperty("details")
	public String getDetails() {
		return details;
	}

	/**
	 * @return Whether the list definition has been modified since last sync.
	 */
	@JsonProperty("metadata_modified")
	public Boolean getMetadataModified() {
		return metadataModified;
	}

	/**
	 * @return Whether one or more list items were added, removed and/or modified since last sync.
	 */
	@JsonProperty("data_modified")
	public Boolean getDataModified() {
		return dataModified;
	}

	/**
	 * @return Whether the list content or metadata were modified since last sync.
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
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, SyncStatus.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce SyncStatus from json.", ex);
		}
	}
}
