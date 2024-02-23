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

import com.vonage.client.common.HalFilterRequest;
import com.vonage.client.common.SortOrder;
import java.time.Instant;
import java.util.Map;

/**
 * Filters results for {@link ConversationsClient#listConversations(ListConversationsRequest)}.
 */
public class ListConversationsRequest extends HalFilterRequest {
	private final Instant startDate, endDate;

	ListConversationsRequest(Builder builder) {
		super(builder);
		startDate = builder.startDate;
		endDate = builder.endDate;
	}
	
	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = super.makeParams();
		if (startDate != null) {
            params.put("date_start", startDate.toString()
					.replace('T', ' ')
					.replace("Z", "")
			);
        }
		if (endDate != null) {
            params.put("date_end", endDate.toString()
					.replace('T', ' ')
					.replace("Z", "")
			);
        }
		return params;
	}

	/**
	 * Filter records that occurred after this point in time.
	 * 
	 * @return The start timestamp for results, or {@code null} if unspecified.
	 */
	public Instant getStartDate() {
		return startDate;
	}

	/**
	 * Filter records that occurred before this point in time.
	 * 
	 * @return The end timestamp for results, or {@code null} if unspecified.
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
	
	public static class Builder extends HalFilterRequest.Builder<ListConversationsRequest, Builder> {
		private Instant startDate, endDate;
	
		Builder() {}

		@Override
		public Builder pageSize(int pageSize) {
			return super.pageSize(pageSize);
		}

		@Override
		public Builder order(SortOrder order) {
			return super.order(order);
		}

		/**
		 * Filter records that occurred after this point in time.
		 *
		 * @param startDate The start timestamp for results, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder startDate(Instant startDate) {
			this.startDate = startDate;
			return this;
		}

		/**
		 * Filter records that occurred before this point in time.
		 *
		 * @param endDate The end timestamp for results, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder endDate(Instant endDate) {
			this.endDate = endDate;
			return this;
		}

		/**
		 * Builds the {@linkplain ListConversationsRequest}.
		 *
		 * @return An instance of ListConversationsRequest, populated with all fields from this builder.
		 */
		public ListConversationsRequest build() {
			return new ListConversationsRequest(this);
		}
	}
}
