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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.util.UUID;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingRoom {
	private UUID themeId;
	private String id, displayName, metadata, createdAt, expiresAt;
	private Boolean isAvailable, expireAfterUse;
	private RoomType type;
	private ApprovalLevel joinApprovalLevel;
	private RecordingOptions recordingOptions;
	private InitialJoinOptions initialJoinOptions;
	private UISettings uiSettings;
	private CallbackUrls callbackUrls;
	private AvailableFeatures availableFeatures;
	private RoomLinks links;

	protected MeetingRoom() {
	}

	MeetingRoom(Builder builder) {
		displayName = builder.displayName;
		metadata = builder.metadata;
		type = builder.type;
		isAvailable = builder.isAvailable;
		expireAfterUse = builder.expireAfterUse;
		recordingOptions = builder.recordingOptions;
		initialJoinOptions = builder.initialJoinOptions;
		callbackUrls = builder.callbackUrls;
		availableFeatures = builder.availableFeatures;
		themeId = builder.themeId;
		joinApprovalLevel = builder.joinApprovalLevel;
		expiresAt = builder.expiresAt;
		uiSettings = builder.uiSettings;
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
	public ApprovalLevel getJoinApprovalLevel() {
		return joinApprovalLevel;
	}

	/**
	 * The time for when the room was created, expressed in ISO 8601 format.
	 *
	 * @return The room creation time.
	 */
	@JsonProperty("created_at")
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * The time for when the room will be expired, expressed in ISO 8601 format.
	 *
	 * @return The room expiration time.
	 */
	@JsonProperty("expires_at")
	public String getExpiresAt() {
		return expiresAt;
	}

	/**
	 * Identifier of the meeting room.
	 *
	 * @return The room ID.
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * Useful links.
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
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, MeetingRoom.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce MeetingRoom from json.", ex);
		}
	}

	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this MeetingRoom object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
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
		private UUID themeId;
		private String displayName, metadata, expiresAt;
		private Boolean isAvailable, expireAfterUse;
		private RoomType type;
		private ApprovalLevel joinApprovalLevel;
		private RecordingOptions recordingOptions;
		private InitialJoinOptions initialJoinOptions;
		private UISettings uiSettings;
		private CallbackUrls callbackUrls;
		private AvailableFeatures availableFeatures;
	
		Builder() {}
	
		/**
		 *
		 * @param displayName Name of the meeting room.
		 *
		 * @return This builder.
		 */
		public Builder displayName(String displayName) {
			this.displayName = displayName;
			return this;
		}

		/**
		 *
		 * @param metadata Free text that can be attached to a room. This will be passed in the form of a header in events related to this room.
		 *
		 * @return This builder.
		 */
		public Builder metadata(String metadata) {
			this.metadata = metadata;
			return this;
		}

		/**
		 *
		 * @param type Type of room.
		 *
		 * @return This builder.
		 */
		public Builder type(RoomType type) {
			this.type = type;
			return this;
		}

		/**
		 *
		 * @param isAvailable Once a room becomes unavailable, no new sessions can be created under it
		 *
		 * @return This builder.
		 */
		public Builder isAvailable(Boolean isAvailable) {
			this.isAvailable = isAvailable;
			return this;
		}

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
		 * @param recordingOptions Options for recording.
		 *
		 * @return This builder.
		 */
		public Builder recordingOptions(RecordingOptions recordingOptions) {
			this.recordingOptions = recordingOptions;
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
		 * @param uiSettings Settings for the user interface of this meeting.
		 *
		 * @return This builder.
		 */
		public Builder uiSettings(UISettings uiSettings) {
			this.uiSettings = uiSettings;
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
		public Builder joinApprovalLevel(ApprovalLevel joinApprovalLevel) {
			this.joinApprovalLevel = joinApprovalLevel;
			return this;
		}

		/**
		 *
		 * @param expiresAt The time for when the room will be expired, expressed in ISO 8601 format.
		 *
		 * @return This builder.
		 */
		public Builder expiresAt(String expiresAt) {
			this.expiresAt = expiresAt;
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
