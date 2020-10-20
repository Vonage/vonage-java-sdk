/*
 * Copyright (c) 2020  Vonage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ASR(Automatic Speech Recognition) settings for Input Actions that will be added to a NCCO object.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SpeechSettings {

    private Collection<String> uuid;
    private Integer endOnSilence;
    private Language language;
    private Collection<String> context;
    private Integer startTimeout;
    private Integer maxDuration;

    public Collection<String> getUuid() {
        return uuid;
    }

    public void setUuid(Collection<String> uuid) {
        this.uuid = uuid;
    }

    public Language getLanguage() {
        return language;
    }

    /**
     * @param language expected language of the user's speech. If empty, default value is en-US.
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    public Integer getEndOnSilence() {
        return endOnSilence;
    }

    /**
     * Controlls how long the system will wait after user stops speaking to decide the input is completed.
     * Timeout range 1-10 seconds. If empty,Default value is 2
     *
     * @param endOnSilence wait time for voice input to complete
     */
    public void setEndOnSilence(Integer endOnSilence) {
        this.endOnSilence = endOnSilence;
    }

    public Collection<String> getContext() {
        return context;
    }

    /**
     * List of hints to improve recognition quality if certain words are expected from the user.
     *
     * @param context list of hints
     */
    public void setContext(Collection<String> context) {
        this.context = context;
    }

    public Integer getStartTimeout() {
        return startTimeout;
    }

    /**
     * Controls how long the system will wait for the user to start speaking.
     * Timeout range 1-10 seconds.
     *
     * @param startTimeout timeout for voice input initiation
     */
    public void setStartTimeout(Integer startTimeout) {
        this.startTimeout = startTimeout;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    /**
     * Controls maximum speech duration from the moment user starts speaking. Default value is 60
     *
     * @param maxDuration speech duration starting from user's initiation of speech
     */
    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public enum Language {
        AFRIKAANS("af-ZA"),
        ALBANIAN("sq-AL"),
        AMHARIC("am-ET"),
        ARABIC_ALGERIA("ar-DZ"), ARABIC_BAHRAIN("ar-BH"), ARABIC_EGYPT("ar-EG"), ARABIC_IRAQ("ar-IQ"),
        ARABIC_ISRAEL("ar-IL"), ARABIC_JORDAN("ar-JO"), ARABIC_KUWAIT("ar-KW"), ARABIC_LEBANON("ar-LB"),
        ARABIC_MOROCCO("ar-MA"), ARABIC_OMAN("ar-OM"), ARABIC_QATAR("ar-QA"), ARABIC_SAUDI_ARABIA("ar-SA"),
        ARABIC_STATE_OF_PALESTINE("ar-PS"), ARABIC_TUNISIA("ar-TN"), ARABIC_UNITED_ARAB_EMIRATES("ar-AE"),
        ARMENIAN("hy-AM"),
        AZERBAIJANI("az-AZ"),
        BASQUE("eu-EX"),
        BENGALI_BANGLADESH("bn-BD"),
        BENGALI_INDIA("BN-IN"),
        BULGARIAN("bg-BG"),
        CATALAN("ca-ES"),
        CHINESE("zh"),
        MANDARIN_CHINESE_SIMPLIFIED("zh-cmn-Hans-CN"),
        CROATIAN("hr-HR"),
        CZECH("cs-CZ"),
        DANISH("da-DK"),
        DUTCH("nl-NL"),
        ENGLISH_AUSTRALIA("en-AU"), ENGLISH_CANADA("en-CA"), ENGLISH_GHANA("en-GH"), ENGLISH_INDIA("en-IN"),
        ENGLISH_IRELAND("en-IE"), ENGLISH_KENYA("en-KE"), ENGLISH_NEW_ZEALAND("en-NZ"), ENGLISH_NIGERIA("en-NG"),
        ENGLISH_PHILIPPINES("en-PH"), ENGLISH_SOUTH_AFRICA("en-ZA"), ENGLISH_TANZANIA("en-TZ"),
        ENGLISH_UNITED_KINGDOM("en-GB"), ENGLISH_UNITED_STATES("en-US"),
        FINNISH("fi-FI"),
        FRENCH_CANADA("fr-CA"),
        FRENCH_FRANCE("fr-FR"),
        GALICIAN("gl-ES"),
        GEORGIAN("ka-GE"),
        GERMAN("de-DE"),
        GREEK("el-GR"),
        GUJARATI("gu-IN"),
        HEBREW("he-IL"),
        HINDI("hi-IN"),
        HUNGARIAN("hu-HU"),
        ICELANDIC("is-IS"),
        INDONESIAN("id-ID"),
        ITALIAN("it-IT"),
        JAPANESE_JAPAN("ja-JP"),
        JAVANESE("jv-ID"),
        KANNADA("kn-IN"),
        KHMER("km-KH"),
        KOREAN("ko-KR"),
        LAO("lo-LA"),
        LATVIAN("lv-LV"),
        LITHUANIAN("lt-LT"),
        MALAY("ms-MY"),
        MALAYALAM("ml-IN"),
        MARATHI("mr-IN"),
        NEPALI("-"),
        NORWEGIAN_BOKMAL("nb-NO"),
        PERSIAN("fa-IR"),
        POLISH("pl-PL"),
        PORTUGUESE_BRAZIL("pt-BR"), PORTUGUESE_PORTUGAL("pt-PT"),
        ROMANIAN("ro-RO"),
        RUSSIAN("ru-RU"),
        SERBIAN("sr-RS"),
        SINHALA("si-LK"),
        SLOVAK("sk-SK"),
        SLOVENIAN("sl-SI"),
        SPANISH_ARGENTINA("es-AR"), SPANISH_BOLIVIA("es-BO"), SPANISH_CHILE("es-CL"), SPANISH_COLOMBIA("es-CO"),
        SPANISH_COSTA_RICA("es-CR"), SPANISH_DOMINICAN_REPUBLIC("es-DO"), SPANISH_ECUADOR("es-EC"), SPANISH_EL_SALVADOR("es-SV"),
        SPANISH_GUATEMALA("es-GT"), SPANISH_HONDURAS("es-HN"), SPANISH_MEXICO("es-MX"), SPANISH_NICARAGUA("es-NI"),
        SPANISH_PANAMA("es-PA"), SPANISH_PARAGUAY("es-PY"), SPANISH_PERU("es-PE"), SPANISH_PUERTO_RICO("es-PR"),
        SPANISH_SPAIN("es-ES"), SPANISH_UNITED_STATES("es-US"), SPANISH_URUGUAY("es-UY"), SPANISH_VENEZUELA("es-VE"),
        SUNDANESE("su-ID"),
        SWAHILI_KENYA("sw-KE"), SWAHILI_TANZANIA("sw-TZ"),
        SWEDISH("sv-SE"),
        TAMIL_INDIA("ta-IN"), TAMIL_MALAYSIA("ta-MY"), TAMIL_SINGAPORE("ta-SG"), TAMIL_SRI_LANKA("ta-LK"),
        TELUGU("te-IN"),
        THAI("th-TH"),
        TURKISH("tr-TR"),
        UKRAINIAN("uk-UA"),
        URDU_INDIA("ur-IN"), URDU_PAKISTAN("ur-PK"),
        VIETNAMESE("vi-VN"),
        ZULU("zu-ZA"),
        @JsonEnumDefaultValue
        UNKNOWN("Unknown");

        private String language;

        Language(String language) {
            this.language = language;
        }

        @JsonValue
        public String getLanguage() {
            return this.language;
        }
    }
}
