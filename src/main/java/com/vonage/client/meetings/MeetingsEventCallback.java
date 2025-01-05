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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.VonageResponseParseException;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents a deserialized callback response webhook for event updates from the Meetings API.
 */
public class MeetingsEventCallback extends JsonableBaseObject {
	private EventType event;
	private String sessionId, participantName, participantType;
	private UUID roomId, recordingId, participantId;
	private RoomType roomType;
	private Instant createdAt, startedAt, expiresAt, endedAt;
	private Integer duration;
	private URI recordingUrl;
	private Boolean isHost;

	protected MeetingsEventCallback() {
	}

	/**
	 * Type of event represented by this object.
	 *
	 * @return The event type as an enum.
	 */
	@JsonProperty("event")
	public EventType getEvent() {
		return event;
	}

	/**
	 * Corresponds to the underlying Video API session ID.
	 *
	 * @return The session ID, or {@code null} if not applicable.
	 */
	@JsonProperty("session_id")
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * The participant name.
	 *
	 * @return Display name of the participant, or {@code null} if not applicable.
	 */
	@JsonProperty("name")
	public String getParticipantName() {
		return participantName;
	}

	/**
	 * The participant type (e.g. "Guest").
	 *
	 * @return Type of participant, or {@code null} if not applicable.
	 */
	@JsonProperty("type")
	public String getParticipantType() {
		return participantType;
	}

	/**
	 * Meeting room's ID.
	 *
	 * @return The room ID, or {@code null} if not applicable.
	 */
	@JsonProperty("room_id")
	public UUID getRoomId() {
		return roomId;
	}

	/**
	 * Unique recording ID for the meeting.
	 *
	 * @return The recording ID, or {@code null} if not applicable.
	 */
	@JsonProperty("recording_id")
	public UUID getRecordingId() {
		return recordingId;
	}

	/**
	 * Unique identifier for the participant.
	 *
	 * @return The participant ID, or {@code null} if not applicable.
	 */
	@JsonProperty("participant_id")
	public UUID getParticipantId() {
		return participantId;
	}

	/**
	 * The type of meeting room.
	 *
	 * @return The meeting room type as an enum, or {@code null} if not applicable.
	 */
	@JsonProperty("room_type")
	public RoomType getRoomType() {
		return roomType;
	}

	/**
	 * The date-time when the room will expire, expressed in ISO 8601 format.
	 *
	 * @return The room expiration timestamp, or {@code null} if not applicable.
	 */
	@JsonProperty("expires_at")
	public Instant getExpiresAt() {
		return expiresAt;
	}

	/**
	 * The date-time when the room was created, expressed in ISO 8601 format.
	 *
	 * @return The room creation timestamp, or {@code null} if not applicable.
	 */
	@JsonProperty("created_at")
	public Instant getCreatedAt() {
		return createdAt;
	}

	/**
	 * The date-time when the session started, expressed in ISO 8601 format.
	 *
	 * @return The start timestamp, or {@code null} if not applicable.
	 */
	@JsonProperty("started_at")
	public Instant getStartedAt() {
		return startedAt;
	}

	/**
	 * The date-time the session or recording ended, expressed in ISO 8601 format.
	 *
	 * @return The end timestamp, or {@code null} if not applicable.
	 */
	@JsonProperty("ended_at")
	public Instant getEndedAt() {
		return endedAt;
	}

	/**
	 * Duration of the recording in seconds.
	 *
	 * @return The duration in seconds, or {@code null} if not applicable.
	 */
	@JsonProperty("duration")
	public Integer getDuration() {
		return duration;
	}

	/**
	 * URL of the uploaded recording.
	 *
	 * @return The recording URL, or {@code null} if not applicable.
	 */
	@JsonProperty("url")
	public URI getRecordingUrl() {
		return recordingUrl;
	}

	/**
	 * Indicates if this participant is the session's host.
	 *
	 * @return Whether the participant is the session host, or {@code null} if not applicable.
	 */
	@JsonProperty("is_host")
	public Boolean getIsHost() {
		return isHost;
	}

	/**
	 * Constructs an instance of this class from a JSON payload.
	 *
	 * @param json The webhook response JSON string.
	 *
	 * @return The deserialized webhook response object.
	 * @throws VonageResponseParseException If the response could not be deserialized.
	 */
	public static MeetingsEventCallback fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
