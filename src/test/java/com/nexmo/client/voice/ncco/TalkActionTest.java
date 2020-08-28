/*
 * Copyright (c) 2011-2017 Vonage Inc
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
package com.nexmo.client.voice.ncco;

import com.nexmo.client.voice.VoiceName;
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
                .voiceName(VoiceName.KIMBERLY)
                .build();

        String expectedJson = "[{\"text\":\"New Text Message\",\"bargeIn\":true,\"loop\":3,\"level\":0.3333,\"voiceName\":\"Kimberly\",\"action\":\"talk\"}]";
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
    public void testVoiceNameField() {
        TalkAction talk = TalkAction.builder("Talk to me").voiceName(VoiceName.JENNIFER).build();

        String expectedJson = "[{\"text\":\"Talk to me\",\"voiceName\":\"Jennifer\",\"action\":\"talk\"}]";
        assertEquals(expectedJson, new Ncco(talk).toJson());
    }
}
