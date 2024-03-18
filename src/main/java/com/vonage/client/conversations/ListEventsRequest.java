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

import java.util.Map;

/**
 * Filters results for {@link ConversationsClient#listEvents(ListEventsRequest)}.
 */
public class ListEventsRequest extends AbstractConversationsFilterRequest {
	private final Boolean excludeDeletedEvents;
	private final String startId, endId;
	private final EventType eventType;

	ListEventsRequest(Builder builder) {
		super(builder);
		excludeDeletedEvents = builder.excludeDeletedEvents;
		startId = builder.startId;
		endId = builder.endId;
		eventType = builder.eventType;
	}
	
	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = super.makeParams();
		if (excludeDeletedEvents != null) {
            params.put("exclude_deleted_events", excludeDeletedEvents.toString());
        }
		if (startId != null) {
            params.put("start_id", startId);
        }
		if (endId != null) {
            params.put("end_id", endId);
        }
		if (eventType != null) {
            params.put("event_type", eventType.toString());
        }
		return params;
	}

	/**
	 * Whether to exclude deleted events from the results.
	 * 
	 * @return {@code true} to exclude deleted events, or {@code null} if unspecified.
	 */
	public Boolean getExcludeDeletedEvents() {
		return excludeDeletedEvents;
	}

	/**
	 * The ID to start returning events at.
	 * 
	 * @return The start ID, or {@code null} if unspecified.
	 */
	public String getStartId() {
		return startId;
	}

	/**
	 * The ID to stop returning events at.
	 * 
	 * @return The end ID, or {@code null} if unspecified.
	 */
	public String getEndId() {
		return endId;
	}

	/**
	 * The type of event to search for. Does not currently support custom events.
	 * 
	 * @return The event type to search for, or {@code null} if unspecified.
	 */
	public EventType getEventType() {
		return eventType;
	}


	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder extends AbstractConversationsFilterRequest.Builder<ListEventsRequest, Builder> {
		private Boolean excludeDeletedEvents;
		private String startId, endId;
		private EventType eventType;
	
		Builder() {}
	
		/**
		 * Whether to exclude deleted events from the results.
		 *
		 * @param excludeDeletedEvents {@code true} to exclude deleted events, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder excludeDeletedEvents(Boolean excludeDeletedEvents) {
			this.excludeDeletedEvents = excludeDeletedEvents;
			return this;
		}

		/**
		 * The ID to start returning events at.
		 *
		 * @param startId The start ID, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder startId(String startId) {
			this.startId = startId;
			return this;
		}

		/**
		 * The ID to stop returning events at.
		 *
		 * @param endId The end ID, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder endId(String endId) {
			this.endId = endId;
			return this;
		}

		/**
		 * The type of event to search for. Does not currently support custom events.
		 *
		 * @param eventType The event type to search for, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder eventType(EventType eventType) {
			this.eventType = eventType;
			return this;
		}

		/**
		 * Builds the {@linkplain ListEventsRequest}.
		 *
		 * @return An instance of ListEventsRequest, populated with all fields from this builder.
		 */
		public ListEventsRequest build() {
			return new ListEventsRequest(this);
		}
	}
}
