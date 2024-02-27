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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.time.Instant;

/**
 * Represents the timestamps in {@link Member#getTimestamp()}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberTimestamp extends JsonableBaseObject {
	private Instant invited;
	private Instant joined;
	private Instant left;

	protected MemberTimestamp() {}

	protected MemberTimestamp(Instant invited, Instant joined, Instant left) {
		this.invited = invited;
		this.joined = joined;
		this.left = left;
	}

	/**
	 * Time that the member was invited.
	 * 
	 * @return The member invitation time as an Instant, or {@code null} if unknown.
	 */
	@JsonProperty("invited")
	public Instant getInvited() {
		return invited;
	}

	/**
	 * Time that the member joined.
	 * 
	 * @return The member join time as an Instant, or {@code null} if unknown.
	 */
	@JsonProperty("joined")
	public Instant getJoined() {
		return joined;
	}

	/**
	 * Time that the member left.
	 * 
	 * @return The member leave time as an Instant, or {@code null} if unknown.
	 */
	@JsonProperty("left")
	public Instant getLeft() {
		return left;
	}
}
