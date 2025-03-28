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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.VonageResponseParseException;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public class CreateSessionResponse extends JsonableBaseObject {
	private String sessionId, createDt;
	private UUID applicationId;
	private URI mediaServerUrl;

	protected CreateSessionResponse() {
	}

	/**
	 * Gets the session ID.
	 *
	 * @return The session ID.
	 */
	@JsonProperty("session_id")
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Gets the application ID.
	 *
	 * @return The application ID.
	 */
	@JsonProperty("application_id")
	public UUID getApplicationId() {
		return applicationId;
	}

	/**
	 * Gets the creation date.
	 *
	 * @return The creation date as a string.
	 */
	@JsonProperty("create_dt")
	public String getCreateDt() {
		return createDt;
	}

	/**
	 * Gets the URL of the Media Router used by the session.
	 *
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
			CreateSessionResponse[] array = Jsonable.createDefaultObjectMapper()
					.readValue(json, CreateSessionResponse[].class);
			if (array.length == 0) {
				return new CreateSessionResponse();
			}
			return array[0];
		}
		catch (IOException ex) {
			throw new VonageResponseParseException("Failed to produce CreateSessionResponse from json.", ex);
		}
	}
}
