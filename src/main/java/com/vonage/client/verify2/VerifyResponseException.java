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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.VonageApiResponseException;
import java.util.UUID;

/**
 * Response returned when verification fails (i.e. returns a non-2xx status code).
 */
public final class VerifyResponseException extends VonageApiResponseException {
	UUID requestId;

	VerifyResponseException() {}

	@Override
	protected void setStatusCode(int statusCode) {
		super.setStatusCode(statusCode);
	}

	/**
	 * The request ID, if the status code is 409 for a failed outbound request.
	 *
	 * @return The request ID, or {@code null} if not applicable / available.
	 */
	@JsonProperty("request_id")
	public UUID getRequestId() {
		return requestId;
	}
}
