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

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageClientException;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class BalanceEndpointTest {
    private BalanceEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        endpoint = new BalanceEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() {
        Class[] auths = endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{TokenAuthMethod.class}, auths);
    }


    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = endpoint.makeRequest(null);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://rest.nexmo.com/account/get-balance", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals(0, params.size());
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        BalanceEndpoint endpoint = new BalanceEndpoint(wrapper);

        RequestBuilder builder = endpoint.makeRequest(null);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://example.com/account/get-balance", builder.build().getURI().toString());
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(200, "{\n" +
                "  \"value\": 3.14159,\n" +
                "  \"autoReload\": false\n" +
                "}}");
        BalanceResponse response = endpoint.parseResponse(stub);
        assertEquals(3.14159, response.getValue(), 0.00001);
        assertFalse(response.isAutoReload());
    }

    private static class StubbedBalanceEndpoint extends BalanceEndpoint {
        public StubbedBalanceEndpoint() {
            super(null);
        }

        @Override
        public BalanceResponse execute() throws VonageClientException {
            return new BalanceResponse(1.5, true);
        }
    }

    @Test
    public void testExecute() {
        BalanceEndpoint endpoint = new StubbedBalanceEndpoint();
        BalanceResponse response = endpoint.execute();
        assertEquals(response.getValue(), 1.5, 0.0001);
        assertTrue(response.isAutoReload());
    }
}
