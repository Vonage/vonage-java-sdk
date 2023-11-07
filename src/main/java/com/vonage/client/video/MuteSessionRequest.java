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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.util.Collection;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
class MuteSessionRequest {
	final boolean active;
	final Collection<String> excludedStreamIds;
	@JsonIgnore final String sessionId;

	MuteSessionRequest(String sessionId, boolean active, Collection<String> excludedStreamIds) {
		this.sessionId = sessionId;
		this.active = active;
		this.excludedStreamIds = excludedStreamIds;
	}

	public boolean isActive() {
		return active;
	}

	public Collection<String> getExcludedStreamIds() {
		return excludedStreamIds;
	}

	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this MuteSessionRequest object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}
}
