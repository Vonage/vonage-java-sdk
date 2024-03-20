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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the possible types of events in {@link Event#getType()}.
 */
public enum EventType {
	EPHEMERAL,
	CUSTOM,
	MESSAGE,
	MESSAGE_SUBMITTED,
	MESSAGE_REJECTED,
	MESSAGE_UNDELIVERABLE,
	MESSAGE_SEEN,
	MESSAGE_DELIVERED,
	AUDIO_PLAY,
	AUDIO_PLAY_STOP,
	AUDIO_SAY,
	AUDIO_SAY_STOP,
	AUDIO_DTMF,
	AUDIO_RECORD,
	AUDIO_RECORD_STOP,
	AUDIO_MUTE_ON,
	AUDIO_MUTE_OFF,
	AUDIO_EARMUFF_ON,
	AUDIO_EARMUFF_OFF;

	@JsonCreator
	public static EventType fromString(String name) {
		try {
			return valueOf(name.toUpperCase().replace(':', '_'));
		}
		catch (NullPointerException | IllegalArgumentException ex) {
			return null;
		}
	}

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase().replace('_', ':');
	}
}
