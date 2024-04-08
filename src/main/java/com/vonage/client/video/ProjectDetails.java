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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;

/**
 * Represents properties of a video project.
 */
public class ProjectDetails extends JsonableBaseObject {
	private String applicationId, name;
	private ProjectStatus status;
	private ProjectEnvironment environment;
	private Long createdAt;

	protected ProjectDetails() {
	}

	/**
	 * @return The Vonage application ID.
	 */
	@JsonProperty("applicationId")
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @return Whether the project is active or suspended.
	 */
	@JsonProperty("status")
	public ProjectStatus getStatus() {
		return status;
	}

	/**
	 * @return The project name, if specified when created.
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * @return The environment the project is running on.
	 */
	@JsonProperty("environment")
	public ProjectEnvironment getEnvironment() {
		return environment;
	}

	/**
	 * @return The time at which the project was created (a UNIX timestamp, in milliseconds).
	 */
	@JsonProperty("createdAt")
	public Long getCreatedAt() {
		return createdAt;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static ProjectDetails fromJson(String json) {
		if (json == null || json.trim().isEmpty()) {
			return new ProjectDetails();
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, ProjectDetails.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce MuteResponse from json.", ex);
		}
	}
}
