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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vonage.client.VonageApiResponseException;

/**
 * Response returned when an error is encountered (i.e. the API returns a non-2xx status code).
 *
 * @since 7.11.0
 */
public final class VoiceResponseException extends VonageApiResponseException {

	void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with all known fields populated from the JSON payload, if present.
	 */
	@JsonCreator
	public static VoiceResponseException fromJson(String json) {
		return fromJson(VoiceResponseException.class, json);
	}
}
