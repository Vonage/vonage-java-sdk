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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a {@linkplain EventType#RTC_TRANSFER} event.
 *
 * @since 8.19.0
 */
public final class RtcTransferEvent extends AbstractChannelEvent<RtcTransferEvent.Body> {
	RtcTransferEvent() {}

	static class Body extends AbstractChannelEvent.Body {
		@JsonProperty("was_member") String wasMember;
		@JsonProperty("user_id") String userId;
		@JsonProperty("transferred_from") String transferredFrom;
	}

	/**
	 * The {@code was_member} field in the event response body.
	 *
	 * @return The was member as a string, or {@code null} if absent.
	 */
	@JsonIgnore
	public String getWasMember() {
		return body != null ? body.wasMember : null;
	}

	/**
	 * The {@code user_id} field in the event response body.
	 *
	 * @return The user ID as a string, or {@code null} if absent.
	 */
	@JsonIgnore
	public String getUserId() {
		return body != null ? body.userId : null;
	}

	/**
	 * The {@code transferred_from} field in the event response body.
	 *
	 * @return The transferred from as a string, or {@code null} if absent.
	 */
	@JsonIgnore
	public String getTransferredFrom() {
		return body != null ? body.transferredFrom : null;
	}
}