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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.common.HalPageResponse;
import java.util.List;

/**
 * HAL response for {@link MeetingsClient#listSessions(ListSessionsRequest)}.
 *
 * @since 7.11.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ListSessionsResponse extends HalPageResponse {
	@JsonProperty("_embedded") private Embedded _embedded;

	ListSessionsResponse() {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static final class Embedded {
		@JsonProperty("sessions") private List<Session> sessions;
	}

	/**
	 * Gets the sessions contained in the {@code _embedded} response.
	 *
	 * @return The sessions for this page.
	 */
	@JsonIgnore
	public List<Session> getSessions() {
		return _embedded != null ? _embedded.sessions : null;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static ListSessionsResponse fromJson(String json) {
		return Jsonable.fromJson(json, ListSessionsResponse.class);
	}
}
