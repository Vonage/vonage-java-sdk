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

import com.vonage.client.Jsonable;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MemberMediaEventTest extends AbstractEventTest {

    @Test
    public void testParseEmptyEvent() {
        var event = parseEvent(EventType.MEMBER_MEDIA, MemberMediaEvent.class, "{\"type\":\"member:media\"}");
        assertNull(event.getAudio());
        assertNull(event.getMedia());
        assertNull(event.getChannel());
    }

    @Test
    public void testFromJsonAllFields() {
        boolean enabled = false, earmuffed = false, muted = true, audioOuter = true, audioInner = false;
        final String json = "{\"body\":{\"audio\":" + audioOuter + ",\"media\":{\"audio\":" + audioInner +
                ",\"audio_settings\":{\"enabled\":" + enabled + ",\"earmuffed\":" + earmuffed +
                ",\"muted\":" + muted + "}}, \"channel\":{}}}";

        var event = Jsonable.fromJson(json, MemberMediaEvent.class);
        testJsonableBaseObject(event);
        assertEquals(audioOuter, event.getAudio());
        var media = event.getMedia();
        assertNotNull(media);
        assertEquals(audioInner, media.getAudio());
        var audioSettings = media.getAudioSettings();
        assertNotNull(audioSettings);
        assertEquals(enabled, audioSettings.getEnabled());
        assertEquals(earmuffed, audioSettings.getEarmuffed());
        assertEquals(muted, audioSettings.getMuted());
        assertNotNull(event.getChannel());
    }
}
