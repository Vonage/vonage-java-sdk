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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents a session for a Meeting.
 *
 * @since 7.11.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session {
	private UUID roomId;
	private String id, roomDisplayName;
	private Instant startTime, endTime;
	private Boolean isRecorded;
	private Integer participantsNumber;
	private Double billedTime, recordingsTime;

	protected Session() {}

	/**
	 * ID of the session.
	 *
	 * @return The session ID, or {@code null} if absent.
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * Room ID for the session.
	 *
	 * @return The room ID, or {@code null} if absent.
	 */
	@JsonProperty("room_id")
	public UUID getRoomId() {
		return roomId;
	}

	/**
	 * Name of the meeting room.
	 *
	 * @return The room name, or {@code null} if absent.
	 */
	@JsonProperty("room_display_name")
	public String getRoomDisplayName() {
		return roomDisplayName;
	}

	/**
	 * Recording start date in ISO-8601 format.
	 *
	 * @return Timestamp of the recording's start, or {@code null} if absent.
	 */
	@JsonProperty("start_time")
	public Instant getStartTime() {
		return startTime;
	}

	/**
	 * Recording end date in ISO-8601 format.
	 *
	 * @return Timestamp of the recording's end, or {@code null} if absent.
	 */
	@JsonProperty("end_time")
	public Instant getEndTime() {
		return endTime;
	}

	/**
	 * Whether the session is recorded.
	 *
	 * @return {@code true} if the session is recorded, {@code false} if not and {@code null} if unknown.
	 */
	@JsonProperty("is_recorded")
	public Boolean getIsRecorded() {
		return isRecorded;
	}

	/**
	 * Number of participants in the meeting.
	 *
	 * @return The total number of participants, or {@code null} if unknown.
	 */
	@JsonProperty("participants_number")
	public Integer getParticipantsNumber() {
		return participantsNumber;
	}

	/**
	 * Duration of the recording that was billed, in minutes.
	 *
	 * @return The number of minutes billed for this meeting, or {@code null} if unknown / not applicable.
	 */
	@JsonProperty("billed_time")
	public Double getBilledTime() {
		return billedTime;
	}

	/**
	 * Aggregated recording time of the session in minutes.
	 *
	 * @return The total number of minutes this meeting was recorded for, or {@code null} if not applicable.
	 */
	@JsonProperty("recordings_time")
	public Double getRecordingsTime() {
		return recordingsTime;
	}
}
