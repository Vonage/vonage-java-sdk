/*
 *   Copyright 2022 Vonage
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Defines the properties used for {@link VideoClient#muteSession(String, MuteSessionRequest)}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MuteSessionRequest {
	private final boolean active;
	private final Collection<String> excludedStreamIds;

	/**
	 *
	 * @param active Whether to mute streams in the session (true) and enable the mute state of the session, or to
	 * disable the mute state of the session (false). With the mute state enabled (true), all current and future
	 * streams published to the session (except streams in "excludedStreamIds") are muted. If this is
	 * set to {@code false}, future streams published to the session are not muted (but any existing muted
	 * streams will remain muted).
	 *
	 * @param excludedStreamIds The stream IDs for streams that should not be muted. This is an optional property.
	 * If you omit this, all streams in the session will be muted. This only applies when the "active" property is set
	 * {@code true}. When the "active" property is set to {@code false}, it is ignored.
	 */
	public MuteSessionRequest(boolean active, Collection<String> excludedStreamIds) {
		this.active = active;
		this.excludedStreamIds = excludedStreamIds;
	}

	/**
	 *
	 * @param active Whether to mute streams in the session (true) and enable the mute state of the session, or to
	 * disable the mute state of the session (false). With the mute state enabled (true), all current and future
	 * streams published to the session (except streams in "excludedStreamIds") are muted. If this is
	 * set to {@code false}, future streams published to the session are not muted (but any existing muted
	 * streams will remain muted).
	 *
	 * @param excludedStreamIds The stream IDs for streams that should not be muted. This is an optional property.
	 * If you omit this, all streams in the session will be muted. This only applies when the "active" property is set
	 * {@code true}. When the "active" property is set to {@code false}, it is ignored.
	 */
	public MuteSessionRequest(boolean active, String... excludedStreamIds) {
		this(active, Arrays.asList(excludedStreamIds));
	}

	/**
	 * Whether to mute streams in the session (true) and enable the mute state of the session, or to disable
	 * the mute state of the session (false). With the mute state enabled (true), all current and future streams
	 * published to the session (except streams in the excludedStreamIds array) are muted. When you call this method
	 * with the active property set to false, future streams published to the session are not muted (but any
	 * existing muted streams remain muted).
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @return The elements in the excludedStreamIds array are stream IDs (strings) for the streams you wish to exclude from being muted.
	 */
	public Collection<String> getExcludedStreamIds() {
		return excludedStreamIds;
	}
	
	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this MuteSessionRequest object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}
}
