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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Base class for {@link Broadcast} and {@link Archive}.
 * Represents the common features of a video call consisting of multiple streams.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public abstract class StreamComposition {
	protected UUID id;
	protected String sessionId;
	protected StreamMode streamMode;
	protected Resolution resolution;
	protected StreamCompositionLayout layout;
	protected Boolean hasVideo, hasAudio;
	@JsonProperty("createdAt") protected Long createdAt;
	protected List<VideoStream> streams;

	protected StreamComposition() {
	}

	protected StreamComposition(Builder builder) {
		sessionId = Objects.requireNonNull(builder.sessionId, "Session ID is required");
		streamMode = builder.streamMode;
		resolution = builder.resolution;
		hasAudio = builder.hasAudio;
		hasVideo = builder.hasVideo;
		layout = builder.layout;
	}

	/**
	 * Unique ID of the composition.
	 *
	 * @return The composition ID.
	 */
	@JsonProperty("id")
	public UUID getId() {
		return id;
	}

	/**
	 * The ID of the video session associated with this composition.
	 *
	 * @return The session ID.
	 */
	@JsonProperty("sessionId")
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * The video resolution of the composition.
	 *
	 * @return The resolution as an enum.
	 */
	@JsonProperty("resolution")
	public Resolution getResolution() {
		return resolution;
	}

	/**
	 * Whether the composition has a video track ({@code true}) or not ({@code false}).
	 *
	 * @return Whether this composition has video.
	 */
	@JsonProperty("hasVideo")
	public Boolean hasVideo() {
		return hasVideo;
	}

	/**
	 * Whether the archive has an audio track ({@code true}) or not ({@code false}).
	 *
	 * @return Whether this composition has sound.
	 */
	@JsonProperty("hasAudio")
	public Boolean hasAudio() {
		return hasAudio;
	}

	/**
	 * The stream mode to used for selecting streams to be included in this composition:
	 * {@link StreamMode#AUTO} or {@link StreamMode#MANUAL}.
	 *
	 * @return The {@linkplain StreamMode}.
	 */
	@JsonProperty("streamMode")
	public StreamMode getStreamMode() {
		return streamMode;
	}

	/**
	 * The time at which this composition was started or created, in milliseconds since the Unix epoch.
	 *
	 * @return The created time as a long, or {@code null} if absent / not applicable.
	 */
	@JsonProperty("createdAt")
	public Long getCreatedAtRaw() {
		return createdAt;
	}

	/**
	 * The time at which this composition was started or created.
	 *
	 * @return The creation time, or {@code null} if absent / not applicable.
	 */
	@JsonIgnore
	public Instant getCreatedAt() {
		return createdAt != null ? Instant.ofEpochMilli(createdAt) : null;
	}

	/**
	 * Returns the streams associated with this composition.
	 *
	 * @return The details for each video stream.
	 */
	@JsonProperty("streams")
	public List<VideoStream> getStreams() {
		return streams;
	}

	/**
	 * Describes how the streams in this composition are displayed.
	 *
	 * @return Layout information for the stream compositions.
	 */
	@JsonProperty("layout")
	public StreamCompositionLayout getLayout() {
		return layout;
	}

	/**
	 * Updates (hydrates) this object's fields from additional JSON data.
	 *
	 * @param json The JSON payload.
	 */
	public void updateFromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.readerForUpdating(this).readValue(json, getClass());
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to update "+getClass().getSimpleName()+" from json.", ex);
		}
	}

	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}

	protected static abstract class Builder {
		protected String sessionId;
		protected Boolean hasAudio, hasVideo;
		protected Resolution resolution;
		protected StreamMode streamMode;
		protected StreamCompositionLayout layout;
	}
}
