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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoStream {
	private String streamId;
	private Boolean hasAudio, hasVideo;

	protected VideoStream() {}


	/**
	 * Gets the ID of the archive stream.
	 *
	 * @return The Stream ID.
	 */
	public String getStreamId() {
		return streamId;
	}

	/**
	 * Whether the recording has audio.
	 *
	 * @return {@code true} if the recording has sound.
	 */
	@JsonProperty("hasAudio")
	public Boolean hasAudio() {
		return hasAudio;
	}

	/**
	 * Whether the recording has video.
	 *
	 * @return {@code true} if the recording has video.
	 */
	@JsonProperty("hasVideo")
	public Boolean hasVideo() {
		return hasVideo;
	}
}
