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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetRoomsResponse {

	static class Embedded {
		@JsonProperty("rooms") List<MeetingRoom> rooms;
	}

	@JsonProperty("_embedded") private Embedded embedded;
	private Integer pageSize, total;
	private NavigationLinks links;

	protected GetRoomsResponse() {
	}

	/**
	 * @return The number of results returned on this page.
	 */
	@JsonProperty("page_size")
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @return The overall number of available rooms.
	 */
	@JsonProperty("total_items")
	public Integer getTotal() {
		return total;
	}

	/**
	 * @return The list of rooms.
	 */
	@JsonProperty("_embedded")
	public List<MeetingRoom> getMeetingRooms() {
		return embedded.rooms;
	}

	/**
	 * Links to other pages.
	 *
	 * @return The navigation links object.
	 */
	@JsonProperty("_links")
	public NavigationLinks getLinks() {
		return links;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static GetRoomsResponse fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, GetRoomsResponse.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce GetRoomsResponse from json.", ex);
		}
	}
}
