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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the available locales for user verification. These are
 * <a href=https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes>ISO_639-1 codes</a>.
 */
public enum Locale {
	ENGLISH_US("en-us"),
	ENGLISH_UK("en-gb"),
	SPANISH_SPAIN("es-es"),
	SPANISH_MEXICO("es-mx"),
	SPANISH_US("es-us"),
	ITALIAN_ITALY("it-it"),
	FRENCH_FRANCE("fr-fr"),
	GERMAN_GERMANY("de-de"),
	RUSSIAN_RUSSIA("ru-ru"),
	HINDI_INDIA("hi-in"),
	PORTUGUESE_BRAZIL("pt-br"),
	PORTUGUESE_PORTUGAL("pt-pt"),
	INDONESIAN_INDONESIA("id-id");

	private final String code;

	Locale(String code) {
		this.code = code;
	}

	@JsonValue
	@Override
	public String toString() {
		return code;
	}
}
