package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TextToSpeechLanguage {
    ARABIC("ar"),
    SPANISH_CATALAN("ca-ES"),
    CHINESE_MANDARIN("cmn-CN"),
    TAIWANESE_MANDARIN("cmn-TW"),
    CZECH("cs-CZ"),
    WELSH("cy-GB"),
    DANISH("da-DK"),
    GERMAN("de-DE"),
    GREEK("el-GR"),
    AUSTRALIAN_ENGLISH("en-AU"),
    UNITED_KINGDOM_ENGLISH("en-GB"),
    WELSH_ENGLISH("en-GB-WLS"),
    INDIAN_ENGLISH("en-IN"),
    AMERICAN_ENGLISH("en-US"),
    SOUTH_AFRICAN_ENGLISH("en-ZA"),
    SPANISH("es-ES"),
    MEXICAN_SPANISH("es-MX"),
    AMERICAN_SPANISH("es-US"),
    BASQUE("eu-ES"),
    FINISH("fi-FI"),
    FILIPINO("fil-PH"),
    CANADIAN_FRENCH("fr-CA"),
    FRENCH("fr-FR"),
    HEBREW("he-IL"),
    HINDI("hi-IN"),
    HUNGARIAN("hu-HU"),
    INDONESIAN("id-ID"),
    ICELANDIC("is-IS"),
    ITALIAN("it-IT"),
    JAPANESE("ja-JP"),
    KOREAN("ko-KR"),
    NORWEGIAN("no-NO"),
    NORWEGIAN_BOKMAL("nb-NO"),
    DUTCH("nl-NL"),
    POLISH("pl-PL"),
    BRAZILIAN_PORTUGUESE("pt-BR"),
    PORTUGUESE("pt-PT"),
    ROMANIAN("ro-RO"),
    RUSSIAN("ru-RU"),
    SLOVAK("sk-SK"),
    SWEDISH("sv-SE"),
    THAI("th-TH"),
    TURKISH("tr-TR"),
    UKRAINIAN("uk-UA"),
    VIETNAMESE("vi-VN"),
    CHINESE_YUE("yue-CN"),
    @JsonEnumDefaultValue
    UNKNOWN("Unknown");
    private final String language;
    TextToSpeechLanguage(String language){this.language=language;}
    @JsonValue
    public String getLanguage(){return this.language;}
}
