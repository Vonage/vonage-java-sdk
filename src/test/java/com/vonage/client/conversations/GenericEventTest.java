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

import com.vonage.client.TestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;

public class GenericEventTest extends AbstractEventTest {

    <E extends GenericEvent, B extends GenericEvent.Builder<E, B>> void testGenericEvent(
            B builder, EventType eventType, String eventTypeStr,
            Class<? extends GenericEvent> eventClass, Map<String, ?> body) {

        if (builder != null) {
            var built = testBaseEvent(eventType, builder.body(body), body);
            assertEquals(body, built.getBody());
        }

        var parsed = parseEvent(eventType, eventClass, "{\n" +
            "  \"id\": " + randomEventId + ",\n" +
            "  \"type\": \"" + eventTypeStr + "\",\n" +
            "  \"body\": " + TestUtils.mapToJson(body) + ",\n" +
            "  \"_links\": {}\n" +
            "}"
        );
        assertEquals(body, parsed.getBody());
        assertEquals(randomEventId, parsed.getId());
    }

    @Test
    public void testEphemeralEvent() {
        testGenericEvent(EphemeralEvent.builder(),
                EventType.EPHEMERAL,
                "ephemeral",
                EphemeralEvent.class,
                Map.of(
                    "foo", "Bar",
                    "bAz", 3,
                    "QUX", true,
                    "Cats", List.of("Lucy", "Jasper"),
                    "Table", Map.of("k1", "V1")
                )
        );
    }

    @Test
    public void testCustomEvent() {
        testGenericEvent(CustomEvent.builder("test"),
                EventType.CUSTOM,
                "custom:test",
                GenericEvent.class,
                Map.of()
        );
        testGenericEvent(CustomEvent.builder(""),
                EventType.CUSTOM,
                "custom:",
                CustomEvent.class,
                Map.of()
        );
    }
}
