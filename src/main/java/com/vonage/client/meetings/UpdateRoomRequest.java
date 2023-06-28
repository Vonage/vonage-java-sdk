/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vonage.client.VonageUnexpectedException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UpdateRoomRequest {
	@JsonIgnore UUID roomId;

	private final Boolean expireAfterUse;
	private final InitialJoinOptions initialJoinOptions;
	private final CallbackUrls callbackUrls;
	private final AvailableFeatures availableFeatures;
	private final JoinApprovalLevel joinApprovalLevel;
	private final UUID themeId;
	@JsonIgnore private final Instant expiresAt;

	UpdateRoomRequest(Builder builder) {
		expireAfterUse = builder.expireAfterUse;
		initialJoinOptions = builder.initialJoinOptions;
		callbackUrls = builder.callbackUrls;
		availableFeatures = builder.availableFeatures;
		joinApprovalLevel = builder.joinApprovalLevel;
		themeId = builder.themeId;
		MeetingRoom.validateExpiresAtAndRoomType(expiresAt = builder.expiresAt, null);
	}

	/**
	 * Close the room after a session ends. Only relevant for long_term rooms.
	 *
	 * @return {@code true} if the room will close after end of session, or {@code null} if unknown / not applicable.
	 */
	@JsonProperty("expire_after_use")
	public Boolean getExpireAfterUse() {
		return expireAfterUse;
	}

	/**
	 * Options for when a participant joins a meeting.
	 *
	 * @return The initial joining options.
	 */
	@JsonProperty("initial_join_options")
	public InitialJoinOptions getInitialJoinOptions() {
		return initialJoinOptions;
	}

	/**
	 * The callback URLs for this meeting.
	 *
	 * @return The CallbackUrls object.
	 */
	@JsonProperty("callback_urls")
	public CallbackUrls getCallbackUrls() {
		return callbackUrls;
	}

	/**
	 * The available features for this meeting.
	 *
	 * @return The AvailableFeatures object.
	 */
	@JsonProperty("available_features")
	public AvailableFeatures getAvailableFeatures() {
		return availableFeatures;
	}

	/**
	 * ID of the theme for this room.
	 *
	 * @return The theme ID.
	 */
	@JsonProperty("theme_id")
	public UUID getThemeId() {
		return themeId;
	}

	/**
	 * The level of approval needed to join the meeting in the room.
	 *
	 * @return The approval level, as an enum.
	 */
	@JsonProperty("join_approval_level")
	public JoinApprovalLevel getJoinApprovalLevel() {
		return joinApprovalLevel;
	}

	/**
	 * The time for when the room will be expired, expressed in ISO 8601 format.
	 *
	 * @return The room expiration time.
	 */
	@JsonProperty("expires_at")
	public Instant getExpiresAt() {
		return expiresAt;
	}
	
	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this UpdateRoomRequest object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			return "{\"update_details\":"+mapper.writeValueAsString(this)+"}";
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
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
		private Boolean expireAfterUse;
		private InitialJoinOptions initialJoinOptions;
		private CallbackUrls callbackUrls;
		private AvailableFeatures availableFeatures;
		private JoinApprovalLevel joinApprovalLevel;
		private UUID themeId;
		private Instant expiresAt;
	
		Builder() {}
	
		/**
		 *
		 * @param expireAfterUse Close the room after a session ends. Only relevant for long_term rooms.
		 *
		 * @return This builder.
		 */
		public Builder expireAfterUse(Boolean expireAfterUse) {
			this.expireAfterUse = expireAfterUse;
			return this;
		}

		/**
		 *
		 * @param initialJoinOptions Options for when a participant joins a meeting.
		 *
		 * @return This builder.
		 */
		public Builder initialJoinOptions(InitialJoinOptions initialJoinOptions) {
			this.initialJoinOptions = initialJoinOptions;
			return this;
		}

		/**
		 *
		 * @param callbackUrls The callback URLs for this meeting.
		 *
		 * @return This builder.
		 */
		public Builder callbackUrls(CallbackUrls callbackUrls) {
			this.callbackUrls = callbackUrls;
			return this;
		}

		/**
		 *
		 * @param availableFeatures The available features for this meeting.
		 *
		 * @return This builder.
		 */
		public Builder availableFeatures(AvailableFeatures availableFeatures) {
			this.availableFeatures = availableFeatures;
			return this;
		}

		/**
		 *
		 * @param themeId ID of the theme for this room.
		 *
		 * @return This builder.
		 */
		public Builder themeId(UUID themeId) {
			this.themeId = themeId;
			return this;
		}

		/**
		 *
		 * @param joinApprovalLevel The level of approval needed to join the meeting in the room.
		 *
		 * @return This builder.
		 */
		public Builder joinApprovalLevel(JoinApprovalLevel joinApprovalLevel) {
			this.joinApprovalLevel = joinApprovalLevel;
			return this;
		}

		/**
		 *
		 * @param expiresAt The time for when the room will be expired, expressed in ISO 8601 format.
		 *
		 * @return This builder.
		 */
		public Builder expiresAt(Instant expiresAt) {
			this.expiresAt = expiresAt.truncatedTo(ChronoUnit.MILLIS);
			return this;
		}

		/**
		 * Builds the {@linkplain UpdateRoomRequest}.
		 *
		 * @return An instance of UpdateRoomRequest, populated with all fields from this builder.
		 */
		public UpdateRoomRequest build() {
			return new UpdateRoomRequest(this);
		}
	}
}
