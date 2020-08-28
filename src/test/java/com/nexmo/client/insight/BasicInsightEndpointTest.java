/*
 * Copyright (c) 2011-2017 Vonage Inc
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
package com.nexmo.client.insight;

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.TestUtils;
import com.nexmo.client.auth.SignatureAuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class BasicInsightEndpointTest {
    private BasicInsightEndpoint endpoint;

    @Before
    public void setUp() {
        this.endpoint = new BasicInsightEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = this.endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(BasicInsightRequest.builder("1234").build());
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/basic/json", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("1234", params.get("number"));
        assertNull(params.get("country"));
    }

    @Test
    public void testMakeRequestWithCountry() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(BasicInsightRequest.builder("1234")
                .country("GB")
                .build());
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/basic/json", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("1234", params.get("number"));
        assertEquals("GB", params.get("country"));
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "    \"status\": 0,\n" + "    \"status_message\": \"Success\",\n"
                        + "    \"request_id\": \"d79c3d82-e2ee-46ff-972a-97b76be419cb\",\n"
                        + "    \"international_format_number\": \"441632960960\",\n"
                        + "    \"national_format_number\": \"01632 960960\",\n" + "    \"country_code\": \"GB\",\n"
                        + "    \"country_code_iso3\": \"GBR\",\n" + "    \"country_name\": \"United Kingdom\",\n"
                        + "    \"country_prefix\": \"44\"\n" + "}"
        );
        BasicInsightResponse response = this.endpoint.parseResponse(stub);
        assertEquals("d79c3d82-e2ee-46ff-972a-97b76be419cb", response.getRequestId());
    }

    @Test
    public void testDefaultUri() throws Exception {
        BasicInsightRequest request = BasicInsightRequest.withNumber("1234");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/ni/basic/json", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        BasicInsightEndpoint endpoint = new BasicInsightEndpoint(wrapper);
        BasicInsightRequest request = BasicInsightRequest.withNumber("1234");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/ni/basic/json", builder.build().getURI().toString());
    }
}
