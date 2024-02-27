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
import java.util.UUID;

/**
 * Represents a conversation membership.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends BaseMember {
	private String conversationId, memberIdInviting, from, invitedBy;
	private UUID knockingId;
	private MemberChannel channel;
	private MemberMedia media;
	private MemberInitiator initiator;
	private MemberTimestamp timestamp;

	protected Member() {
	}

	Member(Builder builder) {
		channel = builder.channel;
		media = builder.media;
		knockingId = builder.knockingId;
		memberIdInviting = builder.memberIdInviting;
		from = builder.from;
		initiator = builder.initiator;
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
	 * Channel details for this membership.
	 * 
	 * @return The channel, or {@code null} if unspecified.
	 */
	@JsonProperty("channel")
	public MemberChannel getChannel() {
		return channel;
	}

	/**
	 * Media settings for this member.
	 * 
	 * @return The media settings object, or {@code null} if unspecified.
	 */
	@JsonProperty("media")
	public MemberMedia getMedia() {
		return media;
	}

	/**
	 * Unique knocking identifier.
	 * 
	 * @return The knocking ID, or {@code null} if unspecified.
	 */
	@JsonProperty("knocking_id")
	public UUID getKnockingId() {
		return knockingId;
	}

	/**
	 * Unique member ID to invite.
	 * 
	 * @return The inviting member ID, or {@code null} if unspecified.
	 */
	@JsonProperty("member_id_inviting")
	public String getMemberIdInviting() {
		return memberIdInviting;
	}

	/**
	 * From (??)
	 * 
	 * @return The from, or {@code null} if unspecified.
	 */
	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	/**
	 * Unique member ID this member was invited by.
	 * 
	 * @return The inviting member ID, or {@code null} if unknown / not applicable.
	 */
	@JsonProperty("invited_by")
	public String getInvitedBy() {
		return invitedBy;
	}

	/**
	 * Describes how this member was initiated.
	 * 
	 * @return The initiator details, or {@code null} if unspecified.
	 */
	@JsonProperty("initiator")
	public MemberInitiator getInitiator() {
		return initiator;
	}

	/**
	 * Timestamps for this member.
	 *
	 * @return The timestamps object, or {@code null} if unknown.
	 */
	@JsonProperty("timestamp")
	public MemberTimestamp getTimestamp() {
		return timestamp;
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
		private MemberChannel channel;
		private MemberMedia media;
		private UUID knockingId;
		private String memberIdInviting;
		private String from;
		private MemberInitiator initiator;
	
		Builder() {}
	
		/**
		 * Channel details for this membership.
		 *
		 * @param channel The channel, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder channel(MemberChannel channel) {
			this.channel = channel;
			return this;
		}

		/**
		 * Media settings for this member.
		 *
		 * @param media The media settings object, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder media(MemberMedia media) {
			this.media = media;
			return this;
		}

		/**
		 * Unique knocking identifier.
		 *
		 * @param knockingId The knocking ID, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder knockingId(UUID knockingId) {
			this.knockingId = knockingId;
			return this;
		}

		/**
		 * Unique member ID to invite.
		 *
		 * @param memberIdInviting The inviting member ID, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder memberIdInviting(String memberIdInviting) {
			this.memberIdInviting = memberIdInviting;
			return this;
		}

		/**
		 * From (??)
		 *
		 * @param from The from, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder from(String from) {
			this.from = from;
			return this;
		}

		/**
		 * Describes how this member was initiated.
		 *
		 * @param initiator The initiator details, or {@code null} if unspecified.
		 *
		 * @return This builder.
		 */
		public Builder initiator(MemberInitiator initiator) {
			this.initiator = initiator;
			return this;
		}

	
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
