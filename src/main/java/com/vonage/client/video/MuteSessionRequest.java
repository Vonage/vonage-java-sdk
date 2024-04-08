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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Collection;

class MuteSessionRequest extends JsonableBaseObject {
	final boolean active;
	final Collection<String> excludedStreamIds;
	@JsonIgnore final String sessionId;

	MuteSessionRequest(String sessionId, boolean active, Collection<String> excludedStreamIds) {
		this.sessionId = sessionId;
		this.active = active;
		this.excludedStreamIds = excludedStreamIds;
	}

	@JsonProperty("active")
	public boolean isActive() {
		return active;
	}

	@JsonProperty("excludedStreamIds")
	public Collection<String> getExcludedStreamIds() {
		return excludedStreamIds;
	}
}
