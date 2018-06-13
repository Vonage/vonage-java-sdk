/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
package com.nexmo.client.account;

import com.nexmo.client.ClientTest;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.auth.TokenAuthMethod;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

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
        String json = "{\n" + "  \"dialingPrefix\": \"1\",\n" + "  \"defaultPrice\": \"0.00570000\",\n" + "  \"currency\": \"EUR\",\n" + "  \"countryDisplayName\": \"United States of America\",\n" + "  \"countryCode\": \"US\",\n" + "  \"countryName\": \"United States\",\n" + "  \"networks\": [\n" + "    {\n" + "      \"type\": \"mobile\",\n" + "      \"price\": \"0.00570000\",\n" + "      \"currency\": \"EUR\",\n" + "      \"mcc\": \"987\",\n" + "      \"mnc\": \"123\",\n" + "      \"networkCode\": \"123456\",\n" + "      \"networkName\": \"Test Mobile\"\n" + "    },\n" + "    {\n" + "      \"type\": \"landline\",\n" + "      \"price\": \"0.00330000\",\n" + "      \"currency\": \"EUR\",\n" + "      \"mcc\": \"123\",\n" + "      \"mnc\": \"456\",\n" + "      \"networkCode\": \"networkcode\",\n" + "      \"networkName\": \"Test Landline\"\n" + "    }  \n" + "  ]\n" + "}\n";
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
        String json = "{\n" + "  \"dialingPrefix\": \"1\",\n" + "  \"defaultPrice\": \"0.00570000\",\n" + "  \"currency\": \"EUR\",\n" + "  \"countryDisplayName\": \"United States of America\",\n" + "  \"countryCode\": \"US\",\n" + "  \"countryName\": \"United States\",\n" + "  \"networks\": [\n" + "    {\n" + "      \"type\": \"mobile\",\n" + "      \"price\": \"0.00570000\",\n" + "      \"currency\": \"EUR\",\n" + "      \"mcc\": \"987\",\n" + "      \"mnc\": \"123\",\n" + "      \"networkCode\": \"123456\",\n" + "      \"networkName\": \"Test Mobile\"\n" + "    },\n" + "    {\n" + "      \"type\": \"landline\",\n" + "      \"price\": \"0.00330000\",\n" + "      \"currency\": \"EUR\",\n" + "      \"mcc\": \"123\",\n" + "      \"mnc\": \"456\",\n" + "      \"networkCode\": \"networkcode\",\n" + "      \"networkName\": \"Test Landline\"\n" + "    }  \n" + "  ]\n" + "}\n";
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
        String json = "{\n" + "    \"count\": 2,\n" + "    \"countries\": [\n" + "        {\n" + "            \"dialingPrefix\": \"1\",\n" + "            \"defaultPrice\": \"0.01270000\",\n" + "            \"currency\": \"EUR\",\n" + "            \"countryDisplayName\": \"Canada\",\n" + "            \"countryCode\": \"CA\",\n" + "            \"countryName\": \"Canada\",\n" + "            \"networks\": [\n" + "                {\n" + "                    \"type\": \"mobile\",\n" + "                    \"price\": \"0.01280000\",\n" + "                    \"currency\": \"EUR\",\n" + "                    \"aliases\": [\n" + "                        \"302998\"\n" + "                    ],\n" + "                    \"mcc\": \"302\",\n" + "                    \"mnc\": \"702\",\n" + "                    \"networkCode\": \"302702\",\n" + "                    \"networkName\": \"BELL ALIANT REGIONAL Communications LP\"\n" + "                },\n" + "                {\n" + "                    \"type\": \"landline\",\n" + "                    \"price\": \"0.01000000\",\n" + "                    \"currency\": \"EUR\",\n" + "                    \"networkCode\": \"CA-FIXED\",\n" + "                    \"networkName\": \"Canada Landline\"\n" + "                }\n" + "            ]\n" + "        },\n" + "        {\n" + "            \"dialingPrefix\": \"1\",\n" + "            \"currency\": \"EUR\",\n" + "            \"countryDisplayName\": \"United States Minor Outlying Islands\",\n" + "            \"countryCode\": \"UM\",\n" + "            \"countryName\": \"United States Minor Outlying Islands\"\n" + "        }\n" + "    ]\n" + "}";
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
        String json = "{\n" + "    \"count\": 2,\n" + "    \"countries\": [\n" + "        {\n" + "            \"dialingPrefix\": \"1\",\n" + "            \"defaultPrice\": \"0.00570000\",\n" + "            \"currency\": \"EUR\",\n" + "            \"countryDisplayName\": \"Canada\",\n" + "            \"countryCode\": \"CA\",\n" + "            \"countryName\": \"Canada\",\n" + "            \"networks\": [\n" + "                {\n" + "                    \"type\": \"mobile\",\n" + "                    \"price\": \"0.00570000\",\n" + "                    \"currency\": \"EUR\",\n" + "                    \"aliases\": [\n" + "                        \"302660\"\n" + "                    ],\n" + "                    \"mcc\": \"302\",\n" + "                    \"mnc\": \"655\",\n" + "                    \"networkCode\": \"302655\",\n" + "                    \"networkName\": \"MTS Communications Inc.\"\n" + "                }\n" + "            ]\n" + "        },\n" + "        {\n" + "            \"dialingPrefix\": \"1\",\n" + "            \"currency\": \"EUR\",\n" + "            \"countryDisplayName\": \"United States Minor Outlying Islands\",\n" + "            \"countryCode\": \"UM\",\n" + "            \"countryName\": \"United States Minor Outlying Islands\"\n" + "        }\n" + "    ]\n" + "}";
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
}
