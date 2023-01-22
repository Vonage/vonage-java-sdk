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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomLinks {
	UrlContainer hostUrl, guestUrl;

	protected RoomLinks() {
	}

	/**
	 * @return The host URL.
	 */
	@JsonProperty("host_url")
	public UrlContainer getHostUrl() {
		return hostUrl;
	}

	/**
	 * @return The guest URL.
	 */
	@JsonProperty("guest_url")
	public UrlContainer getGuestUrl() {
		return guestUrl;
	}
}
