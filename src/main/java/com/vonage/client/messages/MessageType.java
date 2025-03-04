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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the media type of the message.
 *
 * @deprecated Will be replaced by {@link com.vonage.client.common.MessageType} in the next major release.
 */
@Deprecated
public enum MessageType {
	TEXT, IMAGE, AUDIO, VIDEO, FILE, VCARD, TEMPLATE, CUSTOM, LOCATION,
	STICKER, UNSUPPORTED, REPLY, ORDER, CONTACT, BUTTON, REACTION, CONTENT;

	@JsonCreator
	public static MessageType fromString(String value) {
		if (value == null) return null;
		return MessageType.valueOf(value.toUpperCase());
	}

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
