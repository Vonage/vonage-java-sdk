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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Webhook for verification status updates and events. See the
 * <a href=https://developer.vonage.com/en/api/verify.v2#websockets>API reference</a> for details.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationCallback {
	protected Channel channel;
	protected UUID requestId;
	protected Instant triggeredAt, finalizedAt, submittedAt;
	protected VerificationStatus status;
	protected CallbackType type;
	protected String clientRef;
	protected Integer channelTimeout;
	protected Double price;
	protected List<WorkflowStatus> workflows;
	@JsonProperty("action") List<Action> actions;

	@JsonIgnoreProperties(ignoreUnknown = true)
	static class Action {
		@JsonProperty("type") String type;
		@JsonProperty("check_url") URI checkUrl;
	}

	protected VerificationCallback() {
	}

	/**
	 * If {@linkplain #getType()} is {@linkplain CallbackType#EVENT}, this will return
	 * the contact channel of the event update, {@code null} otherwise.
	 *
	 * @return The communication channel, or {@code null} if not applicable.
	 */
	@JsonProperty("channel")
	public Channel getChannel() {
		return channel;
	}

	/**
	 * The ID of the request.
	 *
	 * @return The verification request ID.
	 */
	@JsonProperty("request_id")
	public UUID getRequestId() {
		return requestId;
	}

	/**
	 * If {@linkplain #getType()} is {@linkplain CallbackType#EVENT}, this will return
	 * the date-time the verification request was triggered in ISO 8601 format, {@code null} otherwise.
	 *
	 * @return The verification request's start timestamp, or {@code null} if not applicable.
	 */
	@JsonProperty("triggered_at")
	public Instant getTriggeredAt() {
		return triggeredAt;
	}

	/**
	 * The date and time the verification request was completed in ISO 8601 format
	 * or {@code null} if this callback is not an event update.
	 *
	 * @return The request completion timestamp, or {@code null} if not applicable.
	 */
	@JsonProperty("finalized_at")
	public Instant getFinalizedAt() {
		return finalizedAt;
	}

	/**
	 * If {@linkplain #getType()} is {@linkplain CallbackType#SUMMARY}, this will return the
	 * date-time the verification request was submitted in ISO 8601 format, {@code null} otherwise.
	 *
	 * @return The verification request's start timestamp, or {@code null} if not applicable.
	 */
	@JsonProperty("submitted_at")
	public Instant getSubmittedAt() {
		return submittedAt;
	}

	/**
	 * Current status of this request.
	 *
	 * @return The request status as an enum.
	 */
	@JsonProperty("status")
	public VerificationStatus getStatus() {
		return status;
	}

	/**
	 * Type of response.
	 *
	 * @return The event type as an enum.
	 */
	@JsonProperty("type")
	public CallbackType getType() {
		return type;
	}

	/**
	 * Contains the client reference given in the original Verify request if one was provided.
	 *
	 * @return The client reference, or {@code null} if not available / applicable.
	 */
	@JsonProperty("client_ref")
	public String getClientRef() {
		return clientRef;
	}

	/**
	 * If {@linkplain #getType()} is {@linkplain CallbackType#SUMMARY}, this will return the number of
	 * seconds before the current step in the verification request times out, {@code null} otherwise.
	 *
	 * @return The verification request timeout, or {@code null} if not applicable.
	 */
	@JsonProperty("channel_timeout")
	public Integer getChannelTimeout() {
		return channelTimeout;
	}

	/**
	 * If {@linkplain #getType()} is {@linkplain CallbackType#SUMMARY}, this will return the total cost
	 * for this request so far in Euros (this may differ as the request progresses), {@code null} otherwise.
	 *
	 * @return The total cost of the verification request in EUR, or {@code null} if not applicable.
	 */
	@JsonProperty("price")
	public Double getPrice() {
		return price;
	}

	/**
	 * If {@linkplain #getType()} is {@linkplain CallbackType#SUMMARY}, this will return metadata of the workflow
	 * steps in the order they were declared / executed along with their status, {@code null} otherwise.
	 *
	 * @return The workflow status updates, or {@code null} if not applicable.
	 */
	@JsonProperty("workflow")
	public List<WorkflowStatus> getWorkflows() {
		return workflows;
	}

	/**
	 * If {@linkplain #getChannel()} is {@linkplain Channel#SILENT_AUTH}, this will return
	 * the URL for Silent Auth Verify workflow completion, {@code null} otherwise.
	 *
	 * @return The Silent Authentication URL to check, or {@code null} if not applicable.
	 */
	@JsonIgnore
	public URI getSilentAuthUrl() {
		if (actions == null || actions.isEmpty() || channel != Channel.SILENT_AUTH) {
			return null;
		}
		return actions.get(0).checkUrl;
	}

	/**
	 * Constructs an instance of this class from a JSON payload.
	 *
	 * @param json The webhook response JSON string.
	 *
	 * @return The deserialized webhook response object.
	 * @throws VonageUnexpectedException If the response could not be deserialized.
	 */
	public static VerificationCallback fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.readValue(json, VerificationCallback.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce VerificationCallback from json.", ex);
		}
	}
}
