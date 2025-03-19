/*
 *   Copyright 2025 Vonage
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
        assertNull(fromString(null));
        assertNull(fromString(" "));
        assertEquals(AFRIKAANS, fromString("af-ZA"));
        assertEquals(ARABIC, fromString("ar"));
        assertEquals(BULGARIAN, fromString("bg-BG"));
        assertEquals(BENGALI, fromString("bn-IN"));
        assertEquals(SPANISH_CATALAN, fromString("ca-ES"));
        assertEquals(CHINESE_MANDARIN, fromString("cmn-CN"));
        assertEquals(TAIWANESE_MANDARIN, fromString("cmn-TW"));
        assertEquals(CZECH, fromString("cs-CZ"));
        assertEquals(WELSH, fromString("cy-GB"));
        assertEquals(DANISH, fromString("da-DK"));
        assertEquals(AUSTRIAN_GERMAN, fromString("de-AT"));
        assertEquals(GERMAN, fromString("de-DE"));
        assertEquals(GREEK, fromString("el-GR"));
        assertEquals(AUSTRALIAN_ENGLISH, fromString("en-AU"));
        assertEquals(UNITED_KINGDOM_ENGLISH, fromString("en-GB"));
        assertEquals(SCOTTISH_ENGLISH, fromString("en-GB-SCT"));
        assertEquals(WELSH_ENGLISH, fromString("en-GB-WLS"));
        assertEquals(IRISH_ENGLISH, fromString("en-IE"));
        assertEquals(INDIAN_ENGLISH, fromString("en-IN"));
        assertEquals(NEW_ZEALAND_ENGLISH, fromString("en-NZ"));
        assertEquals(AMERICAN_ENGLISH, fromString("en-US"));
        assertEquals(SOUTH_AFRICAN_ENGLISH, fromString("en-ZA"));
        assertEquals(COLOMBIAN_SPANISH, fromString("es-CO"));
        assertEquals(SPANISH, fromString("es-ES"));
        assertEquals(MEXICAN_SPANISH, fromString("es-MX"));
        assertEquals(AMERICAN_SPANISH, fromString("es-US"));
        assertEquals(BASQUE, fromString("eu-ES"));
        assertEquals(FINISH, fromString("fi-FI"));
        assertEquals(FILIPINO, fromString("fil-PH"));
        assertEquals(CANADIAN_FRENCH, fromString("fr-CA"));
        assertEquals(FRENCH, fromString("fr-FR"));
        assertEquals(SPANISH_GALICIAN, fromString("gl-ES"));
        assertEquals(GUJARATI, fromString("gu-IN"));
        assertEquals(HEBREW, fromString("he-IL"));
        assertEquals(HINDI, fromString("hi-IN"));
        assertEquals(HUNGARIAN, fromString("hu-HU"));
        assertEquals(INDONESIAN, fromString("id-ID"));
        assertEquals(ICELANDIC, fromString("is-IS"));
        assertEquals(ITALIAN, fromString("it-IT"));
        assertEquals(JAPANESE, fromString("ja-JP"));
        assertEquals(KANNADA, fromString("kn-IN"));
        assertEquals(KOREAN, fromString("ko-KR"));
        assertEquals(LATVIAN, fromString("lv-LV"));
        assertEquals(MALAYALAM, fromString("ml-IN"));
        assertEquals(MALAY, fromString("ms-MY"));
        assertEquals(NORWEGIAN_BOKMAL, fromString("nb-NO"));
        assertEquals(NORWEGIAN, fromString("no-NO"));
        assertEquals(BELGIAN_DUTCH, fromString("nl-BE"));
        assertEquals(DUTCH, fromString("nl-NL"));
        assertEquals(PUNJABI, fromString("pa-IN"));
        assertEquals(POLISH, fromString("pl-PL"));
        assertEquals(BRAZILIAN_PORTUGUESE, fromString("pt-BR"));
        assertEquals(PORTUGUESE, fromString("pt-PT"));
        assertEquals(ROMANIAN, fromString("ro-RO"));
        assertEquals(RUSSIAN, fromString("ru-RU"));
        assertEquals(SLOVAK, fromString("sk-SK"));
        assertEquals(SERBIAN, fromString("sr-RS"));
        assertEquals(SWEDISH, fromString("sv-SE"));
        assertEquals(TAMIL, fromString("ta-IN"));
        assertEquals(TELUGU, fromString("te-IN"));
        assertEquals(THAI, fromString("th-TH"));
        assertEquals(TURKISH, fromString("tr-TR"));
        assertEquals(UKRAINIAN, fromString("uk-UA"));
        assertEquals(VIETNAMESE, fromString("vi-VN"));
        assertEquals(CHINESE_YUE, fromString("yue-CN"));
        assertEquals(UNKNOWN, fromString("Klingon"));
    }

    @Test
    public void testToString() {
        for (var language : TextToSpeechLanguage.values()) {
            assertEquals(language, fromString(language.toString()));
        }
    }
}
