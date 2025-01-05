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
import com.vonage.client.common.Webhook;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RtcTest {
    @Test
    public void testEmpty() {
        Rtc rtc = Rtc.builder().build();

        assertEquals(Capability.Type.RTC, rtc.getType());
        assertNull(rtc.getWebhooks());
        assertNull(rtc.getSignedCallbacks());
    }

    @Test
    public void testEventWebhook() {
        Rtc rtc = Rtc.builder()
                .addWebhook(Webhook.Type.EVENT, new Webhook("https://example.com/event", HttpMethod.GET))
                .signedCallbacks(true).build();

        assertEquals(Capability.Type.RTC, rtc.getType());
        assertEquals("https://example.com/event", rtc.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertTrue(rtc.getSignedCallbacks());
        assertEquals(HttpMethod.GET, rtc.getWebhooks().get(Webhook.Type.EVENT).getMethod());
    }

    @Test
    public void testRemoveWebhook() {
        Rtc rtc = Rtc.builder()
                .addWebhook(Webhook.Type.EVENT, new Webhook("https://example.com/event", HttpMethod.POST))
                .removeWebhook(Webhook.Type.EVENT)
                .signedCallbacks(false).build();

        assertEquals(Capability.Type.RTC, rtc.getType());
        assertFalse(rtc.getSignedCallbacks());
        assertNull(rtc.getWebhooks());
    }
}
