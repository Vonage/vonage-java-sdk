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
package com.vonage.client.application;

import com.vonage.client.application.capabilities.Capability;
import com.vonage.client.application.capabilities.Messages;
import com.vonage.client.application.capabilities.Rtc;
import com.vonage.client.application.capabilities.Voice;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.common.Webhook;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApplicationTest {
    @Test
    public void testEmpty() {
        String json = "{}";
        Application application = Application.builder().build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testName() {
        String json = "{\"name\":\"name\"}";
        Application application = Application.builder().name("name").build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testPublicKey() {
        String json = "{\"keys\":{\"public_key\":\"public key\"}}";
        Application application = Application.builder().publicKey("public key").build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testAddCapability() {
        String json = "{\"capabilities\":{\"voice\":{}}}";
        Application application = Application.builder().addCapability(Voice.builder().build()).build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testAddMultipleCapabilitiesOfSameType() {
        String json = "{\"capabilities\":{\"voice\":{}}}";
        Application application = Application.builder()
                .addCapability(Voice.builder().build())
                .addCapability(Voice.builder().build())
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testAddMultipleCapabilitiesOfDifferentType() {
        String json = "{\"capabilities\":{\"voice\":{},\"rtc\":{}}}";
        Application application = Application.builder()
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testRemoveCapability() {
        String json = "{\"capabilities\":{\"rtc\":{}}}";
        Application application = Application.builder()
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .removeCapability(Capability.Type.VOICE)
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testRemoveMultipleCapabilities() {
        String json = "{}";
        Application application = Application.builder()
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .removeCapability(Capability.Type.VOICE)
                .removeCapability(Capability.Type.RTC)
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testRemoveCapabilityThatDoesntExist() {
        String json = "{\"capabilities\":{\"voice\":{},\"rtc\":{}}}";
        Application application = Application.builder()
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .removeCapability(Capability.Type.VBC)
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testAddMessagesCapabilityWithInboundAndStatusWebhooks() {
        String json = "{\"capabilities\":{\"messages\":{\"webhooks\":{\"inbound_url\":{\"address\":\"https://example.com/inbound\",\"http_method\":\"POST\"},\"status_url\":{\"address\":\"https://example.com/status\",\"http_method\":\"GET\"}}}}}";
        Application application = Application.builder()
                .addCapability(
                        Messages.builder()
                                .addWebhook(Webhook.Type.INBOUND, new Webhook("https://example.com/inbound", HttpMethod.POST))
                                .addWebhook(Webhook.Type.STATUS, new Webhook("https://example.com/status", HttpMethod.GET))
                                .build())
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testAddMessagesCapabilityWithInboundWebhook() {
        String json = "{\"capabilities\":{\"messages\":{\"webhooks\":{\"inbound_url\":{\"address\":\"https://example.com/inbound\",\"http_method\":\"POST\"}}}}}";
        Application application = Application.builder()
                .addCapability(
                        Messages.builder()
                                .addWebhook(Webhook.Type.INBOUND, new Webhook("https://example.com/inbound", HttpMethod.POST))
                                .build())
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testUpdatingApplication() {
        String json = "{\"name\":\"updated\"}";
        Application initial = Application.builder().name("initial").build();
        Application updated = Application.builder(initial).name("updated").build();

        assertEquals(json, updated.toJson());
    }
}
