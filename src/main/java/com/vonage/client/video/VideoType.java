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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the video types returned in {@link GetStreamResponse#getVideoType()}.
 */
public enum VideoType {
	/**
	 * Uses the camera as the video source.
	 */
	CAMERA,

	/**
	 * Uses screen sharing on the publisher as the video source.
	 */
	SCREEN,

	/**
	 * Published by a web client using an HTML VideoTrack element as the video source.
	 */
	CUSTOM;

	@JsonCreator
	public static VideoType fromString(String value) {
		try {
			return valueOf(value.toUpperCase());
		}
		catch (NullPointerException | IllegalArgumentException ex) {
			return null;
		}
	}

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
