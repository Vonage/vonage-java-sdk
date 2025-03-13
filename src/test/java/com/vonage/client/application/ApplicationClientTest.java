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
package com.vonage.client.application;

import com.vonage.client.AbstractClientTest;
import com.vonage.client.RestEndpoint;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import com.vonage.client.application.capabilities.*;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ApplicationClientTest extends AbstractClientTest<ApplicationClient> {
    static final UUID SAMPLE_APPLICATION_ID = UUID.randomUUID();

    static final String SAMPLE_APPLICATION = "{\n" +
            "  \"id\": \"78d335fa-323d-0114-9c3d-d6f0d48968cf\",\n" +
            "  \"name\": \"My Application\",\n" +
            "  \"capabilities\": {\n" +
            "    \"voice\": {\n" +
            "      \"webhooks\": {\n" +
            "        \"answer_url\": {\n" +
            "          \"address\": \"https://example.com/webhooks/answer\",\n" +
            "          \"http_method\": \"POST\"\n" +
            "        },\n" +
            "        \"fallback_answer_url\": {\n" +
            "           \"address\": \"https://fallback.example.com/webhooks/answer\",\n" +
            "           \"http_method\": \"POST\",\n" +
            "           \"connection_timeout\": 500,\n" +
            "           \"socket_timeout\": 3000\n" +
            "        },\n" +
            "        \"event_url\": {\n" +
            "          \"address\": \"https://example.com/webhooks/event\",\n" +
            "          \"http_method\": \"POST\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"payment_enabled\": false,\n" +
            "      \"signed_callbacks\": true,\n" +
            "      \"conversations_ttl\": 24,\n" +
            "      \"leg_persistence_time\": 7,\n" +
            "      \"region\": \"eu-west\",\n" +
            "      \"payments\": {\n" +
            "        \"gateways\": []\n" +
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
            "    \"network_apis\": {\n" +
            "      \"network_application_id\": \"2bzfIFqRG128IcjSj1YhZNtw6LADG\",\n" +
            "      \"redirect_uri\": \"https://my-redirect-uri.example.com\"\n" +
            "    },\n" +
            "    \"rtc\": {\n" +
            "      \"webhooks\": {\n" +
            "        \"event_url\": {\n" +
            "          \"address\": \"https://example.com/webhooks/event\",\n" +
            "          \"http_method\": \"POST\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"signed_callbacks\": true\n" +
            "    },\n" +
            "    \"vbc\": {},\n" +
            "    \"verify\": {\n" +
            "       \"version\": \"v2\",\n" +
            "       \"webhooks\": {\n" +
            "          \"status_url\": {\n" +
            "             \"address\": \"https://example.com/webhooks/status\",\n" +
            "             \"http_method\": \"POST\"\n" +
            "          }\n" +
            "       }\n" +
            "    }\n" +
            "  },\n" +
            "  \"keys\": {\n" +
            "    \"public_key\": \"-----BEGIN PUBLIC KEY-----\\nMIIBIjANBgkqhkiG9w0BAQEFAAOCA\\nKOxjsU4pf/sMFi9N0jqcSLcjxu33G\\nd/vynKnlw9SENi+UZR44GdjGdmfm1\\ntL1eA7IBh2HNnkYXnAwYzKJoa4eO3\\n0kYWekeIZawIwe/g9faFgkev+1xsO\\nOUNhPx2LhuLmgwWSRS4L5W851Xe3f\\nUQIDAQAB\\n-----END PUBLIC KEY-----\\n\",\n" +
            "    \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFA\\nASCBKcwggSjAgEAAoIBAQDEPpvi+3\\nRH1efQ\\\\nkveWzZDrNNoEXmBw61w+O\\n0u/N36tJnN5XnYecU64yHzu2ByEr0\\n7iIvYbavFnADwl\\\\nHMTJwqDQakpa3\\n8/SFRnTDq3zronvNZ6nOp7S6K7pcZ\\nrw/CvrL6hXT1x7cGBZ4jPx\\\\nqhjqY\\nuJPgZD7OVB69oYOV92vIIJ7JLYwqb\\n-----END PRIVATE KEY-----\\n\"\n" +
            "  },\n" +
            "  \"privacy\": {\n" +
            "      \"improve_ai\": true\n" +
            "   }\n" +
            "}";

    public ApplicationClientTest() {
        client = new ApplicationClient(wrapper);
    }

    static void assertEqualsSampleApplication(Application response) {
        testJsonableBaseObject(response);
        assertEquals("78d335fa-323d-0114-9c3d-d6f0d48968cf", response.getId());
        assertEquals("My Application", response.getName());

        Application.Capabilities capabilities = response.getCapabilities();
        Voice voice = capabilities.getVoice();
        assertEquals(Capability.Type.VOICE, voice.getType());
        assertEquals("https://example.com/webhooks/event", voice.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.POST, voice.getWebhooks().get(Webhook.Type.EVENT).getMethod());
        assertEquals("https://example.com/webhooks/answer", voice.getWebhooks().get(Webhook.Type.ANSWER).getAddress());
        assertEquals(HttpMethod.POST, voice.getWebhooks().get(Webhook.Type.ANSWER).getMethod());
        Webhook fallback = voice.getWebhooks().get(Webhook.Type.FALLBACK_ANSWER);
        assertEquals("https://fallback.example.com/webhooks/answer", fallback.getAddress());
        assertEquals(HttpMethod.POST, fallback.getMethod());
        assertEquals(500, fallback.getConnectionTimeout().intValue());
        assertEquals(3000, fallback.getSocketTimeout().intValue());
        assertEquals(Region.EU_WEST, voice.getRegion());
        assertEquals(24, voice.getConversationsTtl().intValue());
        assertEquals(7, voice.getLegPersistenceTime().intValue());
        assertTrue(voice.getSignedCallbacks());

        Messages message = capabilities.getMessages();
        assertEquals(Capability.Type.MESSAGES, message.getType());
        assertEquals("https://example.com/webhooks/inbound", message.getWebhooks().get(Webhook.Type.INBOUND).getAddress());
        assertEquals(HttpMethod.POST, message.getWebhooks().get(Webhook.Type.INBOUND).getMethod());
        assertEquals("https://example.com/webhooks/status", message.getWebhooks().get(Webhook.Type.STATUS).getAddress());
        assertEquals(HttpMethod.POST, message.getWebhooks().get(Webhook.Type.STATUS).getMethod());

        NetworkApis networkApis = capabilities.getNetworkApis();
        assertEquals(Capability.Type.NETWORK, networkApis.getType());
        assertEquals("2bzfIFqRG128IcjSj1YhZNtw6LADG", networkApis.getNetworkApplicationId());
        assertEquals(URI.create("https://my-redirect-uri.example.com"), networkApis.getRedirectUri());

        Rtc rtc = capabilities.getRtc();
        assertEquals(Capability.Type.RTC, rtc.getType());
        assertEquals("https://example.com/webhooks/event", rtc.getWebhooks().get(Webhook.Type.EVENT).getAddress());
        assertEquals(HttpMethod.POST, rtc.getWebhooks().get(Webhook.Type.EVENT).getMethod());
        assertTrue(rtc.getSignedCallbacks());

        Vbc vbc = capabilities.getVbc();
        assertEquals(Capability.Type.VBC, vbc.getType());
        assertNull(vbc.getWebhooks());

        Verify verify = capabilities.getVerify();
        assertEquals(Capability.Type.VERIFY, verify.getType());
        assertEquals("https://example.com/webhooks/status", verify.getWebhooks().get(Webhook.Type.STATUS).getAddress());
        assertEquals(HttpMethod.POST, verify.getWebhooks().get(Webhook.Type.STATUS).getMethod());

        Application.Privacy privacy = response.getPrivacy();
        assertNotNull(privacy);
        assertTrue(privacy.getImproveAi());
    }

    void assert400ResponseException(Executable invocation) throws Exception {
        String response = "{\n" +
                "   \"type\": \"https://developer.nexmo.com/api-errors/application#payload-validation\",\n" +
                "   \"title\": \"Bad Request\",\n" +
                "   \"detail\": \"The request failed due to validation errors\",\n" +
                "   \"invalid_parameters\": [\n" +
                "      {\n" +
                "         \"name\": \"capabilities.voice.webhooks.answer_url.http_method\",\n" +
                "         \"reason\": \"must be one of: GET, POST\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" +
                "}";
        assertApiResponseException(400, response, ApplicationResponseException.class, invocation);
    }

    @Test
    public void testCreateApplication() throws Exception {
        stubResponse(201, SAMPLE_APPLICATION);
        Application request = Application.builder().name("My App").build();
        assertEqualsSampleApplication(client.createApplication(request));
        assertThrows(NullPointerException.class, () -> client.createApplication(null));
        assert400ResponseException(() -> client.createApplication(request));
    }

    @Test
    public void testUpdateApplication() throws Exception {
        stubResponse(200, SAMPLE_APPLICATION);
        Application request = Application.builder().name("Test app").build();
        assertEqualsSampleApplication(client.updateApplication(request));
        assertThrows(NullPointerException.class, () -> client.updateApplication(null));
        assert400ResponseException(() -> client.updateApplication(request));
    }

    @Test
    public void testGetApplication() throws Exception {
        stubResponse(200, SAMPLE_APPLICATION);
        String request = SAMPLE_APPLICATION_ID.toString();
        assertEqualsSampleApplication(client.getApplication(request));
        assertThrows(NullPointerException.class, () -> client.getApplication(null));
        assertThrows(IllegalArgumentException.class, () -> client.getApplication("abc123"));
        assert400ResponseException(() -> client.getApplication(request));
    }

    @Test
    public void testDeleteApplication() throws Exception {
        String request = SAMPLE_APPLICATION_ID.toString();
        stubResponseAndRun(204, () -> client.deleteApplication(request));
        assertThrows(NullPointerException.class, () -> client.deleteApplication(null));
        assertThrows(IllegalArgumentException.class, () -> client.deleteApplication("abc123"));
        assert400ResponseException(() -> client.deleteApplication(request));
    }

    @Test
    public void testListApplicationsWithOneResult() throws Exception {
        stubResponse(200, "{\n" +
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
    public void testListApplicationsWithMultipleResults() throws Exception {
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
    public void testListApplicationsWithNoResults() throws Exception {
        String json = "{\"page\":1,\"_embedded\":{\"applications\":[]}}";
        ApplicationList hal = stubResponseAndGet(json, () ->
                client.listApplications(ListApplicationRequest.builder().page(1).build())
        );
        assertEquals(0, hal.getApplications().size());
        assertEquals(1, hal.getPage().intValue());
        assertEquals(0, stubResponseAndGet(json, client::listAllApplications).size());
        assertNotNull(stubResponseAndGet(json, () -> client.listApplications(null)));
        assert400ResponseException(client::listAllApplications);
        assert400ResponseException(client::listApplications);
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
        new ApplicationEndpointTestSpec<UUID, Application>() {

            @Override
            protected RestEndpoint<UUID, Application> endpoint() {
                return client.getApplication;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected UUID sampleRequest() {
                return SAMPLE_APPLICATION_ID;
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
        new ApplicationEndpointTestSpec<UUID, Void>() {

            @Override
            protected RestEndpoint<UUID, Void> endpoint() {
                return client.deleteApplication;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.DELETE;
            }

            @Override
            protected UUID sampleRequest() {
                return SAMPLE_APPLICATION_ID;
            }
        }
        .runTests();
    }
}
