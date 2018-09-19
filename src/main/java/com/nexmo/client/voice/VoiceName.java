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
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Voice used to deliver text to a {@link Call} in a {@link TalkRequest}.
 */

public enum VoiceName {
    SALLI,
    JOEY,
    NAJA,
    MADS,
    MARLENE,
    HANS,
    NICOLE,
    RUSSELL,
    AMY,
    BRIAN,
    EMMA,
    GWYNETH,
    GERAINT,
    RAVEENA,
    CHIPMUNK,
    ERIC,
    IVY,
    JENNIFER,
    JUSTIN,
    KENDRA,
    KIMBERLY,
    CONCHITA,
    ENRIQUE,
    PENELOPE,
    MIGUEL,
    CHANTAL,
    CELINE,
    MATHIEU,
    DORA,
    KARL,
    CARLA,
    GIORGIO,
    LIV,
    LOTTE,
    RUBEN,
    AGNIESZKA,
    JACEK,
    EWA,
    JAN,
    MAJA,
    VITORIA,
    RICARDO,
    CRISTIANO,
    INES,
    CARMEN,
    MAXIM,
    TATYANA,
    ASTRID,
    FILIZ,
    UNKNOWN;

    private static final Map<String, VoiceName> voiceNameIndex = new HashMap<>();

    static {
        for (VoiceName voiceName : VoiceName.values()) {
            voiceNameIndex.put(voiceName.name(), voiceName);
        }
    }

    @JsonValue
    @Override
    public String toString() {
        //API requires voice_name to be sent with first character upper cased
        return StringUtils.capitalize(name().toLowerCase());
    }

    @JsonCreator
    public static VoiceName fromString(String name) {
        VoiceName foundVoiceName = voiceNameIndex.get(name.toUpperCase());
        return (foundVoiceName != null) ? foundVoiceName : UNKNOWN;
    }

}
