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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class MeetingRoom extends JsonableBaseObject {
	private Instant createdAt, expiresAt;
	private UUID id, themeId;
	private String displayName, metadata, meetingCode;
	private Boolean isAvailable, expireAfterUse;
	private RoomType type;
	private JoinApprovalLevel joinApprovalLevel;
	private RecordingOptions recordingOptions;
	private InitialJoinOptions initialJoinOptions;
	private UISettings uiSettings;
	private CallbackUrls callbackUrls;
	private AvailableFeatures availableFeatures;
	private RoomLinks links;

	protected MeetingRoom() {
	}

	MeetingRoom(Builder builder) {
		if ((displayName = builder.displayName) == null || displayName.trim().isEmpty()) {
			throw new IllegalArgumentException("Display name is required and cannot be empty.");
		}
		validateExpiresAtAndRoomType(expiresAt = builder.expiresAt, type = builder.type);
		if ((expireAfterUse = builder.expireAfterUse) != null && type == RoomType.INSTANT) {
			throw new IllegalStateException("expireAfterUse is not applicable to "+type+" rooms.");
		}
		metadata = builder.metadata;
		isAvailable = builder.isAvailable;
		recordingOptions = builder.recordingOptions;
		initialJoinOptions = builder.initialJoinOptions;
		callbackUrls = builder.callbackUrls;
		availableFeatures = builder.availableFeatures;
		themeId = builder.themeId;
		joinApprovalLevel = builder.joinApprovalLevel;
		uiSettings = builder.uiSettings;
	}

	static void validateExpiresAtAndRoomType(Instant expiresAt, RoomType type) {
		if (type == RoomType.INSTANT && expiresAt != null) {
			throw new IllegalStateException("Expiration time should not be specified for "+type+" rooms.");
		}
		else if (type == RoomType.LONG_TERM && expiresAt == null) {
			throw new IllegalStateException("Expiration time must be specified for "+type+" rooms.");
		}
		if (expiresAt != null && expiresAt.isBefore(Instant.now().plusSeconds(600))) {
			throw new IllegalArgumentException("Expiration time should be more than 10 minutes from now.");
		}
	}

	/**
	 * Name of the meeting room.
	 *
	 * @return The display name.
	 */
	@JsonProperty("display_name")
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Free text that can be attached to a room.
	 * This will be passed in the form of a header in events related to this room.
	 *
	 * @return The room description / metadata.
	 */
	@JsonProperty("metadata")
	public String getMetadata() {
		return metadata;
	}

	/**
	 * The meeting code, which is used in the URL to join the meeting.
	 *
	 * @return The share code for this meeting.
	 */
	@JsonProperty("meeting_code")
	public String getMeetingCode() {
		return meetingCode;
	}

	/**
	 * Type of room.
	 *
	 * @return The room type, as an enum.
	 */
	@JsonProperty("type")
	public RoomType getType() {
		return type;
	}

	/**
	 * Once a room becomes unavailable, no new sessions can be created under it.
	 *
	 * @return Whether the room is available, or {@code null} if unknown.
	 */
	@JsonProperty("is_available")
	public Boolean getIsAvailable() {
		return isAvailable;
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
	 * Options for recording.
	 *
	 * @return The recording options.
	 */
	@JsonProperty("recording_options")
	public RecordingOptions getRecordingOptions() {
		return recordingOptions;
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
	 * Settings for the user interface of this meeting.
	 *
	 * @return The UI settings object.
	 */
	@JsonProperty("ui_settings")
	public UISettings getUiSettings() {
		return uiSettings;
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
	 * The time for when the room was created, expressed in ISO 8601 format.
	 *
	 * @return The room creation time.
	 */
	@JsonProperty("created_at")
	public Instant getCreatedAt() {
		return createdAt;
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
	 * Identifier of the meeting room.
	 *
	 * @return The room ID.
	 */
	@JsonProperty("id")
	public UUID getId() {
		return id;
	}

	/**
	 * Useful links.
	 *
	 * @return The nested links object.
	 */
	@JsonProperty("_links")
	public RoomLinks getLinks() {
		return links;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static MeetingRoom fromJson(String json) {
		return Jsonable.fromJson(json);
	}
	
	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @param displayName Name of the meeting room.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder(String displayName) {
		return new Builder(displayName);
	}
	
	public static class Builder {
		private final String displayName;
		private UUID themeId;
		private String metadata;
		private Instant expiresAt;
		private Boolean isAvailable, expireAfterUse;
		private RoomType type;
		private JoinApprovalLevel joinApprovalLevel;
		private RecordingOptions recordingOptions;
		private InitialJoinOptions initialJoinOptions;
		private UISettings uiSettings;
		private CallbackUrls callbackUrls;
		private AvailableFeatures availableFeatures;
	
		Builder(String displayName) {
			this.displayName = displayName;
		}

		/**
		 * Free text that can be attached to a room. This will be passed in the form of a header
		 * in events related to this room.
		 *
		 * @param metadata Additional text / data to be associated with this room.
		 *
		 * @return This builder.
		 */
		public Builder metadata(String metadata) {
			this.metadata = metadata;
			return this;
		}

		/**
		 * Type of room.
		 *
		 * @param type The room type.
		 *
		 * @return This builder.
		 */
		public Builder type(RoomType type) {
			this.type = type;
			return this;
		}

		/**
		 * Once a room becomes unavailable, no new sessions can be created under it.
		 *
		 * @param isAvailable Whether the room is available.
		 *
		 * @return This builder.
		 */
		public Builder isAvailable(boolean isAvailable) {
			this.isAvailable = isAvailable;
			return this;
		}

		/**
		 * Close the room after a session ends. Only relevant for long term rooms.
		 *
		 * @param expireAfterUse Whether the room should close after use.
		 *
		 * @return This builder.
		 */
		public Builder expireAfterUse(boolean expireAfterUse) {
			this.expireAfterUse = expireAfterUse;
			return this;
		}

		/**
		 * Options for recording.
		 *
		 * @param recordingOptions The recording options.
		 *
		 * @return This builder.
		 */
		public Builder recordingOptions(RecordingOptions recordingOptions) {
			this.recordingOptions = recordingOptions;
			return this;
		}

		/**
		 * Options for when a participant joins a meeting.
		 *
		 * @param initialJoinOptions The initial join options for each participant.
		 *
		 * @return This builder.
		 */
		public Builder initialJoinOptions(InitialJoinOptions initialJoinOptions) {
			this.initialJoinOptions = initialJoinOptions;
			return this;
		}

		/**
		 * Settings for the user interface of this meeting.
		 *
		 * @param uiSettings The user interface settings.
		 *
		 * @return This builder.
		 */
		public Builder uiSettings(UISettings uiSettings) {
			this.uiSettings = uiSettings;
			return this;
		}

		/**
		 * Callback URLs for this meeting.
		 *
		 * @param callbackUrls The additional URLs for this meeting.
		 *
		 * @return This builder.
		 */
		public Builder callbackUrls(CallbackUrls callbackUrls) {
			this.callbackUrls = callbackUrls;
			return this;
		}

		/**
		 * Available features for this meeting.
		 *
		 * @param availableFeatures The features available for this room.
		 *
		 * @return This builder.
		 */
		public Builder availableFeatures(AvailableFeatures availableFeatures) {
			this.availableFeatures = availableFeatures;
			return this;
		}

		/**
		 * ID of the theme for this room.
		 *
		 * @param themeId Unique theme identifier.
		 *
		 * @return This builder.
		 */
		public Builder themeId(UUID themeId) {
			this.themeId = themeId;
			return this;
		}

		/**
		 * Level of approval needed to join the meeting in the room.
		 *
		 * @param joinApprovalLevel The permission setting for joining this room.
		 *
		 * @return This builder.
		 */
		public Builder joinApprovalLevel(JoinApprovalLevel joinApprovalLevel) {
			this.joinApprovalLevel = joinApprovalLevel;
			return this;
		}

		/**
		 * NOTE: This parameter is REQUIRED if the room type is {@link RoomType#LONG_TERM},
		 * but should not be present if the room type is {@link RoomType#INSTANT}.
		 *
		 * @param expiresAt The time for when the room will expire, expressed in ISO 8601 format.
		 *
		 * @return This builder.
		 */
		public Builder expiresAt(Instant expiresAt) {
			this.expiresAt = expiresAt.truncatedTo(ChronoUnit.MILLIS);
			return this;
		}

	
		/**
		 * Builds the {@linkplain MeetingRoom}.
		 *
		 * @return An instance of MeetingRoom, populated with all fields from this builder.
		 */
		public MeetingRoom build() {
			return new MeetingRoom(this);
		}
	}
}
