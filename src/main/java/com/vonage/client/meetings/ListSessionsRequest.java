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
package com.vonage.client.meetings;

import com.vonage.client.QueryParamsRequest;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Filter parameters for listing all sessions with {@link MeetingsClient#listSessions(ListSessionsRequest)}.
 *
 * @since 7.10.0
 */
public class ListSessionsRequest implements QueryParamsRequest {
	private final Instant from, to;
	private final Boolean recorded;
	private final Integer offset, pageSize;

	ListSessionsRequest(Builder builder) {
		if ((from = builder.from) != null && from.isAfter(Instant.now())) {
			throw new IllegalArgumentException("'from' date cannot be in the future.");
		}
		if ((to = builder.to) != null) {
			if (to.isAfter(Instant.now())) {
				throw new IllegalArgumentException("'to' date cannot be in the future.");
			}
			else if (from != null && to.isBefore(from)) {
				throw new IllegalStateException("'to' date must be after 'from'.");
			}
		}
		recorded = builder.recorded;
		offset = builder.offset;
		pageSize = builder.pageSize;
	}
	
	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = new LinkedHashMap<>(8);
		if (from != null) {
            params.put("from", from.toString());
        }
		if (to != null) {
            params.put("to", to.toString());
        }
		if (recorded != null) {
            params.put("recorded", recorded.toString());
        }
		if (offset != null) {
            params.put("offset", offset.toString());
        }
		if (pageSize != null) {
            params.put("page_size", pageSize.toString());
        }
		return params;
	}

	/**
	 * Earliest date to return results from, in ISO-8601 format.
	 * 
	 * @return Start timestamp for the results, or {@code null} if absent.
	 */
	public Instant getFrom() {
		return from;
	}

	/**
	 * Latest date to return results from, in ISO-8601 format. Default is today's date.
	 * 
	 * @return End timestamp for the results, or {@code null} if absent.
	 */
	public Instant getTo() {
		return to;
	}

	/**
	 * If the value is set to true, only recorded sessions will be returned. If set to false, non-recorded sessions will be returned. If the value is not set, all sessions will be returned.
	 * 
	 * @return Whether to include only recorded or non-recorded sessions, or {@code null} to include all sessions.
	 */
	public Boolean getRecorded() {
		return recorded;
	}

	/**
	 * Specify how many records to skip before fetching.
	 * 
	 * @return The results offset, or {@code null} if unspecified.
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * Maximum number of items to fetch.
	 * 
	 * @return The results page size, or {@code null} if unspecified.
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private Instant from, to;
		private Boolean recorded;
		private Integer offset, pageSize;
	
		Builder() {}
	
		/**
		 * Earliest date to return results from, in ISO-8601 format.
		 *
		 * @param from Start timestamp for the results, or {@code null} if absent.
		 *
		 * @return This builder.
		 */
		public Builder from(Instant from) {
			this.from = from;
			return this;
		}

		/**
		 * Latest date to return results from, in ISO-8601 format. Default is today's date.
		 *
		 * @param to End timestamp for the results, or {@code null} if absent.
		 *
		 * @return This builder.
		 */
		public Builder to(Instant to) {
			this.to = to;
			return this;
		}

		/**
		 * If the value is set to true, only recorded sessions will be returned. If set to false, non-recorded
		 * sessions will be returned. If the value is not set, all sessions will be returned.
		 *
		 * @param recorded Whether to include only recorded or non-recorded sessions.
		 *
		 * @return This builder.
		 */
		public Builder recorded(boolean recorded) {
			this.recorded = recorded;
			return this;
		}

		/**
		 * Specify how many records to skip before fetching.
		 *
		 * @param offset The results offset, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder offset(int offset) {
			this.offset = offset;
			return this;
		}

		/**
		 * Maximum number of items to fetch.
		 *
		 * @param pageSize The results page size (maximum 1000).
		 *
		 * @return This builder.
		 */
		public Builder pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

	
		/**
		 * Builds the {@linkplain ListSessionsRequest}.
		 *
		 * @return An instance of ListSessionsRequest, populated with all fields from this builder.
		 */
		public ListSessionsRequest build() {
			return new ListSessionsRequest(this);
		}
	}

}
