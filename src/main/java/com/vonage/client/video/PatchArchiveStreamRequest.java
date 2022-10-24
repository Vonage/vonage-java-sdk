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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
class PatchArchiveStreamRequest {
	@JsonIgnore String archiveId;
	private String addStream, removeStream;
	private Boolean hasAudio, hasVideo;

	PatchArchiveStreamRequest(String removeStream) {
		this.removeStream = removeStream;
	}

	PatchArchiveStreamRequest(String addStream, Boolean audio, Boolean video) {
		this.addStream = addStream;
		this.hasAudio = audio;
		this.hasVideo = video;
	}

	/**
	 * Stream ID to remove from the archive.
	 */
	public String getRemoveStream() {
		return removeStream;
	}

	/**
	 * Stream ID to add to the archive.
	 */
	public String getAddStream() {
		return addStream;
	}

	/**
	 * Whether the composed archive should include the stream's audio (true by default).
	 */
	@JsonProperty("hasAudio")
	public Boolean hasAudio() {
		return hasAudio;
	}

	/**
	 * Whether the composed archive should include the stream's video (true by default).
	 */
	@JsonProperty("hasVideo")
	public Boolean hasVideo() {
		return hasVideo;
	}
	
	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this PatchArchiveStreamsRequest object.
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
