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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the language in {@link UISettings}. Default is English.
 */
public enum RoomLanguage {
	/**
	 * English
	 */
	EN,

	/**
	 * Hebrew
	 */
	HE,

	/**
	 * Spanish
	 */
	ES,

	/**
	 * Portuguese
	 *
	 * @deprecated Replaced by {@link #PT_BR}.
	 */
	@Deprecated
	PT,

	/**
	 * Brazilian Portuguese
	 *
	 * @since 7.10.0
	 */
	PT_BR,

	/**
	 * Italian
	 */
	IT,

	/**
	 * Catalan
	 */
	CA,

	/**
	 * French
	 */
	FR,

	/**
	 * German
	 */
	DE,

	/**
	 * Arabic
	 *
	 * @since 7.10.0
	 */
	AR,

	/**
	 * Chinese (Taiwan)
	 *
	 * @since 7.10.0
	 */
	ZH_TW,

	/**
	 * Chinese (Mainland)
	 *
	 * @since 7.10.0
	 */
	ZH_CN;

	@JsonCreator
	public static RoomLanguage fromString(String value) {
		try {
			return valueOf(value.toUpperCase().replace('-', '_'));
		}
		catch (NullPointerException | IllegalArgumentException ex) {
			return null;
		}
	}

	@JsonValue
	@Override
	public String toString() {
		return name().replace('_', '-');
	}	
}
