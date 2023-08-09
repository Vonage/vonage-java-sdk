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
package com.vonage.client.users.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Represents a Facebook Messenger channel.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Messenger messenger = (Messenger) o;
		return Objects.equals(id, messenger.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
