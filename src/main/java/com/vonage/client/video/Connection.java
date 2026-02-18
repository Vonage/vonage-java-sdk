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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.UUID;

/**
 * Represents a connection in a Vonage Video session.
 */
public class Connection extends JsonableBaseObject {
	private UUID connectionId;
	private ConnectionState connectionState;
	private Long createdAt;

	protected Connection() {
	}

	/**
	 * @return The connection ID.
	 */
	@JsonProperty("connectionId")
	public UUID getConnectionId() {
		return connectionId;
	}

	/**
	 * @return The state of the connection.
	 */
	@JsonProperty("connectionState")
	public ConnectionState getConnectionState() {
		return connectionState;
	}

	/**
	 * @return The timestamp for when the connection was created, expressed in milliseconds
	 * since the Unix epoch (January 1, 1970, 00:00:00 UTC).
	 */
	@JsonProperty("createdAt")
	public Long getCreatedAt() {
		return createdAt;
	}
}
