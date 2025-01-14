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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Represents an event in the Proactive Connect API.
 */
public class Event extends JsonableBaseObject {
	private UUID id, actionId, invocationId, runItemId, runId, jobId;
	private String recipientId, sourceContext;
	private EventType type;
	private Instant occurredAt;
	private Map<String, ?> data;

	protected Event() {
	}

	/**
	 * Custom data as key-value pairs for this event.
	 *
	 * @return The event data as a Map, or {@code null} if absent.
	 */
	@JsonProperty("data")
	public Map<String, ?> getData() {
		return data;
	}

	/**
	 * Unique identifier for this item.
	 *
	 * @return The item ID or {@code null} if unknown.
	 */
	@JsonProperty("id")
	public UUID getId() {
		return id;
	}

	/**
	 * Unique identifier for the job.
	 *
	 * @return The job ID or {@code null} if unknown.
	 */
	@JsonProperty("job_id")
	public UUID getJobId() {
		return jobId;
	}

	/**
	 * Unique identifier for the run.
	 *
	 * @return The run ID or {@code null} if unknown.
	 */
	@JsonProperty("run_id")
	public UUID getRunId() {
		return runId;
	}

	/**
	 * Identifier for the item ID during a job run - this is the list item ID which is copied
	 * while taking the list snapshot.
	 *
	 * @return The run's item ID or {@code null} if unknown.
	 */
	@JsonProperty("run_item_id")
	public UUID getRunItemId() {
		return runItemId;
	}

	/**
	 * Unique identifier for the action.
	 *
	 * @return The action ID or {@code null} if unknown.
	 */
	@JsonProperty("action_id")
	public UUID getActionId() {
		return actionId;
	}

	/**
	 * Unique identifier for the action invocation.
	 *
	 * @return The action invocation ID or {@code null} if unknown.
	 */
	@JsonProperty("invocation_id")
	public UUID getInvocationId() {
		return invocationId;
	}

	/**
	 * String identifier of a recipient, for example their email, phone number etc.
	 *
	 * @return The recipient ID or {@code null} if unknown.
	 */
	@JsonProperty("recipient_id")
	public String getRecipientId() {
		return recipientId;
	}

	/**
	 * The name of the segment or matcher.
	 *
	 * @return The source context or {@code null} if unknown.
	 */
	@JsonProperty("src_ctx")
	public String getSourceContext() {
		return sourceContext;
	}

	/**
	 * Date and time the event occurred in ISO 8601 format.
	 *
	 * @return The event timestamp or {@code null} if unknown.
	 */
	@JsonProperty("occurred_at")
	public Instant getOccurredAt() {
		return occurredAt;
	}

	/**
	 * Classification of the event.
	 *
	 * @return The event type as an enum.
	 */
	@JsonProperty("type")
	public EventType getType() {
		return type;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static Event fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
