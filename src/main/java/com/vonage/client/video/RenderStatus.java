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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the possible states in {@link RenderResponse#getStatus()}.
 *
 * @since 8.6.0
 */
public enum RenderStatus {

	/**
	 * The Vonage Video API platform is in the process of connecting to the remote application at the URL provided.
	 * This is the initial state.
	 */
	STARTING,

	/**
	 * The Vonage Video API platform has successfully connected to the remote application server, and is
	 * publishing the web view to a Vonage Video stream.
	 */
	STARTED,

	/**
	 * The Experience Composer has stopped.
	 */
	STOPPED,

	/**
	 * An error occurred and the Experience Composer could not proceed. It may occur at startup if the Vonage server
	 * cannot connect to the remote application server or republish the stream. It may also occur at any point during
	 * the process due to an error in the Vonage Video API platform.
	 */
	FAILED;

	@JsonCreator
	public static RenderStatus fromString(String status) {
		try {
			return RenderStatus.valueOf(status.toUpperCase());
		}
		catch (NullPointerException | IllegalArgumentException e) {
			return null;
		}
	}

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
