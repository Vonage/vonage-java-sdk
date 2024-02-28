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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vonage.client.Jsonable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Options for updating a membership using {@link ConversationsClient#updateMember(UpdateMemberRequest)}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UpdateMemberRequest extends ConversationResourceRequestWrapper implements Jsonable {
	private final MemberState state;
	private final String from;
	@JsonProperty("reason") private final Reason reason;

	UpdateMemberRequest(Builder builder) {
		super(builder.conversationId, builder.memberId);
		state = builder.state;
		from = builder.from;
		reason = builder.reason;
	}

	static final class Reason extends JsonableBaseObject {
		@JsonProperty("code") String code;
		@JsonProperty("text") String text;
	}
	
	@JsonProperty("state")
	public MemberState getState() {
		return state;
	}

	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	@JsonIgnore
	public String getCode() {
		return reason != null ? reason.code : null;
	}

	@JsonIgnore
	public String getText() {
		return reason != null ? reason.text : null;
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
		private Reason reason;
	
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
		 *
		 *
		 * @param code
		 *
		 * @return This builder.
		 */
		public Builder code(String code) {
			if (reason == null) reason = new Reason();
			reason.code = code;
			return this;
		}

		/**
		 *
		 *
		 * @param text
		 *
		 * @return This builder.
		 */
		public Builder text(String text) {
			if (reason == null) reason = new Reason();
			reason.text = text;
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
