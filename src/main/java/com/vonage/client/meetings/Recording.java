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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Recording extends JsonableBaseObject {
	private UUID id;
	private String sessionId;
	private Instant startedAt, endedAt;
	private RecordingStatus status;
	@JsonProperty("_links") private RecordingLinks links;

	protected Recording() {
	}

	/**
	 * Identifier of the recording.
	 *
	 * @return The recording ID.
	 */
	@JsonProperty("id")
	public UUID getId() {
		return id;
	}

	/**
	 * Corresponds to the underlying Video API session ID.
	 *
	 * @return The video session ID.
	 */
	@JsonProperty("session_id")
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Recording start time, in ISO 8601 format.
	 *
	 * @return The recording start time.
	 */
	@JsonProperty("started_at")
	public Instant getStartedAt() {
		return startedAt;
	}

	/**
	 * Recording end time, in ISO 8601 format.
	 *
	 * @return The recording end time.
	 */
	@JsonProperty("ended_at")
	public Instant getEndedAt() {
		return endedAt;
	}

	/**
	 * Status of the recording.
	 *
	 * @return The recording status, as an enum.
	 */
	@JsonProperty("status")
	public RecordingStatus getStatus() {
		return status;
	}

	/**
	 * The URL property of the {@code _links} response.
	 *
	 * @return The recording URL or {@code null} if not available.
	 */
	@JsonIgnore
	public URI getUrl() {
		return links != null ? links.getUrl() : null;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static Recording fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
