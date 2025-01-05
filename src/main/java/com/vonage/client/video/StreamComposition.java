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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Base class for {@link Broadcast} and {@link Archive}.
 * Represents the common features of a video call consisting of multiple streams.
 */
public abstract class StreamComposition extends JsonableBaseObject {
	@JsonProperty("id") protected UUID id;
	@JsonProperty("applicationId") protected UUID applicationId;
	@JsonProperty("sessionId") protected String sessionId;
	@JsonProperty("streamMode") protected StreamMode streamMode;
	@JsonProperty("resolution") protected Resolution resolution;
	@JsonProperty("layout") protected StreamCompositionLayout layout;
	@JsonProperty("hasVideo") protected Boolean hasVideo;
	@JsonProperty("hasAudio") protected Boolean hasAudio;
	@JsonProperty("createdAt") protected Long createdAt;
	@JsonProperty("streams") protected List<VideoStream> streams;
	@JsonProperty("maxBitrate") protected Integer maxBitrate;

	protected StreamComposition() {
	}

	protected StreamComposition(Builder builder) {
		sessionId = Objects.requireNonNull(builder.sessionId, "Session ID is required");
		streamMode = builder.streamMode;
		resolution = builder.resolution;
		hasAudio = builder.hasAudio;
		hasVideo = builder.hasVideo;
		layout = builder.layout;
		maxBitrate = builder.maxBitrate;
	}

	/**
	 * Unique ID of the composition.
	 *
	 * @return The composition ID.
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Vonage video application ID (as used to create the client).
	 *
	 * @return The application ID.
	 */
	public UUID getApplicationId() {
		return applicationId;
	}

	/**
	 * The ID of the video session associated with this composition.
	 *
	 * @return The session ID.
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * The video resolution of the composition.
	 *
	 * @return The resolution as an enum.
	 */
	public Resolution getResolution() {
		return resolution;
	}

	/**
	 * Whether the composition has a video track ({@code true}) or not ({@code false}).
	 *
	 * @return Whether this composition has video.
	 */
	public Boolean hasVideo() {
		return hasVideo;
	}

	/**
	 * Whether the archive has an audio track ({@code true}) or not ({@code false}).
	 *
	 * @return Whether this composition has sound.
	 */
	public Boolean hasAudio() {
		return hasAudio;
	}

	/**
	 * The stream mode to used for selecting streams to be included in this composition:
	 * {@link StreamMode#AUTO} or {@link StreamMode#MANUAL}.
	 *
	 * @return The {@linkplain StreamMode}.
	 */
	public StreamMode getStreamMode() {
		return streamMode;
	}

	/**
	 * Returns the streams associated with this composition. This is only set when the status is
	 * "started" and the stream mode is "manual".
	 *
	 * @return The details for each video stream, or {@code null} if not applicable.
	 */
	public List<VideoStream> getStreams() {
		return streams;
	}

	/**
	 * Describes how the streams in this composition are displayed.
	 *
	 * @return Layout information for the stream compositions.
	 */
	protected StreamCompositionLayout getLayout() {
		return layout;
	}

	/**
	 * The maximum bitrate for the stream in bits per second (if one was set).
	 *
	 * @return The maximum bits per second as an integer, or {@code null} if unset.
	 */
	public Integer getMaxBitrate() {
		return maxBitrate;
	}

	/**
	 * The time at which this composition was started or created, in milliseconds since the Unix epoch.
	 *
	 * @return The created time as a long, or {@code null} if absent / not applicable.
	 */
	@JsonProperty("createdAt")
	public Long getCreatedAtMillis() {
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

	protected static abstract class Builder {
		protected String sessionId;
		protected Integer maxBitrate;
		protected Boolean hasAudio, hasVideo;
		protected Resolution resolution;
		protected StreamMode streamMode;
		protected StreamCompositionLayout layout;
	}
}
