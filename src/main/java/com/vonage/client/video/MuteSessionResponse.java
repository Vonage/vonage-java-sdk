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
import com.vonage.client.Jsonable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MuteSessionResponse implements Jsonable {
	private String applicationId, name;
	private ProjectStatus status;
	private ProjectEnvironment environment;
	private Long createdAt;

	protected MuteSessionResponse() {
	}

	/**
	 * 
	 * @return The Vonage application ID.
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * 
	 * @return Whether the project is active or suspended.
	 */
	public ProjectStatus getStatus() {
		return status;
	}

	/**
	 * 
	 * @return The project name, if specified when created.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return The environment the project is running on.
	 */
	public ProjectEnvironment getEnvironment() {
		return environment;
	}

	/**
	 * 
	 * @return The time at which the project was created (a UNIX timestamp, in milliseconds).
	 */
	public Long getCreatedAt() {
		return createdAt;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static MuteSessionResponse fromJson(String json) {
		return Jsonable.fromJson(json, MuteSessionResponse.class);
	}
}
