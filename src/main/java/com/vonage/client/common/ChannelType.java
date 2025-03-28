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
package com.vonage.client.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vonage.client.Jsonable;

/**
 * Represents the various available channel types.
 *
 * @since 8.4.0
 */
public enum ChannelType {
	APP, PHONE, SMS, MMS, SIP, VBC, WEBSOCKET, VIBER, MESSENGER, WHATSAPP, WHATSAPP_VOICE;

	/**
	 * Convert a string value into a {@link ChannelType} object.
	 *
	 * @param name The string value to convert.
	 *
	 * @return The {@link ChannelType} object for the provided string value, or {@code null} if invalid.
	 */
	@JsonCreator
	public static ChannelType fromString(String name) {
		if ("pstn".equalsIgnoreCase(name)) return PHONE;
		return Jsonable.fromString(name, ChannelType.class);
	}

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase().replace('_', '-');
	}
}
