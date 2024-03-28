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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.common.HalPageResponse;
import java.util.List;

/**
 * HAL response for {@link ConversationsClient#listEvents(String, ListEventsRequest)}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ListEventsResponse extends HalPageResponse {
	@JsonProperty("_embedded") private List<Event> events;

	ListEventsResponse() {
	}

	/**
	 * Gets the events contained in the {@code _embedded} response.
	 *
	 * @return The events for this page.
	 */
	@JsonProperty("_embedded")
	public List<Event> getEvents() {
		return events;
	}
}
