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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the verification channel type in a Workflow.
 */
public enum Channel {
	/**
	 * SMS text message.
	 */
	SMS,

	/**
	 * WhatsApp text message.
	 */
	WHATSAPP,

	/**
	 * Telephone voice call.
	 */
	VOICE,

	/**
	 * E-mail text message.
	 */
	EMAIL,

	/**
	 * Network-based authentication (using the device).
	 */
	SILENT_AUTH;

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
