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
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class StandardInsightEndpointTest {
    private StandardInsightEndpoint endpoint;

    @Before
    public void setUp() {
        endpoint = new StandardInsightEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = endpoint.makeRequest(StandardInsightRequest.builder("1234").build());
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/standard/json", builder.build().getURI().toString());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("1234", params.get("number"));
        assertNull(params.get("country"));
        assertNull(params.get("cnam"));
    }

    @Test
    public void testMakeRequestWithCountry() throws Exception {
        RequestBuilder builder = endpoint.makeRequest(StandardInsightRequest.builder("1234")
                .country("GB")
                .build());
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/standard/json", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("1234", params.get("number"));
        assertEquals("GB", params.get("country"));
        assertNull(params.get("cnam"));
    }

    @Test
    public void testMakeRequestWithCnam() throws Exception {
        RequestBuilder builder = endpoint.makeRequest(StandardInsightRequest.builder("1234")
                .cnam(true)
                .build());
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/standard/json", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("1234", params.get("number"));
        assertNull(params.get("country"));
        assertEquals("true", params.get("cnam"));
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "    \"status\": 0,\n" + "    \"status_message\": \"Success\",\n"
                        + "    \"request_id\": \"34564b7d-df8b-47fd-aa07-b722602dd974\",\n"
                        + "    \"international_format_number\": \"441632960960\",\n"
                        + "    \"national_format_number\": \"01632 960960\",\n" + "    \"country_code\": \"GB\",\n"
                        + "    \"country_code_iso3\": \"GBR\",\n" + "    \"country_name\": \"United Kingdom\",\n"
                        + "    \"country_prefix\": \"44\",\n" + "    \"request_price\": \"0.00500000\",\n"
                        + "    \"remaining_balance\": \"18.34408949\",\n" + "    \"current_carrier\": {\n"
                        + "        \"network_code\": \"GB-FIXED-RESERVED\",\n"
                        + "        \"name\": \"United Kingdom Landline Reserved\",\n" + "        \"country\": \"GB\",\n"
                        + "        \"network_type\": \"landline\"\n" + "    },\n" + "    \"original_carrier\": {\n"
                        + "        \"network_code\": \"GB-HAPPY-RESERVED\",\n"
                        + "        \"name\": \"United Kingdom Mobile Reserved\",\n" + "        \"country\": \"GB\",\n"
                        + "        \"network_type\": \"mobile\"\n" + "    },\n"
                        + "    \"ported\": \"assumed_not_ported\"\n" + "}"
        );
        StandardInsightResponse response = endpoint.parseResponse(stub);
        assertEquals("34564b7d-df8b-47fd-aa07-b722602dd974", response.getRequestId());
    }

    @Test
    public void testDefaultUri() throws Exception {
        StandardInsightRequest request = StandardInsightRequest.withNumber("1234");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/standard/json", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        StandardInsightEndpoint endpoint = new StandardInsightEndpoint(wrapper);
        StandardInsightRequest request = StandardInsightRequest.withNumber("1234");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/ni/standard/json", builder.build().getURI().toString());
    }
}
