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

/**
 * Events are actions that occur within a conversation.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class EventWithBody<T> extends Event {
	@JsonProperty("body") private T body;

	EventWithBody() {}

	EventWithBody(Builder<? extends T, ?, ?> builder) {
		type = builder.type;
		from = builder.from;
		body = builder.body;
	}

	/**
	 * Event-specific data.
	 * 
	 * @return The event body object, or {@code null} if absent.
	 */
	public T getBody() {
		return body;
	}

	@SuppressWarnings("unchecked")
	public abstract static class Builder<T, E extends EventWithBody<T>, B extends Builder<T, ? extends E, ? extends B>> {
		private final EventType type;
		private String from;
		private T body;

		/**
		 * Construct a new builder for a given event type.
		 *
		 * @param type The event type as an enum.
		 */
		protected Builder(EventType type) {
			this.type = type;
		}

		/**
		 * Member ID this event was sent from.
		 *
		 * @param from The member ID, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public B from(String from) {
			this.from = from;
			return (B) this;
		}

		/**
		 * Event-specific data.
		 *
		 * @param body The event body object, or {@code null} if absent
		 *
		 * @return This builder.
		 */
		public B body(T body) {
			this.body = body;
			return (B) this;
		}

		/**
		 * Builds the {@linkplain EventWithBody}.
		 *
		 * @return An instance of Event, populated with all fields from this builder.
		 */
		public abstract E build();
	}
}
