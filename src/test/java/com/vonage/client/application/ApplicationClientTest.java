/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.application;

import com.vonage.client.ClientTest;
import com.vonage.client.application.capabilities.*;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.common.Webhook;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ApplicationClientTest extends ClientTest<ApplicationClient> {

    public ApplicationClientTest() {
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

        Application response = client.createApplication(Application.builder().build());

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

        Application response = client.updateApplication(Application.builder().build());

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
    public void testGetApplication() throws Exception {
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

        Application response = client.getApplication("78d335fa323d01149c3dd6f0d48968cf");

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
    public void testDeleteApplication() throws Exception {
        wrapper.setHttpClient(stubHttpClient(204, ""));

        client.deleteApplication("78d335fa323d01149c3dd6f0d48968cf");
    }

    @Test
    public void testListApplicationWithOneResult() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, "{\n" +
                "  \"page_size\": 10,\n" +
                "  \"page\": 1,\n" +
                "  \"total_items\": 1,\n" +
                "  \"total_pages\": 1,\n" +
                "  \"_embedded\": {\n" +
                "    \"applications\": [\n" +
                "      {\n" +
                "        \"id\": \"78d335fa323d01149c3dd6f0d48968cf\",\n" +
                "        \"name\": \"My Application\",\n" +
                "        \"capabilities\": {\n" +
                "          \"voice\": {\n" +
                "            \"webhooks\": {\n" +
                "              \"answer_url\": {\n" +
                "                \"address\": \"https://example.com/webhooks/answer\",\n" +
                "                \"http_method\": \"POST\"\n" +
                "              },\n" +
                "              \"event_url\": {\n" +
                "                \"address\": \"https://example.com/webhooks/event\",\n" +
                "                \"http_method\": \"POST\"\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"messages\": {\n" +
                "            \"webhooks\": {\n" +
                "              \"inbound_url\": {\n" +
                "                \"address\": \"https://example.com/webhooks/inbound\",\n" +
                "                \"http_method\": \"POST\"\n" +
                "              },\n" +
                "              \"status_url\": {\n" +
                "                \"address\": \"https://example.com/webhooks/status\",\n" +
                "                \"http_method\": \"POST\"\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"rtc\": {\n" +
                "            \"webhooks\": {\n" +
                "              \"event_url\": {\n" +
                "                \"address\": \"https://example.com/webhooks/event\",\n" +
                "                \"http_method\": \"POST\"\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"vbc\": {}\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}"));

        ApplicationList response = client.listApplications();

        assertEquals(10, response.getPageSize());
        assertEquals(1, response.getPage());
        assertEquals(1, response.getTotalItems());
        assertEquals(1, response.getTotalPages());

        assertEquals(1, response.getApplications().size());
    }

    @Test
    public void testListApplicationWithMultipleResults() throws Exception {
        wrapper.setHttpClient(stubHttpClient(200, "{\n" +
                "  \"page_size\": 10,\n" +
                "  \"page\": 1,\n" +
                "  \"total_items\": 2,\n" +
                "  \"total_pages\": 1,\n" +
                "  \"_embedded\": {\n" +
                "    \"applications\": [\n" +
                "      {\n" +
                "        \"id\": \"1\",\n" +
                "        \"name\": \"My Application\",\n" +
                "        \"capabilities\": {\n" +
                "          \"voice\": {\n" +
                "            \"webhooks\": {\n" +
                "              \"answer_url\": {\n" +
                "                \"address\": \"https://example.com/webhooks/answer\",\n" +
                "                \"http_method\": \"POST\"\n" +
                "              },\n" +
                "              \"event_url\": {\n" +
                "                \"address\": \"https://example.com/webhooks/event\",\n" +
                "                \"http_method\": \"POST\"\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": \"2\",\n" +
                "        \"name\": \"My Second Application\",\n" +
                "        \"capabilities\": {\n" +
                "          \"voice\": {\n" +
                "            \"webhooks\": {\n" +
                "              \"answer_url\": {\n" +
                "                \"address\": \"https://example.com/webhooks/answer\",\n" +
                "                \"http_method\": \"POST\"\n" +
                "              },\n" +
                "              \"event_url\": {\n" +
                "                \"address\": \"https://example.com/webhooks/event\",\n" +
                "                \"http_method\": \"POST\"\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}"));

        ApplicationList response = client.listApplications();

        assertEquals(10, response.getPageSize());
        assertEquals(1, response.getPage());
        assertEquals(2, response.getTotalItems());
        assertEquals(1, response.getTotalPages());

        assertEquals(2, response.getApplications().size());
    }
}
