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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the possible types of events in {@link Event#getType()}.
 */
public enum EventType {
	/**
	 * Event type is not defined in this enum.
	 */
	UNKNOWN,

	/**
	 * Ephemeral (temporary) event.
	 */
	EPHEMERAL,

	/**
	 * Custom event.
	 */
	CUSTOM,

	/**
	 * Message event. See {@link com.vonage.client.common.MessageType} for the specifics. Applicable events are:
	 * Text, Image, Audio, Video, File, Template, Custom, VCard, Location, Random.
	 */
	MESSAGE,

	/**
	 * Message submitted.
	 */
	MESSAGE_SUBMITTED,

	/**
	 * Message rejected.
	 */
	MESSAGE_REJECTED,

	/**
	 * Message could not be delivered.
	 */
	MESSAGE_UNDELIVERABLE,

	/**
	 * Message has been seen.
	 */
	MESSAGE_SEEN,

	/**
	 * Message has been delivered.
	 */
	MESSAGE_DELIVERED,

	/**
	 * Play audio.
	 */
	AUDIO_PLAY,

	/**
	 * Stop the audio currently playing.
	 */
	AUDIO_PLAY_STOP,

	/**
	 * Use text-to-speech to say the given text.
	 */
	AUDIO_SAY,

	/**
	 * Stop the currently playing text-to-speech.
	 */
	AUDIO_SAY_STOP,

	/**
	 * Play DTMF audio.
	 */
	AUDIO_DTMF,

	/**
	 * Record the audio.
	 */
	AUDIO_RECORD,

	/**
	 * Stop current recording of audio.
	 */
	AUDIO_RECORD_STOP,

	/**
	 * Mute audio, i.e. the producer won't be able to be heard by others.
	 */
	AUDIO_MUTE_ON,

	/**
	 * Unmute audio, i.e. the producer will be able to be heard by others.
	 */
	AUDIO_MUTE_OFF,

	/**
	 * Earmuff audio, i.e. the receiver won't be able to hear it.
	 */
	AUDIO_EARMUFF_ON,

	/**
	 * Unearmuff audio, i.e. the receiver will be able to hear it.
	 */
	AUDIO_EARMUFF_OFF,

	/**
	 * Event has been deleted.
	 */
	EVENT_DELETE,

	/**
	 * Update on the status of a conversation leg.
	 */
	LEG_STATUS_UPDATE,

	/**
	 * Conversation details have been updated.
	 */
	CONVERSATION_UPDATED,

	/**
	 * Playing text-to-speech has finished.
	 */
	AUDIO_SAY_DONE,

	/**
	 * Playing audio has finished.
	 */
	AUDIO_PLAY_DONE,

	/**
	 * Audio recording has finished.
	 */
	AUDIO_RECORD_DONE,

	/**
	 * Enable speaking.
	 */
	AUDIO_SPEAKING_ON,

	/**
	 * Disable speaking.
	 */
	AUDIO_SPEAKING_OFF,

	/**
	 * Automatic speech recognition has finished.
	 */
	AUDIO_ASR_DONE,

	/**
	 * Automatic speech recognition of recording has finished.
	 */
	AUDIO_ASR_RECORD_DONE,

	/**
	 * Status update on a member message.
	 */
	MEMBER_MESSAGE_STATUS,

	/**
	 * Member has been invited to the conversation.
	 */
	MEMBER_INVITED,

	/**
	 * Member has joined the conversation.
	 */
	MEMBER_JOINED,

	/**
	 * Member has left the conversation.
	 */
	MEMBER_LEFT,

	/**
	 * Member has media.
	 */
	MEMBER_MEDIA,

	/**
	 * Status of SIP.
	 */
	SIP_STATUS,

	/**
	 * SIP call has been hung up.
	 */
	SIP_HANGUP,

	/**
	 * SIP call has been answered.
	 */
	SIP_ANSWERED,

	/**
	 * SIP call encountered machine.
	 */
	SIP_MACHINE,

	/**
	 * SIP call encountered machine using Advanced Machine Detection.
	 */
	SIP_AMD_MACHINE,

	/**
	 * SIP call is ringing.
	 */
	SIP_RINGING,

	/**
	 * RTC status.
	 */
	RTC_STATUS,

	/**
	 * RTC call transfer.
	 */
	RTC_TRANSFER,

	/**
	 * RTC call has hung up.
	 */
	RTC_HANGUP,

	/**
	 * RTC call has been answered.
	 */
	RTC_ANSWERED,

	/**
	 * RTC call is ringing.
	 */
	RTC_RINGING,

	/**
	 * RTC call answer.
	 */
	RTC_ANSWER;

	/**
	 * Convert a string to an EventType enum.
	 *
	 * @param name The string to convert.
	 *
	 * @return The EventType enum, {@code null} if empty and {@linkplain #UNKNOWN} if invalid.
	 */
	@JsonCreator
	public static EventType fromString(String name) {
		if (name == null || name.trim().isEmpty()) {
			return null;
		}
		String upper = name.toUpperCase();
		if (upper.startsWith("CUSTOM:")) {
			return CUSTOM;
		}
		try {
			return valueOf(upper.replace(':', '_'));
		}
		catch (IllegalArgumentException ex) {
			return EventType.UNKNOWN;
		}
	}

	@JsonValue
	@Override
	public String toString() {
		if (this == SIP_AMD_MACHINE) {
			return "sip:amd_machine";
		}
		return name().toLowerCase().replace('_', ':');
	}
}
