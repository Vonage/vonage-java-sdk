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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.voice.CallDirection;

/**
 * Base class for Hangup events.
 *
 * @since 8.20.0
 */
abstract class AbstractHangupEvent extends AbstractChannelEvent<AbstractHangupEvent.Body> {
	AbstractHangupEvent() {}

	static class Body extends AbstractChannelEvent.Body {
		@JsonProperty("direction") CallDirection direction;
		@JsonProperty("quality") HangupQuality quality;
		@JsonProperty("bandwidth") HangupBandwidth bandwidth;
		@JsonProperty("reason") HangupReason reason;
	}

	/**
	 * Gets the direction of the event.
	 *
	 * @return The direction as an enum.
	 */
	@JsonProperty("direction")
	public CallDirection getDirection() {
		return body != null ? body.direction : null;
	}

	/**
	 * Gets the quality metrics of the event.
	 *
	 * @return The quality metrics.
	 */
	@JsonProperty("quality")
	public HangupQuality getQuality() {
		return body != null ? body.quality : null;
	}

	/**
	 * Gets the bandwidth metrics of the event.
	 *
	 * @return The bandwidth metrics.
	 */
	@JsonProperty("bandwidth")
	public HangupBandwidth getBandwidth() {
		return body != null ? body.bandwidth : null;
	}

	/**
	 * Gets the reason for the event.
	 *
	 * @return The reason.
	 */
	@JsonProperty("reason")
	public HangupReason getReason() {
		return body != null ? body.reason : null;
	}
}