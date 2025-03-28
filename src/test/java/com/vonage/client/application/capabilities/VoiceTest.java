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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class VoiceTest {

    @Test
    public void testEmpty() {
        Voice voice = Voice.builder().build();

        assertEquals(Capability.Type.VOICE, voice.getType());
        assertNull(voice.getWebhooks());
        assertNull(voice.getRegion());
        assertNull(voice.getSignedCallbacks());
        assertNull(voice.getConversationsTtl());
        assertNull(voice.getLegPersistenceTime());
    }

    @Test
    public void testAnswerWebhook() {
        Voice voice = Voice.builder().answer(new Webhook("https://example.com/answer", HttpMethod.POST)).build();

        assertEquals(Capability.Type.VOICE, voice.getType());
        assertEquals("https://example.com/answer", voice.getWebhooks().get(Webhook.Type.ANSWER).getAddress());
        assertEquals(HttpMethod.POST, voice.getWebhooks().get(Webhook.Type.ANSWER).getMethod());
    }

    @Test
    public void testEventWebhook() {
        Voice voice = Voice.builder().event(new Webhook("https://example.com/event", HttpMethod.GET)).build();

        assertEquals(Capability.Type.VOICE, voice.getType());
        assertEquals("https://example.com/event", voice.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.GET, voice.getWebhooks().get(Webhook.Type.EVENT).getMethod());
    }

    @Test
    public void testMultipleWebhooks() {
        Voice voice = Voice.builder()
                .answer(new Webhook("https://example.com/answer", HttpMethod.POST))
                .event(new Webhook("https://example.com/event", HttpMethod.GET))
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
                .answer(new Webhook("https://example.com/answer", HttpMethod.POST))
                .event(new Webhook("https://example.com/event", HttpMethod.GET))
                .answer(null)
                .legPersistenceTime(3).signedCallbacks(false)
                .build();

        assertEquals(Capability.Type.VOICE, voice.getType());
        assertEquals("https://example.com/event", voice.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.GET, voice.getWebhooks().get(Webhook.Type.EVENT).getMethod());
        assertNull(voice.getWebhooks().get(Webhook.Type.ANSWER));
        assertEquals(3, voice.getLegPersistenceTime());
        assertFalse(voice.getSignedCallbacks());
    }

    @Test
    public void testRemoveAllWebhooks() {
        Voice voice = Voice.builder()
                .answer(new Webhook("https://example.com/answer", HttpMethod.POST))
                .answer(null)
                .build();

        assertEquals(Capability.Type.VOICE, voice.getType());
        assertNull(voice.getWebhooks());
    }

    @Test
    public void testWebhookProperties() {
        Webhook.Builder whBuilder = Webhook.builder().method(HttpMethod.GET);
        assertThrows(IllegalStateException.class, whBuilder::build);
        whBuilder.address("https://fallback.example.com/webhooks/answer");
        assertEquals(HttpMethod.GET, whBuilder.build().getMethod());
        assertThrows(IllegalStateException.class, () -> whBuilder.method(null).build());
        assertNotNull(whBuilder.method(HttpMethod.GET).build().getAddress());

        Integer connMin = 300, connMax = 1000;
        assertEquals(connMin, whBuilder.connectionTimeout(connMin).build().getConnectionTimeout());
        assertThrows(IllegalArgumentException.class, () -> whBuilder.connectionTimeout(connMin-1).build());
        assertEquals(connMax, whBuilder.connectionTimeout(connMax).build().getConnectionTimeout());
        assertThrows(IllegalArgumentException.class, () -> whBuilder.connectionTimeout(connMax+1).build());
        whBuilder.connectionTimeout(500);

        Integer sockMin = 1000, sockMax = 10000;
        assertEquals(sockMin, whBuilder.socketTimeout(sockMin).build().getSocketTimeout());
        assertThrows(IllegalArgumentException.class, () -> whBuilder.socketTimeout(sockMin-1).build());
        assertEquals(sockMax, whBuilder.socketTimeout(sockMax).build().getSocketTimeout());
        assertThrows(IllegalArgumentException.class, () -> whBuilder.socketTimeout(sockMax+1).build());

        Webhook webhook = whBuilder.socketTimeout(3000).build();
        Voice fallback = Voice.builder().fallbackAnswer(webhook).build();
        assertEquals(1, fallback.getWebhooks().size());
        assertEquals(webhook, fallback.getWebhooks().get(Webhook.Type.FALLBACK_ANSWER));
    }

    @Test
    public void testSerializeAdditionalFields() {
        Voice request = Voice.builder()
                .conversationsTtl(51)
                .signedCallbacks(false)
                .region(Region.APAC_SNG)
                .legPersistenceTime(14)
                .build();

        class Internal implements Jsonable {
            @JsonProperty final Voice voice = request;
        }
        String expectedJson = "{\"voice\":{" +
                "\"region\":\"apac-sng\",\"signed_callbacks\":false,\"conversations_ttl\":51" +
                ",\"leg_persistence_time\":14}}";
        assertEquals(expectedJson, new Internal().toJson());
    }

    @Test
    public void testConversationsTtlBounds() {
        Integer min = 0, max = 744;
        assertEquals(min, Voice.builder().conversationsTtl(min).build().getConversationsTtl());
        assertEquals(max, Voice.builder().conversationsTtl(max).build().getConversationsTtl());
        assertThrows(IllegalArgumentException.class, () -> Voice.builder().conversationsTtl(min-1).build());
        assertThrows(IllegalArgumentException.class, () -> Voice.builder().conversationsTtl(max+1).build());
    }

    @Test
    public void testLegPersistenceTimeBounds() {
        Integer min = 0, max = 31;
        assertEquals(min, Voice.builder().legPersistenceTime(min).build().getLegPersistenceTime());
        assertEquals(max, Voice.builder().legPersistenceTime(max).build().getLegPersistenceTime());
        assertThrows(IllegalArgumentException.class, () -> Voice.builder().legPersistenceTime(min-1).build());
        assertThrows(IllegalArgumentException.class, () -> Voice.builder().legPersistenceTime(max+1).build());
    }
}
