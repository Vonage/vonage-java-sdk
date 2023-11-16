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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.Jsonable;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSessionResponse implements Jsonable {
	private String sessionId, createDt;
	private UUID applicationId;
	private URI mediaServerUrl;

	protected CreateSessionResponse() {
	}

	/**
	 * @return The session ID.
	 */
	@JsonProperty("session_id")
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @return The application ID.
	 */
	@JsonProperty("application_id")
	public UUID getApplicationId() {
		return applicationId;
	}

	/**
	 * @return The creation date.
	 */
	@JsonProperty("create_dt")
	public String getCreateDt() {
		return createDt;
	}

	/**
	 * @return The URL of the Media Router used by the session.
	 */
	@JsonProperty("media_server_url")
	public URI getMediaServerUrl() {
		return mediaServerUrl;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static CreateSessionResponse fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			CreateSessionResponse[] array = mapper.readValue(json, CreateSessionResponse[].class);
			if (array == null || array.length == 0) {
				return new CreateSessionResponse();
			}
			return array[0];
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce CreateSessionResponse from json.", ex);
		}
	}
}
