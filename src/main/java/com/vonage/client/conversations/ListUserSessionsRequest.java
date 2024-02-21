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
import java.util.Map;
import java.util.Objects;

/**
 * Filters results for {@link ConversationsClient#listUserSessions(ListUserSessionsRequest)}.
 */
public class ListUserSessionsRequest extends HalFilterRequest {
	private final String userId;

	ListUserSessionsRequest(Builder builder) {
		super(builder);
		userId = Objects.requireNonNull(builder.userId);
	}
	
	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = super.makeParams();
		if (userId != null) {
            params.put("user_id", userId);
        }
		return params;
	}

	
	public String getUserId() {
		return userId;
	}


	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder extends HalFilterRequest.Builder<ListUserSessionsRequest, Builder> {
		private String userId;
	
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
		 * Builds the {@linkplain ListUserSessionsRequest}.
		 *
		 * @return An instance of ListUserSessionsRequest, populated with all fields from this builder.
		 */
		public ListUserSessionsRequest build() {
			return new ListUserSessionsRequest(this);
		}
	}
}
