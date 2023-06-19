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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.VonageApiResponseException;
import org.apache.http.HttpResponse;
import java.io.IOException;
import java.util.List;

/**
 * Response returned when an error is encountered (i.e. the API returns a non-2xx status code).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProactiveConnectResponseException extends VonageApiResponseException {
	List<?> errors;

	void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Additional description of problems encountered with the request.
	 * This is typically only applicable to 400 or 409 error codes.
	 *
	 * @return The list of errors returned from the server (could be a Map or String), or {@code null} if none.
	 */
	@JsonProperty("errors")
	public List<?> getErrors() {
		return errors;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with all known fields populated from the JSON payload, if present.
	 */
	@JsonCreator
	public static ProactiveConnectResponseException fromJson(String json) {
		return fromJson(ProactiveConnectResponseException.class, json);
	}

	static ProactiveConnectResponseException fromHttpResponse(HttpResponse response) throws IOException {
		return fromHttpResponse(ProactiveConnectResponseException.class, response);
	}
}
