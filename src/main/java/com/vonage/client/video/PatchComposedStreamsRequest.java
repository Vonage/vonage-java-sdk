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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
class PatchComposedStreamsRequest implements Jsonable {
	@JsonIgnore String id;
	private String addStream, removeStream;
	private Boolean hasAudio, hasVideo;

	PatchComposedStreamsRequest(String removeStream) {
		this.removeStream = removeStream;
	}

	PatchComposedStreamsRequest(String addStream, Boolean audio, Boolean video) {
		this.addStream = addStream;
		this.hasAudio = audio;
		this.hasVideo = video;
	}

	/**
	 * @return Stream ID to remove from the composition.
	 */
	@JsonProperty("removeStream")
	public String getRemoveStream() {
		return removeStream;
	}

	/**
	 * @return Stream ID to add to the composition.
	 */
	@JsonProperty("addStream")
	public String getAddStream() {
		return addStream;
	}

	/**
	 * @return Whether the composition should include the stream's audio (true by default).
	 */
	@JsonProperty("hasAudio")
	public Boolean hasAudio() {
		return hasAudio;
	}

	/**
	 * @return Whether the composition should include the stream's video (true by default).
	 */
	@JsonProperty("hasVideo")
	public Boolean hasVideo() {
		return hasVideo;
	}
}
