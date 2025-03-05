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
import com.vonage.client.JsonableBaseObject;

/**
 * Represents a {@linkplain EventType#RTC_ANSWER} event.
 *
 * @since 8.19.0
 */
public final class RtcAnswerEvent extends EventWithBody<RtcAnswerEvent.Body> {
	RtcAnswerEvent() {}

	static class Body extends JsonableBaseObject {
		@JsonProperty("answer") String answer;
		@JsonProperty("rtc_id") String rtcId;
		@JsonProperty("session_destination") String sessionDestination;
		@JsonProperty("isFromMb") Boolean isFromMb;
	}

	/**
	 * The {@code answer} field in the event response body.
	 *
	 * @return The answer as a string, or {@code null} if absent.
	 */
	@JsonIgnore
	public String getAnswer() {
		return body != null ? body.answer : null;
	}

	/**
	 * The {@code rtc_id} field in the event response body.
	 *
	 * @return The RTC ID as a string, or {@code null} if absent.
	 */
	@JsonIgnore
	public String getRtcId() {
		return body != null ? body.rtcId : null;
	}

	/**
	 * The {@code session_destination} field in the event response body.
	 *
	 * @return The session destination as a string, or {@code null} if absent.
	 */
	@JsonIgnore
	public String getSessionDestination() {
		return body != null ? body.sessionDestination : null;
	}

	/**
	 * The {@code isFromMb} field in the event response body.
	 *
	 * @return The isFromMb as a boolean, or {@code null} if absent.
	 */
	@JsonIgnore
	public Boolean getIsFromMb() {
		return body != null ? body.isFromMb : null;
	}
}