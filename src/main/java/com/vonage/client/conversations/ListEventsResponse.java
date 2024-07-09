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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.HalPageResponse;
import java.util.List;

/**
 * HAL response for {@link ConversationsClient#listEvents(String, ListEventsRequest)}.
 */
public final class ListEventsResponse extends HalPageResponse {
	@JsonProperty("_embedded") private Embedded _embedded;

	ListEventsResponse() {
	}

	static final class Embedded extends JsonableBaseObject {
		@JsonProperty("events") private List<Event> events;
	}

	/**
	 * Gets the events contained in the {@code _embedded} response.
	 *
	 * @return The events for this page.
	 */
	@JsonIgnore
	public List<Event> getEvents() {
		return _embedded != null ? _embedded.events : null;
	}
}
