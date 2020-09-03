/*
 * Copyright (c) 2020 Vonage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.incoming;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

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
