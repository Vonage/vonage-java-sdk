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
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.UUID;

public class RtcTransferEventTest extends AbstractEventTest {

    @Test
    public void testParseEmptyEvent() {
        var event = parseEvent(EventType.RTC_TRANSFER, RtcTransferEvent.class, "{\"type\":\"rtc:transfer\"}");
        assertNull(event.getTransferredFrom());
        assertNull(event.getUserId());
        assertNull(event.getWasMember());
    }

    @Test
    public void testFromJsonAllFields() {
        final String wasMember = "MEM-" + UUID.randomUUID(),
                userId = "USR-" + UUID.randomUUID(),
                transferredFrom = "MEM-" + UUID.randomUUID(),
                json = "{\"body\":{\"was_member\":\"" + wasMember + "\",\"user_id\":\"" + userId +
                        "\",\"transferred_from\":\"" + transferredFrom + "\"}}";

        var event = Jsonable.fromJson(json, RtcTransferEvent.class);
        testJsonableBaseObject(event);
        assertEquals(wasMember, event.getWasMember());
        assertEquals(userId, event.getUserId());
        assertEquals(transferredFrom, event.getTransferredFrom());
    }
}
