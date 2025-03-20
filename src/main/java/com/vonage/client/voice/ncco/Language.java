package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the languages supported by the ASR (Automatic Speech Recognition) feature.
 *
 * @since 9.0.0 Moved from {@linkplain SpeechSettings}.
 */
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
    NEPALI("ne-NP"),
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

    private final String language;

    @JsonCreator
    Language(String language) {
        this.language = language;
    }

    /**
     * Retrieve the language code associated with this enum.
     *
     * @return The language code as a string.
     */
    @JsonValue
    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return getLanguage();
    }
}
