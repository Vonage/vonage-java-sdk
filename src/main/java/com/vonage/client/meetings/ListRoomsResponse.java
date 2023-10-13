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
import com.vonage.client.Jsonable;
import com.vonage.client.common.HalPageResponse;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class ListRoomsResponse extends HalPageResponse {
	@JsonProperty("_embedded") private List<MeetingRoom> rooms;

	protected ListRoomsResponse() {
	}

	/**
	 * The embedded response containing the list of meeting rooms.
	 *
	 * @return The list of rooms.
	 */
	public List<MeetingRoom> getMeetingRooms() {
		return rooms;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static ListRoomsResponse fromJson(String json) {
		return Jsonable.fromJson(json, ListRoomsResponse.class);
	}
}
