/*
 *   Copyright 2020 Vonage
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
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClientException;
import com.vonage.client.auth.TokenAuthMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AccountClientTest extends ClientTest<AccountClient> {
    private BalanceResponse sentinel;

    @Before
    public void setUp() throws Exception {
        wrapper = new HttpWrapper(new TokenAuthMethod("not-an-api-key", "secret"));
        client = new AccountClient(wrapper);
        client.balance = mock(BalanceEndpoint.class);
        sentinel = new BalanceResponse(1.1, true);
        when(client.balance.execute()).thenReturn(sentinel);
    }

    @Test
    public void testGetBalance() throws Exception {
        BalanceResponse response = client.getBalance();
        verify(client.balance).execute();
        Assert.assertEquals(sentinel, response);
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
        wrapper.setHttpClient(this.stubHttpClient(200, json));
        PricingResponse response = client.getSmsPrice("US");
        Assert.assertEquals("1", response.getDialingPrefix());
        Assert.assertEquals(new BigDecimal("0.00570000"), response.getDefaultPrice());
        Assert.assertEquals("EUR", response.getCurrency());
        Assert.assertEquals("United States of America", response.getCountry().getDisplayName());
        Assert.assertEquals("US", response.getCountry().getCode());
        Assert.assertEquals("United States", response.getCountry().getName());

        Assert.assertEquals(2, response.getNetworks().size());
        Network first = response.getNetworks().get(0);
        Assert.assertEquals(Network.Type.MOBILE, first.getType());
        Assert.assertEquals(new BigDecimal("0.00570000"), first.getPrice());
        Assert.assertEquals("EUR", first.getCurrency());
        Assert.assertEquals("987", first.getMcc());
        Assert.assertEquals("123", first.getMnc());
        Assert.assertEquals("123456", first.getCode());
        Assert.assertEquals("Test Mobile", first.getName());


        Network second = response.getNetworks().get(1);
        Assert.assertEquals(Network.Type.LANDLINE, second.getType());
        Assert.assertEquals(new BigDecimal("0.00330000"), second.getPrice());
        Assert.assertEquals("EUR", second.getCurrency());
        Assert.assertEquals("123", second.getMcc());
        Assert.assertEquals("456", second.getMnc());
        Assert.assertEquals("networkcode", second.getCode());
        Assert.assertEquals("Test Landline", second.getName());
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
        wrapper.setHttpClient(this.stubHttpClient(200, json));
        PricingResponse response = client.getVoicePrice("US");
        Assert.assertEquals("1", response.getDialingPrefix());
        Assert.assertEquals(new BigDecimal("0.00570000"), response.getDefaultPrice());
        Assert.assertEquals("EUR", response.getCurrency());
        Assert.assertEquals("United States of America", response.getCountry().getDisplayName());
        Assert.assertEquals("US", response.getCountry().getCode());
        Assert.assertEquals("United States", response.getCountry().getName());

        Assert.assertEquals(2, response.getNetworks().size());
        Network first = response.getNetworks().get(0);
        Assert.assertEquals(Network.Type.MOBILE, first.getType());
        Assert.assertEquals(new BigDecimal("0.00570000"), first.getPrice());
        Assert.assertEquals("EUR", first.getCurrency());
        Assert.assertEquals("987", first.getMcc());
        Assert.assertEquals("123", first.getMnc());
        Assert.assertEquals("123456", first.getCode());
        Assert.assertEquals("Test Mobile", first.getName());


        Network second = response.getNetworks().get(1);
        Assert.assertEquals(Network.Type.LANDLINE, second.getType());
        Assert.assertEquals(new BigDecimal("0.00330000"), second.getPrice());
        Assert.assertEquals("EUR", second.getCurrency());
        Assert.assertEquals("123", second.getMcc());
        Assert.assertEquals("456", second.getMnc());
        Assert.assertEquals("networkcode", second.getCode());
        Assert.assertEquals("Test Landline", second.getName());
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
        wrapper.setHttpClient(this.stubHttpClient(200, json));
        PrefixPricingResponse response = client.getPrefixPrice(ServiceType.VOICE, "1");
        Assert.assertEquals(2, response.getCount());
        Assert.assertEquals(2, response.getCountries().size());

        PricingResponse firstResponse = response.getCountries().get(0);
        Assert.assertEquals("1", firstResponse.getDialingPrefix());
        Assert.assertEquals(new BigDecimal("0.01270000"), firstResponse.getDefaultPrice());
        Assert.assertEquals("EUR", firstResponse.getCurrency());
        Assert.assertEquals("Canada", firstResponse.getCountry().getDisplayName());
        Assert.assertEquals("CA", firstResponse.getCountry().getCode());
        Assert.assertEquals("Canada", firstResponse.getCountry().getName());

        Assert.assertEquals(2, firstResponse.getNetworks().size());
        Network firstResponseFirstNetwork = firstResponse.getNetworks().get(0);
        Assert.assertEquals(Network.Type.MOBILE, firstResponseFirstNetwork.getType());
        Assert.assertEquals(new BigDecimal("0.01280000"), firstResponseFirstNetwork.getPrice());
        Assert.assertEquals("EUR", firstResponseFirstNetwork.getCurrency());
        Assert.assertEquals("302", firstResponseFirstNetwork.getMcc());
        Assert.assertEquals("702", firstResponseFirstNetwork.getMnc());
        Assert.assertEquals("302702", firstResponseFirstNetwork.getCode());
        Assert.assertEquals("BELL ALIANT REGIONAL Communications LP", firstResponseFirstNetwork.getName());


        Network firstResponseSecondNetwork = firstResponse.getNetworks().get(1);
        Assert.assertEquals(Network.Type.LANDLINE, firstResponseSecondNetwork.getType());
        Assert.assertEquals(new BigDecimal("0.01000000"), firstResponseSecondNetwork.getPrice());
        Assert.assertEquals("EUR", firstResponseSecondNetwork.getCurrency());
        Assert.assertEquals("CA-FIXED", firstResponseSecondNetwork.getCode());
        Assert.assertEquals("Canada Landline", firstResponseSecondNetwork.getName());

        PricingResponse secondResponse = response.getCountries().get(1);
        Assert.assertEquals("1", secondResponse.getDialingPrefix());
        Assert.assertEquals("EUR", secondResponse.getCurrency());
        Assert.assertEquals("United States Minor Outlying Islands", secondResponse.getCountry().getDisplayName());
        Assert.assertEquals("UM", secondResponse.getCountry().getCode());
        Assert.assertEquals("United States Minor Outlying Islands", secondResponse.getCountry().getName());
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
        wrapper.setHttpClient(this.stubHttpClient(200, json));
        PrefixPricingResponse response = client.getPrefixPrice(ServiceType.SMS, "1");
        Assert.assertEquals(2, response.getCount());
        Assert.assertEquals(2, response.getCountries().size());

        PricingResponse firstResponse = response.getCountries().get(0);
        Assert.assertEquals("1", firstResponse.getDialingPrefix());
        Assert.assertEquals(new BigDecimal("0.00570000"), firstResponse.getDefaultPrice());
        Assert.assertEquals("EUR", firstResponse.getCurrency());
        Assert.assertEquals("Canada", firstResponse.getCountry().getDisplayName());
        Assert.assertEquals("CA", firstResponse.getCountry().getCode());
        Assert.assertEquals("Canada", firstResponse.getCountry().getName());

        Assert.assertEquals(1, firstResponse.getNetworks().size());
        Network network = firstResponse.getNetworks().get(0);
        Assert.assertEquals(Network.Type.MOBILE, network.getType());
        Assert.assertEquals(new BigDecimal("0.00570000"), network.getPrice());
        Assert.assertEquals("EUR", network.getCurrency());
        Assert.assertEquals("302", network.getMcc());
        Assert.assertEquals("655", network.getMnc());
        Assert.assertEquals("302655", network.getCode());
        Assert.assertEquals("MTS Communications Inc.", network.getName());

        PricingResponse secondResponse = response.getCountries().get(1);
        Assert.assertEquals("1", secondResponse.getDialingPrefix());
        Assert.assertEquals("EUR", secondResponse.getCurrency());
        Assert.assertEquals("United States Minor Outlying Islands", secondResponse.getCountry().getDisplayName());
        Assert.assertEquals("UM", secondResponse.getCountry().getCode());
        Assert.assertEquals("United States Minor Outlying Islands", secondResponse.getCountry().getName());
    }

    @Test
    public void testTopUpSuccessful() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(200, ""));
        // No assertions as an exception will be thrown if failure occurs.
        client.topUp("ABC123");
    }

    @Test(expected = VonageClientException.class)
    public void testTopUpFailedAuth() throws Exception {
        String json = "{\"error-code\":\"401\",\"error-code-label\":\"authentication failed\"}";
        wrapper.setHttpClient(this.stubHttpClient(401, json));
        client.topUp("ABC123");
    }

    @Test(expected = VonageClientException.class)
    public void testTopUpFailed() throws Exception {
        String json = "{\"error-code\":\"420\",\"error-code-label\":\"topup failed\"}";
        wrapper.setHttpClient(this.stubHttpClient(401, json));
        client.topUp("ABC123");
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
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        ListSecretsResponse response = client.listSecrets("abcd1234");
        SecretResponse[] responses = response.getSecrets().toArray(new SecretResponse[0]);

        Calendar calendar = new GregorianCalendar(2017, Calendar.MARCH, 2, 16, 34, 49);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals("secret-id-one", responses[0].getId());
        assertEquals(calendar.getTime(), responses[0].getCreated());
        assertEquals("/accounts/abcd1234/secrets/secret-id-one", responses[0].getSelf().getHref());

        calendar.set(2016, Calendar.JANUARY, 20, 16, 34, 49);
        assertEquals("secret-id-two", responses[1].getId());
        assertEquals(calendar.getTime(), responses[1].getCreated());
        assertEquals("/accounts/abcd1234/secrets/secret-id-two", responses[1].getSelf().getHref());
    }

    @Test(expected = VonageClientException.class)
    public void testListSecretFailedAuth() throws Exception {
        String json = "{\n" + "  \"type\": \"https://developer.nexmo.com/api-errors#unauthorized\",\n"
                + "  \"title\": \"Invalid credentials supplied\",\n"
                + "  \"detail\": \"You did not provide correct credentials.\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(401, json));
        client.listSecrets("ABC123");
    }

    @Test(expected = VonageClientException.class)
    public void testListSecretNotFound() throws Exception {
        String json = "{\n" + "  \"type\": \"https://developer.nexmo.com/api-errors#invalid-api-key\",\n"
                + "  \"title\": \"Invalid API Key\",\n"
                + "  \"detail\": \"API key 'ABC123' does not exist, or you do not have access\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(404, json));
        client.listSecrets("ABC123");
    }

    @Test
    public void testCreateSecretSuccessful() throws Exception {
        String json = "{\n" + "  \"_links\": {\n" + "    \"self\": {\n"
                + "      \"href\": \"/accounts/abcd1234/secrets/secret-id-one\"\n" + "    }\n" + "  },\n"
                + "  \"id\": \"secret-id-one\",\n" + "  \"created_at\": \"2017-03-02T16:34:49Z\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(201, json));

        SecretResponse response = client.createSecret("apiKey", "secret");

        Calendar calendar = new GregorianCalendar(2017, Calendar.MARCH, 2, 16, 34, 49);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals("secret-id-one", response.getId());
        assertEquals(calendar.getTime(), response.getCreated());
        assertEquals("/accounts/abcd1234/secrets/secret-id-one", response.getSelf().getHref());
    }

    @Test(expected = VonageClientException.class)
    public void testCreateSecretBadRequest() throws Exception {
        String json =
                "{\n" + "  \"type\": \"https://developer.nexmo.com/api-errors/account/secret-management#validation\",\n"
                        + "  \"title\": \"Bad Request\",\n"
                        + "  \"detail\": \"The request failed due to validation errors\",\n"
                        + "  \"invalid_parameters\": [\n" + "    {\n" + "      \"name\": \"secret\",\n"
                        + "      \"reason\": \"Does not meet complexity requirements\"\n" + "    }\n" + "  ],\n"
                        + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(400, json));
        client.createSecret("key", "secret");
    }

    @Test(expected = VonageClientException.class)
    public void testCreateSecretFailedAuth() throws Exception {
        String json = "{\n" + "  \"type\": \"https://developer.nexmo.com/api-errors#unauthorized\",\n"
                + "  \"title\": \"Invalid credentials supplied\",\n"
                + "  \"detail\": \"You did not provide correct credentials.\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(401, json));
        client.createSecret("key", "secret");
    }

    @Test(expected = VonageClientException.class)
    public void testCreateSecretMaxSecrets() throws Exception {
        String json = "{\n"
                + "  \"type\": \"https://developer.nexmo.com/api-errors/account/secret-management#maximum-secrets-allowed\",\n"
                + "  \"title\": \"Maxmimum number of secrets already met\",\n"
                + "  \"detail\": \"This account has reached maximum number of '2' allowed secrets\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(403, json));
        client.createSecret("key", "secret");
    }

    @Test(expected = VonageClientException.class)
    public void testCreateSecretAccountNotFound() throws Exception {
        String json = "{\n" + "  \"type\": \"https://developer.nexmo.com/api-errors#invalid-api-key\",\n"
                + "  \"title\": \"Invalid API Key\",\n"
                + "  \"detail\": \"API key 'ABC123' does not exist, or you do not have access\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(404, json));
        client.createSecret("key", "secret");
    }

    @Test
    public void testGetSecretSuccessful() throws Exception {
        String json = "{\n" + "  \"_links\": {\n" + "    \"self\": {\n"
                + "      \"href\": \"/accounts/abcd1234/secrets/secret-id-one\"\n" + "    }\n" + "  },\n"
                + "  \"id\": \"secret-id-one\",\n" + "  \"created_at\": \"2017-03-02T16:34:49Z\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        SecretResponse response = client.getSecret("apiKey", "secret-id-one");

        Calendar calendar = new GregorianCalendar(2017, Calendar.MARCH, 2, 16, 34, 49);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals("secret-id-one", response.getId());
        assertEquals(calendar.getTime(), response.getCreated());
        assertEquals("/accounts/abcd1234/secrets/secret-id-one", response.getSelf().getHref());
    }

    @Test(expected = VonageClientException.class)
    public void testGetSecretFailedAuth() throws Exception {
        String json = "{\n" + "  \"type\": \"https://developer.nexmo.com/api-errors#unauthorized\",\n"
                + "  \"title\": \"Invalid credentials supplied\",\n"
                + "  \"detail\": \"You did not provide correct credentials.\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(401, json));
        client.getSecret("apiKey", "secret-id-one");
    }

    @Test(expected = VonageClientException.class)
    public void testGetSecretNotFound() throws Exception {
        String json = "{\n" + "  \"type\": \"https://developer.nexmo.com/api-errors#invalid-api-key\",\n"
                + "  \"title\": \"Invalid API Key\",\n"
                + "  \"detail\": \"API key 'ABC123' does not exist, or you do not have access\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(404, json));
        client.getSecret("apiKey", "secret-id-one");
    }

    @Test
    public void testRevokeSecretSuccessful() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(204, ""));
        // No assertions as an exception will be thrown if failure occurs.
        client.revokeSecret("apiKey", "secretId");
    }

    @Test(expected = VonageClientException.class)
    public void testRevokeSecretFailedAuth() throws Exception {
        String json = "{\n" + "  \"type\": \"https://developer.nexmo.com/api-errors#unauthorized\",\n"
                + "  \"title\": \"Invalid credentials supplied\",\n"
                + "  \"detail\": \"You did not provide correct credentials.\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(401, json));
        client.revokeSecret("apiKey", "secret-id-one");
    }

    @Test(expected = VonageClientException.class)
    public void testRevokeSecretForbidden() throws Exception {
        String json = "{\n"
                + "  \"type\": \"https://developer.nexmo.com/api-errors/account/secret-management#delete-last-secret\",\n"
                + "  \"title\": \"Secret Deletion Forbidden\",\n"
                + "  \"detail\": \"Can not delete the last secret. The account must always have at least 1 secret active at any time\",\n"
                + "  \"instance\": \"797a8f199c45014ab7b08bfe9cc1c12c\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(403, json));
        client.revokeSecret("apiKey", "secret-id-one");
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
        wrapper.setHttpClient(this.stubHttpClient(200, json));
        SettingsResponse response = client.updateSmsIncomingUrl("https://example.com/webhooks/inbound-sms");

        Assert.assertEquals("https://example.com/webhooks/inbound-sms", response.getIncomingSmsUrl());
        Assert.assertEquals("https://example.com/webhooks/delivery-receipt", response.getDeliveryReceiptUrl());
        Assert.assertEquals(Integer.valueOf(30), response.getMaxApiCallsPerSecond());
        Assert.assertEquals(Integer.valueOf(20), response.getMaxInboundMessagesPerSecond());
        Assert.assertEquals(Integer.valueOf(10), response.getMaxOutboundMessagesPerSecond());
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
        wrapper.setHttpClient(this.stubHttpClient(200, json));
        SettingsResponse response = client.updateDeliveryReceiptUrl("https://example.com/webhooks/delivery-receipt");

        Assert.assertEquals("https://example.com/webhooks/inbound-sms", response.getIncomingSmsUrl());
        Assert.assertEquals("https://example.com/webhooks/delivery-receipt", response.getDeliveryReceiptUrl());
        Assert.assertEquals(Integer.valueOf(30), response.getMaxApiCallsPerSecond());
        Assert.assertEquals(Integer.valueOf(20), response.getMaxInboundMessagesPerSecond());
        Assert.assertEquals(Integer.valueOf(10), response.getMaxOutboundMessagesPerSecond());
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

        wrapper.setHttpClient(this.stubHttpClient(200, json));
        SettingsResponse response = client.updateSettings(new SettingsRequest("https://example.com/webhooks/inbound-sms", "https://example.com/webhooks/delivery-receipt"));

        Assert.assertEquals("https://example.com/webhooks/inbound-sms", response.getIncomingSmsUrl());
        Assert.assertEquals("https://example.com/webhooks/delivery-receipt", response.getDeliveryReceiptUrl());
        Assert.assertEquals(Integer.valueOf(30), response.getMaxApiCallsPerSecond());
        Assert.assertEquals(Integer.valueOf(20), response.getMaxInboundMessagesPerSecond());
        Assert.assertEquals(Integer.valueOf(10), response.getMaxOutboundMessagesPerSecond());
    }
}
