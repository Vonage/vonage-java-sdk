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
package com.vonage.client.voice.ncco;

import com.vonage.client.voice.TextToSpeechLanguage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class TalkActionTest {
    @Test
    public void testBuilderMultipleInstances() {
        TalkAction.Builder builder = TalkAction.builder("Test message.");
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        TalkAction talk = TalkAction.builder("Test message")
                .text("New Text Message")
                .bargeIn(true)
                .loop(3)
                .level(0.3333f)
                .language(TextToSpeechLanguage.AMERICAN_ENGLISH)
                .style(2)
                .build();

        String expectedJson = "[{\"text\":\"New Text Message\",\"bargeIn\":true,\"loop\":3,\"level\":0.3333,\"language\":\"en-US\",\"style\":2,\"action\":\"talk\"}]";
        assertEquals(expectedJson, new Ncco(talk).toJson());
    }

    @Test
    public void testGetAction() {
        TalkAction talk = TalkAction.builder("Test message").build();
        assertEquals("talk", talk.getAction());
    }

    @Test
    public void testTalkField() {
        TalkAction talk = TalkAction.builder("Talk to me").text("Still talk to me").build();

        String expectedJson = "[{\"text\":\"Still talk to me\",\"action\":\"talk\"}]";
        assertEquals(expectedJson, new Ncco(talk).toJson());
    }

    @Test
    public void testBargeInField() {
        TalkAction talk = TalkAction.builder("Talk to me").bargeIn(true).build();

        String expectedJson = "[{\"text\":\"Talk to me\",\"bargeIn\":true,\"action\":\"talk\"}]";
        assertEquals(expectedJson, new Ncco(talk).toJson());
    }

    @Test
    public void testLoopField() {
        TalkAction talk = TalkAction.builder("Talk to me").loop(3).build();

        String expectedJson = "[{\"text\":\"Talk to me\",\"loop\":3,\"action\":\"talk\"}]";
        assertEquals(expectedJson, new Ncco(talk).toJson());
    }

    @Test
    public void testLevelField() {
        TalkAction talk = TalkAction.builder("Talk to me").level(-0.34f).build();

        String expectedJson = "[{\"text\":\"Talk to me\",\"level\":-0.34,\"action\":\"talk\"}]";
        assertEquals(expectedJson, new Ncco(talk).toJson());
    }

    @Test
    public void testPremiumField() {
        TalkAction talk = TalkAction.builder("Talk to me").premium(true).build();
        String expectedJson = "[{\"text\":\"Talk to me\",\"premium\":true,\"action\":\"talk\"}]";
        assertEquals(expectedJson, new Ncco(talk).toJson());

        talk = TalkAction.builder("Talk to me").premium(false).build();
        expectedJson = "[{\"text\":\"Talk to me\",\"premium\":false,\"action\":\"talk\"}]";
        assertEquals(expectedJson, new Ncco(talk).toJson());
    }
}
