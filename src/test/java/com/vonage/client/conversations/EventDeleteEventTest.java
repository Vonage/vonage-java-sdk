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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Random;

public class EventDeleteEventTest extends AbstractEventTest {

    @Test
    public void testParseDeleteEvent() {
        int bodyId = new Random().nextInt();
        var event = parseEvent(EventType.EVENT_DELETE, EventDeleteEvent.class, "{\n" +
            "  \"id\": " + randomEventId + ",\n" +
            "  \"type\": \"event:delete\",\n" +
            "  \"from\": \"" + from + "\",\n" +
            "  \"body\": {\n" +
            "    \"event_id\": \"" + bodyId + "\"\n" +
            "  },\n" +
            "  \"timestamp\": \"2020-01-01T14:00:00.00Z\",\n" +
            "  \"_embedded\": {\n" +
            "    \"from_user\": {\n" +
            "      \"id\": \"USR-82e028d9-5201-4f1e-8188-604b2d3471ec\",\n" +
            "      \"name\": \"my_user_name\",\n" +
            "      \"display_name\": \"My User Name\",\n" +
            "      \"image_url\": \"https://example.com/image.png\",\n" +
            "      \"custom_data\": {}\n" +
            "    },\n" +
            "    \"from_member\": {\n" +
            "      \"id\": \"string\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"string\"\n" +
            "    }\n" +
            "  }\n" +
            "}"
        );
        assertEquals(randomEventId, event.getId());
        assertEquals(bodyId, event.getEventId());
        assertNotNull(event.getTimestamp());
        assertNotNull(event.getFromUser());
        assertNotNull(event.getFromMember());
    }
}
