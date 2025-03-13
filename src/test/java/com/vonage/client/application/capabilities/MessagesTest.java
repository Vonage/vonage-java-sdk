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
package com.vonage.client.application.capabilities;

import com.vonage.client.common.HttpMethod;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MessagesTest {
    @Test
    public void testEmpty() {
        Messages messages = Messages.builder().build();

        assertEquals(Capability.Type.MESSAGES, messages.getType());
        assertNull(messages.getWebhooks());
    }

    @Test
    public void testInboundWebhook() {
        Messages messages = Messages.builder().addWebhook(Webhook.Type.INBOUND, new Webhook("https://example.com/inbound", HttpMethod.POST)).build();

        assertEquals(Capability.Type.MESSAGES, messages.getType());
        assertEquals("https://example.com/inbound", messages.getWebhooks().get(Webhook.Type.INBOUND).getAddress());
        assertEquals(HttpMethod.POST, messages.getWebhooks().get(Webhook.Type.INBOUND).getMethod());
    }

    @Test
    public void testStatusWebhook() {
        Messages messages = Messages.builder().addWebhook(Webhook.Type.STATUS, new Webhook("https://example.com/status", HttpMethod.GET)).build();

        assertEquals(Capability.Type.MESSAGES, messages.getType());
        assertEquals("https://example.com/status", messages.getWebhooks().get(Webhook.Type.STATUS).getAddress());
        assertEquals(HttpMethod.GET, messages.getWebhooks().get(Webhook.Type.STATUS).getMethod());
    }

    @Test
    public void testMultipleWebhooks() {
        Messages messages = Messages.builder()
                .addWebhook(Webhook.Type.INBOUND, new Webhook("https://example.com/inbound", HttpMethod.POST))
                .addWebhook(Webhook.Type.STATUS, new Webhook("https://example.com/status", HttpMethod.GET))
                .build();

        assertEquals(Capability.Type.MESSAGES, messages.getType());
        assertEquals("https://example.com/inbound", messages.getWebhooks().get(Webhook.Type.INBOUND).getAddress());
        assertEquals(HttpMethod.POST, messages.getWebhooks().get(Webhook.Type.INBOUND).getMethod());
        assertEquals("https://example.com/status", messages.getWebhooks().get(Webhook.Type.STATUS).getAddress());
        assertEquals(HttpMethod.GET, messages.getWebhooks().get(Webhook.Type.STATUS).getMethod());
    }

    @Test
    public void testRemoveWebhook() {
        Messages messages = Messages.builder()
                .addWebhook(Webhook.Type.INBOUND, new Webhook("https://example.com/inbound", HttpMethod.POST))
                .addWebhook(Webhook.Type.STATUS, new Webhook("https://example.com/status", HttpMethod.GET))
                .removeWebhook(Webhook.Type.INBOUND)
                .build();

        assertEquals(Capability.Type.MESSAGES, messages.getType());
        assertEquals("https://example.com/status", messages.getWebhooks().get(Webhook.Type.STATUS).getAddress());
        assertEquals(HttpMethod.GET, messages.getWebhooks().get(Webhook.Type.STATUS).getMethod());
    }

    @Test
    public void testRemoveAllWebhooks() {
        Messages messages = Messages.builder()
                .addWebhook(Webhook.Type.INBOUND, new Webhook("https://example.com/inbound", HttpMethod.POST))
                .removeWebhook(Webhook.Type.INBOUND)
                .build();

        assertEquals(Capability.Type.MESSAGES, messages.getType());
        assertNull(messages.getWebhooks());
    }
}
