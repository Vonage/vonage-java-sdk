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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.List;

public class MemberTransitionEventTest extends AbstractEventTest {

    @Test
    public void testParseEmptyEvent() {
        for (var tuple : List.of(
                new ParseEventTuple(MemberJoinedEvent.class, EventType.MEMBER_JOINED, "member:joined"),
                new ParseEventTuple(MemberLeftEvent.class, EventType.MEMBER_LEFT, "member:left")
        )) {
            var event = (AbstractMemberStateEvent) parseEvent(
                    tuple.eventTypeEnum(), tuple.clazz(), "{\"body\":{},\"type\":\""+tuple.eventTypeStr()+"\"}"
            );
            assertNotNull(event.getMember());
            event = (AbstractMemberStateEvent) parseEvent(
                    tuple.eventTypeEnum(), tuple.clazz(), "{\"type\":\""+tuple.eventTypeStr()+"\"}"
            );
            assertNull(event.getMember());
        }

        var tuple = new ParseEventTuple(MemberInvitedEvent.class, EventType.MEMBER_INVITED, "member:invited");
        var event = (MemberInvitedEvent) parseEvent(
                tuple.eventTypeEnum(), tuple.clazz(), "{\"body\":{},\"type\":\""+tuple.eventTypeStr()+"\"}"
        );
        assertNotNull(event.getMember());
        event = (MemberInvitedEvent) parseEvent(
                tuple.eventTypeEnum(), tuple.clazz(), "{\"type\":\""+tuple.eventTypeStr()+"\"}"
        );
        assertNull(event.getMember());
    }
}
