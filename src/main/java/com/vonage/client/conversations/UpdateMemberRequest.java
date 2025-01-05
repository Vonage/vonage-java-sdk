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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vonage.client.Jsonable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Objects;

/**
 * Options for updating a membership using {@link ConversationsClient#updateMember(UpdateMemberRequest)}.
 */
public final class UpdateMemberRequest extends ConversationResourceRequestWrapper implements Jsonable {
	private final MemberState state;
	private final String from;
	@JsonProperty("reason") private final Reason reason;

	UpdateMemberRequest(Builder builder) {
		super(builder.conversationId, builder.memberId);
		switch (state = Objects.requireNonNull(builder.state, "State is required.")) {
			case JOINED: case LEFT: break;
			default: throw new IllegalArgumentException("Invalid state: "+state);
		}
		if ((reason = builder.reason) != null && state != MemberState.LEFT) {
			throw new IllegalStateException("Reason is only applicable when leaving.");
		}
		from = builder.from;
	}

	static final class Reason extends JsonableBaseObject {
		@JsonProperty("code") String code;
		@JsonProperty("text") String text;
	}

	/**
	 * State to transition the member into.
	 *
	 * @return The updated state as an enum.
	 */
	@JsonProperty("state")
	public MemberState getState() {
		return state;
	}

	/**
	 * TODO document this.
	 *
	 * @return The from, or {@code null} if unspecified.
	 */
	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	/**
	 * Reason code for leaving. Only applicable when {@linkplain #getState()} is {@linkplain MemberState#LEFT}.
	 *
	 * @return The reason code, or {@code null} if unspecified / not applicable.
	 */
	@JsonIgnore
	public String getCode() {
		return reason != null ? reason.code : null;
	}

	/**
	 * Reason text for leaving. Only applicable when {@linkplain #getState()} is {@linkplain MemberState#LEFT}.
	 *
	 * @return The reason text, or {@code null} if unspecified / not applicable.
	 */
	@JsonIgnore
	public String getText() {
		return reason != null ? reason.text : null;
	}

	/**
	 * Unique Conversation identifier.
	 *
	 * @return The conversation ID.
	 */
	@JsonIgnore
	public String getConversationId() {
		return conversationId;
	}

	/**
	 * Unique Member identifier.
	 *
	 * @return The member ID.
	 */
	@JsonIgnore
	public String getMemberId() {
		return resourceId;
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
	 * Builder for constructing an UpdateMemberRequest. Note the mandatory parameters.
	 */
	public static final class Builder {
		private String conversationId, memberId, from;
		private MemberState state;
		private Reason reason;
	
		Builder() {}

		/**
		 * (REQUIRED) Unique conversation identifier.
		 *
		 * @param conversationId The conversation ID.
		 *
		 * @return This builder.
		 */
		public Builder conversationId(String conversationId) {
			this.conversationId = conversationId;
			return this;
		}

		/**
		 * (REQUIRED) Unique member identifier.
		 *
		 * @param memberId The member ID.
		 *
		 * @return This builder.
		 */
		public Builder memberId(String memberId) {
			this.memberId = memberId;
			return this;
		}

		/**
		 * (REQUIRED) State to transition the member into.
		 *
		 * @param state The updated state as an enum.
		 *
		 * @return This builder.
		 */
		public Builder state(MemberState state) {
			this.state = state;
			return this;
		}

		/**
		 * TODO document this
		 *
		 * @param from The from (??)
		 *
		 * @return This builder.
		 */
		public Builder from(String from) {
			this.from = from;
			return this;
		}

		/**
		 * Reason code for leaving.
		 * Only applicable when {@linkplain #state(MemberState)} is {@linkplain MemberState#LEFT}.
		 *
		 * @param code The reason code as a string.
		 *
		 * @return This builder.
		 */
		public Builder code(String code) {
			if (reason == null) reason = new Reason();
			reason.code = code;
			return this;
		}

		/**
		 * Reason text for leaving.
		 * Only applicable when {@linkplain #state(MemberState)} is {@linkplain MemberState#LEFT}.
		 *
		 * @param text The reason text.
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
