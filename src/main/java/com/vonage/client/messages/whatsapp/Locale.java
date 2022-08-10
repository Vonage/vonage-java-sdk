/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing BCP-47 locales supported by WhatsApp.
 * The values are derived from the "Supported Languages" section of the
 * <a href=https://developers.facebook.com/docs/whatsapp/api/messages/message-templates>
 * WhatsApp Message Templates documentation</a>.
 *
 * @since 7.0.0
 */
public enum Locale {
	AFRIKAANS("af"),
	ALBANIAN("sq"),
	ARABIC("ar"),
	AZERBAIJANI("az"),
	BENGALI("bn"),
	BULGARIAN("bg"),
	CATALAN("ca"),
	CHINESE_CHN("zh_CN"),
	CHINESE_HKG("zh_HK"),
	CHINESE_TAI("zh_TW"),
	CROATIAN("hr"),
	CZECH("cs"),
	DANISH("da"),
	DUTCH("nl"),
	ENGLISH("en"),
	ENGLISH_UK("en_GB"),
	ENGLISH_US("en_US"),
	ESTONIAN("et"),
	FILIPINO("fil"),
	FINNISH("fi"),
	FRENCH("fr"),
	GEORGIAN("ka"),
	GERMAN("de"),
	GREEK("el"),
	GUJARATI("gu"),
	HAUSA("ha"),
	HEBREW("he"),
	HINDI("hi"),
	HUNGARIAN("hu"),
	INDONESIAN("id"),
	IRISH("ga"),
	ITALIAN("it"),
	JAPANESE("ja"),
	KANNADA("kn"),
	KAZAKH("kk"),
	KINYARWANDA("rw_RW"),
	KOREAN("ko"),
	KYRGYZ_KYRGYZSTAN("ky_KG"),
	LAO("lo"),
	LATVIAN("lv"),
	LITHUANIAN("lt"),
	MACEDONIAN("mk"),
	MALAY("ms"),
	MALAYALAM("ml"),
	MARATHI("mr"),
	NORWEGIAN("nb"),
	PERSIAN("fa"),
	POLISH("pl"),
	PORTUGUESE_BR("pt_BR"),
	PORTUGUESE_POR("pt_PT"),
	PUNJABI("pa"),
	ROMANIAN("ro"),
	RUSSIAN("ru"),
	SERBIAN("sr"),
	SLOVAK("sk"),
	SLOVENIAN("sl"),
	SPANISH("es"),
	SPANISH_ARG("es_AR"),
	SPANISH_SPA("es_ES"),
	SPANISH_MEX("es_MX"),
	SWAHILI("sw"),
	SWEDISH("sv"),
	TAMIL("ta"),
	TELUGU("te"),
	THAI("th"),
	TURKISH("tr"),
	UKRAINIAN("uk"),
	URDU("ur"),
	UZBEK("uz"),
	VIETNAMESE("vi"),
	ZULU("zu");

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
