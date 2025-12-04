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
package com.vonage.client.messages.rcs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;

/**
 * Base interface for RCS suggestion objects (replies and actions).
 *
 * @since 9.6.0
 */
public interface RcsSuggestion extends Jsonable {

	/**
	 * The type of suggestion.
	 *
	 * @return The suggestion type as a string.
	 */
	@JsonProperty("type")
	String getType();

	/**
	 * The text to display on the suggestion chip.
	 *
	 * @return The suggestion text (max 25 characters).
	 */
	@JsonProperty("text")
	String getText();

	/**
	 * The data that will be sent back in the inbound webhook when the user taps the suggestion chip.
	 *
	 * @return The postback data string.
	 */
	@JsonProperty("postback_data")
	String getPostbackData();
}
