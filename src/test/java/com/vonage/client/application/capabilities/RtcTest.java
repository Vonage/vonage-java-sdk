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

public class RtcTest {
    @Test
    public void testEmpty() {
        Rtc rtc = Rtc.builder().build();

        assertEquals(Capability.Type.RTC, rtc.getType());
        assertNull(rtc.getWebhooks());
    }

    @Test
    public void testEventWebhook() {
        Rtc rtc = Rtc.builder().addWebhook(Webhook.Type.EVENT, new Webhook("https://example.com/event", HttpMethod.GET)).build();

        assertEquals(Capability.Type.RTC, rtc.getType());
        assertEquals("https://example.com/event", rtc.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.GET, rtc.getWebhooks().get(Webhook.Type.EVENT).getMethod());
    }

    @Test
    public void testRemoveWebhook() {
        Rtc rtc = Rtc.builder()
                .addWebhook(Webhook.Type.EVENT, new Webhook("https://example.com/event", HttpMethod.POST))
                .removeWebhook(Webhook.Type.EVENT)
                .build();

        assertEquals(Capability.Type.RTC, rtc.getType());
        assertNull(rtc.getWebhooks());
    }
}
