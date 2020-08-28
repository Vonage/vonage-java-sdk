/*
 * Copyright (c) 2011-2017 Vonage Inc
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
package com.nexmo.client.voice.ncco;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class NotifyActionTest {
    @Test
    public void testBuilderMultipleInstances() {
        NotifyAction.Builder builder = NotifyAction.builder(new HashMap<>(), "http://example.com/webhooks/event");
        assertNotSame(builder.build(), builder.build());
    }

    @Test
    public void testAllFields() {
        Map<String, String> payload = new HashMap<>();
        payload.put("key", "value");
        payload.put("key2", "value2");

        NotifyAction notify = NotifyAction.builder(new HashMap<>(), "")
                .payload(payload)
                .eventUrl("http://example.com/webhooks/event")
                .eventMethod(EventMethod.GET)
                .build();

        assertEquals(
                "[{\"payload\":{\"key2\":\"value2\",\"key\":\"value\"},\"eventUrl\":[\"http://example.com/webhooks/event\"],\"eventMethod\":\"GET\",\"action\":\"notify\"}]",
                new Ncco(notify).toJson()
        );
    }

    @Test
    public void testGetAction() {
        NotifyAction notify = NotifyAction.builder(new HashMap<>(), "http://example.com/webhooks/event").build();
        assertEquals("notify", notify.getAction());
    }

    @Test
    public void testDefault() {
        NotifyAction notify = NotifyAction.builder(new HashMap<>(), "http://example.com/webhooks/event").build();
        assertEquals("[{\"payload\":{},\"eventUrl\":[\"http://example.com/webhooks/event\"],\"action\":\"notify\"}]", new Ncco(notify).toJson());
    }

    @Test
    public void testPayloadWithNonNestedProperties() {
        Map<String, String> payload = new HashMap<>();
        payload.put("key", "value");
        payload.put("key2", "value2");

        NotifyAction notify = NotifyAction.builder(payload, "http://example.com/webhooks/event").build();

        assertEquals("[{\"payload\":{\"key2\":\"value2\",\"key\":\"value\"},\"eventUrl\":[\"http://example.com/webhooks/event\"],\"action\":\"notify\"}]", new Ncco(notify).toJson());
    }

    @Test
    public void testPayloadWithNestedProperties() {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> nested = new HashMap<>();
        Map<String, Object> innerNested = new HashMap<>();

        innerNested.put("innerInnerKey", "innerInnerValue");
        innerNested.put("innerInnerKey2", "innerInnerValue2");

        nested.put("nestKey", "nestValue");
        nested.put("nestKey2", "nestValue2");
        nested.put("nestKey3", innerNested);

        payload.put("key", "value");
        payload.put("nested", nested);

        NotifyAction notify = NotifyAction.builder(payload, "http://example.com/webhooks/event").build();

        assertEquals("[{\"payload\":{\"nested\":{\"nestKey2\":\"nestValue2\",\"nestKey\":\"nestValue\",\"nestKey3\":{\"innerInnerKey\":\"innerInnerValue\",\"innerInnerKey2\":\"innerInnerValue2\"}},\"key\":\"value\"},\"eventUrl\":[\"http://example.com/webhooks/event\"],\"action\":\"notify\"}]", new Ncco(notify).toJson());
    }

    @Test
    public void testEventMethod() {
        NotifyAction notifyGet = NotifyAction.builder(new HashMap<>(), "").eventMethod(EventMethod.GET).build();
        NotifyAction notifyPost = NotifyAction.builder(new HashMap<>(), "").eventMethod(EventMethod.POST).build();
        assertEquals("[{\"payload\":{},\"eventUrl\":[\"\"],\"eventMethod\":\"GET\",\"action\":\"notify\"}]", new Ncco(notifyGet).toJson());
        assertEquals("[{\"payload\":{},\"eventUrl\":[\"\"],\"eventMethod\":\"POST\",\"action\":\"notify\"}]", new Ncco(notifyPost).toJson());
    }

    @Test
    public void testEventUrl() {
        NotifyAction record = NotifyAction.builder(new HashMap<>(), "").eventUrl("https://example.com").build();
        assertEquals("[{\"payload\":{},\"eventUrl\":[\"https://example.com\"],\"action\":\"notify\"}]", new Ncco(record).toJson());
    }
}
