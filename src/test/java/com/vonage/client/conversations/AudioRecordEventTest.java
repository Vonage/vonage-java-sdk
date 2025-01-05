/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.conversations;

import com.vonage.client.OrderedMap;
import static com.vonage.client.OrderedMap.entry;
import com.vonage.client.voice.TextToSpeechLanguage;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Map;

public class AudioRecordEventTest extends AbstractEventTest {

    private final String format = "ogg";
    private final boolean
            beepStart = true, beepStop = true,
            detectSpeech = false, multitrack = false, split = true,
            sentimentAnalysis = false, streamed = true;
    private final int channels = 2, validity = 79;
    private final TextToSpeechLanguage language = TextToSpeechLanguage.GREEK;

    @Test
    public void testAllFields() {
        var event = testBaseEvent(EventType.AUDIO_RECORD,
                AudioRecordEvent.builder()
                    .beepStart(beepStart).beepStop(beepStop).split(split)
                    .sentimentAnalysis(sentimentAnalysis).streamed(streamed)
                    .detectSpeech(detectSpeech).multitrack(multitrack).format(format)
                    .channels(channels).validity(validity).language(language),

                new OrderedMap(
                entry("transcription", new OrderedMap(
                            entry("language", language.getLanguage()),
                            entry("sentiment_analysis", sentimentAnalysis)
                        )),
                        entry("format", format),
                        entry("validity", validity),
                        entry("channels", channels),
                        entry("streamed", streamed),
                        entry("split", split),
                        entry("multitrack", multitrack),
                        entry("detect_speech", detectSpeech),
                        entry("beep_start", beepStart),
                        entry("beep_stop", beepStop)
                )
        );
        assertEquals(language, event.getLanguage());
        assertEquals(sentimentAnalysis, event.getSentimentAnalysis());
        assertEquals(format, event.getFormat());
        assertEquals(validity, event.getValidity());
        assertEquals(channels, event.getChannels());
        assertEquals(streamed, event.getStreamed());
        assertEquals(split, event.getSplit());
        assertEquals(multitrack, event.getMultitrack());
        assertEquals(detectSpeech, event.getDetectSpeech());
        assertEquals(beepStart, event.getBeepStart());
        assertEquals(beepStop, event.getBeepStop());
    }

    @Test
    public void testRequiredFields() {
        var event = testBaseEvent(EventType.AUDIO_RECORD, AudioRecordEvent.builder(), Map.of());
        assertNull(event.getLanguage());
        assertNull(event.getSentimentAnalysis());
        assertNull(event.getFormat());
        assertNull(event.getValidity());
        assertNull(event.getChannels());
        assertNull(event.getStreamed());
        assertNull(event.getSplit());
        assertNull(event.getMultitrack());
        assertNull(event.getDetectSpeech());
        assertNull(event.getBeepStart());
        assertNull(event.getBeepStop());
    }
}
