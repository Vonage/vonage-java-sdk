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

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for the role parameter of the {@link TokenOptions.Builder#role(Role role)} method.
 */
public enum Role {

	/**
	 * A subscriber can only subscribe to streams.
	 */
	SUBSCRIBER,

	/**
	 * A publisher can publish streams, subscribe to streams, and signal. This is the default value
	 * if you do not set a role by calling the {@link TokenOptions.Builder#role(Role role)} method.
	 */
	PUBLISHER,

	/**
	 * In addition to the privileges granted to a publisher, a moderator can perform moderation functions, such as
	 * forcing clients to disconnect, to stop publishing streams, or to mute audio in published streams. See the
	 * <a href="https://tokbox.com/developer/guides/moderation/">Moderation developer guide</a>.
	 */
	MODERATOR,

	/**
	 * A publisher-only role can publish streams, but not signal.
	 *
	 * @since 8.5.0
	 */
	PUBLISHER_ONLY;

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase().replace("_", "");
	}
}
