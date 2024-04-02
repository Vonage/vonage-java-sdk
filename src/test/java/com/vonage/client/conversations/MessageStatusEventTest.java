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

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;

public class MessageStatusEventTest extends AbstractEventTest {

    <E extends MessageStatusEvent, B extends MessageStatusEvent.Builder<E, B>> E testMessageEvent(B builder, EventType type) {
        var event = testBaseEvent(builder.eventId(randomEventId));
        assertEquals(type, event.getType());
        assertEquals(randomEventId, event.getEventId());
        return event;
    }

    @Test
    public void testMessageStatusEvents() {
        testMessageEvent(MessageSeenEvent.builder(), EventType.MESSAGE_SEEN);
        testMessageEvent(MessageSubmittedEvent.builder(), EventType.MESSAGE_SUBMITTED);
        testMessageEvent(MessageRejectedEvent.builder(), EventType.MESSAGE_REJECTED);
        testMessageEvent(MessageDeliveredEvent.builder(), EventType.MESSAGE_DELIVERED);
        testMessageEvent(MessageUndeliverableEvent.builder(), EventType.MESSAGE_UNDELIVERABLE);
    }
}
