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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents a {@linkplain com.vonage.client.common.MessageType#REACTION} message.
 *
 * @since 8.11.0
 */
public final class Reaction extends JsonableBaseObject {
	private final Action action;
	private final String emoji;

	/**
	 * Unreact to a message.
	 */
	public Reaction() {
		action = Action.UNREACT;
		emoji = null;
	}

	/**
	 * Create a new reaction to a message.
	 *
	 * @param emoji The reaction emoji to use.
	 */
	public Reaction(String emoji) {
		action = Action.REACT;
		this.emoji = emoji;
	}

	/**
	 * The action to take. If {@link Action#REACT}, then an emoji must be set.
	 *
	 * @return The action taken as an enum.
	 */
	@JsonProperty("action")
	public Action getAction() {
		return action;
	}

	/**
	 * The emoji used as a reaction.
	 *
	 * @return The emoji as a string, or {@code null} if not applicable.
	 */
	@JsonProperty("emoji")
	public String getEmoji() {
		return emoji;
	}

	/**
	 * Represents the action to be taken.
	 */
	public enum Action {
		/**
		 * React to a message.
		 */
		REACT,

		/**
		 * Remove a reaction to a message.
		 */
		UNREACT;

		@JsonValue
		@Override
		public String toString() {
			return name().toLowerCase();
		}

		/**
		 * Convert a string value to an Action enum.
		 *
		 * @param value The string value to convert.
		 *
		 * @return The action as an enum, or {@code null} if invalid.
		 */
		@JsonCreator
		public static Action fromString(String value) {
			return Jsonable.fromString(value, Action.class);
		}
	}
}
