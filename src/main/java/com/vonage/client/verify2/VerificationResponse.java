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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageResponseParseException;
import java.io.IOException;
import java.util.UUID;

/**
 * Metadata about an outbound verification request.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationResponse {
	protected UUID requestId;

	/**
	 * Protected to prevent users from explicitly creating this object.
	 */
	protected VerificationResponse() {
	}

	/**
	 * Returns the UUID of the request that was sent.
	 *
	 * @return The unique request ID.
	 */
	@JsonProperty("request_id")
	public UUID getRequestId() {
		return requestId;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+" {requestId='"+requestId+"'}";
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static VerificationResponse fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, VerificationResponse.class);
		}
		catch (IOException ex) {
			throw new VonageResponseParseException("Failed to produce VerificationResponse from json.", ex);
		}
	}
}
