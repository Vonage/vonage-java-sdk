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
