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
import java.time.Instant;

public class RtcStatusEventTest extends AbstractEventTest {

    @Test
    public void testParseEmptyEvent() {
        var event = parseEvent(EventType.RTC_STATUS, RtcStatusEvent.class, "{\"type\":\"rtc:status\"}");
        assertNull(event.getDuration());
        assertNull(event.getStartTime());
        assertNull(event.getEndTime());
        assertNull(event.getPriceCurrency());
        assertNull(event.getPrice());
        assertNull(event.getMos());
        assertNull(event.getChannel());
    }

    @Test
    public void testFromJsonAllFields() {
        final Instant startTime = Instant.parse("2021-02-03T04:56:28Z"),
                endTime = Instant.parse("2021-02-03T05:17:40Z");
        Integer mos = 5;
        String duration = "1782", priceCurrency = "EUR", price = String.valueOf(Math.PI),
                json = "{\"body\":{\"duration\":\"" + duration + "\",\"start_time\":\"" + startTime +
                        "\",\"end_time\":\"" + endTime + "\",\"price_currency\":\"" + priceCurrency +
                        "\",\"price\":\"" + price + "\",\"mos\":" + mos + ",\"channel\":{}}}";


        var event = Jsonable.fromJson(json, RtcStatusEvent.class);
        testJsonableBaseObject(event);
        assertEquals(duration, event.getDuration());
        assertEquals(startTime, event.getStartTime());
        assertEquals(endTime, event.getEndTime());
        assertEquals(priceCurrency, event.getPriceCurrency());
        assertEquals(price, event.getPrice());
        assertEquals(mos, event.getMos());
        assertNotNull(event.getChannel());
    }
}
