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

import com.vonage.client.Jsonable;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import com.vonage.client.application.capabilities.*;
import com.vonage.client.application.capabilities.Capability.Type;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.UUID;

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
    public void testAddSingleCapability() {
        String json = "{\"capabilities\":{\"verify\":{}}}";
        Application application = Application.builder().addCapability(Verify.builder().build()).build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testAddAllCapabilities() {
        Application application = Application.builder()
                .addCapability(Messages.builder().build())
                .addCapability(Verify.builder().build())
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .addCapability(Vbc.builder().build())
                .build();

        assertEquals(
                "{\"capabilities\":{\"voice\":{},\"messages\":{},\"rtc\":{},\"vbc\":{},\"verify\":{}}}",
                application.toJson()
        );

        testJsonableBaseObject(application);
    }

    @Test
    public void testAddMultipleCapabilitiesOfSameType() {
        String expectedJson = "{\"capabilities\":{\"voice\":{\"webhooks\":{\"fallback_answer_url\":" +
                "{\"address\":\"https://fallback.example.com/webhooks/answer\",\"http_method\":\"GET\"," +
                "\"connection_timeout\":500,\"socket_timeout\":3600}}}}}";

        Application application = Application.builder()
                .addCapability(Voice.builder().addWebhook(Webhook.Type.EVENT, Webhook.builder()
                        .address("https://example.com/webhooks/event").method(HttpMethod.POST).build()
                ).build())
                .addCapability(Voice.builder().addWebhook(Webhook.Type.FALLBACK_ANSWER,
                        Webhook.builder().method(HttpMethod.GET)
                                .address("https://fallback.example.com/webhooks/answer")
                                .connectionTimeout(500).socketTimeout(3600).build()
                ).build())
                .build();

        assertEquals(expectedJson, application.toJson());
        testJsonableBaseObject(application);
    }

    @Test
    public void testRemoveCapability() {
        String json = "{\"capabilities\":{\"rtc\":{}}}";
        Application application = Application.builder()
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .removeCapability(Type.VOICE)
                .build();

        assertEquals(json, application.toJson());
        testJsonableBaseObject(application);
    }

    @Test
    public void testRemoveMultipleCapabilities() {
        Application application = Application.builder()
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .removeCapabilities(Type.VOICE, Type.RTC)
                .build();

        assertEquals("{}", application.toJson());
        testJsonableBaseObject(application);
    }

    @Test
    public void testRemoveAllCapabilities() {
        Application application = Application.builder()
                .addCapability(Messages.builder().build())
                .addCapability(Verify.builder().build())
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .addCapability(Vbc.builder().build())
                .removeCapabilities(Type.MESSAGES, Type.VERIFY, Type.VOICE, Type.RTC, Type.VBC)
                .build();

        assertEquals("{}", application.toJson());
        testJsonableBaseObject(application);
    }

    @Test
    public void testRemoveCapabilityThatDoesntExist() {
        String json = "{\"capabilities\":{\"messages\":{},\"verify\":{},\"network_apis\":{}}}";
        Application application = Application.builder()
                .addCapability(NetworkApis.builder().build())
                .addCapability(Messages.builder().build())
                .addCapability(Verify.builder().build())
                .removeCapability(Type.VBC)
                .build();

        assertEquals(json, application.toJson());
        testJsonableBaseObject(application);
    }

    @Test
    public void testRemoveCapabilityThatDoesExist() {
        String json = "{\"capabilities\":{\"verify\":{},\"network_apis\":{}}}";
        Application application = Application.builder()
                .addCapability(NetworkApis.builder().build())
                .addCapability(Messages.builder().build())
                .addCapability(Verify.builder().build())
                .removeCapability(Type.MESSAGES)
                .build();

        assertEquals(json, application.toJson());
        testJsonableBaseObject(application);

        json = "{\"capabilities\":{\"network_apis\":{}}}";
        application = Application.builder()
                .addCapability(NetworkApis.builder().build())
                .addCapability(Verify.builder().build())
                .removeCapability(Type.VERIFY)
                .build();

        assertEquals(json, application.toJson());
        testJsonableBaseObject(application);
    }

    @Test
    public void testRemoveNullCapability() {
        assertThrows(NullPointerException.class, () -> Application.builder()
                .addCapability(Rtc.builder().build())
                .removeCapability(null)
        );
    }

    @Test
    public void testRemoveCapabilityWhenThereAreNoneAnyway() {
        Application application = Application.builder().removeCapability(Type.VBC).build();
        assertEquals("{}", application.toJson());
        testJsonableBaseObject(application);
    }

    @Test
    public void testRemoveLastRemainingCapability() {
        Application application = Application.builder()
                .addCapability(Vbc.builder().build())
                .removeCapability(Type.VBC).build();
        assertEquals("{}", application.toJson());
        testJsonableBaseObject(application);
    }

    @Test
    public void testRemoveWebhookWhenThereAreNone() {
        var voice = Voice.builder().removeWebhook(Webhook.Type.FALLBACK_ANSWER).build();
        assertNotNull(voice);
        assertNull(voice.getWebhooks());
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
        testJsonableBaseObject(application);
    }

    @Test
    public void testAddVerifyCapabilityWithStatusWebhooks() {
        String json = "{\"capabilities\":{\"verify\":{\"webhooks\":{\"status_url\":{\"address\":\"https://example.org/status_cb\",\"http_method\":\"GET\"}}}}}";
        Application application = Application.builder()
                .addCapability(Verify.builder()
                        .addWebhook(Webhook.Type.STATUS, new Webhook("https://example.org/status_cb", HttpMethod.GET))
                        .build()
                )
                .build();

        assertEquals(json, application.toJson());
        testJsonableBaseObject(application);
    }

    @Test
    public void testAddMessagesCapabilityWithInboundWebhook() {
        String json = "{\"capabilities\":{\"messages\":{\"webhooks\":{\"inbound_url\":{\"address\":\"https://example.com/inbound\",\"http_method\":\"POST\"}}}}}";
        Application application = Application.builder().addCapability(
                Messages.builder()
                        .addWebhook(Webhook.Type.INBOUND, new Webhook("https://example.com/inbound", HttpMethod.POST))
                        .build()
                )
            .build();

        assertEquals(json, application.toJson());
        testJsonableBaseObject(application);
    }

    @Test
    public void testAddNetworkApisCapabilityWithRedirectUriAndId() {
        String redirectUri = "http://localhost:8080/camara/redirect",
                networkApplicationId = "my-network-application-id",
                expectedJson = "{\"capabilities\":{\"network_apis\":{\"redirect_uri\":\"" +
                        redirectUri + "\",\"network_application_id\":\""+networkApplicationId+"\"}}}";

        Application application = Application.builder().addCapability(
                NetworkApis.builder()
                    .redirectUri(redirectUri)
                    .networkApplicationId(networkApplicationId)
                    .build()
                )
                .build();

        testJsonableBaseObject(application);
        assertEquals(expectedJson, application.toJson());
    }

    @Test
    public void testUpdatingApplication() {
        String json = "{\"name\":\"updated\"}";
        Application initial = Application.builder().name("initial").build();
        Application updated = Application.builder(initial).name("updated").build();

        assertEquals(json, updated.toJson());
    }

    @Test
    public void testPrivacy() {
        String json = "{\"privacy\":{";

        Application request = Application.builder().improveAi(false).improveAi(true).build();
        assertEquals(json + "\"improve_ai\":"+request.getPrivacy().getImproveAi()+"}}", request.toJson());
        Application parsed = Jsonable.fromJson(json + "}}");
        assertNotNull(parsed.getPrivacy());
        assertNull(parsed.getPrivacy().getImproveAi());

        parsed.updateFromJson(request.toJson());
        assertTrue(parsed.getPrivacy().getImproveAi());

        assertNull(request.getId());
        request.updateFromJson(json + "}, \"id\": \""+ UUID.randomUUID()+"\"}");
        assertNotNull(request.getId());
        assertNotNull(request.getPrivacy());
        assertNull(request.getPrivacy().getImproveAi());
        testJsonableBaseObject(request);
    }

    @Test
    public void testParseUnknownVoiceFields() {
        String json = "{\n" +
                "    \"id\": \"968331b7-db66-421d-bbb1-ea04c238b52f\",\n" +
                "    \"name\": \"neru_vapi_chatgpt\",\n" +
                "    \"keys\": {\n" +
                "    },\n" +
                "    \"privacy\": {\n" +
                "        \"improve_ai\": false\n" +
                "    },\n" +
                "    \"capabilities\": {\n" +
                "        \"voice\": {\n" +
                "            \"webhooks\": {\n" +
                "            },\n" +
                "            \"payment_enabled\": false,\n" +
                "            \"signed_callbacks\": false,\n" +
                "            \"conversations_ttl\": 0,\n" +
                "            \"region\": \"jupiter\",\n" +
                "            \"payments\": {\n" +
                "                \"gateways\": []\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"_links\": {\n" +
                "        \"self\": {\n" +
                "            \"href\": \"/v2/applications/968331b7-db66-421d-bbb1-ea04c238b52f\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        Application parsed = Jsonable.fromJson(json);

        assertNotNull(UUID.fromString(parsed.getId()));
        assertNotNull(parsed.getName());
        assertNotNull(parsed.getPrivacy());
        assertNotNull(parsed.getKeys());
        assertNotNull(parsed.getCapabilities());
        Voice voice = parsed.getCapabilities().getVoice();
        assertNotNull(voice);
        assertNotNull(voice.getWebhooks());
        assertEquals(0, voice.getWebhooks().size());
        assertEquals(0, voice.getConversationsTtl().intValue());
        assertFalse(voice.getSignedCallbacks());
        assertNull(voice.getRegion());
    }

    @Test
    public void testMissingWebhookAddress() {
        assertThrows(IllegalStateException.class, () -> Webhook.builder().build());
        assertThrows(IllegalStateException.class, () -> Webhook.builder().address("  ").build());
    }
}
