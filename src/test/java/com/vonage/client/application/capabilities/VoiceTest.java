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
package com.vonage.client.application.capabilities;

import com.vonage.client.common.HttpMethod;
import com.vonage.client.common.Webhook;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VoiceTest {
    @Test
    public void testEmpty() {
        Voice voice = Voice.builder().build();

        assertEquals(Capability.Type.VOICE, voice.getType());
        assertNull(voice.getWebhooks());
    }

    @Test
    public void testAnswerWebhook() {
        Voice voice = Voice.builder().addWebhook(Webhook.Type.ANSWER, new Webhook("https://example.com/answer", HttpMethod.POST)).build();

        assertEquals(Capability.Type.VOICE, voice.getType());
        assertEquals("https://example.com/answer", voice.getWebhooks().get(Webhook.Type.ANSWER).getAddress());
        assertEquals(HttpMethod.POST, voice.getWebhooks().get(Webhook.Type.ANSWER).getMethod());
    }

    @Test
    public void testEventWebhook() {
        Voice voice = Voice.builder().addWebhook(Webhook.Type.EVENT, new Webhook("https://example.com/event", HttpMethod.GET)).build();

        assertEquals(Capability.Type.VOICE, voice.getType());
        assertEquals("https://example.com/event", voice.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.GET, voice.getWebhooks().get(Webhook.Type.EVENT).getMethod());
    }

    @Test
    public void testMultipleWebhooks() {
        Voice voice = Voice.builder()
                .addWebhook(Webhook.Type.ANSWER, new Webhook("https://example.com/answer", HttpMethod.POST))
                .addWebhook(Webhook.Type.EVENT, new Webhook("https://example.com/event", HttpMethod.GET))
                .build();

        assertEquals(Capability.Type.VOICE, voice.getType());
        assertEquals("https://example.com/answer", voice.getWebhooks().get(Webhook.Type.ANSWER).getAddress());
        assertEquals(HttpMethod.POST, voice.getWebhooks().get(Webhook.Type.ANSWER).getMethod());
        assertEquals("https://example.com/event", voice.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.GET, voice.getWebhooks().get(Webhook.Type.EVENT).getMethod());
    }

    @Test
    public void testRemoveWebhook() {
        Voice voice = Voice.builder()
                .addWebhook(Webhook.Type.ANSWER, new Webhook("https://example.com/answer", HttpMethod.POST))
                .addWebhook(Webhook.Type.EVENT, new Webhook("https://example.com/event", HttpMethod.GET))
                .removeWebhook(Webhook.Type.ANSWER)
                .build();

        assertEquals(Capability.Type.VOICE, voice.getType());
        assertEquals("https://example.com/event", voice.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.GET, voice.getWebhooks().get(Webhook.Type.EVENT).getMethod());
    }

    @Test
    public void testRemoveAllWebhooks() {
        Voice voice = Voice.builder()
                .addWebhook(Webhook.Type.ANSWER, new Webhook("https://example.com/answer", HttpMethod.POST))
                .removeWebhook(Webhook.Type.ANSWER)
                .build();

        assertEquals(Capability.Type.VOICE, voice.getType());
        assertNull(voice.getWebhooks());
    }
}
