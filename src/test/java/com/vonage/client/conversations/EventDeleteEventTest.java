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
        var event = parseEvent(EventType.EVENT_DELETE, EventDeleteEvent.class, STR."""
            {
               "id": \{randomEventId},
               "type": "event:delete",
               "from": "\{from}",
               "body": {
                  "event_id": "\{bodyId}"
               },
               "timestamp": "2020-01-01T14:00:00.00Z",
               "_embedded": {
                  "from_user": {
                     "id": "USR-82e028d9-5201-4f1e-8188-604b2d3471ec",
                     "name": "my_user_name",
                     "display_name": "My User Name",
                     "image_url": "https://example.com/image.png",
                     "custom_data": {}
                  },
                  "from_member": {
                     "id": "string"
                  }
               },
               "_links": {
                  "self": {
                     "href": "string"
                  }
               }
            }
            """
        );
        assertEquals(randomEventId, event.getId());
        assertEquals(bodyId, event.getEventId());
        assertNotNull(event.getTimestamp());
        assertNotNull(event.getFromUser());
        assertNotNull(event.getFromMember());
    }
}
