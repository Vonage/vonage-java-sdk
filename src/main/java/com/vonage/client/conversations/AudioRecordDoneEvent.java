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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.voice.ncco.RecordingFormat;
import java.net.URI;
import java.time.Instant;

/**
 * Represents a {@linkplain EventType#AUDIO_RECORD_DONE} event.
 *
 * @since 8.19.0
 */
public final class AudioRecordDoneEvent extends EventWithBody<AudioRecordDoneEvent.Body> {
	AudioRecordDoneEvent() {}

	static class Body extends JsonableBaseObject {
		@JsonProperty("event_id") Integer eventId;
		@JsonProperty("recording_id") String recordingId;
		@JsonProperty("destination_url") URI destinationUrl;
		@JsonProperty("format") RecordingFormat format;
		@JsonProperty("start_time") Instant startTime;
		@JsonProperty("end_time") Instant endTime;
		@JsonProperty("size") Integer size;
	}

	/**
	 * Returns the event ID.
	 *
	 * @return the event ID, or {@code null} if unknown.
	 */
	@JsonIgnore
	public Integer getEventId() {
		return body != null ? body.eventId : null;
	}

	/**
	 * Returns the recording ID.
	 *
	 * @return the recording ID, or {@code null} if unknown.
	 */
	@JsonIgnore
	public String getRecordingId() {
	    return body != null ? body.recordingId : null;
	}

	/**
	 * Returns the destination URL.
	 *
	 * @return the destination URL, or {@code null} if unknown.
	 */
	@JsonIgnore
	public URI getDestinationUrl() {
	    return body != null ? body.destinationUrl : null;
	}

	/**
	 * Returns the recording format.
	 *
	 * @return the recording format, or {@code null} if unknown.
	 */
	@JsonIgnore
	public RecordingFormat getFormat() {
	    return body != null ? body.format : null;
	}

	/**
	 * Returns the start time.
	 *
	 * @return the start time, or {@code null} if unknown.
	 */
	@JsonIgnore
	public Instant getStartTime() {
	    return body != null ? body.startTime : null;
	}

	/**
	 * Returns the end time.
	 *
	 * @return the end time, or {@code null} if unknown.
	 */
	@JsonIgnore
	public Instant getEndTime() {
	    return body != null ? body.endTime : null;
	}

	/**
	 * Returns the size.
	 *
	 * @return the size, or {@code null} if unknown.
	 */
	@JsonIgnore
	public Integer getSize() {
	    return body != null ? body.size : null;
	}
}