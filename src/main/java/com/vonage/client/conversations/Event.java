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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.users.User;
import java.time.Instant;
import java.util.Objects;

/**
 * Events are actions that occur within a conversation.
 */
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.EXISTING_PROPERTY,
		property = "type",
		visible = true,
		defaultImpl = GenericEvent.class
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = EventDeleteEvent.class, name = "event:delete"),
		@JsonSubTypes.Type(value = ConversationUpdatedEvent.class, name = "conversation:updated"),
		@JsonSubTypes.Type(value = MessageSeenEvent.class, name = "message:seen"),
		@JsonSubTypes.Type(value = MessageSubmittedEvent.class, name = "message:submitted"),
		@JsonSubTypes.Type(value = MessageDeliveredEvent.class, name = "message:delivered"),
		@JsonSubTypes.Type(value = MessageUndeliverableEvent.class, name = "message:undeliverable"),
		@JsonSubTypes.Type(value = MessageRejectedEvent.class, name = "message:rejected"),
		@JsonSubTypes.Type(value = AudioPlayEvent.class, name = "audio:play"),
		@JsonSubTypes.Type(value = AudioPlayStopEvent.class, name = "audio:play:stop"),
		@JsonSubTypes.Type(value = AudioPlayDoneEvent.class, name = "audio:play:done"),
		@JsonSubTypes.Type(value = AudioSayEvent.class, name = "audio:say"),
		@JsonSubTypes.Type(value = AudioSayStopEvent.class, name = "audio:say:stop"),
		@JsonSubTypes.Type(value = AudioSayDoneEvent.class, name = "audio:say:done"),
		@JsonSubTypes.Type(value = AudioRecordEvent.class, name = "audio:record"),
		@JsonSubTypes.Type(value = AudioRecordStopEvent.class, name = "audio:record:stop"),
		@JsonSubTypes.Type(value = AudioMuteOnEvent.class, name = "audio:mute:on"),
		@JsonSubTypes.Type(value = AudioMuteOffEvent.class, name = "audio:mute:off"),
		@JsonSubTypes.Type(value = AudioEarmuffOnEvent.class, name = "audio:earmuff:on"),
		@JsonSubTypes.Type(value = AudioEarmuffOffEvent.class, name = "audio:earmuff:off"),
		@JsonSubTypes.Type(value = EphemeralEvent.class, name = "ephemeral"),
		@JsonSubTypes.Type(value = CustomEvent.class, name = "custom")
})
public abstract class Event extends JsonableBaseObject {
	@JsonIgnore String conversationId;
	@JsonProperty("type") EventType type;
	@JsonProperty("id") Integer id;
	@JsonProperty("from") String from;
	@JsonProperty("timestamp") Instant timestamp;
	@JsonProperty("_embedded") Embedded _embedded;

	private static class Embedded extends JsonableBaseObject {
		@JsonProperty("from_user") User fromUser;
		@JsonProperty("from_member") BaseMember fromMember;
	}

	protected Event() {
	}

	Event(Builder<?, ?> builder) {
		type = Objects.requireNonNull(builder.type, "Event type is required.");
		from = builder.from;
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
	 * Builder for constructing an event request's parameters.
	 *
	 * @param <E> The event type.
	 * @param <B> The builder type.
	 */
	@SuppressWarnings("unchecked")
	public abstract static class Builder<E extends Event, B extends Builder<? extends E, ? extends B>> {
		private final EventType type;
		private String from;

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
		 * @param from The member ID.
		 *
		 * @return This builder.
		 */
		public B from(String from) {
			this.from = ConversationsClient.validateMemberId(from);
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
