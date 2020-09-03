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
package com.nexmo.client.application.capabilities;

import com.nexmo.client.common.HttpMethod;
import com.nexmo.client.common.Webhook;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
