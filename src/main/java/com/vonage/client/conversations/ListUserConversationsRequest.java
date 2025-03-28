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

import java.time.Instant;
import java.util.Map;

/**
 * Represents filter options for {@link ConversationsClient#listUserConversations(String, ListUserConversationsRequest)}.
 */
public final class ListUserConversationsRequest extends AbstractListUserRequest {
	private final MemberState state;
	private final OrderBy orderBy;
	private final Boolean includeCustomData;

	ListUserConversationsRequest(Builder builder) {
		super(builder);
		state = builder.state;
		orderBy = builder.orderBy;
		includeCustomData = builder.includeCustomData;
	}
	
	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = super.makeParams();
		conditionalAdd("state", state);
		conditionalAdd("order_by", orderBy);
		conditionalAdd("include_custom_data", includeCustomData);
		return params;
	}


	@Override
	public Instant getStartDate() {
		return startDate;
	}

	/**
	 * Only include conversations with this member state.
	 *
	 * @return The state to filter by, or {@code null} if not specified.
	 */
	public MemberState getState() {
		return state;
	}

	/**
	 * Determines how the results should be compared and ordered.
	 *
	 * @return The result ordering strategy, or {@code null} if not specified.
	 */
	public OrderBy getOrderBy() {
		return orderBy;
	}

	/**
	 * Whether to include custom data in the responses.
	 *
	 * @return {@code true} if custom data should be included, or {@code null} if not specified.
	 */
	public Boolean getIncludeCustomData() {
		return includeCustomData;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder extends AbstractConversationsFilterRequest.Builder<ListUserConversationsRequest, Builder> {
		private MemberState state;
		private OrderBy orderBy;
		private Boolean includeCustomData;
	
		Builder() {}

		@Override
		public Builder startDate(Instant startDate) {
			return super.startDate(startDate);
		}

		/**
		 * Only include conversations with this member state.
		 *
		 * @param state The state to filter by..
		 *
		 * @return This builder.
		 */
		public Builder state(MemberState state) {
			this.state = state;
			return this;
		}

		/**
		 * Determines how the results should be compared and ordered.
		 *
		 * @param orderBy The result ordering strategy.
		 *
		 * @return This builder.
		 */
		public Builder orderBy(OrderBy orderBy) {
			this.orderBy = orderBy;
			return this;
		}

		/**
		 * Whether to include custom data in the responses.
		 *
		 * @param includeCustomData {@code true} if custom data should be included.
		 *
		 * @return This builder.
		 */
		public Builder includeCustomData(Boolean includeCustomData) {
			this.includeCustomData = includeCustomData;
			return this;
		}

		/**
		 * Builds the {@linkplain ListUserConversationsRequest}.
		 *
		 * @return An instance of ListUserConversationsRequest, populated with all fields from this builder.
		 */
		public ListUserConversationsRequest build() {
			return new ListUserConversationsRequest(this);
		}
	}
}
