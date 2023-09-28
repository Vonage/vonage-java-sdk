/*
 *   Copyright 2023 Vonage
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

import static org.junit.Assert.*;
import org.junit.Test;

public class TalkPayloadTest {

    @Test
    public void testAllParams() {
        TalkPayload payload = TalkPayload.builder("Hola Mundo!")
                .language(TextToSpeechLanguage.NORWEGIAN)
                .style(2).premium(true).level(0.3).build();

        String expectedJson = "{\"text\":\"Hola Mundo!\",\"style\":2,\"level\":0.3,\"premium\":true," +
                "\"language\":\"no-NO\"}", actualJson = payload.toJson();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testRequiredParams() {
        String text = "Bonjour, wie geht es dier?";
        TalkPayload payload = TalkPayload.builder(text).build();
        assertEquals("{\"text\":\""+text+"\"}", payload.toJson());
        assertNull(payload.getLanguage());
        assertNull(payload.getLoop());
        assertNull(payload.getStyle());
        assertNull(payload.getLevel());
        assertNull(payload.getPremium());
    }

    @Test
    public void testNoText() {
        assertThrows(IllegalArgumentException.class, () -> TalkPayload.builder(null).build());
        assertThrows(IllegalArgumentException.class, () -> TalkPayload.builder("").build());
        assertThrows(IllegalArgumentException.class, () -> TalkPayload.builder(" ").build());
    }

    @Test
    public void testInvalidVolumeLevel() {
        TalkPayload.Builder builder = TalkPayload.builder("Guten tag");
        assertEquals(-1.0, builder.level(-1).build().getLevel(), 0.01);
        assertEquals(1.0, builder.level(1).build().getLevel(), 0.01);
        assertThrows(IllegalArgumentException.class, () -> builder.level(-1.1).build());
        assertThrows(IllegalArgumentException.class, () -> builder.level(1.1).build());
    }

    @Test
    public void testInvalidStyle() {
        TalkPayload.Builder builder = TalkPayload.builder("Guten tag");
        assertEquals(0, builder.style(0).build().getStyle().intValue());
        assertThrows(IllegalArgumentException.class, () -> builder.style(-1).build());
    }

    @Test
    public void testInvalidLoop() {
        TalkPayload.Builder builder = TalkPayload.builder("Guten tag");
        assertEquals(0, builder.loop(0).build().getLoop().intValue());
        assertThrows(IllegalArgumentException.class, () -> builder.loop(-1).build());
    }
}
