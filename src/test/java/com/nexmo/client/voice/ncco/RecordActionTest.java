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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class RecordActionTest {
    @Test
    public void testBuilderMultipleInstances() {
        RecordAction.Builder builder = new RecordAction.Builder();
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        RecordAction record = new RecordAction.Builder()
                .format(RecordingFormat.MP3)
                .split(SplitRecording.CONVERSATION)
                .endOnSilence(3)
                .endOnKey('#')
                .timeOut(5)
                .beepStart(true)
                .eventMethod(EventMethod.POST)
                .eventUrl("https://example.com").channels(10)
                .build();

        assertEquals(
                "[{\"format\":\"mp3\",\"endOnSilence\":3,\"endOnKey\":\"#\",\"timeOut\":5,\"beepStart\":true,\"eventUrl\":[\"https://example.com\"],\"eventMethod\":\"POST\",\"split\":\"conversation\",\"channels\":10,\"action\":\"record\"}]",
                new Ncco(record).toJson()
        );
    }

    @Test
    public void testGetAction() {
        RecordAction record = new RecordAction.Builder().build();
        assertEquals("record", record.getAction());
    }

    @Test
    public void testDefault() {
        RecordAction record = new RecordAction.Builder().build();
        assertEquals("[{\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testFormatField() {
        RecordAction.Builder builder = new RecordAction.Builder();
        RecordAction recordMp3 = builder.format(RecordingFormat.MP3).build();
        RecordAction recordWav = builder.format(RecordingFormat.WAV).build();

        assertEquals("[{\"format\":\"mp3\",\"action\":\"record\"}]", new Ncco(recordMp3).toJson());
        assertEquals("[{\"format\":\"wav\",\"action\":\"record\"}]", new Ncco(recordWav).toJson());
    }

    @Test
    public void testSplit() {
        RecordAction record = new RecordAction.Builder().split(SplitRecording.CONVERSATION).build();
        assertEquals("[{\"split\":\"conversation\",\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testSplitIsSetWhenChannelsGreaterThanOne() {
        RecordAction record = new RecordAction.Builder().channels(2).build();
        assertEquals("[{\"split\":\"conversation\",\"channels\":2,\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testSplitIsNotSetWhenChannelsIsOne() {
        RecordAction record = new RecordAction.Builder().channels(1).build();
        assertEquals("[{\"channels\":1,\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testEndOnSilence() {
        RecordAction record = new RecordAction.Builder().endOnSilence(3).build();
        assertEquals("[{\"endOnSilence\":3,\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testEndOnKey() {
        RecordAction record = new RecordAction.Builder().endOnKey('#').build();
        assertEquals("[{\"endOnKey\":\"#\",\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testTimeout() {
        RecordAction record = new RecordAction.Builder().timeOut(5).build();
        assertEquals("[{\"timeOut\":5,\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testBeepStart() {
        RecordAction record = new RecordAction.Builder().beepStart(true).build();
        assertEquals("[{\"beepStart\":true,\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testEventMethod() {
        RecordAction recordGet = new RecordAction.Builder().eventMethod(EventMethod.GET).build();
        RecordAction recordPost = new RecordAction.Builder().eventMethod(EventMethod.POST).build();
        assertEquals("[{\"eventMethod\":\"GET\",\"action\":\"record\"}]", new Ncco(recordGet).toJson());
        assertEquals("[{\"eventMethod\":\"POST\",\"action\":\"record\"}]", new Ncco(recordPost).toJson());
    }

    @Test
    public void testEventUrl() {
        RecordAction record = new RecordAction.Builder().eventUrl("https://example.com").build();
        assertEquals("[{\"eventUrl\":[\"https://example.com\"],\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testChannels() {
        RecordAction record = new RecordAction.Builder().channels(32).build();
        assertEquals("[{\"split\":\"conversation\",\"channels\":32,\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testMultipleBuilderCallWithDifferentChannelsSetsAndUnsetsSplitCorrectly() {
        RecordAction.Builder recordBuilder = new RecordAction.Builder();
        assertEquals(
                "[{\"split\":\"conversation\",\"channels\":2,\"action\":\"record\"}]",
                new Ncco(recordBuilder.channels(2).build()).toJson()
        );
        assertEquals("[{\"channels\":1,\"action\":\"record\"}]", new Ncco(recordBuilder.channels(1).build()).toJson());
    }
}
