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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.UUID;

/**
 * Represents an Experience Composer response.
 *
 * @since 8.6.0
 */
public class RenderResponse extends JsonableBaseObject {
	private UUID id, applicationId, streamId;
	private String sessionId, name, reason;
	private Long createdAt, updatedAt;
	private URI url, callbackUrl;
	private Resolution resolution;
	private RenderStatus status;

	protected RenderResponse() {}

	/**
	 * Unique identifier for the Experience Composer.
	 *
	 * @return The render ID.
	 */
	@JsonProperty("id")
	public UUID getId() {
		return id;
	}

	/**
	 * Session ID of the Vonage Video session you are working with.
	 *
	 * @return The Vonage Video session ID.
	 */
	@JsonProperty("sessionId")
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Vonage Application ID.
	 *
	 * @return The application UUID.
	 */
	@JsonAlias("projectId")
	@JsonProperty("applicationId")
	public UUID getApplicationId() {
		return applicationId;
	}

	/**
	 * The time the Experience Composer started, expressed in milliseconds since the Unix epoch.
	 *
	 * @return The render start time as a Long.
	 */
	@JsonProperty("createdAt")
	public Long getCreatedAt() {
		return createdAt;
	}

	/**
	 * UNIX timestamp when the Experience Composer status was last updated. When starting an Experience Composer
	 * for the first time, this will be the same as {@linkplain #getCreatedAt()}.
	 *
	 * @return The last update time as a Long.
	 */
	@JsonProperty("updatedAt")
	public Long getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * A publicly reachable URL controlled by the customer and capable of generating the
	 * content to be rendered without user intervention.
	 *
	 * @return The URL.
	 */
	@JsonProperty("url")
	public URI getUrl() {
		return url;
	}

	/**
	 * Callback URL for Experience Composer events (if one was set).
	 *
	 * @return The callback URL, or {@code null} if unspecified.
	 */
	@JsonProperty("callbackUrl")
	public URI getCallbackUrl() {
		return callbackUrl;
	}

	/**
	 * Output resolution of the Experience Composer (either "640x480", "1280x720", "480x640", or "720x1280").
	 *
	 * @return The render resolution as an enum.
	 */
	@JsonProperty("resolution")
	public Resolution getResolution() {
		return resolution;
	}

	/**
	 * Status of the Experience Composer.
	 *
	 * @return The render status as an enum.
	 */
	@JsonProperty("status")
	public RenderStatus getStatus() {
		return status;
	}

	/**
	 * ID of the composed stream being published. This is not available when the status is
	 * {@linkplain RenderStatus#STARTING} and may not be available when the status is {@linkplain RenderStatus#FAILED}.
	 *
	 * @return The stream ID, or {@code null} if unavailable.
	 */
	@JsonProperty("streamId")
	public UUID getStreamId() {
		return streamId;
	}

	/**
	 * Name of the composed output stream which is published to the session.
	 *
	 * @return The render name.
	 * @since 8.12.0
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * The reason field is only available when the status is either {@linkplain RenderStatus#STOPPED} or
	 * {@linkplain RenderStatus#FAILED}. If the status is "stopped", the reason field will contain either
	 * "Max Duration Exceeded" or "Stop Requested." If the status is "failed", the reason will contain a
	 * more specific error message.
	 *
	 * @return The reason, or {@code null} if not applicable.
	 */
	@JsonProperty("reason")
	public String getReason() {
		return reason;
	}
}
