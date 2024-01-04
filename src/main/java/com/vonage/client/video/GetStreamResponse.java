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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;

/**
 * Represents details of a stream, as returned from {@link VideoClient#getStream(String, String)}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetStreamResponse extends SessionStream {
	private VideoType videoType;
	private String name;

	protected GetStreamResponse() {
	}

	/**
	 * @return The video source for the stream.
	 */
	@JsonProperty("videoType")
	public VideoType getVideoType() {
		return videoType;
	}

	/**
	 * @return The name of the stream (if one was set when the client published the stream).
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static GetStreamResponse fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
