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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.vonage.client.voice.TextToSpeechLanguage.*;

public class TextToSpeechLanguageTest {

    @Test
    public void testLanguageNames() {
        assertEquals("af-ZA", AFRIKAANS.getLanguage());
        assertEquals("ar", ARABIC.getLanguage());
        assertEquals("bg-BG", BULGARIAN.getLanguage());
        assertEquals("bn-IN", BENGALI.getLanguage());
        assertEquals("ca-ES", SPANISH_CATALAN.getLanguage());
        assertEquals("cmn-CN", CHINESE_MANDARIN.getLanguage());
        assertEquals("cmn-TW", TAIWANESE_MANDARIN.getLanguage());
        assertEquals("cs-CZ", CZECH.getLanguage());
        assertEquals("cy-GB", WELSH.getLanguage());
        assertEquals("da-DK", DANISH.getLanguage());
        assertEquals("de-AT", AUSTRIAN_GERMAN.getLanguage());
        assertEquals("de-DE", GERMAN.getLanguage());
        assertEquals("el-GR", GREEK.getLanguage());
        assertEquals("en-AU", AUSTRALIAN_ENGLISH.getLanguage());
        assertEquals("en-GB", UNITED_KINGDOM_ENGLISH.getLanguage());
        assertEquals("en-GB-SCT", SCOTTISH_ENGLISH.getLanguage());
        assertEquals("en-GB-WLS", WELSH_ENGLISH.getLanguage());
        assertEquals("en-IE", IRISH_ENGLISH.getLanguage());
        assertEquals("en-IN", INDIAN_ENGLISH.getLanguage());
        assertEquals("en-NZ", NEW_ZEALAND_ENGLISH.getLanguage());
        assertEquals("en-US", AMERICAN_ENGLISH.getLanguage());
        assertEquals("en-ZA", SOUTH_AFRICAN_ENGLISH.getLanguage());
        assertEquals("es-CO", COLOMBIAN_SPANISH.getLanguage());
        assertEquals("es-ES", SPANISH.getLanguage());
        assertEquals("es-MX", MEXICAN_SPANISH.getLanguage());
        assertEquals("es-US", AMERICAN_SPANISH.getLanguage());
        assertEquals("eu-ES", BASQUE.getLanguage());
        assertEquals("fi-FI", FINISH.getLanguage());
        assertEquals("fil-PH", FILIPINO.getLanguage());
        assertEquals("fr-CA", CANADIAN_FRENCH.getLanguage());
        assertEquals("fr-FR", FRENCH.getLanguage());
        assertEquals("gl-ES", SPANISH_GALICIAN.getLanguage());
        assertEquals("gu-IN", GUJARATI.getLanguage());
        assertEquals("he-IL", HEBREW.getLanguage());
        assertEquals("hi-IN", HINDI.getLanguage());
        assertEquals("hu-HU", HUNGARIAN.getLanguage());
        assertEquals("id-ID", INDONESIAN.getLanguage());
        assertEquals("is-IS", ICELANDIC.getLanguage());
        assertEquals("it-IT", ITALIAN.getLanguage());
        assertEquals("ja-JP", JAPANESE.getLanguage());
        assertEquals("kn-IN", KANNADA.getLanguage());
        assertEquals("ko-KR", KOREAN.getLanguage());
        assertEquals("lv-LV", LATVIAN.getLanguage());
        assertEquals("ml-IN", MALAYALAM.getLanguage());
        assertEquals("ms-MY", MALAY.getLanguage());
        assertEquals("nb-NO", NORWEGIAN_BOKMAL.getLanguage());
        assertEquals("no-NO", NORWEGIAN.getLanguage());
        assertEquals("nl-BE", BELGIAN_DUTCH.getLanguage());
        assertEquals("nl-NL", DUTCH.getLanguage());
        assertEquals("pa-IN", PUNJABI.getLanguage());
        assertEquals("pl-PL", POLISH.getLanguage());
        assertEquals("pt-BR", BRAZILIAN_PORTUGUESE.getLanguage());
        assertEquals("pt-PT", PORTUGUESE.getLanguage());
        assertEquals("ro-RO", ROMANIAN.getLanguage());
        assertEquals("ru-RU", RUSSIAN.getLanguage());
        assertEquals("sk-SK", SLOVAK.getLanguage());
        assertEquals("sr-RS", SERBIAN.getLanguage());
        assertEquals("sv-SE", SWEDISH.getLanguage());
        assertEquals("ta-IN", TAMIL.getLanguage());
        assertEquals("te-IN", TELUGU.getLanguage());
        assertEquals("th-TH", THAI.getLanguage());
        assertEquals("tr-TR", TURKISH.getLanguage());
        assertEquals("uk-UA", UKRAINIAN.getLanguage());
        assertEquals("vi-VN", VIETNAMESE.getLanguage());
        assertEquals("yue-CN", CHINESE_YUE.getLanguage());
        assertEquals("Unknown", UNKNOWN.getLanguage());
    }
}
