/*
 * Copyright (c) 2011-2018 Nexmo Inc
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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VoiceNameTest {
    @Test
    public void testGetVoiceNameFromString() {
        assertEquals(VoiceName.CARLA, VoiceName.fromString("Carla"));
    }

    @Test
    public void testDeserializeUnknownEnumsFallbackToUnknown() {
        assertEquals(VoiceName.UNKNOWN, VoiceName.fromString("Test Unknown Voice Name"));
    }

    @Test
    public void testEachVoiceNameForCorrectCapitalization() {
        // Each voice name should have the first character capitalized.
        assertEquals(VoiceName.SALLI.getDisplayName(), "Salli");
        assertEquals(VoiceName.JOEY.getDisplayName(), "Joey");
        assertEquals(VoiceName.NAJA.getDisplayName(), "Naja");
        assertEquals(VoiceName.MADS.getDisplayName(), "Mads");
        assertEquals(VoiceName.MARLENE.getDisplayName(), "Marlene");
        assertEquals(VoiceName.HANS.getDisplayName(), "Hans");
        assertEquals(VoiceName.NICOLE.getDisplayName(), "Nicole");
        assertEquals(VoiceName.RUSSELL.getDisplayName(), "Russell");
        assertEquals(VoiceName.AMY.getDisplayName(), "Amy");
        assertEquals(VoiceName.BRIAN.getDisplayName(), "Brian");
        assertEquals(VoiceName.EMMA.getDisplayName(), "Emma");
        assertEquals(VoiceName.GWYNETH.getDisplayName(), "Gwyneth");
        assertEquals(VoiceName.GERAINT.getDisplayName(), "Geraint");
        assertEquals(VoiceName.RAVEENA.getDisplayName(), "Raveena");
        assertEquals(VoiceName.CHIPMUNK.getDisplayName(), "Chipmunk");
        assertEquals(VoiceName.ERIC.getDisplayName(), "Eric");
        assertEquals(VoiceName.IVY.getDisplayName(), "Ivy");
        assertEquals(VoiceName.JENNIFER.getDisplayName(), "Jennifer");
        assertEquals(VoiceName.JUSTIN.getDisplayName(), "Justin");
        assertEquals(VoiceName.KENDRA.getDisplayName(), "Kendra");
        assertEquals(VoiceName.KIMBERLY.getDisplayName(), "Kimberly");
        assertEquals(VoiceName.CONCHITA.getDisplayName(), "Conchita");
        assertEquals(VoiceName.ENRIQUE.getDisplayName(), "Enrique");
        assertEquals(VoiceName.PENELOPE.getDisplayName(), "Penelope");
        assertEquals(VoiceName.MIGUEL.getDisplayName(), "Miguel");
        assertEquals(VoiceName.CHANTAL.getDisplayName(), "Chantal");
        assertEquals(VoiceName.CELINE.getDisplayName(), "Celine");
        assertEquals(VoiceName.MATHIEU.getDisplayName(), "Mathieu");
        assertEquals(VoiceName.DORA.getDisplayName(), "Dora");
        assertEquals(VoiceName.KARL.getDisplayName(), "Karl");
        assertEquals(VoiceName.CARLA.getDisplayName(), "Carla");
        assertEquals(VoiceName.GIORGIO.getDisplayName(), "Giorgio");
        assertEquals(VoiceName.LIV.getDisplayName(), "Liv");
        assertEquals(VoiceName.LOTTE.getDisplayName(), "Lotte");
        assertEquals(VoiceName.RUBEN.getDisplayName(), "Ruben");
        assertEquals(VoiceName.AGNIESZKA.getDisplayName(), "Agnieszka");
        assertEquals(VoiceName.JACEK.getDisplayName(), "Jacek");
        assertEquals(VoiceName.EWA.getDisplayName(), "Ewa");
        assertEquals(VoiceName.JAN.getDisplayName(), "Jan");
        assertEquals(VoiceName.MAJA.getDisplayName(), "Maja");
        assertEquals(VoiceName.VITORIA.getDisplayName(), "Vitoria");
        assertEquals(VoiceName.RICARDO.getDisplayName(), "Ricardo");
        assertEquals(VoiceName.CRISTIANO.getDisplayName(), "Cristiano");
        assertEquals(VoiceName.INES.getDisplayName(), "Ines");
        assertEquals(VoiceName.CARMEN.getDisplayName(), "Carmen");
        assertEquals(VoiceName.MAXIM.getDisplayName(), "Maxim");
        assertEquals(VoiceName.TATYANA.getDisplayName(), "Tatyana");
        assertEquals(VoiceName.ASTRID.getDisplayName(), "Astrid");
        assertEquals(VoiceName.FILIZ.getDisplayName(), "Filiz");
        assertEquals(VoiceName.MIZUKI.getDisplayName(), "Mizuki");
        assertEquals(VoiceName.SEOYEON.getDisplayName(), "Seoyeon");
        assertEquals(VoiceName.LAILA.getDisplayName(), "Laila");
        assertEquals(VoiceName.MAGED.getDisplayName(), "Maged");
        assertEquals(VoiceName.TARIK.getDisplayName(), "Tarik");
        assertEquals(VoiceName.DAMAYANTI.getDisplayName(), "Damayanti");
        assertEquals(VoiceName.MIREN.getDisplayName(), "Miren");
        assertEquals(VoiceName.SIN_JI.getDisplayName(), "Sin-Ji");
        assertEquals(VoiceName.JORDI.getDisplayName(), "Jordi");
        assertEquals(VoiceName.MONTSERRAT.getDisplayName(), "Montserrat");
        assertEquals(VoiceName.IVETA.getDisplayName(), "Iveta");
        assertEquals(VoiceName.ZUZANA.getDisplayName(), "Zuzana");
        assertEquals(VoiceName.TESSA.getDisplayName(), "Tessa");
        assertEquals(VoiceName.SATU.getDisplayName(), "Satu");
        assertEquals(VoiceName.MELINA.getDisplayName(), "Melina");
        assertEquals(VoiceName.NIKOS.getDisplayName(), "Nikos");
        assertEquals(VoiceName.CARMIT.getDisplayName(), "Carmit");
        assertEquals(VoiceName.LEKHA.getDisplayName(), "Lekha");
        assertEquals(VoiceName.MARISKA.getDisplayName(), "Mariska");
        assertEquals(VoiceName.SORA.getDisplayName(), "Sora");
        assertEquals(VoiceName.TIAN_TIAN.getDisplayName(), "Tian-Tian");
        assertEquals(VoiceName.MEI_JIA.getDisplayName(), "Mei-Jia");
        assertEquals(VoiceName.NORA.getDisplayName(), "Nora");
        assertEquals(VoiceName.HENRIK.getDisplayName(), "Henrik");
        assertEquals(VoiceName.LUCIANA.getDisplayName(), "Luciana");
        assertEquals(VoiceName.FELIPE.getDisplayName(), "Felipe");
        assertEquals(VoiceName.CATARINA.getDisplayName(), "Catarina");
        assertEquals(VoiceName.JOANA.getDisplayName(), "Joana");
        assertEquals(VoiceName.IOANA.getDisplayName(), "Ioana");
        assertEquals(VoiceName.LAURA.getDisplayName(), "Laura");
        assertEquals(VoiceName.ALVA.getDisplayName(), "Alva");
        assertEquals(VoiceName.OSKAR.getDisplayName(), "Oskar");
        assertEquals(VoiceName.KANYA.getDisplayName(), "Kanya");
        assertEquals(VoiceName.CEM.getDisplayName(), "Cem");
        assertEquals(VoiceName.YELDA.getDisplayName(), "Yelda");
        assertEquals(VoiceName.EMPAR.getDisplayName(), "Empar");
        assertEquals(VoiceName.UNKNOWN.getDisplayName(), "Unknown");
    }
}
