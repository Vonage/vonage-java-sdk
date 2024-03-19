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

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.Jsonable;
import com.vonage.client.common.ChannelType;
import com.vonage.client.users.BaseUser;
import com.vonage.client.users.channels.Channel;
import java.util.Objects;
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
		user = Objects.requireNonNull(builder.user, "User is required.");
		state = Objects.requireNonNull(builder.state, "State is required.");
		channel = Objects.requireNonNull(builder.channel, "Channel is required.");
		media = builder.media;
		knockingId = builder.knockingId;
		memberIdInviting = builder.memberIdInviting;
		from = builder.from;
	}

	@JsonSetter("conversation_id")
	void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	/**
	 * Unique identifier for this member's conversation.
	 * 
	 * @return The conversation ID, or {@code null} if this is a request.
	 */
	@JsonIgnore
	public String getConversationId() {
		return conversationId;
	}

	/**
	 * Channel details for this membership.
	 * 
	 * @return The channel.
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
	 * Unique member ID this member was invited by.
	 * This will be set when the invite has been created but not accepted.
	 * 
	 * @return The inviting member ID, or {@code null} if unspecified.
	 * @see #getInvitedBy()
	 */
	@JsonProperty("member_id_inviting")
	public String getMemberIdInviting() {
		return memberIdInviting;
	}

	/**
	 * TODO document this
	 * 
	 * @return The from field, or {@code null} if unspecified.
	 */
	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	/**
	 * Unique member ID this member was invited by.
	 * This will be set when the invite has been accepted.
	 * 
	 * @return The inviting member ID, or {@code null} if unknown / not applicable.
	 * @see #getMemberIdInviting()
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
	 * @return The timestamps object, or {@code null} if this is a request.
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
		private BaseUser user;
		private MemberState state;
		private MemberChannel channel;
		private MemberMedia media;
		private UUID knockingId;
		private String memberIdInviting, from;
	
		Builder() {}

		private MemberChannel initChannel() {
			if (channel == null) channel = new MemberChannel();
			return channel;
		}

		/**
		 * (REQUIRED) Either the user ID or unique name is required. This method will automatically
		 * determine which is provided. User IDs start with {@code USR-} followed by a UUID.
		 *
		 * @param userNameOrId Either the unique user ID, or the name.
		 *
		 * @return This builder.
		 */
		public Builder user(String userNameOrId) {
			if (userNameOrId == null || userNameOrId.trim().isEmpty()) {
				throw new IllegalArgumentException("Invalid user name or ID.");
			}
			if (userNameOrId.startsWith("USR-") && userNameOrId.length() == 40) {
				user = new BaseUser(userNameOrId, null);
			}
			else {
				user = new BaseUser(null, userNameOrId);
			}
			return this;
		}

		/**
		 * (REQUIRED) Invite or join a member to a conversation.
		 *
		 * @param state The state as an enum.
		 *
		 * @return This builder.
		 */
		public Builder state(MemberState state) {
			this.state = state;
			return this;
		}

		/**
		 * (REQUIRED) Top-level channel type. You should also provide {@linkplain #fromChannel(Channel)} and
		 * {@linkplain #toChannel(Channel)}. If this is set to anything other than {@linkplain ChannelType#APP},
		 * then both {@linkplain #fromChannel(Channel)} and {@linkplain #toChannel(Channel)}
		 * must be of the same type as each other.
		 *
		 * @param channelType The channel type as an enum.
		 *
		 * @return This builder.
		 */
		public Builder channelType(ChannelType channelType) {
			initChannel().type = channelType;
			return this;
		}

		/**
		 * (OPTIONAL) Concrete channel to use when sending messages.
		 * See {@linkplain com.vonage.client.users.channels} for options.
		 *
		 * @param from The sender channel.
		 *
		 * @return This builder.
		 * @see #channelType(ChannelType)
		 */
		public Builder fromChannel(Channel from) {
			(initChannel().from = from).setTypeField();
			return this;
		}

		/**
		 * (OPTIONAL) Concrete channel to use when receiving messages.
		 * See {@linkplain com.vonage.client.users.channels} for options.
		 *
		 * @param to The receiver channel.
		 *
		 * @return This builder.
		 * @see #channelType(ChannelType)
		 */
		public Builder toChannel(Channel to) {
			(initChannel().to = to).setTypeField();
			return this;
		}

		/**
		 * (OPTIONAL) Media settings for this member.
		 *
		 * @param media The media settings object.
		 *
		 * @return This builder.
		 */
		public Builder media(MemberMedia media) {
			this.media = media;
			return this;
		}

		/**
		 * (OPTIONAL) Unique knocking identifier.
		 *
		 * @param knockingId The knocking ID as a string.
		 *
		 * @return This builder.
		 */
		public Builder knockingId(String knockingId) {
			this.knockingId = UUID.fromString(knockingId);
			return this;
		}

		/**
		 * (OPTIONAL) Unique member ID to invite.
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
		 * (OPTIONAL) TODO document this
		 *
		 * @param from The from field.
		 *
		 * @return This builder.
		 */
		public Builder from(String from) {
			this.from = from;
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
