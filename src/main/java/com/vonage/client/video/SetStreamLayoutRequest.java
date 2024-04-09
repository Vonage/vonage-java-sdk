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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import java.util.List;

class SetStreamLayoutRequest implements Jsonable {
	private final List<SessionStream> items;
	@JsonIgnore final String sessionId;

	SetStreamLayoutRequest(String sessionId, List<SessionStream> items) {
		this.sessionId = sessionId;
		if ((this.items = items) == null || items.isEmpty()) {
			throw new IllegalArgumentException("At least one stream is required.");
		}
	}

	@JsonProperty("items")
	public List<SessionStream> getItems() {
		return items;
	}
}
