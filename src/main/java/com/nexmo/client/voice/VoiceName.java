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
    SALLI("Salli"),
    JOEY("Joey"),
    MATTHEW("Matthew"),
    NAJA("Naja"),
    MADS("Mads"),
    MARLENE("Marlene"),
    HANS("Hans"),
    NICOLE("Nicole"),
    RUSSELL("Russell"),
    AMY("Amy"),
    BRIAN("Brian"),
    EMMA("Emma"),
    GWYNETH("Gwyneth"),
    GERAINT("Geraint"),
    RAVEENA("Raveena"),
    CHIPMUNK("Chipmunk"),
    ERIC("Eric"),
    IVY("Ivy"),
    JENNIFER("Jennifer"),
    JUSTIN("Justin"),
    KENDRA("Kendra"),
    KIMBERLY("Kimberly"),
    CONCHITA("Conchita"),
    ENRIQUE("Enrique"),
    PENELOPE("Penelope"),
    MIGUEL("Miguel"),
    CHANTAL("Chantal"),
    CELINE("Celine"),
    MATHIEU("Mathieu"),
    DORA("Dora"),
    KARL("Karl"),
    CARLA("Carla"),
    GIORGIO("Giorgio"),
    LIV("Liv"),
    LOTTE("Lotte"),
    RUBEN("Ruben"),
    AGNIESZKA("Agnieszka"),
    JACEK("Jacek"),
    EWA("Ewa"),
    JAN("Jan"),
    MAJA("Maja"),
    VITORIA("Vitoria"),
    RICARDO("Ricardo"),
    CRISTIANO("Cristiano"),
    INES("Ines"),
    CARMEN("Carmen"),
    MAXIM("Maxim"),
    TATYANA("Tatyana"),
    ASTRID("Astrid"),
    FILIZ("Filiz"),
    MIZUKI("Mizuki"),
    SEOYEON("Seoyeon"),
    LAILA("Laila"),
    MAGED("Maged"),
    TARIK("Tarik"),
    DAMAYANTI("Damayanti"),
    MIREN("Miren"),
    SIN_JI("Sin-Ji"),
    JORDI("Jordi"),
    MONTSERRAT("Montserrat"),
    IVETA("Iveta"),
    ZUZANA("Zuzana"),
    TESSA("Tessa"),
    SATU("Satu"),
    MELINA("Melina"),
    NIKOS("Nikos"),
    CARMIT("Carmit"),
    LEKHA("Lekha"),
    MARISKA("Mariska"),
    SORA("Sora"),
    TIAN_TIAN("Tian-Tian"),
    MEI_JIA("Mei-Jia"),
    NORA("Nora"),
    HENRIK("Henrik"),
    LUCIANA("Luciana"),
    FELIPE("Felipe"),
    CATARINA("Catarina"),
    JOANA("Joana"),
    IOANA("Ioana"),
    LAURA("Laura"),
    ALVA("Alva"),
    OSKAR("Oskar"),
    KANYA("Kanya"),
    CEM("Cem"),
    YELDA("Yelda"),
    EMPAR("Empar"),
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
