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

import com.vonage.client.TestUtils;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Map;
import java.util.UUID;

abstract class AbstractEventTest {
    private static final String FROM = STR."MEM-\{UUID.randomUUID()}";

    final int randomEventId = ConversationsClientTest.EVENT_ID;
    final UUID randomId = UUID.randomUUID();
    final String randomIdStr = randomId.toString();

    <E extends EventWithBody<?>, B extends EventWithBody.Builder<? extends E, B>> B applyBaseFields(B builder) {
        return builder.from(FROM);
    }

    <E extends EventWithBody<?>, B extends EventWithBody.Builder<E, B>> E testBaseEvent(
            B builder, Map<String, ?> bodyFields) {

        var event = applyBaseFields(builder).build();
        testJsonableBaseObject(event);
        assertEquals(FROM, event.getFrom());
        var json = event.toJson();
        assertTrue(json.contains("\"from\":\""+FROM+"\""));
        if (bodyFields != null && !bodyFields.isEmpty()) {
            var bodyPartialJson = "\"body\":" + TestUtils.mapToJson(bodyFields);
            assertTrue(json.contains(bodyPartialJson));
        }
        return event;
    }
}
