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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the available TTS language options. See
 * <a href=https://developer.vonage.com/voice/voice-api/guides/text-to-speech#supported-languages>
 * the documentation</a> for previews and valid styles.
 */
public enum TextToSpeechLanguage {
    AFRIKAANS("af-ZA"),
    ARABIC("ar"),
    BULGARIAN("bg-BG"),
    BENGALI("bn-IN"),
    SPANISH_CATALAN("ca-ES"),
    CHINESE_MANDARIN("cmn-CN"),
    TAIWANESE_MANDARIN("cmn-TW"),
    CZECH("cs-CZ"),
    WELSH("cy-GB"),
    DANISH("da-DK"),
    AUSTRIAN_GERMAN("de-AT"),
    GERMAN("de-DE"),
    GREEK("el-GR"),
    AUSTRALIAN_ENGLISH("en-AU"),
    UNITED_KINGDOM_ENGLISH("en-GB"),
    SCOTTISH_ENGLISH("en-GB-SCT"),
    WELSH_ENGLISH("en-GB-WLS"),
    IRISH_ENGLISH("en-IE"),
    INDIAN_ENGLISH("en-IN"),
    NEW_ZEALAND_ENGLISH("en-NZ"),
    AMERICAN_ENGLISH("en-US"),
    SOUTH_AFRICAN_ENGLISH("en-ZA"),
    COLOMBIAN_SPANISH("es-CO"),
    SPANISH("es-ES"),
    MEXICAN_SPANISH("es-MX"),
    AMERICAN_SPANISH("es-US"),
    BASQUE("eu-ES"),
    FINISH("fi-FI"),
    FILIPINO("fil-PH"),
    CANADIAN_FRENCH("fr-CA"),
    FRENCH("fr-FR"),
    SPANISH_GALICIAN("gl-ES"),
    GUJARATI("gu-IN"),
    HEBREW("he-IL"),
    HINDI("hi-IN"),
    HUNGARIAN("hu-HU"),
    INDONESIAN("id-ID"),
    ICELANDIC("is-IS"),
    ITALIAN("it-IT"),
    JAPANESE("ja-JP"),
    KANNADA("kn-IN"),
    KOREAN("ko-KR"),
    LATVIAN("lv-LV"),
    MALAYALAM("ml-IN"),
    MALAY("ms-MY"),
    NORWEGIAN("no-NO"),
    NORWEGIAN_BOKMAL("nb-NO"),
    BELGIAN_DUTCH("nl-BE"),
    DUTCH("nl-NL"),
    PUNJABI("pa-IN"),
    POLISH("pl-PL"),
    BRAZILIAN_PORTUGUESE("pt-BR"),
    PORTUGUESE("pt-PT"),
    ROMANIAN("ro-RO"),
    RUSSIAN("ru-RU"),
    SLOVAK("sk-SK"),
    SERBIAN("sr-RS"),
    SWEDISH("sv-SE"),
    TAMIL("ta-IN"),
    TELUGU("te-IN"),
    THAI("th-TH"),
    TURKISH("tr-TR"),
    UKRAINIAN("uk-UA"),
    VIETNAMESE("vi-VN"),
    CHINESE_YUE("yue-CN"),

    @JsonEnumDefaultValue
    UNKNOWN("Unknown");

    private final String language;

    TextToSpeechLanguage(String language) {
        this.language = language;
    }

    @JsonValue
    public String getLanguage() {
        return this.language;
    }
}
