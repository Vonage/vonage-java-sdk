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
import com.vonage.client.Jsonable;
import java.util.UUID;

/**
 * Response from following the redirects when attempting to synchronously process a Silent Authentication workflow.
 *
 * @since 7.10.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class SilentAuthResponse implements Jsonable {
	protected UUID requestId;
	protected String code;

	/**
	 * Protected to prevent users from explicitly creating this object.
	 */
	protected SilentAuthResponse() {
	}

	/**
	 * UUID of the request with the Silent Auth workflow.
	 *
	 * @return The unique request ID.
	 */
	@JsonProperty("request_id")
	public UUID getRequestId() {
		return requestId;
	}

	/**
	 * Code to use for completing the workflow by calling {@link Verify2Client#checkVerificationCode(UUID, String)}.
	 *
	 * @return The verification code.
	 */
	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static SilentAuthResponse fromJson(String json) {
		SilentAuthResponse response = new SilentAuthResponse();
		response.updateFromJson(json);
		return response;
	}
}
