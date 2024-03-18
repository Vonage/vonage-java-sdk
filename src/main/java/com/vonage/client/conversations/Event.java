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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.users.User;
import java.time.Instant;

/**
 * Events are actions that occur within a conversation.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Event extends JsonableBaseObject {
	@JsonIgnore String conversationId;
	@JsonProperty("id") private Integer id;
	@JsonProperty("type") private EventType type;
	@JsonProperty("from") private String from;
	@JsonProperty("timestamp") private Instant timestamp;
	@JsonProperty("_embedded") private Embedded _embedded;
	@JsonProperty("body") private Object body;

	private static class Embedded extends JsonableBaseObject {
		@JsonProperty("from_user") User fromUser;
		@JsonProperty("from_member") BaseMember fromMember;
	}

	protected Event() {
	}

	Event(Builder<?, ?> builder) {
		type = builder.type;
		from = builder.from;
		body = builder.body;
	}

	/**
	 * Event id. This is a progressive integer.
	 * 
	 * @return The event ID as an integer, or {@code null} if unknown.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Type of event.
	 * 
	 * @return The event type as an enum.
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * Member ID this event was sent from.
	 * 
	 * @return The member ID, or {@code null} if unspecified.
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Time of creation.
	 * 
	 * @return The event timestamp, or {@code null} if unknown.
	 */
	public Instant getTimestamp() {
		return timestamp;
	}

	/**
	 * Details about the user that initiated the event.
	 * 
	 * @return The embedded {@code from_user} object, or {@code null} if absent.
	 */
	@JsonIgnore
	public User getFromUser() {
		return _embedded != null ? _embedded.fromUser : null;
	}

	/**
	 * Member that initiated the event. Only the {@code id} field will be present.
	 * 
	 * @return The embedded {@code from_member} object, or {@code null} if absent.
	 */
	@JsonIgnore
	public BaseMember getFromMember() {
		return _embedded != null ? _embedded.fromMember : null;
	}

	/**
	 * Event-specific data.
	 * 
	 * @return The event body object, or {@code null} if absent
	 */
	public Object getBody() {
		return body;
	}

	@SuppressWarnings("unchecked")
	public abstract static class Builder<E extends Event, B extends Builder<? extends E, ? extends B>> {
		private final EventType type;
		private String from;
		private AbstractEventBody body;

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
		public B body(AbstractEventBody body) {
			this.body = body;
			return (B) this;
		}

		/**
		 * Builds the {@linkplain Event}.
		 *
		 * @return An instance of Event, populated with all fields from this builder.
		 */
		public abstract E build();
	}
}
