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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.time.Instant;

/**
 * Describes the status of a workflow.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowStatus extends JsonableBaseObject {
	protected Channel channel;
	protected Instant initiatedAt;
	protected VerificationStatus status;

	protected WorkflowStatus() {
	}

	/**
	 * The workflow type.
	 *
	 * @return The channel enum.
	 */
	@JsonProperty("channel")
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Date and time this workflow was initiated.
	 *
	 * @return The initiation timestamp.
	 */
	@JsonProperty("initiated_at")
	public Instant getInitiatedAt() {
		return initiatedAt;
	}

	/**
	 * State of the verification workflow.
	 *
	 * @return The status enum.
	 */
	@JsonProperty("status")
	public VerificationStatus getStatus() {
		return status;
	}
}
