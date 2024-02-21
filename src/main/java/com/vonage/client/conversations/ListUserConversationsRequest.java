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
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Represents filter options for {@link ConversationsClient#listUserConversations(ListUserConversationsRequest)}.
 */
public class ListUserConversationsRequest extends HalFilterRequest {
	private final String userId;
	private final MemberState state;
	private final OrderBy orderBy;
	private final Boolean includeCustomData;
	private final Instant startDate;

	ListUserConversationsRequest(Builder builder) {
		super(builder);
		userId = Objects.requireNonNull(builder.userId);
		state = builder.state;
		orderBy = builder.orderBy;
		includeCustomData = builder.includeCustomData;
		startDate = builder.startDate;
	}
	
	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = super.makeParams();
		if (userId != null) {
            params.put("user_id", userId);
        }
		if (state != null) {
            params.put("state", state.toString());
        }
		if (orderBy != null) {
            params.put("order_by", orderBy.toString());
        }
		if (includeCustomData != null) {
            params.put("include_custom_data", includeCustomData.toString());
        }
		if (startDate != null) {
            params.put("date_start", startDate.toString());
        }
		return params;
	}

	
	public String getUserId() {
		return userId;
	}

	
	public MemberState getState() {
		return state;
	}

	
	public OrderBy getOrderBy() {
		return orderBy;
	}

	
	public Boolean getIncludeCustomData() {
		return includeCustomData;
	}

	
	public Instant getStartDate() {
		return startDate;
	}


	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder extends HalFilterRequest.Builder<ListUserConversationsRequest, Builder> {
		private String userId;
		private MemberState state;
		private OrderBy orderBy;
		private Boolean includeCustomData;
		private Instant startDate;
	
		Builder() {}
	
		/**
		 * 
		 *
		 * @param userId 
		 *
		 * @return This builder.
		 */
		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		/**
		 * 
		 *
		 * @param state 
		 *
		 * @return This builder.
		 */
		public Builder state(MemberState state) {
			this.state = state;
			return this;
		}

		/**
		 * 
		 *
		 * @param orderBy 
		 *
		 * @return This builder.
		 */
		public Builder orderBy(OrderBy orderBy) {
			this.orderBy = orderBy;
			return this;
		}

		/**
		 * 
		 *
		 * @param includeCustomData 
		 *
		 * @return This builder.
		 */
		public Builder includeCustomData(Boolean includeCustomData) {
			this.includeCustomData = includeCustomData;
			return this;
		}

		/**
		 * 
		 *
		 * @param startDate 
		 *
		 * @return This builder.
		 */
		public Builder startDate(Instant startDate) {
			this.startDate = startDate;
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
