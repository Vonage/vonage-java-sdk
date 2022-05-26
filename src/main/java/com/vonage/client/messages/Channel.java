/*
 *   Copyright 2022 Vonage
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

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collection;
import java.util.EnumSet;

import static com.vonage.client.messages.MessageType.*;

public enum Channel {
	SMS (TEXT),
	MMS (IMAGE, VCARD, AUDIO, VIDEO),
	WHATSAPP (TEXT, IMAGE, AUDIO, VIDEO, FILE, TEMPLATE, CUSTOM),
	MESSENGER (TEXT, IMAGE, AUDIO, VIDEO, FILE),
	VIBER (TEXT, IMAGE);

	private final Collection<MessageType> supportedTypes;

	Channel(MessageType type1, MessageType... additionalTypes) {
		this.supportedTypes = EnumSet.of(type1, additionalTypes);
	}

	public boolean supportsMessageType(MessageType type) {
		return supportedTypes.contains(type);
	}

	@JsonValue
	@Override
	public String toString() {
		if (this == VIBER) {
			return "viber_service";
		}
		return name().toLowerCase();
	}
}
