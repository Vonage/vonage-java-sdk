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
package com.vonage.client.meetings;

import com.vonage.client.VonageResponseParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

public class MeetingsEventCallbackTest {
	static final UUID
			ROOM_ID = UUID.randomUUID(),
			RECORDING_ID = UUID.randomUUID(),
			PARTICIPANT_ID = UUID.randomUUID();
	static final String
			PARTICIPANT_NAME = "John Smith",
			PARTICIPANT_TYPE = "Guest",
			SESSION_ID = "2_MX40NjMwODczMn5-MTU3NTgyODEwNzQ2MH5OZDJrVmdBRUNDbG5MUzNqNX20yQ1Z-fg";
	static final Instant
			CREATED_AT = Instant.parse("2023-06-06T06:45:07.135Z"),
			EXPIRES_AT = Instant.parse("2023-06-06T06:55:07.134Z"),
			STARTED_AT = Instant.parse("2023-06-06T07:15:13.974Z"),
			ENDED_AT = Instant.now();
	static final URI RECORDING_URL = URI.create("https://prod-meetings-recordings.s3.amazonaws.com/123/UUID/archive.mp4");
	static final Boolean IS_HOST = true;
	static final Integer DURATION = 3598;
	static final RoomType ROOM_TYPE = RoomType.INSTANT;

	@Test
	public void testRoomExpired() {
		MeetingsEventCallback event = MeetingsEventCallback.fromJson(
				"{\n" +
				"   \"event\": \"room:expired\",\n" +
				"   \"room_id\": \""+ROOM_ID+"\",\n" +
				"   \"room_type\": \""+ROOM_TYPE+"\",\n" +
				"   \"expires_at\": \""+EXPIRES_AT+"\",\n" +
				"   \"created_at\": \""+CREATED_AT+"\"\n" +
				"}"
		);

		assertNotNull(event);
		assertEquals(EventType.ROOM_EXPIRED, event.getEvent());
		assertEquals(ROOM_ID, event.getRoomId());
		assertEquals(ROOM_TYPE, event.getRoomType());
		assertEquals(CREATED_AT, event.getCreatedAt());
		assertNull(event.getStartedAt());
		assertEquals(EXPIRES_AT, event.getExpiresAt());
		assertNull(event.getEndedAt());
		assertNull(event.getSessionId());
		assertNull(event.getParticipantId());
		assertNull(event.getParticipantName());
		assertNull(event.getParticipantType());
		assertNull(event.getIsHost());
		assertNull(event.getRecordingId());
		assertNull(event.getRecordingUrl());
		assertNull(event.getDuration());
	}

	@Test
	public void testSessionStarted() {
		MeetingsEventCallback event = MeetingsEventCallback.fromJson(
				"{\n" +
				"   \"event\": \"session:started\",\n" +
				"   \"session_id\": \""+SESSION_ID+"\",\n" +
				"   \"room_id\": \""+ROOM_ID+"\",\n" +
				"   \"started_at\": \""+STARTED_AT+"\"\n" +
				"}"
		);

		assertNotNull(event);
		assertEquals(EventType.SESSION_STARTED, event.getEvent());
		assertEquals(ROOM_ID, event.getRoomId());
		assertNull(event.getRoomType());
		assertNull(event.getCreatedAt());
		assertEquals(STARTED_AT, event.getStartedAt());
		assertNull(event.getExpiresAt());
		assertNull(event.getEndedAt());
		assertEquals(SESSION_ID, event.getSessionId());
		assertNull(event.getParticipantId());
		assertNull(event.getParticipantName());
		assertNull(event.getParticipantType());
		assertNull(event.getIsHost());
		assertNull(event.getRecordingId());
		assertNull(event.getRecordingUrl());
		assertNull(event.getDuration());
	}

	@Test
	public void testSessionEnded() {
		MeetingsEventCallback event = MeetingsEventCallback.fromJson(
				"{\n" +
				"   \"event\": \"session:ended\",\n" +
				"   \"session_id\": \""+SESSION_ID+"\",\n" +
				"   \"room_id\": \""+ROOM_ID+"\",\n" +
				"   \"started_at\": \""+STARTED_AT+"\",\n" +
				"   \"ended_at\": \""+ENDED_AT+"\"\n" +
				"}"
		);

		assertNotNull(event);
		assertEquals(EventType.SESSION_ENDED, event.getEvent());
		assertEquals(ROOM_ID, event.getRoomId());
		assertNull(event.getRoomType());
		assertNull(event.getCreatedAt());
		assertEquals(STARTED_AT, event.getStartedAt());
		assertNull(event.getExpiresAt());
		assertEquals(ENDED_AT, event.getEndedAt());
		assertEquals(SESSION_ID, event.getSessionId());
		assertNull(event.getParticipantId());
		assertNull(event.getParticipantName());
		assertNull(event.getParticipantType());
		assertNull(event.getIsHost());
		assertNull(event.getRecordingId());
		assertNull(event.getRecordingUrl());
		assertNull(event.getDuration());
	}

	@Test
	public void testRecordingStarted() {
		MeetingsEventCallback event = MeetingsEventCallback.fromJson(
				"{\n" +
				"   \"event\": \"recording:started\",\n" +
				"   \"recording_id\": \""+RECORDING_ID+"\",\n" +
				"   \"session_id\": \""+SESSION_ID+"\"\n" +
				"}"
		);

		assertNotNull(event);
		assertEquals(EventType.RECORDING_STARTED, event.getEvent());
		assertNull(event.getRoomId());
		assertNull(event.getRoomType());
		assertNull(event.getCreatedAt());
		assertNull(event.getStartedAt());
		assertNull(event.getExpiresAt());
		assertNull(event.getEndedAt());
		assertEquals(SESSION_ID, event.getSessionId());
		assertNull(event.getParticipantId());
		assertNull(event.getParticipantName());
		assertNull(event.getParticipantType());
		assertNull(event.getIsHost());
		assertEquals(RECORDING_ID, event.getRecordingId());
		assertNull(event.getRecordingUrl());
		assertNull(event.getDuration());
	}

	@Test
	public void testRecordingEnded() {
		MeetingsEventCallback event = MeetingsEventCallback.fromJson(
				"{\n" +
				"   \"event\": \"recording:ended\",\n" +
				"   \"recording_id\": \""+RECORDING_ID+"\",\n" +
				"   \"session_id\": \""+SESSION_ID+"\",\n" +
				"   \"started_at\": \""+STARTED_AT+"\",\n" +
				"   \"ended_at\": \""+ENDED_AT+"\",\n" +
				"   \"duration\": "+DURATION+"\n" +
				"}"
		);

		assertNotNull(event);
		assertEquals(EventType.RECORDING_ENDED, event.getEvent());
		assertNull(event.getRoomId());
		assertNull(event.getRoomType());
		assertNull(event.getCreatedAt());
		assertEquals(STARTED_AT, event.getStartedAt());
		assertNull(event.getExpiresAt());
		assertEquals(ENDED_AT, event.getEndedAt());
		assertEquals(SESSION_ID, event.getSessionId());
		assertNull(event.getParticipantId());
		assertNull(event.getParticipantName());
		assertNull(event.getParticipantType());
		assertNull(event.getIsHost());
		assertEquals(RECORDING_ID, event.getRecordingId());
		assertNull(event.getRecordingUrl());
		assertEquals(DURATION, event.getDuration());
	}

	@Test
	public void testRecordingUploaded() {
		MeetingsEventCallback event = MeetingsEventCallback.fromJson(
				"{\n" +
				"   \"event\": \"recording:uploaded\",\n" +
				"   \"recording_id\": \""+RECORDING_ID+"\",\n" +
				"   \"session_id\": \""+SESSION_ID+"\",\n" +
				"   \"room_id\": \""+ROOM_ID+"\",\n" +
				"   \"started_at\": \""+STARTED_AT+"\",\n" +
				"   \"ended_at\": \""+ENDED_AT+"\",\n" +
				"   \"duration\": \""+DURATION+"\",\n" +
				"   \"url\": \""+ RECORDING_URL +"\"\n" +
				"}"
		);

		assertNotNull(event);
		assertEquals(EventType.RECORDING_UPLOADED, event.getEvent());
		assertEquals(ROOM_ID, event.getRoomId());
		assertNull(event.getRoomType());
		assertNull(event.getCreatedAt());
		assertEquals(STARTED_AT, event.getStartedAt());
		assertNull(event.getExpiresAt());
		assertEquals(ENDED_AT, event.getEndedAt());
		assertEquals(SESSION_ID, event.getSessionId());
		assertNull(event.getParticipantId());
		assertNull(event.getParticipantName());
		assertNull(event.getParticipantType());
		assertNull(event.getIsHost());
		assertEquals(RECORDING_ID, event.getRecordingId());
		assertEquals(RECORDING_URL, event.getRecordingUrl());
		assertEquals(DURATION, event.getDuration());
	}

	@Test
	public void testParticipantJoined() {
		MeetingsEventCallback event = MeetingsEventCallback.fromJson(
				"{\n" +
				"   \"event\": \"session:participant:joined\",\n" +
				"   \"participant_id\": \""+PARTICIPANT_ID+"\",\n" +
				"   \"session_id\": \""+SESSION_ID+"\",\n" +
				"   \"room_id\": \""+ROOM_ID+"\",\n" +
				"   \"name\": \""+PARTICIPANT_NAME+"\",\n" +
				"   \"type\": \""+PARTICIPANT_TYPE+"\",\n" +
				"   \"is_host\": "+IS_HOST+"\n" +
				"}"
		);

		assertNotNull(event);
		assertEquals(EventType.SESSION_PARTICIPANT_JOINED, event.getEvent());
		assertEquals(ROOM_ID, event.getRoomId());
		assertNull(event.getRoomType());
		assertNull(event.getCreatedAt());
		assertNull(event.getStartedAt());
		assertNull(event.getExpiresAt());
		assertNull(event.getEndedAt());
		assertEquals(SESSION_ID, event.getSessionId());
		assertEquals(PARTICIPANT_ID, event.getParticipantId());
		assertEquals(PARTICIPANT_NAME, event.getParticipantName());
		assertEquals(PARTICIPANT_TYPE, event.getParticipantType());
		assertEquals(IS_HOST, event.getIsHost());
		assertNull(event.getRecordingId());
		assertNull(event.getRecordingUrl());
		assertNull(event.getDuration());
	}

	@Test
	public void testParticipantLeft() {
		MeetingsEventCallback event = MeetingsEventCallback.fromJson(
				"{\n" +
				"   \"event\": \"session:participant:left\",\n" +
				"   \"participant_id\": \""+PARTICIPANT_ID+"\",\n" +
				"   \"session_id\": \""+SESSION_ID+"\",\n" +
				"   \"room_id\": \""+ROOM_ID+"\",\n" +
				"   \"name\": \""+PARTICIPANT_NAME+"\",\n" +
				"   \"type\": \""+PARTICIPANT_TYPE+"\",\n" +
				"   \"is_host\": "+IS_HOST+"\n" +
				"}"
		);

		assertNotNull(event);
		assertEquals(EventType.SESSION_PARTICIPANT_LEFT, event.getEvent());
		assertEquals(ROOM_ID, event.getRoomId());
		assertNull(event.getRoomType());
		assertNull(event.getCreatedAt());
		assertNull(event.getStartedAt());
		assertNull(event.getExpiresAt());
		assertNull(event.getEndedAt());
		assertEquals(SESSION_ID, event.getSessionId());
		assertEquals(PARTICIPANT_ID, event.getParticipantId());
		assertEquals(PARTICIPANT_NAME, event.getParticipantName());
		assertEquals(PARTICIPANT_TYPE, event.getParticipantType());
		assertEquals(IS_HOST, event.getIsHost());
		assertNull(event.getRecordingId());
		assertNull(event.getRecordingUrl());
		assertNull(event.getDuration());
	}

	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> MeetingsEventCallback.fromJson("{malformed]"));
	}
}
