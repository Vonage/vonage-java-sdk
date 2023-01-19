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
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DialNumbers {
	private String number, displayName;
	private Locale locale;

	protected DialNumbers() {
	}

	/**
	 * The dial-in number.
	 *
	 * @return The number in E164 format.
	 */
	@JsonProperty("number")
	public String getNumber() {
		return number;
	}

	/**
	 * The number locale.
	 *
	 * @return The Locale.
	 */
	@JsonProperty("locale")
	public Locale getLocale() {
		return locale;
	}

	/**
	 * The country name of the locale.
	 *
	 * @return The country's name.
	 */
	@JsonProperty("display_name")
	public String getDisplayName() {
		return displayName;
	}
}