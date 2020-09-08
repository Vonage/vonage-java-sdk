/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.numbers;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class UpdateNumberEndpointTest {
    private UpdateNumberEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new UpdateNumberEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = this.endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(new UpdateNumberRequest("447700900013", "UK"));
        assertEquals("POST", builder.getMethod());
        assertEquals("https://rest.nexmo.com/number/update", builder.build().getURI().toString());

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals(2, params.size());
        assertEquals("447700900013", params.get("msisdn"));
        assertEquals("UK", params.get("country"));
    }

    @Test
    public void testMakeRequestWithOptionalParams() throws Exception {
        UpdateNumberRequest request = new UpdateNumberRequest("447700900013", "UK");
        request.setMoHttpUrl("https://api.example.com/mo");
        request.setMoSmppSysType("inbound");
        request.setVoiceCallbackValue("1234-5678-9123-4567");
        request.setVoiceCallbackType(UpdateNumberRequest.CallbackType.APP);
        request.setVoiceStatusCallback("https://api.example.com/callback");
        request.setMessagesCallbackValue("MESSAGES-APPLICATION-ID");

        RequestBuilder builder = this.endpoint.makeRequest(request);

        assertEquals("POST", builder.getMethod());
        assertEquals("https://rest.nexmo.com/number/update", builder.build().getURI().toString());

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals(9, params.size());
        assertEquals("447700900013", params.get("msisdn"));
        assertEquals("UK", params.get("country"));
        assertEquals("https://api.example.com/mo", params.get("moHttpUrl"));
        assertEquals("inbound", params.get("moSmppSysType"));
        assertEquals("1234-5678-9123-4567", params.get("voiceCallbackValue"));
        assertEquals("app", params.get("voiceCallbackType"));
        assertEquals("https://api.example.com/callback", params.get("voiceStatusCallback"));
        assertEquals("MESSAGES-APPLICATION-ID", params.get("messagesCallbackValue"));
        assertEquals(UpdateNumberRequest.CallbackType.APP.paramValue(), params.get("messagesCallbackType"));
    }

    @Test
    public void testParseResponse() throws Exception {
        try {
            HttpResponse stub = TestUtils.makeJsonHttpResponse(200,
                    "{\n" + "  \"error-code\":\"200\",\n" + "  \"error-code-label\":\"success\"\n" + "}"
            );
            this.endpoint.parseResponse(stub);
        } catch (Exception e) {
            fail("Parsing a 200 response should not raise an error.");
        }
    }

    @Test
    public void testParseErrorResponse() throws Exception {
        try {
            HttpResponse stub = TestUtils.makeJsonHttpResponse(
                    500,
                    "{\n" + "  \"error-code\":\"500\",\n" + "  \"error-code-label\":\"There was an error\"\n" + "}"
            );
            this.endpoint.parseResponse(stub);
            fail("An exception should have been thrown here.");
        } catch (Exception e) {
            // This is expected.
        }
    }

    @Test
    public void testDefaultUri() throws Exception {
        UpdateNumberRequest request = new UpdateNumberRequest("447700900013", "UK");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://rest.nexmo.com/number/update", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        UpdateNumberEndpoint endpoint = new UpdateNumberEndpoint(wrapper);
        UpdateNumberRequest request = new UpdateNumberRequest("447700900013", "UK");

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/number/update", builder.build().getURI().toString());
    }
}
