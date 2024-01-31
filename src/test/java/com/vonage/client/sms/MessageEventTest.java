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
package com.vonage.client.sms;

import com.vonage.client.TestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MessageEventTest {

    @Test
    public void testDeserializeIncomingMessage() {
        String json = """
                {
                  "msisdn": "447700900001",
                  "to": "447700900000",
                  "messageId": "0A0000000123ABCD1",
                  "text": "Hello world",
                  "type": "text",
                  "keyword": "Hello",
                  "message-timestamp": "2020-01-01 12:00:00",
                  "timestamp": "1578787200",
                  "nonce": "aaaaaaaa-bbbb-cccc-dddd-0123456789ab",
                  "concat": "true",
                  "concat-ref": "1",
                  "concat-total": "3",
                  "concat-part": "2",
                  "data": "abc123",
                  "udh": "abc123"
                }""";

        var messageEvent = MessageEvent.fromJson(json);
        TestUtils.testJsonableBaseObject(messageEvent);

        assertEquals("447700900001", messageEvent.getMsisdn());
        assertEquals("447700900000", messageEvent.getTo());
        assertEquals("0A0000000123ABCD1", messageEvent.getMessageId());
        assertEquals("Hello world", messageEvent.getText());
        assertEquals("Hello", messageEvent.getKeyword());
        assertEquals(1578787200L, messageEvent.getTimestamp());
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", messageEvent.getNonce());
        assertEquals(true, messageEvent.getConcat());
        assertEquals(1, messageEvent.getConcatRef());
        assertEquals(3, messageEvent.getConcatTotal());
        assertEquals(2, messageEvent.getConcatPart());
        assertEquals("abc123", messageEvent.getData());
        assertEquals("abc123", messageEvent.getUdh());

        var calendar = new GregorianCalendar(2020, Calendar.JANUARY, 1, 12, 0, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        var time = calendar.toZonedDateTime().toInstant();
        assertEquals(time, messageEvent.getMessageTimestamp());
    }

    @Test
    public void testUnknownMessageType() {
        var me = MessageEvent.fromJson("{\"type\": \"pigeon\"}");
        TestUtils.testJsonableBaseObject(me);
        assertNull(me.getConcat());
        assertNull(me.getMessageId());
        assertNull(me.getMessageTimestamp());
        assertNull(me.getConcatPart());
        assertNull(me.getConcatTotal());
        assertNull(me.getData());
        assertNull(me.getUdh());
        assertNull(me.getText());
        assertNull(me.getMsisdn());
        assertNull(me.getKeyword());
        assertNull(me.getNonce());
        assertNull(me.getTo());
        assertEquals(MessageType.UNKNOWN, me.getType());

        me = MessageEvent.fromJson("{}");
        assertNull(me.getType());
    }
}
