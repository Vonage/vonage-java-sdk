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
package com.vonage.client.insight;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class AdvancedInsightEndpointTest {
    private AdvancedInsightEndpoint endpoint;

    @Before
    public void setUp() {
        endpoint = new AdvancedInsightEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = endpoint.makeRequest(AdvancedInsightRequest.builder("1234").build());
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/advanced/json", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("1234", params.get("number"));
        assertNull(params.get("country"));
        assertNull(params.get("ip"));

    }

    @Test
    public void testMakeRequestWithCountry() throws Exception {
        RequestBuilder builder = endpoint.makeRequest(AdvancedInsightRequest.builder("1234")
                .country("GB")
                .build());
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/advanced/json", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("1234", params.get("number"));
        assertEquals("GB", params.get("country"));
        assertNull(params.get("ip"));
        assertNull(params.get("cnam"));
    }

    @Test
    public void testMakeRequestWithIpAddress() throws Exception {
        RequestBuilder builder = endpoint.makeRequest(AdvancedInsightRequest.builder("1234")
                .ipAddress("123.123.123.123")
                .build());

        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/advanced/json", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("1234", params.get("number"));
        assertEquals("123.123.123.123", params.get("ip"));
        assertNull(params.get("country"));
        assertNull(params.get("cnam"));
    }

    @Test
    public void testMakeRequestWithCnam() throws Exception {
        RequestBuilder builder = endpoint.makeRequest(AdvancedInsightRequest.builder("1234")
                .cnam(true)
                .build());
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/advanced/json", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals(params.get("number"), "1234");
        assertNull(params.get("country"));
        assertNull(params.get("ip"));
        assertEquals("true", params.get("cnam"));
    }

    @Test
    public void testMakeAsyncRequest() throws Exception {
        RequestBuilder builder = endpoint.makeRequest(AdvancedInsightRequest.builder("1234")
                .async(true)
                .callback("https://example.com")
                .build());

        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/advanced/async/json", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals(params.get("number"), "1234");
        assertEquals(params.get("callback"), "https://example.com");

    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "    \"status\": 0,\n" + "    \"status_message\": \"Success\",\n"
                        + "    \"lookup_outcome\": 1,\n"
                        + "    \"lookup_outcome_message\": \"Partial success - some fields populated\",\n"
                        + "    \"request_id\": \"0c082a69-85df-4bbc-aae6-ee998e17e5a4\",\n"
                        + "    \"international_format_number\": \"441632960960\",\n"
                        + "    \"national_format_number\": \"01632 960960\",\n" + "    \"country_code\": \"GB\",\n"
                        + "    \"country_code_iso3\": \"GBR\",\n" + "    \"country_name\": \"United Kingdom\",\n"
                        + "    \"country_prefix\": \"44\",\n" + "    \"request_price\": \"0.03000000\",\n"
                        + "    \"remaining_balance\": \"18.30908949\",\n" + "    \"current_carrier\": {\n"
                        + "        \"network_code\": \"GB-FIXED-RESERVED\",\n"
                        + "        \"name\": \"United Kingdom Landline Reserved\",\n" + "        \"country\": \"GB\",\n"
                        + "        \"network_type\": \"landline\"\n" + "    },\n" + "    \"original_carrier\": {\n"
                        + "        \"network_code\": \"GB-HAPPY-RESERVED\",\n"
                        + "        \"name\": \"United Kingdom Mobile Reserved\",\n" + "        \"country\": \"GB\",\n"
                        + "        \"network_type\": \"mobile\"\n" + "    },\n" + "    \"valid_number\": \"valid\",\n"
                        + "    \"reachable\": \"unknown\",\n" + "    \"ported\": \"assumed_not_ported\",\n"
                        + "    \"roaming\": {\"status\": \"not_roaming\"}\n" + "}"
        );
        AdvancedInsightResponse response = endpoint.parseResponse(stub);
        assertEquals("0c082a69-85df-4bbc-aae6-ee998e17e5a4", response.getRequestId());
    }

    @Test
    public void testDefaultUri() throws Exception {
        AdvancedInsightRequest request = AdvancedInsightRequest.withNumber("1234");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/advanced/json", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        AdvancedInsightEndpoint endpoint = new AdvancedInsightEndpoint(wrapper);
        AdvancedInsightRequest request = AdvancedInsightRequest.withNumber("1234");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/ni/advanced/json", builder.build().getURI().toString());
    }

    @Test
    public void testDefaultUriWithAsync() throws Exception {
        AdvancedInsightRequest request = AdvancedInsightRequest.builder("1234")
                .async(true)
                .callback("https://example.com")
                .build();


        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/advanced/async/json", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUriWithAsync() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        AdvancedInsightEndpoint endpoint = new AdvancedInsightEndpoint(wrapper);
        AdvancedInsightRequest request = AdvancedInsightRequest.builder("1234")
                .async(true)
                .callback("https://example.com")
                .build();

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/ni/advanced/async/json", builder.build().getURI().toString());
    }
}
