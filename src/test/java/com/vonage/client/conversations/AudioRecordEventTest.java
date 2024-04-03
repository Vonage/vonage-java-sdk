/*
 *   Copyright 2024 Vonage
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

import com.vonage.client.OrderedJsonMap;
import static com.vonage.client.OrderedJsonMap.entry;
import com.vonage.client.voice.TextToSpeechLanguage;
import org.junit.jupiter.api.*;
import java.util.Map;

public class AudioRecordEventTest extends AbstractEventTest {

    private final boolean
            beepStart = true, beepStop = true,
            detectSpeech = false, multitrack = false, split = true,
            sentimentAnalysis = false, streamed = true;
    private final int channels = 2, validity = 79;
    private final TextToSpeechLanguage language = TextToSpeechLanguage.GREEK;

    @Test
    public void testAllFields() {
        testBaseEvent(EventType.AUDIO_RECORD,
                AudioRecordEvent.builder()
                    .beepStart(beepStart).beepStop(beepStop).split(split)
                    .sentimentAnalysis(sentimentAnalysis).streamed(streamed)
                    .detectSpeech(detectSpeech).multitrack(multitrack)
                    .channels(channels).validity(validity).language(language),

                new OrderedJsonMap(
                entry("transcription", new OrderedJsonMap(
                            entry("language", language.getLanguage()),
                            entry("sentiment_analysis", sentimentAnalysis)
                        )),
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
    }

    @Test
    public void testRequiredFields() {
        testBaseEvent(EventType.AUDIO_RECORD, AudioRecordEvent.builder(), Map.of());
    }
}
