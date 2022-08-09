/*
 *   Copyright 2022 Vonage
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

import java.io.IOException;

public class CreateSessionResponse {
	private String sessionId, applicationId, createDt, mediaServerUrl;

	protected CreateSessionResponse() {
	}

	@JsonProperty("session_id")
	public String getSessionId() {
		return sessionId;
	}

	@JsonProperty("application_id")
	public String getApplicationId() {
		return applicationId;
	}

	@JsonProperty("create_dt")
	public String getCreateDt() {
		return createDt;
	}

	@JsonProperty("media_server_url")
	public String getMediaServerUrl() {
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
			return mapper.readValue(json, CreateSessionResponse.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce MessageResponse from json.", ex);
		}
	}
}
