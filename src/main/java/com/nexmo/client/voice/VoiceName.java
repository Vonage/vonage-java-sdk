/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.voice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Voice used to deliver text to a {@link Call} in a {@link TalkRequest}.
 */

public enum VoiceName {
    SALLI("Salli", "en-US", Gender.FEMALE, true),
    JOEY("Joey", "en-US", Gender.MALE, true),
    NAJA("Naja", "da-DK", Gender.FEMALE, true),
    MADS("Mads", "da-DK", Gender.MALE, true),
    MARLENE("Marlene", "de-DE", Gender.FEMALE, true),
    HANS("Hans", "de-DE", Gender.MALE, true),
    NICOLE("Nicole", "en-AU", Gender.FEMALE, true),
    RUSSELL("Russell", "en-AU", Gender.MALE, true),
    AMY("Amy", "en-GB", Gender.FEMALE, true),
    BRIAN("Brian", "en-GB", Gender.MALE, true),
    EMMA("Emma", "en-GB", Gender.FEMALE, true),
    GWYNETH("Gwyneth", "en-GB", Gender.FEMALE, true),
    GWYNETH_CY("Gwyneth", "cy-GB", Gender.FEMALE, true),
    GERAINT("Geraint", "en-GB", Gender.MALE, true),
    GERAINT_CY("Geraint", "cy-GB", Gender.MALE, true),
    RAVEENA("Raveena", "en-IN", Gender.FEMALE, true),
    IVY("Ivy", "en-US", Gender.FEMALE, true),
    MATTHEW("Matthew", "en-US", Gender.MALE, true),
    JUSTIN("Justin", "en-US", Gender.MALE, true),
    KENDRA("Kendra", "en-US", Gender.FEMALE, true),
    KIMBERLY("Kimberly", "en-US", Gender.FEMALE, true),
    JOANNA("Joanna", "en-US", Gender.FEMALE, true),
    CONCHITA("Conchita", "es-ES", Gender.FEMALE, true),
    ENRIQUE("Enrique", "es-ES", Gender.MALE, true),
    PENELOPE("Penelope", "es-US", Gender.FEMALE, true),
    MIGUEL("Miguel", "es-US", Gender.MALE, true),
    CHANTAL("Chantal", "fr-CA", Gender.FEMALE, true),
    CELINE("Celine", "fr-FR", Gender.FEMALE, true),
    MATHIEU("Mathieu", "fr-FR", Gender.MALE, true),
    ADITI("Aditi", "hi-IN", Gender.FEMALE, true),
    DORA("Dora", "is-IS", Gender.FEMALE, true),
    KARL("Karl", "is-IS", Gender.MALE, true),
    CARLA("Carla", "it-IT", Gender.FEMALE, true),
    GIORGIO("Giorgio", "it-IT", Gender.MALE, true),
    LIV("Liv", "nb-NO", Gender.FEMALE, true),
    LOTTE("Lotte", "nl-NL", Gender.FEMALE, true),
    RUBEN("Ruben", "nl-NL", Gender.MALE, true),
    JACEK("Jacek", "pl-PL", Gender.MALE, true),
    EWA("Ewa", "pl-PL", Gender.FEMALE, true),
    JAN("Jan", "pl-PL", Gender.MALE, true),
    MAJA("Maja", "pl-PL", Gender.FEMALE, true),
    VITORIA("Vitoria", "pt-BR", Gender.FEMALE, true),
    RICARDO("Ricardo", "pt-BR", Gender.MALE, true),
    CRISTIANO("Cristiano", "pt-PT", Gender.MALE, true),
    INES("Ines", "pt-PT", Gender.FEMALE, true),
    CARMEN("Carmen", "ro-RO", Gender.FEMALE, true),
    MAXIM("Maxim", "ru-RU", Gender.MALE, true),
    TATYANA("Tatyana", "ru-RU", Gender.FEMALE, true),
    ASTRID("Astrid", "sv-SE", Gender.FEMALE, true),
    FILIZ("Filiz", "tr-TR", Gender.FEMALE, true),
    MIZUKI("Mizuki", "ja-JP", Gender.FEMALE, true),
    SEOYEON("Seoyeon", "ko-KR", Gender.FEMALE, true),
    ZEINA("Zeina", "ar", Gender.FEMALE, true),
    ZHIYU("Zhiyu", "cmn-CN", Gender.FEMALE, true),
    LEA("Lea", "fr-FR", Gender.FEMALE, true),
    VICKI("Vicki", "de-DE", Gender.FEMALE, true),
    BIANCA("Bianca", "it-IT", Gender.FEMALE, true),
    TAKUMI("Takumi", "ja-JP", Gender.MALE, true),
    MIA("Mia", "es-MX", Gender.FEMALE, true),
    LUCIA("Lucia", "es-ES", Gender.FEMALE, true),
    LAILA("Laila", "ara-XWW", Gender.FEMALE, false),
    MAGED("Maged", "ara-XWW", Gender.MALE, false),
    TARIK("Tarik", "ara-XWW", Gender.MALE, false),
    DAMAYANTI("Damayanti", "ind-IDN", Gender.FEMALE, false),
    MIREN("Miren", "baq-ESP", Gender.FEMALE, false),
    SIN_JI("Sin-Ji", "yue-CHN", Gender.FEMALE, false),
    JORDI("Jordi", "cat-ESP", Gender.MALE, false),
    MONTSERRAT("Montserrat", "cat-ESP", Gender.FEMALE, false),
    IVETA("Iveta", "ces-CZE", Gender.FEMALE, false),
    ZUZANA("Zuzana", "ces-CZE", Gender.FEMALE, false),
    TESSA("Tessa", "eng-ZAF", Gender.FEMALE, false),
    SATU("Satu", "fin-FIN", Gender.FEMALE, false),
    MELINA("Melina", "ell-GRC", Gender.FEMALE, false),
    NIKOS("Nikos", "ell-GRC", Gender.MALE, false),
    CARMIT("Carmit", "heb-ISR", Gender.FEMALE, false),
    LEKHA("Lekha", "hin-IND", Gender.FEMALE, false),
    MARISKA("Mariska", "hun-HUN", Gender.FEMALE, false),
    SORA("Sora", "kor-KOR", Gender.FEMALE, false),
    TIAN_TIAN("Tian-Tian", "cmn-CHN", Gender.FEMALE, false),
    MEI_JIA("Mei-Jia", "cmn-TWN", Gender.FEMALE, false),
    NORA("Nora", "nor-NOR", Gender.FEMALE, false),
    HENRIK("Henrik", "nor-NOR", Gender.MALE, false),
    LUCIANA("Luciana", "por-BRA", Gender.FEMALE, false),
    FELIPE("Felipe", "por-BRA", Gender.MALE, false),
    CATARINA("Catarina", "por-PRT", Gender.FEMALE, false),
    JOANA("Joana", "por-PRT", Gender.FEMALE, false),
    IOANA("Ioana", "ron-ROU", Gender.FEMALE, false),
    LAURA("Laura", "slk-SVK", Gender.FEMALE, false),
    ALVA("Alva", "swe-SWE", Gender.FEMALE, false),
    OSKAR("Oskar", "swe-SWE", Gender.MALE, false),
    KANYA("Kanya", "tha-THA", Gender.FEMALE, false),
    CEM("Cem", "tur-TUR", Gender.MALE, false),
    YELDA("Yelda", "tur-TUR", Gender.FEMALE, false),
    EMPAR("Empar", "spa-ESP", Gender.FEMALE, false),
    /**
    * Now redirects to Justin.
    */
    @Deprecated
    CHIPMUNK("Chipmunk", "en-US", Gender.MALE, true),
    /**
    * Now redirects to Justin.
    */
    @Deprecated
    ERIC("Eric", "en-US", Gender.MALE, true),
    /**
    * Now redirects to Kimberly.
    */
    @Deprecated
    JENNIFER("Jennifer", "en-US", Gender.FEMALE, true),
    /**
    * Now redirects to Ewa.
    */
    @Deprecated
    AGNIESZKA("Agnieszka", "pl-PL", Gender.FEMALE, true),
    UNKNOWN("Unknown", "Unknown", Gender.UNKNOWN, false);

    private static final Map<String, VoiceName> voiceNameIndex = new HashMap<>();

    static {
        for (VoiceName voiceName : VoiceName.values()) {
            voiceNameIndex.put(voiceName.name(), voiceName);
        }
    }

    private final String displayName;
    private final String language;
    private final Gender gender;
    private final boolean supportSSML;

    VoiceName(String displayName, String language, Gender gender, boolean supportSSML) {
        this.displayName = displayName;
        this.gender = gender;
        this.language = language;
        this.supportSSML = supportSSML;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static VoiceName fromString(String name) {
        VoiceName foundVoiceName = voiceNameIndex.get(name.toUpperCase());
        return (foundVoiceName != null) ? foundVoiceName : UNKNOWN;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public String getLanguage() {
        return language;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean isSupportSSML() {
        return supportSSML;
    }

}
