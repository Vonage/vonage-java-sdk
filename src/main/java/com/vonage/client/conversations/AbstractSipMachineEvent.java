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
 * Base class for SIP machine detection events.
 *
 * @since 8.19.0
 */
abstract class AbstractSipMachineEvent extends AbstractChannelEvent<AbstractSipMachineEvent.Body> {
	AbstractSipMachineEvent() {}

	/**
	 * The main body container for SIP machine detection events.
	 */
	static class Body extends AbstractChannelEvent.Body {
		@JsonProperty("type") String type;
		@JsonProperty("confidence") Integer confidence;
	}

	/**
	 * The {@code type} field in the body.
	 *
	 * @return The machine detection type as a string, or {@code null} if absent.
	 */
	@JsonIgnore
	public String getBodyType() {
		return body != null ? body.type : null;
	}

	/**
	 * The {@code confidence} field in the body.
	 *
	 * @return The machine detection confidence as an integer, or {@code null} if absent.
	 */
	@JsonIgnore
	public Integer getConfidence() {
		return body != null ? body.confidence : null;
	}
}