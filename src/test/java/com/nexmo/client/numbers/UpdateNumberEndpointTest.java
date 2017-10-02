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
package com.nexmo.client.numbers;

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

public class UpdateNumberEndpointTest {
    private UpdateNumberEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new UpdateNumberEndpoint(null);
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = this.endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(new UpdateNumberRequest(

        ));
        // TODO: Check method and URL are correct:
        assertEquals("POST", builder.getMethod());
        assertEquals("https://rest.nexmo.com/number/update", builder.build().getURI().toString());
        
        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        // TODO: Assert params are as expected:
        assertEquals(0, params.size());
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(200, "{}");
        UpdateNumberResponse response = this.endpoint.parseResponse(stub);
        // TODO: Assert response values are correct:
        // assertEquals(3.14159, response.getValue(), 0.00001);
    }
}
