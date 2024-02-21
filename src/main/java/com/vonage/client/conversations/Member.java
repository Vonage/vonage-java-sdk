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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;

/*
 * Represents a conversation membership.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends BaseMember {
	String conversationId;

	protected Member() {
	}

	Member(Builder builder) {
	}

	/**
	 * Unique identifier for this member's conversation.
	 * 
	 * @return The conversation ID, or {@code null} if unknown.
	 */
	@JsonProperty("conversation_id")
	public String getConversationId() {
		return conversationId;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static Member fromJson(String json) {
		return Jsonable.fromJson(json);
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
	
		Builder() {}
	
	
		/**
		 * Builds the {@linkplain Member}.
		 *
		 * @return An instance of Member, populated with all fields from this builder.
		 */
		public Member build() {
			return new Member(this);
		}
	}

}
