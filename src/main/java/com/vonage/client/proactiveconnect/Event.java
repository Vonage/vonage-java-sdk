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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents an event in the Proactive Connect API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
	private UUID id, actionId, invocationId, runItemId, runId, jobId;
	private String recipientId, sourceContext;
	private EventType type;
	private Instant occuredAt;
	@JsonProperty("data") private DataWrapper data;

	protected Event() {
	}

	/**
	 * Custom data as key-value pairs for this event.
	 *
	 * @return The event data as a Map, or {@code null} if absent.
	 */
	@JsonProperty("data")
	public DataWrapper getData() {
		return data;
	}

	@JsonGetter("data")
	private DataWrapper getDataRaw() {
		return data;
	}

	@JsonSetter("data")
	private void setDataRaw(DataWrapper data) {
		this.data = data;
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
	 * Identifier the item ID during a job run - this is the list item ID which is copied while taking the list snapshot.
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
	 * src_ctx field.
	 * @return The source context or {@code null} if unknown.
	 */
	@JsonProperty("src_ctx")
	public String getSourceContext() {
		return sourceContext;
	}

	/**
	 * Date & time the event occured in ISO 8601 format.
	 * @return The event timestamp or {@code null} if unknown.
	 */
	@JsonProperty("occured_at")
	public Instant getOccuredAt() {
		return occuredAt;
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
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, Event.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce Event from json.", ex);
		}
	}
}
