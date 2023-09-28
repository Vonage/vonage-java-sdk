/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.incoming;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class MessageEventTest {
    @Test
    public void testDeserializeIncomingMessage() {
        String json = "{\n" + "  \"msisdn\": \"447700900001\",\n" + "  \"to\": \"447700900000\",\n"
                + "  \"messageId\": \"0A0000000123ABCD1\",\n" + "  \"text\": \"Hello world\",\n"
                + "  \"type\": \"text\",\n" + "  \"keyword\": \"Hello\",\n"
                + "  \"message-timestamp\": \"2020-01-01 12:00:00\",\n" + "  \"timestamp\": \"1578787200\",\n"
                + "  \"nonce\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" + "  \"concat\": \"true\",\n"
                + "  \"concat-ref\": \"1\",\n" + "  \"concat-total\": \"3\",\n" + "  \"concat-part\": \"2\",\n"
                + "  \"data\": \"abc123\",\n" + "  \"udh\": \"abc123\"\n" + "}";

        MessageEvent messageEvent = MessageEvent.fromJson(json);
        assertEquals("447700900001", messageEvent.getMsisdn());
        assertEquals("447700900000", messageEvent.getTo());
        assertEquals("0A0000000123ABCD1", messageEvent.getMessageId());
        assertEquals("Hello world", messageEvent.getText());
        assertEquals("Hello", messageEvent.getKeyword());
        assertEquals("1578787200", messageEvent.getTimestamp());
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", messageEvent.getNonce());
        assertEquals(true, messageEvent.getConcat());
        assertEquals(1, messageEvent.getConcatRef());
        assertEquals(3, messageEvent.getConcatTotal());
        assertEquals(2, messageEvent.getConcatPart());
        assertEquals("abc123", messageEvent.getData());
        assertEquals("abc123", messageEvent.getUdh());

        Calendar calendar = new GregorianCalendar(2020, Calendar.JANUARY, 1, 12, 0, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertEquals(calendar.getTime(), messageEvent.getMessageTimestamp());
    }
}
