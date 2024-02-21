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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.UUID;

/**
 * Parameters for {@link Callback#getParams()}.
 */
public class CallbackParams extends JsonableBaseObject {
	private UUID applicationId;
	private URI nccoUrl;

	protected CallbackParams(UUID applicationId, URI nccoUrl) {
		this.applicationId = applicationId;
		this.nccoUrl = nccoUrl;
	}

	/**
	 * Vonage Application ID.
	 * 
	 * @return The application ID, or {@code null} if unspecified.
	 */
	@JsonProperty("applicationId")
	public UUID getApplicationId() {
		return applicationId;
	}

	/**
	 * Call Control Object URL to use for the callback.
	 * 
	 * @return The NCCO URL, or {@code null} if unspecified.
	 */
	@JsonProperty("ncco_url")
	public URI getNccoUrl() {
		return nccoUrl;
	}
}
