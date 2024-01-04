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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used for inbound messages.
 *
 * @since 7.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Profile {
	private String name;

	Profile() {}

	/**
	 * The WhatsApp number's displayed profile name.
	 *
	 * @return The profile name.
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}
}
