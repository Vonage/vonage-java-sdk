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
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageStatusEventTest extends AbstractEventTest {

    @SuppressWarnings("unchecked")
    <E extends MessageStatusEvent, B extends MessageStatusEvent.Builder<E, B>> void testMessageEvent(
            B builder, EventType type) {

        var event = testBaseEvent(type,
                builder.eventId(randomEventId),
                Map.of("event_id", randomEventId)
        );
        assertEquals(randomEventId, event.getEventId());
        var eventClass = (Class<E>) builder.getClass().getEnclosingClass();
        E parsed = parseEvent(type, eventClass, "{\n" +
            "  \"id\": 123,\n" +
            "  \"body\": {},\n" +
            "  \"type\": \"" + type + "\"\n" +
            "}"
        );
        assertEquals(123, parsed.getId());
        assertNull(parsed.getEventId());
        assertNull(parsed.getFrom());
        assertNull(parsed.getFromMember());
        assertNull(parsed.getTimestamp());
    }

    @Test
    public void testMessageStatusEvents() {
        testMessageEvent(MessageSeenEvent.builder(), EventType.MESSAGE_SEEN);
        testMessageEvent(MessageSubmittedEvent.builder(), EventType.MESSAGE_SUBMITTED);
        testMessageEvent(MessageRejectedEvent.builder(), EventType.MESSAGE_REJECTED);
        testMessageEvent(MessageDeliveredEvent.builder(), EventType.MESSAGE_DELIVERED);
        testMessageEvent(MessageUndeliverableEvent.builder(), EventType.MESSAGE_UNDELIVERABLE);
    }

    @Test
    public void testFromEmptyJson() {
        final String json = "{}";
        for (var clazz : List.of(
                MessageSeenEvent.class,
                MessageSubmittedEvent.class,
                MessageRejectedEvent.class,
                MessageDeliveredEvent.class,
                MessageUndeliverableEvent.class
        )) {
            var event = Jsonable.fromJson(json, clazz);
            assertNotNull(event);
            assertNull(event.getEventId());
        }
    }
}
