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
package com.vonage.client.voice.ncco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import org.junit.jupiter.api.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NotifyActionTest {

    @Test
    public void testBuilderMultipleInstances() {
        var map = Collections.<String, Object> emptyMap();
        var url = "http://example.com/webhooks/event";

        NotifyAction.Builder builder = NotifyAction.builder();
        assertNotSame(builder.build(), builder.build());
        assertNotSame(
                NotifyAction.builder(map, url),
                NotifyAction.builder(map, url)
        );
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

        assertEquals("[{\"payload\":{\"key2\":\"value2\",\"key\":\"value\"},\"eventUrl\":[\"http://example.com/webhooks/event\"],\"eventMethod\":\"GET\",\"action\":\"notify\"}]", new Ncco(notify).toJson());
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
