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

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.TestUtils;
import com.nexmo.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BalanceEndpointTest {
    private BalanceEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new BalanceEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = this.endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{TokenAuthMethod.class}, auths);
    }


    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(null);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://rest.nexmo.com/account/get-balance", builder.build().getURI().toString());
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals(0, params.size());

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
        BalanceResponse response = this.endpoint.parseResponse(stub);
        assertEquals(3.14159, response.getValue(), 0.00001);
        assertEquals(false, response.isAutoReload());
    }

    private class StubbedBalanceEndpoint extends BalanceEndpoint {
        public StubbedBalanceEndpoint() {
            super(null);
        }

        @Override
        public BalanceResponse execute() throws IOException, NexmoClientException {
            return new BalanceResponse(1.5, true);
        }
    }

    @Test
    public void testExecute() throws Exception {
        BalanceEndpoint endpoint = new StubbedBalanceEndpoint();
        BalanceResponse response = endpoint.execute();
        assertEquals(response.getValue(), 1.5, 0.0001);
        assertEquals(response.isAutoReload(), true);
    }
}
