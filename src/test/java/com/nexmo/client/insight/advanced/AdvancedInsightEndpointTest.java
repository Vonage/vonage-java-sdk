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
package com.nexmo.client.insight.advanced;

import com.nexmo.client.TestUtils;
import com.nexmo.client.auth.SignatureAuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
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
        this.endpoint = new AdvancedInsightEndpoint(null);
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = this.endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(new AdvancedInsightRequest("1234"));
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/advanced/json", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals(params.get("number"), "1234");
        assertNull(params.get("country"));
        assertNull(params.get("ip"));

    }

    @Test
    public void testMakeRequestWithCountry() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(new AdvancedInsightRequest("1234", "GB", "123.123.123.123"));
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/advanced/json", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals(params.get("number"), "1234");
        assertEquals(params.get("country"), "GB");
        assertEquals(params.get("ip"), "123.123.123.123");
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(200, "{\n" +
                "    \"status\": 0,\n" +
                "    \"status_message\": \"Success\",\n" +
                "    \"lookup_outcome\": 1,\n" +
                "    \"lookup_outcome_message\": \"Partial success - some fields populated\",\n" +
                "    \"request_id\": \"0c082a69-85df-4bbc-aae6-ee998e17e5a4\",\n" +
                "    \"international_format_number\": \"441632960960\",\n" +
                "    \"national_format_number\": \"01632 960960\",\n" +
                "    \"country_code\": \"GB\",\n" +
                "    \"country_code_iso3\": \"GBR\",\n" +
                "    \"country_name\": \"United Kingdom\",\n" +
                "    \"country_prefix\": \"44\",\n" +
                "    \"request_price\": \"0.03000000\",\n" +
                "    \"remaining_balance\": \"18.30908949\",\n" +
                "    \"current_carrier\": {\n" +
                "        \"network_code\": \"GB-FIXED-RESERVED\",\n" +
                "        \"name\": \"United Kingdom Landline Reserved\",\n" +
                "        \"country\": \"GB\",\n" +
                "        \"network_type\": \"landline\"\n" +
                "    },\n" +
                "    \"original_carrier\": {\n" +
                "        \"network_code\": \"GB-HAPPY-RESERVED\",\n" +
                "        \"name\": \"United Kingdom Mobile Reserved\",\n" +
                "        \"country\": \"GB\",\n" +
                "        \"network_type\": \"mobile\"\n" +
                "    },\n" +
                "    \"valid_number\": \"valid\",\n" +
                "    \"reachable\": \"unknown\",\n" +
                "    \"ported\": \"assumed_not_ported\",\n" +
                "    \"roaming\": {\"status\": \"not_roaming\"}\n" +
                "}");
        AdvancedInsightResponse response = this.endpoint.parseResponse(stub);
        assertEquals("0c082a69-85df-4bbc-aae6-ee998e17e5a4", response.getRequestId());

    }
}
