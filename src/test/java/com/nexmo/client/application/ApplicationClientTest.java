/*
 * Copyright (c) 2011-2019 Nexmo Inc
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
package com.nexmo.client.application;

import com.nexmo.client.ClientTest;
import com.nexmo.client.application.capabilities.*;
import com.nexmo.client.common.HttpMethod;
import com.nexmo.client.common.Webhook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ApplicationClientTest extends ClientTest<ApplicationClient> {
    @Before
    public void setUp() {
        client = new ApplicationClient(wrapper);
    }

    @Test
    public void testCreateApplication() throws Exception {
        wrapper.setHttpClient(stubHttpClient(201, "{\n" +
                "  \"id\": \"78d335fa323d01149c3dd6f0d48968cf\",\n" +
                "  \"name\": \"My Application\",\n" +
                "  \"capabilities\": {\n" +
                "    \"voice\": {\n" +
                "      \"webhooks\": {\n" +
                "        \"answer_url\": {\n" +
                "          \"address\": \"https://example.com/webhooks/answer\",\n" +
                "          \"http_method\": \"POST\"\n" +
                "        },\n" +
                "        \"event_url\": {\n" +
                "          \"address\": \"https://example.com/webhooks/event\",\n" +
                "          \"http_method\": \"POST\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"messages\": {\n" +
                "      \"webhooks\": {\n" +
                "        \"inbound_url\": {\n" +
                "          \"address\": \"https://example.com/webhooks/inbound\",\n" +
                "          \"http_method\": \"POST\"\n" +
                "        },\n" +
                "        \"status_url\": {\n" +
                "          \"address\": \"https://example.com/webhooks/status\",\n" +
                "          \"http_method\": \"POST\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"rtc\": {\n" +
                "      \"webhooks\": {\n" +
                "        \"event_url\": {\n" +
                "          \"address\": \"https://example.com/webhooks/event\",\n" +
                "          \"http_method\": \"POST\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"vbc\": {}\n" +
                "  },\n" +
                "  \"keys\": {\n" +
                "    \"public_key\": \"-----BEGIN PUBLIC KEY-----\\nMIIBIjANBgkqhkiG9w0BAQEFAAOCA\\nKOxjsU4pf/sMFi9N0jqcSLcjxu33G\\nd/vynKnlw9SENi+UZR44GdjGdmfm1\\ntL1eA7IBh2HNnkYXnAwYzKJoa4eO3\\n0kYWekeIZawIwe/g9faFgkev+1xsO\\nOUNhPx2LhuLmgwWSRS4L5W851Xe3f\\nUQIDAQAB\\n-----END PUBLIC KEY-----\\n\",\n" +
                "    \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFA\\nASCBKcwggSjAgEAAoIBAQDEPpvi+3\\nRH1efQ\\\\nkveWzZDrNNoEXmBw61w+O\\n0u/N36tJnN5XnYecU64yHzu2ByEr0\\n7iIvYbavFnADwl\\\\nHMTJwqDQakpa3\\n8/SFRnTDq3zronvNZ6nOp7S6K7pcZ\\nrw/CvrL6hXT1x7cGBZ4jPx\\\\nqhjqY\\nuJPgZD7OVB69oYOV92vIIJ7JLYwqb\\n-----END PRIVATE KEY-----\\n\"\n" +
                "  }\n" +
                "}"));

        Application response = client.create(Application.builder().build());

        assertEquals("78d335fa323d01149c3dd6f0d48968cf", response.getId());
        assertEquals("My Application", response.getName());

        Application.Capabilities capabilities = response.getCapabilities();

        Voice voice = capabilities.getVoice();
        assertEquals(Capability.Type.VOICE, voice.getType());
        assertEquals("https://example.com/webhooks/event", voice.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.POST, voice.getWebhooks().get(Webhook.Type.EVENT).getMethod());
        assertEquals("https://example.com/webhooks/answer", voice.getWebhooks().get(Webhook.Type.ANSWER).getAddress());
        assertEquals(HttpMethod.POST, voice.getWebhooks().get(Webhook.Type.ANSWER).getMethod());

        Messages message = capabilities.getMessages();
        assertEquals(Capability.Type.MESSAGES, message.getType());
        assertEquals("https://example.com/webhooks/inbound", message.getWebhooks().get(Webhook.Type.INBOUND).getAddress());
        assertEquals(HttpMethod.POST, message.getWebhooks().get(Webhook.Type.INBOUND).getMethod());
        assertEquals("https://example.com/webhooks/status", message.getWebhooks().get(Webhook.Type.STATUS).getAddress());
        assertEquals(HttpMethod.POST, message.getWebhooks().get(Webhook.Type.STATUS).getMethod());

        Rtc rtc = capabilities.getRtc();
        assertEquals(Capability.Type.RTC, rtc.getType());
        assertEquals("https://example.com/webhooks/event", rtc.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.POST, rtc.getWebhooks().get(Webhook.Type.EVENT).getMethod());

        Vbc vbc = capabilities.getVbc();
        assertEquals(Capability.Type.VBC, vbc.getType());
        assertNull(vbc.getWebhooks());
    }

    @Test
    public void testUpdateApplication() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, "{\n" +
                "  \"id\": \"78d335fa323d01149c3dd6f0d48968cf\",\n" +
                "  \"name\": \"My Application\",\n" +
                "  \"capabilities\": {\n" +
                "    \"voice\": {\n" +
                "      \"webhooks\": {\n" +
                "        \"answer_url\": {\n" +
                "          \"address\": \"https://example.com/webhooks/answer\",\n" +
                "          \"http_method\": \"POST\"\n" +
                "        },\n" +
                "        \"event_url\": {\n" +
                "          \"address\": \"https://example.com/webhooks/event\",\n" +
                "          \"http_method\": \"POST\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"messages\": {\n" +
                "      \"webhooks\": {\n" +
                "        \"inbound_url\": {\n" +
                "          \"address\": \"https://example.com/webhooks/inbound\",\n" +
                "          \"http_method\": \"POST\"\n" +
                "        },\n" +
                "        \"status_url\": {\n" +
                "          \"address\": \"https://example.com/webhooks/status\",\n" +
                "          \"http_method\": \"POST\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"rtc\": {\n" +
                "      \"webhooks\": {\n" +
                "        \"event_url\": {\n" +
                "          \"address\": \"https://example.com/webhooks/event\",\n" +
                "          \"http_method\": \"POST\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"vbc\": {}\n" +
                "  },\n" +
                "  \"keys\": {\n" +
                "    \"public_key\": \"-----BEGIN PUBLIC KEY-----\\nMIIBIjANBgkqhkiG9w0BAQEFAAOCA\\nKOxjsU4pf/sMFi9N0jqcSLcjxu33G\\nd/vynKnlw9SENi+UZR44GdjGdmfm1\\ntL1eA7IBh2HNnkYXnAwYzKJoa4eO3\\n0kYWekeIZawIwe/g9faFgkev+1xsO\\nOUNhPx2LhuLmgwWSRS4L5W851Xe3f\\nUQIDAQAB\\n-----END PUBLIC KEY-----\\n\",\n" +
                "    \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFA\\nASCBKcwggSjAgEAAoIBAQDEPpvi+3\\nRH1efQ\\\\nkveWzZDrNNoEXmBw61w+O\\n0u/N36tJnN5XnYecU64yHzu2ByEr0\\n7iIvYbavFnADwl\\\\nHMTJwqDQakpa3\\n8/SFRnTDq3zronvNZ6nOp7S6K7pcZ\\nrw/CvrL6hXT1x7cGBZ4jPx\\\\nqhjqY\\nuJPgZD7OVB69oYOV92vIIJ7JLYwqb\\n-----END PRIVATE KEY-----\\n\"\n" +
                "  }\n" +
                "}"));

        Application response = client.update(Application.builder().build());

        assertEquals("78d335fa323d01149c3dd6f0d48968cf", response.getId());
        assertEquals("My Application", response.getName());

        Application.Capabilities capabilities = response.getCapabilities();

        Voice voice = capabilities.getVoice();
        assertEquals(Capability.Type.VOICE, voice.getType());
        assertEquals("https://example.com/webhooks/event", voice.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.POST, voice.getWebhooks().get(Webhook.Type.EVENT).getMethod());
        assertEquals("https://example.com/webhooks/answer", voice.getWebhooks().get(Webhook.Type.ANSWER).getAddress());
        assertEquals(HttpMethod.POST, voice.getWebhooks().get(Webhook.Type.ANSWER).getMethod());

        Messages message = capabilities.getMessages();
        assertEquals(Capability.Type.MESSAGES, message.getType());
        assertEquals("https://example.com/webhooks/inbound", message.getWebhooks().get(Webhook.Type.INBOUND).getAddress());
        assertEquals(HttpMethod.POST, message.getWebhooks().get(Webhook.Type.INBOUND).getMethod());
        assertEquals("https://example.com/webhooks/status", message.getWebhooks().get(Webhook.Type.STATUS).getAddress());
        assertEquals(HttpMethod.POST, message.getWebhooks().get(Webhook.Type.STATUS).getMethod());

        Rtc rtc = capabilities.getRtc();
        assertEquals(Capability.Type.RTC, rtc.getType());
        assertEquals("https://example.com/webhooks/event", rtc.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.POST, rtc.getWebhooks().get(Webhook.Type.EVENT).getMethod());

        Vbc vbc = capabilities.getVbc();
        assertEquals(Capability.Type.VBC, vbc.getType());
        assertNull(vbc.getWebhooks());
    }
}
