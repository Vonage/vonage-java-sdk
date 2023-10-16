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
 * <a href=https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes>ISO 639-1 codes</a>.
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
	INDONESIAN_INDONESIA("id-id"),
	JAPANESE_JAPAN("ja-jp"),
	HEBREW_ISRAEL("he-il"),
	YUE_CHINESE_CHINA("yue-cn"),
	ARABIC_ARABIAN_PENINSULA("ar-xa"),
	CZECH_CZECH_REPUBLIC("cs-cz"),
	WELSH_UK("cy-gb"),
	GREEK_GREECE("el-gr"),
	ENGLISH_AUSTRALIA("en-au"),
	ENGLISH_INDIA("en-in"),
	FINNISH_FINLAND("fi-fi"),
	FILIPINO_PHILIPPINES("fil-ph"),
	FRENCH_CANADA("fr-ca"),
	HUNGARIAN_HUNGARY("hu-hu"),
	ICELANDIC_ICELAND("is-is"),
	NORWEGIAN_BOKMAL_NORWAY("nb-no"),
	DUTCH_NETHERLANDS("nl-nl"),
	POLISH_POLAND("pl-pl"),
	ROMANIAN_ROMANIA("ro-ro"),
	SWEDISH_SWEDEN("sv-se"),
	THAI_THAILAND("th-th"),
	TURKISH_TURKEY("tr-tr"),
	VIETNAMESE_VIETNAM("vi-vn"),
	CHINESE_CHINA("zh-cn"),
	CHINESE_TAIWAN("zh-tw");

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
