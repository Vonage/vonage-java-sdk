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
package com.nexmo.client.voice.ncco;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TalkNccoTest {
    @Test
    public void testJsonHasTalkAndActionWhenGivenOnlyRequiredParameters() {
        String json = "{\"text\":\"Talk to me\",\"action\":\"talk\"}";
        TalkNcco ncco = new TalkNcco("Talk to me");
        assertEquals(json, ncco.toJson());
    }

    @Test
    public void testJsonHasBargeInWhenProvidedAndIsTrue() {
        String json = "{\"text\":\"Talk to me\",\"bargeIn\":true,\"action\":\"talk\"}";
        TalkNcco ncco = new TalkNcco("Talk to me");
        ncco.setBargeIn(true);
        assertEquals(json, ncco.toJson());
    }

    @Test
    public void testJsonHasBargeInWhenProvidedAndIsFalse() {
        String json = "{\"text\":\"Talk to me\",\"bargeIn\":false,\"action\":\"talk\"}";
        TalkNcco ncco = new TalkNcco("Talk to me");
        ncco.setBargeIn(false);
        assertEquals(json, ncco.toJson());
    }

    @Test
    public void testJsonHasLoopWhenProvided() {
        String json = "{\"text\":\"Talk to me\",\"loop\":3,\"action\":\"talk\"}";
        TalkNcco ncco = new TalkNcco("Talk to me");
        ncco.setLoop(3);
        assertEquals(json, ncco.toJson());
    }

    @Test
    public void testJsonHasLevelWhenProvided() {
        String json = "{\"text\":\"Talk to me\",\"level\":-0.34,\"action\":\"talk\"}";
        TalkNcco ncco = new TalkNcco("Talk to me");
        ncco.setLevel(new BigDecimal("-0.34"));
        assertEquals(json, ncco.toJson());
    }

    @Test
    public void testJsonHasVoiceNameWhenProvided() {
        String json = "{\"text\":\"Talk to me\",\"voiceName\":\"Kimberly\",\"action\":\"talk\"}";
        TalkNcco ncco = new TalkNcco("Talk to me");
        ncco.setVoiceName("Kimberly");
        assertEquals(json, ncco.toJson());
    }
}