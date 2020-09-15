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
