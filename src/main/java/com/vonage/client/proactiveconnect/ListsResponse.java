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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.common.HalPageResponse;
import java.io.IOException;
import java.util.List;

/**
 * HAL response for {@link ProactiveConnectClient#listLists(int, int)}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ListsResponse extends HalPageResponse {
	@JsonProperty("_embedded") private Embedded _embedded;

	ListsResponse() {}

	@JsonIgnoreProperties(ignoreUnknown = true)
	static final class Embedded {
		private List<ContactsList> lists;

		@JsonProperty("lists")
		public List<ContactsList> getLists() {
			return lists;
		}
	}

	/**
	 * Gets the lists contained in the {@code _embedded} response.
	 *
	 * @return The lists for this page.
	 */
	@JsonIgnore
	public List<ContactsList> getLists() {
		return _embedded != null ? _embedded.getLists() : null;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static ListsResponse fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, ListsResponse.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce ListsResponse from json.", ex);
		}
	}
}
