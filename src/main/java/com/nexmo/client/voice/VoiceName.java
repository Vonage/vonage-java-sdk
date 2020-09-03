/*
 * Copyright (c) 2020 Vonage
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
    ADITI("Aditi"),
    AGNIESZKA("Agnieszka"),
    ALVA("Alva"),
    AMY("Amy"),
    ASTRID("Astrid"),
    BIANCA("Bianca"),
    BRIAN("Brian"),
    CARLA("Carla"),
    CARMEN("Carmen"),
    CARMIT("Carmit"),
    CATARINA("Catarina"),
    CELINE("Celine"),
    CEM("Cem"),
    CHANTAL("Chantal"),
    CHIPMUNK("Chipmunk"),
    CONCHITA("Conchita"),
    CRISTIANO("Cristiano"),
    DAMAYANTI("Damayanti"),
    EMPAR("Empar"),
    DORA("Dora"),
    EMMA("Emma"),
    ENRIQUE("Enrique"),
    ERIC("Eric"),
    EWA("Ewa"),
    FELIPE("Felipe"),
    FILIZ("Filiz"),
    GERAINT("Geraint"),
    GIORGIO("Giorgio"),
    GWYNETH("Gwyneth"),
    HANS("Hans"),
    HENRIK("Henrik"),
    INES("Ines"),
    IOANA("Ioana"),
    IVETA("Iveta"),
    IVY("Ivy"),
    JACEK("Jacek"),
    JAN("Jan"),
    JENNIFER("Jennifer"),
    JOANA("Joana"),
    JOEY("Joey"),
    JORDI("Jordi"),
    JUSTIN("Justin"),
    KANYA("Kanya"),
    KARL("Karl"),
    KENDRA("Kendra"),
    KIMBERLY("Kimberly"),
    LAILA("Laila"),
    LAURA("Laura"),
    LEA("Lea"),
    LEKHA("Lekha"),
    LIV("Liv"),
    LOTTE("Lotte"),
    LUCIA("Lucia"),
    LUCIANA("Luciana"),
    MADS("Mads"),
    MAGED("Maged"),
    MAJA("Maja"),
    MARISKA("Mariska"),
    MARLENE("Marlene"),
    MATHIEU("Mathieu"),
    MATTHEW("Matthew"),
    MAXIM("Maxim"),
    MEI_JIA("Mei-Jia"),
    MELINA("Melina"),
    MIA("Mia"),
    MIGUEL("Miguel"),
    MIREN("Miren"),
    MIZUKI("Mizuki"),
    MONTSERRAT("Montserrat"),
    NAJA("Naja"),
    NICOLE("Nicole"),
    NIKOS("Nikos"),
    NORA("Nora"),
    OSKAR("Oskar"),
    PENELOPE("Penelope"),
    RAVEENA("Raveena"),
    RICARDO("Ricardo"),
    RUBEN("Ruben"),
    RUSSELL("Russell"),
    SALLI("Salli"),
    SATU("Satu"),
    SEOYEON("Seoyeon"),
    SIN_JI("Sin-Ji"),
    SORA("Sora"),
    TAKUMI("Takumi"),
    TARIK("Tarik"),
    TATYANA("Tatyana"),
    TESSA("Tessa"),
    TIAN_TIAN("Tian-Tian"),
    VICKI("Vicki"),
    VITORIA("Vitoria"),
    YELDA("Yelda"),
    ZEINA("Zeina"),
    ZHIYU("Zhiyu"),
    ZUZANA("Zuzana"),
    UNKNOWN("Unknown");

    private static final Map<String, VoiceName> voiceNameIndex = new HashMap<>();

    static {
        for (VoiceName voiceName : VoiceName.values()) {
            voiceNameIndex.put(voiceName.name(), voiceName);
        }
    }

    private String displayName;

    VoiceName(String displayName) {
        this.displayName = displayName;
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
}
