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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class RecordActionTest {
    @Test
    public void testBuilderMultipleInstances() {
        RecordAction.Builder builder = RecordAction.builder();
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        RecordAction record = RecordAction.builder()
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
                "[{\"format\":\"mp3\",\"endOnSilence\":3,\"timeOut\":5,\"channels\":10,\"endOnKey\":\"#\",\"beepStart\":true,\"eventUrl\":[\"https://example.com\"],\"eventMethod\":\"POST\",\"split\":\"conversation\",\"action\":\"record\"}]",
                new Ncco(record).toJson()
        );
    }

    @Test
    public void testGetAction() {
        RecordAction record = RecordAction.builder().build();
        assertEquals("record", record.getAction());
    }

    @Test
    public void testDefault() {
        RecordAction record = RecordAction.builder().build();
        assertEquals("[{\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testFormatField() {
        RecordAction.Builder builder = RecordAction.builder();
        RecordAction recordMp3 = builder.format(RecordingFormat.MP3).build();
        RecordAction recordWav = builder.format(RecordingFormat.WAV).build();

        assertEquals("[{\"format\":\"mp3\",\"action\":\"record\"}]", new Ncco(recordMp3).toJson());
        assertEquals("[{\"format\":\"wav\",\"action\":\"record\"}]", new Ncco(recordWav).toJson());
    }

    @Test
    public void testSplit() {
        RecordAction record = RecordAction.builder().split(SplitRecording.CONVERSATION).build();
        assertEquals("[{\"split\":\"conversation\",\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testSplitIsSetWhenChannelsGreaterThanOne() {
        RecordAction record = RecordAction.builder().channels(2).build();
        assertEquals("[{\"channels\":2,\"split\":\"conversation\",\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testSplitIsNotSetWhenChannelsIsOne() {
        RecordAction record = RecordAction.builder().channels(1).build();
        assertEquals("[{\"channels\":1,\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testEndOnSilence() {
        RecordAction record = RecordAction.builder().endOnSilence(3).build();
        assertEquals("[{\"endOnSilence\":3,\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testEndOnKey() {
        RecordAction record = RecordAction.builder().endOnKey('#').build();
        assertEquals("[{\"endOnKey\":\"#\",\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testTimeout() {
        RecordAction record = RecordAction.builder().timeOut(5).build();
        assertEquals("[{\"timeOut\":5,\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testBeepStart() {
        RecordAction record = RecordAction.builder().beepStart(true).build();
        assertEquals("[{\"beepStart\":true,\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testEventMethod() {
        RecordAction recordGet = RecordAction.builder().eventMethod(EventMethod.GET).build();
        RecordAction recordPost = RecordAction.builder().eventMethod(EventMethod.POST).build();
        assertEquals("[{\"eventMethod\":\"GET\",\"action\":\"record\"}]", new Ncco(recordGet).toJson());
        assertEquals("[{\"eventMethod\":\"POST\",\"action\":\"record\"}]", new Ncco(recordPost).toJson());
    }

    @Test
    public void testEventUrl() {
        RecordAction record = RecordAction.builder().eventUrl("https://example.com").build();
        assertEquals("[{\"eventUrl\":[\"https://example.com\"],\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testChannels() {
        RecordAction record = RecordAction.builder().channels(32).build();
        assertEquals("[{\"channels\":32,\"split\":\"conversation\",\"action\":\"record\"}]", new Ncco(record).toJson());
    }

    @Test
    public void testMultipleBuilderCallWithDifferentChannelsSetsAndUnsetsSplitCorrectly() {
        RecordAction.Builder recordBuilder = RecordAction.builder();
        assertEquals(
                "[{\"channels\":2,\"split\":\"conversation\",\"action\":\"record\"}]",
                new Ncco(recordBuilder.channels(2).build()).toJson()
        );
        assertEquals("[{\"channels\":1,\"action\":\"record\"}]", new Ncco(recordBuilder.channels(1).build()).toJson());
    }
}
