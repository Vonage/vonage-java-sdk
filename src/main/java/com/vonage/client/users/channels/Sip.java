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
package com.vonage.client.users.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.net.URI;
import java.util.Objects;

/**
 * Represents a Session Initiation Protocol (SIP) channel.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, visible = true)
public class Sip extends Channel {
	private URI uri;
	private String username, password;

	protected Sip() {}

	public Sip(String uri) {
		Objects.requireNonNull(uri, "SIP URI is required");
		if (!(uri.startsWith("sip:") || uri.startsWith("sips:"))) {
			throw new IllegalArgumentException("Invalid protocol for SIP URI.");
		}
		this.uri = URI.create(uri);
	}

	public Sip(String uri, String username, String password) {
		this(uri);
		this.username = username;
		if ((this.password = password) != null && username == null) {
			throw new IllegalArgumentException("Username should be provided along with password.");
		}
	}

	/**
	 * Full SIP URI, including number, domain and (optionally) whether TLS is used.
	 *
	 * @return The SIP URI, or {@code null} if not specified.
	 */
	@JsonProperty("uri")
	public URI getUri() {
		return uri;
	}

	/**
	 * SIP username.
	 *
	 * @return The username, or {@code null} if not specified.
	 */
	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	/**
	 * SIP user password.
	 *
	 * @return The password, or {@code null} if not specified.
	 */
	@JsonProperty("password")
	public String getPassword() {
		return password;
	}
}
