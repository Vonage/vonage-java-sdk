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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the supported BCP-47 language codes for Live Captions.
 *
 * @since 8.5.0
 */
public enum Language {
	/**
	 * American English
	 */
	EN_US,

	/**
	 * Australian English
	 */
	EN_AU,

	/**
	 * British English
	 */
	EN_GB,

	/**
	 * Simplified Chinese
	 */
	ZH_CN,

	/**
	 * French
	 */
	FR_FR,

	/**
	 * German
	 */
	DE_DE,

	/**
	 * Hindi
	 */
	HI_IN,

	/**
	 * Italian
	 */
	IT_IT,

	/**
	 * Japanese
	 */
	JA_JP,

	/**
	 * Korean
	 */
	KO_KR,

	/**
	 * Brazilian Portuguese
	 */
	PT_BR,

	/**
	 * Thai
	 */
	TH_TH;

	@JsonCreator
	public static Language fromString(String name) {
		if (name == null) return null;
		try {
			return valueOf(name.toUpperCase().replace('-', '_'));
		}
		catch (IllegalArgumentException ex) {
			return null;
		}
	}

	@JsonValue
	@Override
	public String toString() {
		String[] split = name().split("_");
		return split[0].toLowerCase() + '-' + split[1];
	}	
}
