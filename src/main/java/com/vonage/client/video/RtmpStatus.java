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
 * Represents the status of an RTMP stream.
 *
 * @see Rtmp#getStatus()
 */
public enum RtmpStatus {

	/**
	 * The Vonage video platform is in the process of connecting to the remote RTMP server.
	 * This is the initial state, and it is the status if you start when there are no streams published in the session.
	 * It changes to {@linkplain #LIVE} when there are streams (or it changes to one of the other states).
	 */
	CONNECTING,

	/**
	 * The Vonage video platform has successfully connected to the remote RTMP server, and the media is streaming.
	 */
	LIVE,

	/**
	 * The Vonage video platform could not connect to the remote RTMP server. This is due to an unreachable server
	 * or an error in the RTMP handshake. Causes include rejected RTMP connections, non-existing RTMP applications,
	 * rejected stream names, authentication errors, etc. Check that the server is online, and that you have
	 * provided the correct server URL and stream name.
	 */
	OFFLINE,

	/**
	 * There is an error in the Vonage video platform.
	 */
	ERROR;

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}

	@JsonCreator
	public static RtmpStatus fromString(String value) {
		try {
			return RtmpStatus.valueOf(value.toUpperCase());
		}
		catch (NullPointerException | IllegalArgumentException ex) {
			return null;
		}
	}
}
