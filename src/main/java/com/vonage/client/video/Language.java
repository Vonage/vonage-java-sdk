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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the supported BCP-47 language codes for Live Captions.
 *
 * @since 8.5.0
 */
public enum Language {
	EN_US,
	EN_AU,
	EN_GB,
	ZH_CN,
	FR_FR,
	DE_DE,
	HI_IN,
	IT_IT,
	JA_JP,
	KO_KR,
	PT_BR,
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
		assert split.length == 2;
		return split[0].toLowerCase() + '-' + split[1];
	}	
}
