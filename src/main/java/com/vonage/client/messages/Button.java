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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.Map;

/**
 * Used for inbound Button messages.
 *
 * @since 8.11.0
 */
public final class Button extends JsonableBaseObject {
	private Object payload;
	private String text, subtype;

	Button() {}

	/**
	 * Payload for the button. Contents can be varied depending on the type of button.
	 * For WhatsApp messages, this will be a Map. For RCS, this will be a String.
	 *
	 * @return The button payload, or {@code null} if absent.
	 */
	@JsonProperty("payload")
	public Object getPayload() {
		return payload;
	}

	/**
	 * Additional context for the button.
	 *
	 * @return The button text, or {@code null} if absent.
	 */
	@JsonProperty("text")
	public String getText() {
		return text;
	}

	/**
	 * Subtype of button being received. This only applies to WhatsApp messages.
	 *
	 * @return The button subtype, or {@code null} if absent / not applicable.
	 */
	@JsonProperty("subtype")
	public String getSubtype() {
		return subtype;
	}
}
