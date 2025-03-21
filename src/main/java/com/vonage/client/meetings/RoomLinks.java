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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.common.UrlContainer;
import java.net.URI;

public class RoomLinks extends JsonableBaseObject {
	@JsonProperty("host_url") UrlContainer hostUrl;
	@JsonProperty("guest_url") UrlContainer guestUrl;

	protected RoomLinks() {
	}

	/**
	 * @return The host URL.
	 */
	public URI getHostUrl() {
		return hostUrl != null ? hostUrl.getHref() : null;
	}

	/**
	 * @return The guest URL.
	 */
	public URI getGuestUrl() {
		return guestUrl != null ? guestUrl.getHref() : null;
	}
}
