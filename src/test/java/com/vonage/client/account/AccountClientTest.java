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
package com.vonage.client.account;

import com.vonage.client.*;
import static com.vonage.client.TestUtils.API_KEY;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;

public class AccountClientTest extends AbstractClientTest<AccountClient> {
    static final String SECRET_ID = UUID.randomUUID().toString();

    public AccountClientTest() {
        client = new AccountClient(wrapper);
    }

    @Test
    public void testGetBalance() throws Exception {
        String json = "{\"value\": 10.28, \"autoReload\": true}";
        stubResponse(200, json);
        BalanceResponse response = client.getBalance();
        assertEquals(10.28, response.getValue(), 0.0001);
        assertTrue(response.isAutoReload());
        BalanceResponse fromJson = Jsonable.fromJson(json);
        assertEquals(response, fromJson);
        assertEquals(response.hashCode(), fromJson.hashCode());
        assertEquals("BalanceResponse "+json.replace(" ", ""), response.toString());
    }

    @Test
    public void testTopUpSuccessful() throws Exception {
        stubResponse(200);
        // No assertions as an exception will be thrown if failure occurs.
        client.topUp("ABC123");
    }

    @Test
    public void testTopUpNoTransactionId() throws Exception {
        stubResponse(200);
        assertThrows(IllegalArgumentException.class, () -> client.topUp(" "));
        assertThrows(IllegalArgumentException.class, () -> client.topUp(null));
    }

    @Test
    public void testTopUpFailed() throws Exception {
        String json = "{\"error-code\":\"420\",\"error-code-label\":\"topup failed\"}";
        AccountResponseException ex = assertApiResponseException(401, json, AccountResponseException.class,
                () -> client.topUp("ABC123")
        );
        assertEquals(401, ex.getStatusCode());
        assertEquals("topup failed", ex.getErrorCodeLabel());
    }

    @Test
    public void testListSecretSuccessful() throws Exception {
        String json = "{\n" + "    \"_links\": {\n" + "        \"self\": {\n"
                + "            \"href\": \"/accounts/abcd1234/secrets\"\n" + "        }\n" + "    },\n"
                + "    \"_embedded\": {\n" + "        \"secrets\": [\n" + "            {\n"
                + "                \"_links\": {\n" + "                    \"self\": {\n"
                + "                        \"href\": \"/accounts/abcd1234/secrets/secret-id-one\"\n"
                + "                    }\n" + "                },\n" + "                \"id\": \"secret-id-one\",\n"
                + "                \"created_at\": \"2017-03-02T16:34:49Z\"\n" + "            },\n" + "            {\n"
                + "                \"_links\": {\n" + "                    \"self\": {\n"
                + "                        \"href\": \"/accounts/abcd1234/secrets/secret-id-two\"\n"
                + "                    }\n" + "                },\n" + "                \"id\": \"secret-id-two\",\n"
                + "                \"created_at\": \"2016-01-20T16:34:49Z\"\n" + "            }\n" + "        ]\n"
                + "    }\n" + "}";
        stubResponse(200, json);

        ListSecretsResponse response = client.listSecrets();
        TestUtils.testJsonableBaseObject(response);
        SecretResponse[] responses = response.getSecrets().toArray(new SecretResponse[0]);

        Calendar calendar = new GregorianCalendar(2017, Calendar.MARCH, 2, 16, 34, 49);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals("secret-id-one", responses[0].getId());
        assertEquals(calendar.getTime(), Date.from(responses[0].getCreated()));
        //assertEquals("/accounts/abcd1234/secrets/secret-id-one", responses[0].getSelf().getHref());

        calendar.set(2016, Calendar.JANUARY, 20, 16, 34, 49);
        assertEquals("secret-id-two", responses[1].getId());
        assertEquals(calendar.getTime(), Date.from(responses[1].getCreated()));
        //assertEquals("/accounts/abcd1234/secrets/secret-id-two", responses[1].getSelf().getHref());
    }

    @Test
    public void testParseListSecretsMalformed() throws Exception {
        stubResponse(200, "{malformed]");
        assertThrows(VonageResponseParseException.class, () -> client.listSecrets(API_KEY));
    }

    @Test
    public void testListSecretFailed() throws Exception {
        String json = "{\n" + "  \"type\": \"https://developer.nexmo.com/api-errors#unauthorized\",\n"
                + "  \"title\": \"Invalid credentials supplied\",\n"
                + "  \"detail\": \"You did not provide correct credentials.\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        AccountResponseException ex = assertApiResponseException(401, json, AccountResponseException.class,
                () -> client.listSecrets("ABC123")
        );
        assertNotNull(ex.getType());
        assertNotNull(ex.getTitle());
        assertNotNull(ex.getDetail());
        assertNotNull(ex.getInstance());
        assertEquals(
                "401 (Invalid credentials supplied): You did not provide correct credentials.",
                ex.getMessage()
        );
    }

    @Test
    public void testListSecretNoApiKey() throws Exception {
        stubResponse(200, "{}");
        assertThrows(IllegalArgumentException.class, () -> client.listSecrets("  "));
        assertThrows(IllegalArgumentException.class, () -> client.listSecrets(null));
    }

    @Test
    public void testCreateSecretNoApiKey() throws Exception {
        stubResponse(200, "{}");
        assertThrows(IllegalArgumentException.class, () -> client.createSecret("  ", SECRET_ID));
    }

    @Test
    public void testCreateSecretSuccessful() throws Exception {
        String json = "{\n" + "  \"_links\": {\n" + "    \"self\": {\n"
                + "      \"href\": \"/accounts/abcd1234/secrets/secret-id-one\"\n" + "    }\n" + "  },\n"
                + "  \"id\": \"secret-id-one\",\n" + "  \"created_at\": \"2017-03-02T16:34:49Z\"\n" + "}";
        stubResponse(201, json);

        SecretResponse response = client.createSecret( SECRET_ID);
        TestUtils.testJsonableBaseObject(response);
        Calendar calendar = new GregorianCalendar(2017, Calendar.MARCH, 2, 16, 34, 49);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals("secret-id-one", response.getId());
        assertEquals(calendar.getTime(), Date.from(response.getCreated()));
        //assertEquals("/accounts/abcd1234/secrets/secret-id-one", response.getSelf().getHref());
    }

    @Test
    public void testCreateSecretFailed() throws Exception {
        String json =
                "{\n" + "  \"type\": \"https://developer.nexmo.com/api-errors/account/secret-management#validation\",\n"
                        + "  \"title\": \"Bad Request\",\n"
                        + "  \"detail\": \"The request failed due to validation errors\",\n"
                        + "  \"invalid_parameters\": [\n" + "    {\n" + "      \"name\": \"secret\",\n"
                        + "      \"reason\": \"Does not meet complexity requirements\"\n" + "    }\n" + "  ],\n"
                        + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        stubResponse(400, json);
        assertThrows(AccountResponseException.class, () -> client.createSecret(API_KEY, "secret"));
    }

    @Test
    public void testGetSecretSuccessful() throws Exception {
        String json = "{\n" + "  \"_links\": {\n" + "    \"self\": {\n"
                + "      \"href\": \"/accounts/abcd1234/secrets/secret-id-one\"\n" + "    }\n" + "  },\n"
                + "  \"id\": \"secret-id-one\",\n" + "  \"created_at\": \"2017-03-02T16:34:49Z\"\n" + "}";
        stubResponse(200, json);

        SecretResponse response = client.getSecret(SECRET_ID);

        Calendar calendar = new GregorianCalendar(2017, Calendar.MARCH, 2, 16, 34, 49);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals("secret-id-one", response.getId());
        assertEquals(calendar.getTime(), Date.from(response.getCreated()));
        //assertEquals("/accounts/abcd1234/secrets/secret-id-one", response.getSelf().getHref());
    }

    @Test
    public void testGetSecretMalformed() throws Exception {
        stubResponse(200, "{malformed]");
        assertThrows(VonageResponseParseException.class, () -> client.getSecret(API_KEY, SECRET_ID));
    }

    @Test
    public void testGetSecretNoSecretId() throws Exception {
        stubResponse(200, "{}");
        assertThrows(IllegalArgumentException.class, () -> client.getSecret(API_KEY, "  "));
        assertThrows(IllegalArgumentException.class, () -> client.getSecret(API_KEY, null));
    }

    @Test
    public void testGetSecretNoApiKey() throws Exception {
        stubResponse(200, "{}");
        assertThrows(IllegalArgumentException.class, () -> client.getSecret("\n", SECRET_ID));
        assertThrows(IllegalArgumentException.class, () -> client.getSecret(null, SECRET_ID));
    }

    @Test
    public void testGetSecretFailed() throws Exception {
        String json = "{\n" + "  \"type\": \"https://developer.nexmo.com/api-errors#invalid-api-key\",\n"
                + "  \"title\": \"Invalid API Key\",\n"
                + "  \"detail\": \"API key 'ABC123' does not exist, or you do not have access\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        stubResponse(404, json);
        assertThrows(AccountResponseException.class, () -> client.getSecret("apiKey", "secret-id-one"));
    }

    @Test
    public void testRevokeSecretSuccessful() throws Exception {
        stubResponse(204);
        // No assertions as an exception will be thrown if failure occurs.
        client.revokeSecret(SECRET_ID);
    }

    @Test
    public void testRevokeSecretFailed() throws Exception {
        String json = "{\n"
                + "  \"type\": \"https://developer.nexmo.com/api-errors/account/secret-management#delete-last-secret\",\n"
                + "  \"title\": \"Secret Deletion Forbidden\",\n"
                + "  \"detail\": \"Can not delete the last secret. The account must always have at least 1 secret active at any time\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        stubResponse(403, json);
        assertThrows(AccountResponseException.class, () -> client.revokeSecret(API_KEY, SECRET_ID));
    }

    @Test
    public void testUpdateIncomingSmsUrl() throws Exception {
        String json = "{\n" +
                "  \"mo-callback-url\": \"https://example.com/webhooks/inbound-sms\",\n" +
                "  \"dr-callback-url\": \"https://example.com/webhooks/delivery-receipt\",\n" +
                "  \"max-outbound-request\": 10,\n" +
                "  \"max-inbound-request\": 20,\n" +
                "  \"max-calls-per-second\": 30\n" +
                "}";
        stubResponse(200, json);
        SettingsResponse response = client.updateSmsIncomingUrl("https://example.com/webhooks/inbound-sms");
        TestUtils.testJsonableBaseObject(response);

        assertEquals("https://example.com/webhooks/inbound-sms", response.getIncomingSmsUrl());
        assertEquals("https://example.com/webhooks/delivery-receipt", response.getDeliveryReceiptUrl());
        assertEquals(Integer.valueOf(30), response.getMaxApiCallsPerSecond());
        assertEquals(Integer.valueOf(20), response.getMaxInboundMessagesPerSecond());
        assertEquals(Integer.valueOf(10), response.getMaxOutboundMessagesPerSecond());
    }

    @Test
    public void testUpdateDeliveryReceiptUrl() throws Exception {
        String json = "{\n" +
                "  \"mo-callback-url\": \"https://example.com/webhooks/inbound-sms\",\n" +
                "  \"dr-callback-url\": \"https://example.com/webhooks/delivery-receipt\",\n" +
                "  \"max-outbound-request\": 10,\n" +
                "  \"max-inbound-request\": 20,\n" +
                "  \"max-calls-per-second\": 30\n" +
                "}";
        stubResponse(200, json);
        SettingsResponse response = client.updateDeliveryReceiptUrl("https://example.com/webhooks/delivery-receipt");

        assertEquals("https://example.com/webhooks/inbound-sms", response.getIncomingSmsUrl());
        assertEquals("https://example.com/webhooks/delivery-receipt", response.getDeliveryReceiptUrl());
        assertEquals(Integer.valueOf(30), response.getMaxApiCallsPerSecond());
        assertEquals(Integer.valueOf(20), response.getMaxInboundMessagesPerSecond());
        assertEquals(Integer.valueOf(10), response.getMaxOutboundMessagesPerSecond());
    }

    @Test
    public void testUpdatingAccountSettings() throws Exception {
        String json = "{\n" +
                "  \"mo-callback-url\": \"https://example.com/webhooks/inbound-sms\",\n" +
                "  \"dr-callback-url\": \"https://example.com/webhooks/delivery-receipt\",\n" +
                "  \"max-outbound-request\": 10,\n" +
                "  \"max-inbound-request\": 20,\n" +
                "  \"max-calls-per-second\": 30\n" +
                "}";

        stubResponse(200, json);
        SettingsResponse response = client.updateSettings(new SettingsRequest("https://example.com/webhooks/inbound-sms", "https://example.com/webhooks/delivery-receipt"));

        assertEquals("https://example.com/webhooks/inbound-sms", response.getIncomingSmsUrl());
        assertEquals("https://example.com/webhooks/delivery-receipt", response.getDeliveryReceiptUrl());
        assertEquals(Integer.valueOf(30), response.getMaxApiCallsPerSecond());
        assertEquals(Integer.valueOf(20), response.getMaxInboundMessagesPerSecond());
        assertEquals(Integer.valueOf(10), response.getMaxOutboundMessagesPerSecond());
    }

    // ENDPOINT TESTS

    // BALANCE ENDPOINTS

    @Test
    public void testBalanceEndpoint() throws Exception {
        new AccountEndpointTestSpec<Void, BalanceResponse>() {

            @Override
            protected RestEndpoint<Void, BalanceResponse> endpoint() {
                return client.balance;
            }

            @Override
            protected String expectedEndpointUri(Void request) {
                return "/account/get-balance";
            }

            @Override
            protected Void sampleRequest() {
                return null;
            }
        }
        .runTests();
    }

    @Test
    public void testTopUpEndpoint() throws Exception {
        new AccountEndpointTestSpec<TopUpRequest, Void>() {

            @Override
            protected RestEndpoint<TopUpRequest, Void> endpoint() {
                return client.topUp;
            }

            @Override
            protected String expectedEndpointUri(TopUpRequest request) {
                return "/account/top-up";
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected String expectedContentTypeHeader(TopUpRequest request) {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected TopUpRequest sampleRequest() {
                return new TopUpRequest("8ef2447e69604f642ae59363aa5f781b");
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                return Collections.singletonMap("trx", sampleRequest().trx);
            }
        }
        .runTests();
    }

    // CONFIGURATION ENDPOINT

    @Test
    public void testSettingsEndpoint() throws Exception {
        new AccountEndpointTestSpec<SettingsRequest, SettingsResponse>() {

            @Override
            protected RestEndpoint<SettingsRequest, SettingsResponse> endpoint() {
                return client.settings;
            }

            @Override
            protected String expectedEndpointUri(SettingsRequest request) {
                return "/account/settings";
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected String expectedContentTypeHeader(SettingsRequest request) {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected SettingsRequest sampleRequest() {
                return new SettingsRequest(
                        "https://example.com/inbound-sms",
                        "https://example.com/delivery-receipt"
                );
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                Map<String, String> params = new LinkedHashMap<>();
                params.put("moCallBackUrl", "https://example.com/inbound-sms");
                params.put("drCallBackUrl", "https://example.com/delivery-receipt");
                return params;
            }
        }
        .runTests();
    }

    // SECRET ENDPOINTS

    @Test
    public void testCreateSecretEndpoint() throws Exception {
        new AccountSecretsEndpointTestSpec<CreateSecretRequest, SecretResponse>() {

            @Override
            protected RestEndpoint<CreateSecretRequest, SecretResponse> endpoint() {
                return client.createSecret;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected CreateSecretRequest sampleRequest() {
                return new CreateSecretRequest(API_KEY, SECRET_ID);
            }
        }
        .runTests();
    }

    @Test
    public void testGetSecretEndpoint() throws Exception {
        new AccountSecretsEndpointTestSpec<SecretRequest, SecretResponse>() {

            @Override
            protected RestEndpoint<SecretRequest, SecretResponse> endpoint() {
                return client.getSecret;
            }

            @Override
            protected SecretRequest sampleRequest() {
                return new SecretRequest(API_KEY, SECRET_ID);
            }
        }
        .runTests();
    }

    @Test
    public void testListSecretsEndpoint() throws Exception {
        new AccountSecretsEndpointTestSpec<String, ListSecretsResponse>() {

            @Override
            protected RestEndpoint<String, ListSecretsResponse> endpoint() {
                return client.listSecrets;
            }

            @Override
            protected String sampleRequest() {
                return API_KEY;
            }
        }
        .runTests();
    }

    @Test
    public void testRevokeSecretEndpoint() throws Exception {
        new AccountSecretsEndpointTestSpec<SecretRequest, Void>() {

            @Override
            protected RestEndpoint<SecretRequest, Void> endpoint() {
                return client.revokeSecret;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.DELETE;
            }

            @Override
            protected SecretRequest sampleRequest() {
                return new SecretRequest(API_KEY, SECRET_ID);
            }
        }
        .runTests();
    }
}
