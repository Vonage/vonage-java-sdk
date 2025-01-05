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

/**
 * Represents a Facebook Messenger channel.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, visible = true)
public class Messenger extends Channel {
	private String id;

	protected Messenger() {}

	/**
	 * Creates a new Messenger channel.
	 *
	 * @param id The Facebook Messenger user ID.
	 */
	public Messenger(String id) {
		this.id = id;
	}

	/**
	 * Facebook Messenger ID.
	 * 
	 * @return The Facebook user ID.
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}
}
