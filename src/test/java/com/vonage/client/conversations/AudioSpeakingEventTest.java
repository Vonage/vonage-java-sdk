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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.*;
import java.util.List;

public class AudioSpeakingEventTest extends AbstractEventTest {

    @Test
    public void testFromJson() {
        final String json = "{\"body\":{\"channel\":{}}}";
        for (var clazz : List.of(
                AudioSpeakingOnEvent.class,
                AudioSpeakingOffEvent.class
        )) {
            var event = Jsonable.fromJson(json, clazz);
            assertNotNull(event);
            assertEquals(clazz, event.getClass());
            var channel = event.getChannel();
            assertNotNull(channel);
        }
    }
}
