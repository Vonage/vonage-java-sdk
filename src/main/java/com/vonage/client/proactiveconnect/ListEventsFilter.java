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
package com.vonage.client.proactiveconnect;

import com.vonage.client.QueryParamsRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Filter options for finding events using {@link ProactiveConnectClient#listEvents(ListEventsFilter)}.
 */
public class ListEventsFilter implements QueryParamsRequest {
	private final SortOrder order;
	private final UUID runId, runItemId, invocationId, actionId, traceId;
	private final String recipientId, sourceContext;
	private final SourceType sourceType;
	private final Instant startDate, endDate;

	ListEventsFilter(Builder builder) {
		order = builder.order;
		runId = builder.runId != null ? UUID.fromString(builder.runId) : null;
		runItemId = builder.runItemId != null ? UUID.fromString(builder.runItemId) : null;
		invocationId = builder.invocationId != null ? UUID.fromString(builder.invocationId) : null;
		actionId = builder.actionId != null ? UUID.fromString(builder.actionId) : null;
		traceId = builder.traceId != null ? UUID.fromString(builder.traceId) : null;
		recipientId = builder.recipientId;
		sourceContext = builder.sourceContext;
		sourceType = builder.sourceType;
		startDate = builder.startDate;
		if ((endDate = builder.endDate) != null && startDate != null && startDate.isAfter(endDate)) {
			throw new IllegalStateException("Start date cannot be later than end date.");
		}
	}

	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = new LinkedHashMap<>();
		params.put("page", "1");
		params.put("page_size", "1000");
		if (order != null) {
			params.put("order", order.toString());
		}
		if (runId != null) {
			params.put("run_id", runId.toString());
		}
		if (runItemId != null) {
			params.put("run_item_id", runItemId.toString());
		}
		if (invocationId != null) {
			params.put("invocation_id", invocationId.toString());
		}
		if (actionId != null) {
			params.put("action_id", actionId.toString());
		}
		if (traceId != null) {
			params.put("trace_id", traceId.toString());
		}
		if (recipientId != null) {
			params.put("recipient_id", recipientId);
		}
		if (sourceContext != null) {
			params.put("src_ctx", sourceContext);
		}
		if (sourceType != null) {
			params.put("src_type", sourceType.toString());
		}
		if (startDate != null) {
			params.put("date_start", startDate.truncatedTo(ChronoUnit.SECONDS).toString());
		}
		if (endDate != null) {
			params.put("date_end", endDate.truncatedTo(ChronoUnit.SECONDS).toString());
		}
		return params;
	}

	/**
	 * Sort in either ascending or descending order.
	 *
	 * @return The sort order as an enum, or {@code null} if unspecified.
	 */
	public SortOrder getOrder() {
		return order;
	}

	/**
	 * Run ID to filter by.
	 *
	 * @return The run ID, or {@code null} if unspecified.
	 */
	public UUID getRunId() {
		return runId;
	}

	/**
	 * Run item ID to filter by.
	 *
	 * @return The run item ID, or {@code null} if unspecified.
	 */
	public UUID getRunItemId() {
		return runItemId;
	}

	/**
	 * Invocation ID to filter by.
	 *
	 * @return The invocation ID, or {@code null} if unspecified.
	 */
	public UUID getInvocationId() {
		return invocationId;
	}

	/**
	 * Action ID to filter by.
	 *
	 * @return The action ID, or {@code null} if unspecified.
	 */
	public UUID getActionId() {
		return actionId;
	}

	/**
	 * Trace ID to filter by.
	 *
	 * @return The trace ID, or {@code null} if unspecified.
	 */
	public UUID getTraceId() {
		return traceId;
	}

	/**
	 * Recipient ID to filter by.
	 *
	 * @return The recipient ID, or {@code null} if unspecified.
	 */
	public String getRecipientId() {
		return recipientId;
	}

	/**
	 * The name of the segment / matcher the item / event to filter by (exact string).
	 *
	 * @return The source context string, or {@code null} if unspecified.
	 */
	public String getSourceContext() {
		return sourceContext;
	}

	/**
	 * Source type to filter by.
	 *
	 * @return The source type as an enum, or {@code null} if unspecified.
	 */
	public SourceType getSourceType() {
		return sourceType;
	}

	/**
	 * ISO-8601 formatted date for when to begin events filter.
	 *
	 * @return The start date for events as an Instant, or {@code null} if unspecified.
	 */
	public Instant getStartDate() {
		return startDate;
	}

	/**
	 * ISO-8601 formatted date for when to end events filter.
	 *
	 * @return The end date for events as an Instant, or {@code null} if unspecified.
	 */
	public Instant getEndDate() {
		return endDate;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for specifying options to filter events by. All fields are optional.
	 */
	public static class Builder {
		private SortOrder order;
		private String runId, runItemId, invocationId, actionId, traceId, recipientId, sourceContext;
		private SourceType sourceType;
		private Instant startDate, endDate;

		Builder() {}

		/**
		 * Sort in either ascending or descending order.
		 *
		 * @param order The sort order as an enum.
		 *
		 * @return This builder.
		 */
		public Builder order(SortOrder order) {
			this.order = order;
			return this;
		}

		/**
		 * Run ID to filter by.
		 *
		 * @param runId The run ID.
		 *
		 * @return This builder.
		 */
		public Builder runId(String runId) {
			this.runId = runId;
			return this;
		}

		/**
		 * Run item ID to filter by.
		 *
		 * @param runItemId The run item ID.
		 *
		 * @return This builder.
		 */
		public Builder runItemId(String runItemId) {
			this.runItemId = runItemId;
			return this;
		}

		/**
		 * Invocation ID to filter by.
		 *
		 * @param invocationId The invocation ID.
		 *
		 * @return This builder.
		 */
		public Builder invocationId(String invocationId) {
			this.invocationId = invocationId;
			return this;
		}

		/**
		 * Action ID to filter by.
		 *
		 * @param actionId The action ID.
		 *
		 * @return This builder.
		 */
		public Builder actionId(String actionId) {
			this.actionId = actionId;
			return this;
		}

		/**
		 * Trace ID to filter by.
		 *
		 * @param traceId The trace ID.
		 *
		 * @return This builder.
		 */
		public Builder traceId(String traceId) {
			this.traceId = traceId;
			return this;
		}

		/**
		 * Recipient ID to filter by.
		 *
		 * @param recipientId The recipient ID.
		 *
		 * @return This builder.
		 */
		public Builder recipientId(String recipientId) {
			this.recipientId = recipientId;
			return this;
		}

		/**
		 * The name of the segment / matcher the item / event to filter by (exact string).
		 *
		 * @param sourceContext The source context string.
		 *
		 * @return This builder.
		 */
		public Builder sourceContext(String sourceContext) {
			this.sourceContext = sourceContext;
			return this;
		}

		/**
		 * Source type to filter by.
		 *
		 * @param sourceType The source type as an enum.
		 *
		 * @return This builder.
		 */
		public Builder sourceType(SourceType sourceType) {
			this.sourceType = sourceType;
			return this;
		}

		/**
		 * ISO-8601 formatted date for when to begin events filter.
		 *
		 * @param startDate The start date for events as an Instant.
		 *
		 * @return This builder.
		 */
		public Builder startDate(Instant startDate) {
			this.startDate = startDate;
			return this;
		}

		/**
		 * ISO-8601 formatted date for when to end events filter.
		 *
		 * @param endDate The end date for events as an Instant.
		 *
		 * @return This builder.
		 */
		public Builder endDate(Instant endDate) {
			this.endDate = endDate;
			return this;
		}

		/**
		 * Builds the {@linkplain ListEventsFilter}.
		 *
		 * @return An instance of ListEventsFilter, populated with all fields from this builder.
		 */
		public ListEventsFilter build() {
			return new ListEventsFilter(this);
		}
	}
}
