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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.VonageApiResponseException;

/**
 * Response returned when an error is encountered (i.e. the API returns a non-2xx status code).
 */
public final class MeetingsResponseException extends VonageApiResponseException {
	@JsonProperty("status") private Integer status;
	@JsonProperty("name") private String name;
	@JsonProperty("message") private String message;

	public MeetingsResponseException() {
		super();
	}

	MeetingsResponseException(String message) {
		super(message);
	}

	void setStatusCode(Integer status) {
		this.status = statusCode = status;
	}

	public String getName() {
		return name;
	}

	@JsonGetter("message")
	@Override
	public String getMessage() {
		return message != null ? message : super.getMessage();
	}

	private static MeetingsResponseException setStatus(MeetingsResponseException ex) {
		if (ex.status == null || ex.status < 100) {
			ex.setStatusCode(ex.statusCode);
		}
		else if (ex.statusCode < 100) {
			ex.setStatusCode(ex.status);
		}
		return ex;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with all known fields populated from the JSON payload, if present.
	 */
	@JsonCreator
	public static MeetingsResponseException fromJson(String json) {
		return setStatus(fromJson(MeetingsResponseException.class, json));
	}
}
