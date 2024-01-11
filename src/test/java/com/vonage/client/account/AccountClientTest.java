/*
 *   Copyright 2024 Vonage
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

import com.vonage.client.ClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.util.*;

public class AccountClientTest extends ClientTest<AccountClient> {
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
        BalanceResponse fromJson = BalanceResponse.fromJson(json);
        assertEquals(response, fromJson);
        assertEquals(response.hashCode(), fromJson.hashCode());
        assertEquals("BalanceResponse "+json.replace(" ", ""), response.toString());
    }

    @Test
    public void testGetFullPricingEmptyCountries() throws Exception {
        stubResponse(200, "{\"count\":0,\"countries\":[]}");
        List<PricingResponse> response = client.listPriceAllCountries(ServiceType.VOICE);
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    public void testGetFullPricingNoCountries() throws Exception {
        stubResponse(200, "{}");
        assertNull(client.listPriceAllCountries(ServiceType.VOICE));
    }

    @Test
    public void testGetFullPricingNoService() throws Exception {
        stubResponse(200, "{}");
        assertThrows(NullPointerException.class, () -> client.listPriceAllCountries(null));
    }

    @Test
    public void testGetFullPricing() throws Exception {
        String json = "{\n" +
                "   \"count\": \"243\",\n" +
                "   \"countries\": [{},\n" +
                "      {\n" +
                "         \"countryName\": \"Canada\",\n" +
                "         \"countryDisplayName\": \"Canada\",\n" +
                "         \"currency\": \"EUR\",\n" +
                "         \"defaultPrice\": \"0.00620000\",\n" +
                "         \"dialingPrefix\": \"1\",\n" +
                "         \"networks\": [\n" +
                "            {\n" +
                "               \"type\": \"mobile\",\n" +
                "               \"price\": \"0.00590000\",\n" +
                "               \"currency\": \"EUR\",\n" +
                "               \"mcc\": \"302\",\n" +
                "               \"mnc\": \"530\",\n" +
                "               \"networkCode\": \"302530\",\n" +
                "               \"networkName\": \"Keewaytinook Okimakanak\"\n" +
                "            },{}\n" +
                "         ]\n" +
                "      },{}\n" +
                "   ]\n" +
                "}";
        stubResponse(200, json);
        List<PricingResponse> countries = client.listPriceAllCountries(ServiceType.SMS);
        assertNotNull(countries);
        assertEquals(3, countries.size());
        PricingResponse canada = countries.get(1);
        assertNotNull(canada);
        assertEquals("Canada", canada.getCountry().getName());
        assertEquals("Canada", canada.getCountry().getDisplayName());
        assertEquals("EUR", canada.getCurrency());
        assertEquals(0.0062, canada.getDefaultPrice().doubleValue(), 0.000000001);
        assertEquals("1", canada.getDialingPrefix());
        List<Network> canadaNetworks = canada.getNetworks();
        assertNotNull(canadaNetworks);
        assertEquals(2, canadaNetworks.size());
        Network canadaNetwork = canadaNetworks.get(0);
        assertNotNull(canadaNetwork);
        assertEquals(Network.Type.MOBILE, canadaNetwork.getType());
        assertEquals(0.0059, canadaNetwork.getPrice().doubleValue(), 0.000000001);
        assertEquals("302", canadaNetwork.getMcc());
        assertEquals("530", canadaNetwork.getMnc());
        assertEquals("302530", canadaNetwork.getCode());
        assertEquals("Keewaytinook Okimakanak", canadaNetwork.getName());
    }

    @Test
    public void testGetSmsPrice() throws Exception {
        String json = "{\n" + "  \"dialingPrefix\": \"1\",\n" + "  \"defaultPrice\": \"0.00570000\",\n"
                + "  \"currency\": \"EUR\",\n" + "  \"countryDisplayName\": \"United States of America\",\n"
                + "  \"countryCode\": \"US\",\n" + "  \"countryName\": \"United States\",\n" + "  \"networks\": [\n"
                + "    {\n" + "      \"type\": \"mobile\",\n" + "      \"price\": \"0.00570000\",\n"
                + "      \"currency\": \"EUR\",\n" + "      \"mcc\": \"987\",\n" + "      \"mnc\": \"123\",\n"
                + "      \"networkCode\": \"123456\",\n" + "      \"networkName\": \"Test Mobile\"\n" + "    },\n"
                + "    {\n" + "      \"type\": \"landline\",\n" + "      \"price\": \"0.00330000\",\n"
                + "      \"currency\": \"EUR\",\n" + "      \"mcc\": \"123\",\n" + "      \"mnc\": \"456\",\n"
                + "      \"networkCode\": \"networkcode\",\n" + "      \"networkName\": \"Test Landline\"\n"
                + "    }  \n" + "  ]\n" + "}\n";
        stubResponse(200, json);

        PricingResponse response = client.getSmsPrice("US");
        assertEquals("1", response.getDialingPrefix());
        assertEquals(new BigDecimal("0.00570000"), response.getDefaultPrice());
        assertEquals("EUR", response.getCurrency());
        assertEquals("United States of America", response.getCountry().getDisplayName());
        assertEquals("US", response.getCountry().getCode());
        assertEquals("United States", response.getCountry().getName());

        assertEquals(2, response.getNetworks().size());
        Network first = response.getNetworks().get(0);
        assertEquals(Network.Type.MOBILE, first.getType());
        assertEquals("mobile", first.getType().toString());
        assertEquals(new BigDecimal("0.00570000"), first.getPrice());
        assertEquals("EUR", first.getCurrency());
        assertEquals("987", first.getMcc());
        assertEquals("123", first.getMnc());
        assertEquals("123456", first.getCode());
        assertEquals("Test Mobile", first.getName());

        Network second = response.getNetworks().get(1);
        assertEquals(Network.Type.fromString("landline"), second.getType());
        assertEquals(new BigDecimal("0.00330000"), second.getPrice());
        assertEquals("EUR", second.getCurrency());
        assertEquals("123", second.getMcc());
        assertEquals("456", second.getMnc());
        assertEquals("networkcode", second.getCode());
        assertEquals("Test Landline", second.getName());

        assertEquals(Network.Type.UNKNOWN, Network.Type.fromString(";invalid>"));
    }

    @Test
    public void testGetVoicePrice() throws Exception {
        String json = "{\n" + "  \"dialingPrefix\": \"1\",\n" + "  \"defaultPrice\": \"0.00570000\",\n"
                + "  \"currency\": \"EUR\",\n" + "  \"countryDisplayName\": \"United States of America\",\n"
                + "  \"countryCode\": \"US\",\n" + "  \"countryName\": \"United States\",\n" + "  \"networks\": [\n"
                + "    {\n" + "      \"type\": \"mobile\",\n" + "      \"price\": \"0.00570000\",\n"
                + "      \"currency\": \"EUR\",\n" + "      \"mcc\": \"987\",\n" + "      \"mnc\": \"123\",\n"
                + "      \"networkCode\": \"123456\",\n" + "      \"networkName\": \"Test Mobile\"\n" + "    },\n"
                + "    {\n" + "      \"type\": \"landline\",\n" + "      \"price\": \"0.00330000\",\n"
                + "      \"currency\": \"EUR\",\n" + "      \"mcc\": \"123\",\n" + "      \"mnc\": \"456\",\n"
                + "      \"networkCode\": \"networkcode\",\n" + "      \"networkName\": \"Test Landline\"\n"
                + "    }  \n" + "  ]\n" + "}\n";
        stubResponse(200, json);
        PricingResponse response = client.getVoicePrice("US");
        assertEquals("1", response.getDialingPrefix());
        assertEquals(new BigDecimal("0.00570000"), response.getDefaultPrice());
        assertEquals("EUR", response.getCurrency());
        assertEquals("United States of America", response.getCountry().getDisplayName());
        assertEquals("US", response.getCountry().getCode());
        assertEquals("United States", response.getCountry().getName());

        assertEquals(2, response.getNetworks().size());
        Network first = response.getNetworks().get(0);
        assertEquals(Network.Type.MOBILE, first.getType());
        assertEquals(new BigDecimal("0.00570000"), first.getPrice());
        assertEquals("EUR", first.getCurrency());
        assertEquals("987", first.getMcc());
        assertEquals("123", first.getMnc());
        assertEquals("123456", first.getCode());
        assertEquals("Test Mobile", first.getName());

        Network second = response.getNetworks().get(1);
        assertEquals(Network.Type.LANDLINE, second.getType());
        assertEquals(new BigDecimal("0.00330000"), second.getPrice());
        assertEquals("EUR", second.getCurrency());
        assertEquals("123", second.getMcc());
        assertEquals("456", second.getMnc());
        assertEquals("networkcode", second.getCode());
        assertEquals("Test Landline", second.getName());
    }

    @Test
    public void testGetPrefixVoicePrice() throws Exception {
        String json = "{\n" + "    \"count\": 2,\n" + "    \"countries\": [\n" + "        {\n"
                + "            \"dialingPrefix\": \"1\",\n" + "            \"defaultPrice\": \"0.01270000\",\n"
                + "            \"currency\": \"EUR\",\n" + "            \"countryDisplayName\": \"Canada\",\n"
                + "            \"countryCode\": \"CA\",\n" + "            \"countryName\": \"Canada\",\n"
                + "            \"networks\": [\n" + "                {\n"
                + "                    \"type\": \"mobile\",\n" + "                    \"price\": \"0.01280000\",\n"
                + "                    \"currency\": \"EUR\",\n" + "                    \"aliases\": [\n"
                + "                        \"302998\"\n" + "                    ],\n"
                + "                    \"mcc\": \"302\",\n" + "                    \"mnc\": \"702\",\n"
                + "                    \"networkCode\": \"302702\",\n"
                + "                    \"networkName\": \"BELL ALIANT REGIONAL Communications LP\"\n"
                + "                },\n" + "                {\n" + "                    \"type\": \"landline\",\n"
                + "                    \"price\": \"0.01000000\",\n" + "                    \"currency\": \"EUR\",\n"
                + "                    \"networkCode\": \"CA-FIXED\",\n"
                + "                    \"networkName\": \"Canada Landline\"\n" + "                }\n"
                + "            ]\n" + "        },\n" + "        {\n" + "            \"dialingPrefix\": \"1\",\n"
                + "            \"currency\": \"EUR\",\n"
                + "            \"countryDisplayName\": \"United States Minor Outlying Islands\",\n"
                + "            \"countryCode\": \"UM\",\n"
                + "            \"countryName\": \"United States Minor Outlying Islands\"\n" + "        }\n" + "    ]\n"
                + "}";
        stubResponse(200, json);
        PrefixPricingResponse response = client.getPrefixPrice(ServiceType.VOICE, "1");
        assertNotNull(response);
        PrefixPricingResponse fromJson = PrefixPricingResponse.fromJson(json);
        assertEquals(response, fromJson);
        assertEquals(response.hashCode(), fromJson.hashCode());
        assertEquals(response.toString(), "PrefixPricingResponse "+fromJson.toJson());

        assertEquals(2, response.getCount());
        assertEquals(2, response.getCountries().size());

        PricingResponse firstResponse = response.getCountries().get(0);
        assertEquals("1", firstResponse.getDialingPrefix());
        assertEquals(new BigDecimal("0.01270000"), firstResponse.getDefaultPrice());
        assertEquals("EUR", firstResponse.getCurrency());
        assertEquals("Canada", firstResponse.getCountry().getDisplayName());
        assertEquals("CA", firstResponse.getCountry().getCode());
        assertEquals("Canada", firstResponse.getCountry().getName());

        assertEquals(2, firstResponse.getNetworks().size());
        Network firstResponseFirstNetwork = firstResponse.getNetworks().get(0);
        assertEquals(Network.Type.MOBILE, firstResponseFirstNetwork.getType());
        assertEquals(new BigDecimal("0.01280000"), firstResponseFirstNetwork.getPrice());
        assertEquals("EUR", firstResponseFirstNetwork.getCurrency());
        assertEquals("302", firstResponseFirstNetwork.getMcc());
        assertEquals("702", firstResponseFirstNetwork.getMnc());
        assertEquals("302702", firstResponseFirstNetwork.getCode());
        assertEquals("BELL ALIANT REGIONAL Communications LP", firstResponseFirstNetwork.getName());

        Network firstResponseSecondNetwork = firstResponse.getNetworks().get(1);
        assertEquals(Network.Type.LANDLINE, firstResponseSecondNetwork.getType());
        assertEquals(new BigDecimal("0.01000000"), firstResponseSecondNetwork.getPrice());
        assertEquals("EUR", firstResponseSecondNetwork.getCurrency());
        assertEquals("CA-FIXED", firstResponseSecondNetwork.getCode());
        assertEquals("Canada Landline", firstResponseSecondNetwork.getName());

        PricingResponse secondResponse = response.getCountries().get(1);
        assertEquals("1", secondResponse.getDialingPrefix());
        assertEquals("EUR", secondResponse.getCurrency());
        assertEquals("United States Minor Outlying Islands", secondResponse.getCountry().getDisplayName());
        assertEquals("UM", secondResponse.getCountry().getCode());
        assertEquals("United States Minor Outlying Islands", secondResponse.getCountry().getName());
    }

    @Test
    public void testGetPrefixSmsPrice() throws Exception {
        String json = "{\n" + "    \"count\": 2,\n" + "    \"countries\": [\n" + "        {\n"
                + "            \"dialingPrefix\": \"1\",\n" + "            \"defaultPrice\": \"0.00570000\",\n"
                + "            \"currency\": \"EUR\",\n" + "            \"countryDisplayName\": \"Canada\",\n"
                + "            \"countryCode\": \"CA\",\n" + "            \"countryName\": \"Canada\",\n"
                + "            \"networks\": [\n" + "                {\n"
                + "                    \"type\": \"mobile\",\n" + "                    \"price\": \"0.00570000\",\n"
                + "                    \"currency\": \"EUR\",\n" + "                    \"aliases\": [\n"
                + "                        \"302660\"\n" + "                    ],\n"
                + "                    \"mcc\": \"302\",\n" + "                    \"mnc\": \"655\",\n"
                + "                    \"networkCode\": \"302655\",\n"
                + "                    \"networkName\": \"MTS Communications Inc.\"\n" + "                }\n"
                + "            ]\n" + "        },\n" + "        {\n" + "            \"dialingPrefix\": \"1\",\n"
                + "            \"currency\": \"EUR\",\n"
                + "            \"countryDisplayName\": \"United States Minor Outlying Islands\",\n"
                + "            \"countryCode\": \"UM\",\n"
                + "            \"countryName\": \"United States Minor Outlying Islands\"\n" + "        }\n" + "    ]\n"
                + "}";
        stubResponse(200, json);
        PrefixPricingResponse response = client.getPrefixPrice(ServiceType.SMS, "1");
        assertEquals(2, response.getCount());
        assertEquals(2, response.getCountries().size());

        PricingResponse firstResponse = response.getCountries().get(0);
        assertEquals("1", firstResponse.getDialingPrefix());
        assertEquals(new BigDecimal("0.00570000"), firstResponse.getDefaultPrice());
        assertEquals("EUR", firstResponse.getCurrency());
        assertEquals("Canada", firstResponse.getCountry().getDisplayName());
        assertEquals("CA", firstResponse.getCountry().getCode());
        assertEquals("Canada", firstResponse.getCountry().getName());

        assertEquals(1, firstResponse.getNetworks().size());
        Network network = firstResponse.getNetworks().get(0);
        assertEquals(Network.Type.MOBILE, network.getType());
        assertEquals(new BigDecimal("0.00570000"), network.getPrice());
        assertEquals("EUR", network.getCurrency());
        assertEquals("302", network.getMcc());
        assertEquals("655", network.getMnc());
        assertEquals("302655", network.getCode());
        assertEquals("MTS Communications Inc.", network.getName());

        PricingResponse secondResponse = response.getCountries().get(1);
        assertEquals("1", secondResponse.getDialingPrefix());
        assertEquals("EUR", secondResponse.getCurrency());
        assertEquals("United States Minor Outlying Islands", secondResponse.getCountry().getDisplayName());
        assertEquals("UM", secondResponse.getCountry().getCode());
        assertEquals("United States Minor Outlying Islands", secondResponse.getCountry().getName());
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
        assertThrows(VonageResponseParseException.class, () -> client.listSecrets(apiKey));
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
        assertThrows(AccountResponseException.class, () -> client.createSecret(apiKey, "secret"));
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
        assertThrows(VonageResponseParseException.class, () -> client.getSecret(apiKey, SECRET_ID));
    }

    @Test
    public void testGetSecretNoSecretId() throws Exception {
        stubResponse(200, "{}");
        assertThrows(IllegalArgumentException.class, () -> client.getSecret(apiKey, "  "));
    }

    @Test
    public void testGetSecretNoApiKey() throws Exception {
        stubResponse(200, "{}");
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
        assertThrows(AccountResponseException.class, () -> client.revokeSecret(apiKey, SECRET_ID));
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
                return Collections.singletonMap("trx", sampleRequest().getTrx());
            }
        }
        .runTests();
    }

    // PRICING ENDPOINTS

    @Test
    public void testPricingEndpoint() throws Exception {
        new AccountEndpointTestSpec<PricingRequest, PricingResponse>() {

            @Override
            protected RestEndpoint<PricingRequest, PricingResponse> endpoint() {
                return client.pricing;
            }

            @Override
            protected String expectedEndpointUri(PricingRequest request) {
                return "/account/get-pricing/outbound/" + request.getServiceType();
            }

            @Override
            protected PricingRequest sampleRequest() {
                return new PricingRequest("de", ServiceType.SMS);
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                PricingRequest request = sampleRequest();
                assertEquals("de", request.getCountryCode());
                assertEquals("sms", request.getServiceType());

                Map<String, String> params = new LinkedHashMap<>();
                params.put("country", request.getCountryCode());
                return params;
            }
        }
        .runTests();
    }

    @Test
    public void testFullPricingEndpoint() throws Exception {
        new AccountEndpointTestSpec<FullPricingRequest, FullPricingResponse>() {

            @Override
            protected RestEndpoint<FullPricingRequest, FullPricingResponse> endpoint() {
                return client.fullPricing;
            }

            @Override
            protected String expectedEndpointUri(FullPricingRequest request) {
                return "/account/get-full-pricing/outbound/" + request.getServiceType();
            }

            @Override
            protected FullPricingRequest sampleRequest() {
                FullPricingRequest request = new FullPricingRequest(ServiceType.SMS_TRANSIT);
                assertEquals("sms-transit", request.getServiceType().toString());
                return request;
            }
        }
        .runTests();
    }

    @Test
    public void testPrefixPricingEndpoint() throws Exception {
        new AccountEndpointTestSpec<PrefixPricingRequest, PrefixPricingResponse>() {

            @Override
            protected RestEndpoint<PrefixPricingRequest, PrefixPricingResponse> endpoint() {
                return client.prefixPricing;
            }

            @Override
            protected String expectedEndpointUri(PrefixPricingRequest request) {
                return "/account/get-prefix-pricing/outbound/" + request.getServiceType();
            }

            @Override
            protected PrefixPricingRequest sampleRequest() {
                return new PrefixPricingRequest(ServiceType.VOICE, "44");
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                PrefixPricingRequest request = sampleRequest();
                assertEquals("voice", request.getServiceType().toString());
                assertEquals("44", request.getPrefix());

                Map<String, String> params = new LinkedHashMap<>();
                params.put("prefix", request.getPrefix());
                return params;
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
                SettingsRequest request = sampleRequest();
                String smsUrl = request.getIncomingSmsUrl();
                String drUrl = request.getDeliveryReceiptUrl();
                assertEquals("https://example.com/inbound-sms", smsUrl);
                assertEquals("https://example.com/delivery-receipt", drUrl);

                Map<String, String> params = new LinkedHashMap<>();
                params.put("moCallBackUrl", smsUrl);
                params.put("drCallBackUrl", drUrl);
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
                return new CreateSecretRequest(apiKey, SECRET_ID);
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
                return new SecretRequest(apiKey, SECRET_ID);
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
                return apiKey;
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
                return new SecretRequest(apiKey, SECRET_ID);
            }
        }
        .runTests();
    }
}
