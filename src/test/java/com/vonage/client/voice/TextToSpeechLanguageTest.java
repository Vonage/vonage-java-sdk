package com.vonage.client.voice;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextToSpeechLanguageTest {
    @Test
    public void testLanguageNames(){
        assertEquals("ar",TextToSpeechLanguage.ARABIC.getLanguage());
        assertEquals("ca-ES",TextToSpeechLanguage.SPANISH_CATALAN.getLanguage());
        assertEquals("cmn-CN",TextToSpeechLanguage.CHINESE_MANDARIN.getLanguage());
        assertEquals("cmn-TW",TextToSpeechLanguage.TAIWANESE_MANDARIN.getLanguage());
        assertEquals("cs-CZ",TextToSpeechLanguage.CZECH.getLanguage());
        assertEquals("cy-GB",TextToSpeechLanguage.WELSH.getLanguage());
        assertEquals("da-DK",TextToSpeechLanguage.DANISH.getLanguage());
        assertEquals("de-DE",TextToSpeechLanguage.GERMAN.getLanguage());
        assertEquals("el-GR",TextToSpeechLanguage.GREEK.getLanguage());
        assertEquals("en-AU",TextToSpeechLanguage.AUSTRALIAN_ENGLISH.getLanguage());
        assertEquals("en-GB", TextToSpeechLanguage.UNITED_KINGDOM_ENGLISH.getLanguage());
        assertEquals("en-GB-WLS",TextToSpeechLanguage.WELSH_ENGLISH.getLanguage());
        assertEquals("en-IN",TextToSpeechLanguage.INDIAN_ENGLISH.getLanguage());
        assertEquals("en-US",TextToSpeechLanguage.AMERICAN_ENGLISH.getLanguage());
        assertEquals("en-ZA",TextToSpeechLanguage.SOUTH_AFRICAN_ENGLISH.getLanguage());
        assertEquals("es-ES",TextToSpeechLanguage.SPANISH.getLanguage());
        assertEquals("es-MX",TextToSpeechLanguage.MEXICAN_SPANISH.getLanguage());
        assertEquals("es-US",TextToSpeechLanguage.AMERICAN_SPANISH.getLanguage());
        assertEquals("eu-ES",TextToSpeechLanguage.BASQUE.getLanguage());
        assertEquals("fi-FI",TextToSpeechLanguage.FINISH.getLanguage());
        assertEquals("fil-PH",TextToSpeechLanguage.FILIPINO.getLanguage());
        assertEquals("fr-CA",TextToSpeechLanguage.CANADIAN_FRENCH.getLanguage());
        assertEquals("he-IL",TextToSpeechLanguage.HEBREW.getLanguage());
        assertEquals("hi-IN",TextToSpeechLanguage.HINDI.getLanguage());
        assertEquals("hu-HU", TextToSpeechLanguage.HUNGARIAN.getLanguage());
        assertEquals("id-ID", TextToSpeechLanguage.INDONESIAN.getLanguage());
        assertEquals("is-IS",TextToSpeechLanguage.ICELANDIC.getLanguage());
        assertEquals("it-IT",TextToSpeechLanguage.ITALIAN.getLanguage());
        assertEquals("ja-JP",TextToSpeechLanguage.JAPANESE.getLanguage());
        assertEquals("ko-KR",TextToSpeechLanguage.KOREAN.getLanguage());
        assertEquals("nb-NO", TextToSpeechLanguage.NORWEGIAN.getLanguage());
        assertEquals("nl-NL", TextToSpeechLanguage.DUTCH.getLanguage());
        assertEquals("pl-PL",TextToSpeechLanguage.POLISH.getLanguage());
        assertEquals("pt-BR",TextToSpeechLanguage.BRAZILIAN_PORTUGUESE.getLanguage());
        assertEquals("pt-PT",TextToSpeechLanguage.PORTUGUESE.getLanguage());
        assertEquals("ro-RO",TextToSpeechLanguage.ROMANIAN.getLanguage());
        assertEquals("ru-RU",TextToSpeechLanguage.RUSSIAN.getLanguage());
        assertEquals("sk-SK",TextToSpeechLanguage.SLOVAK.getLanguage());
        assertEquals("sv-SE",TextToSpeechLanguage.SWEDISH.getLanguage());
        assertEquals("th-TH",TextToSpeechLanguage.THAI.getLanguage());
        assertEquals("tr-TR", TextToSpeechLanguage.TURKISH.getLanguage());
        assertEquals("uk-UA",TextToSpeechLanguage.UKRAINIAN.getLanguage());
        assertEquals("vi-VN",TextToSpeechLanguage.VIETNAMESE.getLanguage());
        assertEquals("yue-CN",TextToSpeechLanguage.CHINESE_YUE.getLanguage());
    }
}
