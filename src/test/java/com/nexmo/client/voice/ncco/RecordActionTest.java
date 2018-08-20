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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RecordActionTest {
    @Test
    public void testToJson() throws Exception {
        assertEquals("{\"action\":\"record\"}", new RecordAction().toJson());
    }

    @Test
    public void testJson() throws Exception {
        String json;
        {
            RecordAction ncco = new RecordAction();
            ncco.setEventUrl("https://api.example.com/event");
            ncco.setEventMethod("GET");
            ncco.setBeepStart(true);
            ncco.setEndOnKey('#');
            ncco.setEndOnSilence(3);
            ncco.setFormat(RecordingFormat.MP3);
            ncco.setTimeout(20);
            ncco.setSplit(SplitRecording.CONVERSATION);
            json = ncco.toJson();
        }

        RecordAction ncco = new ObjectMapper().readValue(json, RecordAction.class);
        assertArrayEquals(new String[]{"https://api.example.com/event"}, ncco.getEventUrl());
        assertEquals("GET", ncco.getEventMethod());
        assertEquals(true, ncco.getBeepStart());
        assertEquals('#', (char) ncco.getEndOnKey());
        assertEquals(3, (int) ncco.getEndOnSilence());
        assertEquals(RecordingFormat.MP3, ncco.getFormat());
        assertEquals(20, (int) ncco.getTimeout());
        assertEquals(SplitRecording.CONVERSATION, ncco.getSplit());
    }

    @Test
    public void testDefault() {
        RecordAction ncco = new RecordAction();
        assertEquals("{\"action\":\"record\"}", ncco.toJson());
    }

    @Test
    public void testEventUrl() {
        RecordAction ncco = new RecordAction();
        ncco.setEventUrl("https://example.com");
        assertEquals("{\"action\":\"record\",\"eventUrl\":[\"https://example.com\"]}", ncco.toJson());
    }

    @Test
    public void testEventMethod() {
        RecordAction ncco = new RecordAction();
        ncco.setEventMethod("GET");
        assertEquals("{\"eventMethod\":\"GET\",\"action\":\"record\"}", ncco.toJson());
    }

    @Test
    public void testEndOnKey() {
        RecordAction ncco = new RecordAction();
        ncco.setEndOnKey('#');
        assertEquals("{\"endOnKey\":\"#\",\"action\":\"record\"}", ncco.toJson());
    }

    @Test
    public void testEndOnSilence() {
        RecordAction ncco = new RecordAction();
        ncco.setEndOnSilence(3);
        assertEquals("{\"endOnSilence\":3,\"action\":\"record\"}", ncco.toJson());
    }

    @Test
    public void testFormat() {
        RecordAction ncco = new RecordAction();
        ncco.setFormat(RecordingFormat.WAV);
        assertEquals("{\"format\":\"wav\",\"action\":\"record\"}", ncco.toJson());
    }

    @Test
    public void testTimeout() {
        RecordAction ncco = new RecordAction();
        ncco.setTimeout(5);
        assertEquals("{\"timeout\":5,\"action\":\"record\"}", ncco.toJson());
    }

    @Test
    public void testSplit() {
        RecordAction ncco = new RecordAction();
        ncco.setSplit(SplitRecording.CONVERSATION);
        assertEquals("{\"split\":\"conversation\",\"action\":\"record\"}", ncco.toJson());
    }
}
