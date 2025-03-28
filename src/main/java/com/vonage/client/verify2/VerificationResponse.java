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
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.UUID;

/**
 * Metadata about an outbound verification request.
 */
public class VerificationResponse extends JsonableBaseObject {
	protected UUID requestId;
	protected URI checkUrl;

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

	/**
	 * URL for Silent Auth Verify workflow completion (only present if using Silent Auth).
	 *
	 * @return The URL to check for Silent Authentication, or {@code null} if not applicable.
	 *
	 * @since 7.10.0
	 */
	@JsonProperty("check_url")
	public URI getCheckUrl() {
		return checkUrl;
	}
}
