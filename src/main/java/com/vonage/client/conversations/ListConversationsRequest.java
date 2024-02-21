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

/**
 * Filters results for {@link ConversationsClient#listConversations(ListConversationsRequest)}.
 */
public class ListConversationsRequest extends HalFilterRequest {

	ListConversationsRequest(Builder builder) {
		super(builder);
	}
	
	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = super.makeParams();
		return params;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder extends HalFilterRequest.Builder<ListConversationsRequest, Builder> {
	
		Builder() {}
	
	
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
