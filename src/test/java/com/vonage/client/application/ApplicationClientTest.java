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
package com.vonage.client.application;

import com.vonage.client.ClientTest;
import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageApiResponseException;
import com.vonage.client.application.capabilities.*;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.common.Webhook;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class ApplicationClientTest extends ClientTest<ApplicationClient> {

    public ApplicationClientTest() {
        client = new ApplicationClient(wrapper);
    }

    @Test
    public void testCreateApplication() throws Exception {
        stubResponse(201, "{\n" +
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
                "  },\n" +
                "  \"privacy\": {\n" +
                "    \"improve_ai\": false\n" +
                "  }\n" +
                "}"
        );

        Application response = client.createApplication(Application.builder().build());

        assertFalse(response.getPrivacy().getImproveAi());
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
        stubResponse( "{\n" +
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
                "}"
        );

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
        stubResponse( "{\n" +
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
                "}"
        );

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
        stubResponseAndRun(204, () -> client.deleteApplication("78d335fa323d01149c3dd6f0d48968cf"));
    }

    @Test
    public void testListApplicationWithOneResult() throws Exception {
        stubResponse("{\n" +
                "  \"page_size\": 10,\n" +
                "  \"page\": 5,\n" +
                "  \"total_items\": 6,\n" +
                "  \"total_pages\": 7,\n" +
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
                "}"
        );

        ApplicationList response = client.listApplications();
        assertEquals((Object) 10, response.getPageSize());
        assertEquals((Object) 5, response.getPage());
        assertEquals((Object) 6, response.getTotalItems());
        assertEquals((Object) 7, response.getTotalPages());
        assertEquals(1, response.getApplications().size());
    }

    @Test
    public void testListApplicationWithMultipleResults() throws Exception {
        String json = "{\n" +
                "  \"page_size\": 10,\n" +
                "  \"page\": 1,\n" +
                "  \"total_items\": 12,\n" +
                "  \"total_pages\": 3,\n" +
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
                "}";

        ApplicationList response = stubResponseAndGet(json, client::listApplications);

        assertEquals((Object) 10, response.getPageSize());
        assertEquals((Object) 1, response.getPage());
        assertEquals((Object) 12, response.getTotalItems());
        assertEquals((Object) 3, response.getTotalPages());

        List<Application> applications = response.getApplications();
        assertNotNull(applications);
        assertEquals(2, applications.size());

        assertEquals("My Application", applications.get(0).getName());
        assertEquals("My Second Application", applications.get(1).getName());

        applications = stubResponseAndGet(json, client::listAllApplications);
        assertEquals(2, applications.size());
    }

    @Test
    public void testListApplicationWithNoResults() throws Exception {
        String json = "{\"page\":1,\"_embedded\":{\"applications\":[]}}";
        ApplicationList hal = stubResponseAndGet(json, () ->
                client.listApplications(ListApplicationRequest.builder().page(1).build())
        );
        assertEquals(0, hal.getApplications().size());
        assertEquals(1, hal.getPage().intValue());
        assertEquals(0, stubResponseAndGet(json, client::listAllApplications).size());
    }

    static abstract class ApplicationEndpointTestSpec<T, R> extends DynamicEndpointTestSpec<T, R> {

        @Override
        protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
            return Collections.singletonList(TokenAuthMethod.class);
        }

        @Override
        protected Class<? extends Exception> expectedResponseExceptionType() {
            return VonageApiResponseException.class;
        }

        @Override
        protected String expectedDefaultBaseUri() {
            return "https://api.nexmo.com";
        }

        @Override
        protected String expectedEndpointUri(T request) {
            String base = "/v2/applications", suffix;
            if (request instanceof String) {
                suffix = (String) request;
            }
            else if (request instanceof Application && HttpMethod.PUT.equals(expectedHttpMethod())) {
                suffix = ((Application) request).getId();
            }
            else {
                suffix = null;
            }
            return suffix != null ? base + "/" + suffix : base;
        }

        @Override
        protected String sampleRequestBodyString() {
            return null;
        }
    }

    @Test
    public void testListApplicationsEndpoint() throws Exception {
        new ApplicationEndpointTestSpec<ListApplicationRequest, ApplicationList>() {

            @Override
            protected RestEndpoint<ListApplicationRequest, ApplicationList> endpoint() {
                return client.listApplications;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected ListApplicationRequest sampleRequest() {
                return ListApplicationRequest.builder().page(14).pageSize(25).build();
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                ListApplicationRequest request = sampleRequest();
                Map<String, String> params = new LinkedHashMap<>();
                params.put("page", String.valueOf(request.getPage()));
                params.put("page_size", String.valueOf(request.getPageSize()));
                return params;
            }
        }
        .runTests();
    }

    @Test
    public void testCreateApplicationEndpoint() throws Exception {
        new ApplicationEndpointTestSpec<Application, Application>() {

            @Override
            protected RestEndpoint<Application, Application> endpoint() {
                return client.createApplication;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected Application sampleRequest() {
                return Application.builder().name("Test app").build();
            }

            @Override
            protected String sampleRequestBodyString() {
                return "{\"name\":\"Test app\"}";
            }
        }
        .runTests();
    }

    @Test
    public void testGetApplicationEndpoint() throws Exception {
        new ApplicationEndpointTestSpec<String, Application>() {

            @Override
            protected RestEndpoint<String, Application> endpoint() {
                return client.getApplication;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected String sampleRequest() {
                return UUID.randomUUID().toString();
            }
        }
        .runTests();
    }

    @Test
    public void testUpdateApplicationEndpoint() throws Exception {
        new ApplicationEndpointTestSpec<Application, Application>() {
            final String APP_ID = UUID.randomUUID().toString();

            @Override
            protected RestEndpoint<Application, Application> endpoint() {
                return client.updateApplication;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.PUT;
            }

            @Override
            protected Application sampleRequest() {
                return Application.fromJson(sampleRequestBodyString());
            }

            @Override
            protected String sampleRequestBodyString() {
                return "{\"id\":\""+APP_ID+"\"}";
            }
        }
        .runTests();
    }

    @Test
    public void testDeleteApplicationEndpoint() throws Exception {
        new ApplicationEndpointTestSpec<String, Void>() {

            @Override
            protected RestEndpoint<String, Void> endpoint() {
                return client.deleteApplication;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.DELETE;
            }

            @Override
            protected String sampleRequest() {
                return UUID.randomUUID().toString();
            }
        }
        .runTests();
    }
}
