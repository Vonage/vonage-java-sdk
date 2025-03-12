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
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AudioDtmfEventTest extends AbstractEventTest {

    @Test
    public void parseEmptyEvent() {
        var event = parseEvent(EventType.AUDIO_DTMF, AudioDtmfEvent.class, "{\"type\":\"audio:dtmf\"}");
        assertNull(event.getDigits());
        assertNull(event.getDtmfSeq());
        assertNull(event.getChannel());
    }

    @Test
    public void testAllFields() {
        String digits = "123p4#";
        int dtmfSeq = 90;
        var builder = AudioDtmfEvent.builder()
                .digits(digits).dtmfSeq(dtmfSeq)
                .channel(MemberChannel.builder().build());

        var tested = testBaseEvent(EventType.AUDIO_DTMF, builder, new OrderedMap(
                entry("channel", new OrderedMap()),
                entry("digits", digits),
                entry("dtmf_seq", dtmfSeq)
        ));
        assertEquals(tested.toString(), builder.build().toString());
        assertEquals(dtmfSeq, tested.getDtmfSeq());
        assertEquals(digits, tested.getDigits());
        assertNotNull(tested.getChannel());
    }
}
