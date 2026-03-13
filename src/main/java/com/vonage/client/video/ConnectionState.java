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
import com.vonage.client.Jsonable;

/**
 * Represents the state of a connection in a Vonage Video session.
 */
public enum ConnectionState {
	/**
	 * The connection is in the process of connecting to the session.
	 */
	CONNECTING,
	
	/**
	 * The connection is established and actively connected to the session.
	 */
	CONNECTED,

	/**
	 * The connection has been disconnected from the session, either by the client or by the server.
	 */
	DISCONNECTED;

	/**
	 * Convert a string to a ConnectionState enum.
	 *
	 * @param value The string to convert.
	 *
	 * @return The ConnectionState as an enum, or {@code null} if invalid.
	 */
	@JsonCreator
	public static ConnectionState fromString(String value) {
		return Jsonable.fromString(value, ConnectionState.class);
	}

	@JsonValue
	@Override
	public String toString() {
		return name().charAt(0) + name().substring(1).toLowerCase();
	}
}
