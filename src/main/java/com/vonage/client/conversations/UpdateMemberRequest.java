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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vonage.client.Jsonable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Options for updating a membership using {@link ConversationsClient#updateMember(UpdateMemberRequest)}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UpdateMemberRequest extends ConversationResourceRequestWrapper implements Jsonable {
	private final MemberState state;
	private final String from;

	UpdateMemberRequest(Builder builder) {
		super(builder.conversationId, builder.memberId);
		state = builder.state;
		from = builder.from;
	}
	
	@JsonProperty("state")
	public MemberState getState() {
		return state;
	}

	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		private String conversationId, memberId;
		private MemberState state;
		private String from;
	
		Builder() {}

		public Builder conversationId(String conversationId) {
			this.conversationId = conversationId;
			return this;
		}

		public Builder memberId(String memberId) {
			this.memberId = memberId;
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
		 * @param from 
		 *
		 * @return This builder.
		 */
		public Builder from(String from) {
			this.from = from;
			return this;
		}

	
		/**
		 * Builds the {@linkplain UpdateMemberRequest}.
		 *
		 * @return An instance of UpdateMemberRequest, populated with all fields from this builder.
		 */
		public UpdateMemberRequest build() {
			return new UpdateMemberRequest(this);
		}
	}
}
