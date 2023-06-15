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
import com.vonage.client.VonageResponseParseException;
import java.io.IOException;

/**
 * Results from list upload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadListItemsResponse {
	private Integer inserted, updated, deleted;

	protected UploadListItemsResponse() {
	}

	/**
	 * Items inserted in the list.
	 *
	 * @return The number of inserted items, or {@code null} if not applicable.
	 */
	@JsonProperty("inserted")
	public Integer getInserted() {
		return inserted;
	}

	/**
	 * Items updated in the list.
	 *
	 * @return The number of updated items, or {@code null} if not applicable.
	 */
	@JsonProperty("updated")
	public Integer getUpdated() {
		return updated;
	}

	/**
	 * Items deleted in the list.
	 *
	 * @return The number of deleted items, or {@code null} if not applicable.
	 */
	@JsonProperty("deleted")
	public Integer getDeleted() {
		return deleted;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static UploadListItemsResponse fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, UploadListItemsResponse.class);
		}
		catch (IOException ex) {
			throw new VonageResponseParseException("Failed to produce UploadListItemsResponse from json.", ex);
		}
	}
}
