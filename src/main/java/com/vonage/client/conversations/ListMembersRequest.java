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

/**
 * Represents filter options for {@link ConversationsClient#listMembers(ListMembersRequest)}.
 */
public final class ListMembersRequest extends HalFilterRequest {
	String conversationId;

	ListMembersRequest(Builder builder) {
		super(builder);
	}

	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder extends HalFilterRequest.Builder<ListMembersRequest, Builder> {
		Builder() {}
	
		/**
		 * Builds the {@linkplain ListMembersRequest}.
		 *
		 * @return An instance of ListMembersRequest, populated with all fields from this builder.
		 */
		public ListMembersRequest build() {
			return new ListMembersRequest(this);
		}
	}
}
