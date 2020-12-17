/*
 *   Copyright 2020 Vonage
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Voice used to deliver text to a {@link Call} in a {@link TalkRequest}.
 */

@Deprecated
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
