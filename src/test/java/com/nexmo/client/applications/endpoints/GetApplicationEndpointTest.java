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
package com.nexmo.client.applications.endpoints;

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.TestUtils;
import com.nexmo.client.applications.ApplicationDetails;
import com.nexmo.client.applications.ApplicationKeys;
import com.nexmo.client.applications.WebHook;
import com.nexmo.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class GetApplicationEndpointTest {
    private GetApplicationEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new GetApplicationEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = this.endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest("app-id");
        assertEquals("GET", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/applications/app-id", builder.build().getURI().toString());

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals(0, params.size());
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(200,
                "{\n" + "  \"id\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" + "  \"name\": \"My Application\",\n"
                        + "  \"voice\": {\n" + "    \"webhooks\": [\n" + "      {\n"
                        + "        \"endpoint_type\": \"answer_url\",\n"
                        + "        \"endpoint\": \"https://example.com/answer\",\n"
                        + "        \"http_method\": \"GET\"\n" + "      },\n" + "      {\n"
                        + "        \"endpoint_type\": \"event_url\",\n"
                        + "        \"endpoint\": \"https://example.com/event\",\n"
                        + "        \"http_method\": \"POST\"\n" + "      }\n" + "    ]\n" + "  },\n" + "  \"keys\": {\n"
                        + "    \"public_key\": \"PUBLIC_KEY\",\n" + "    \"private_key\": \"PRIVATE_KEY\"\n" + "  },\n"
                        + "  \"_links\": {\n" + "    \"self\": {\n"
                        + "      \"href\": \"/v1/applications/aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" + "    }\n"
                        + "  }\n" + "}"
        );
        ApplicationDetails response = this.endpoint.parseResponse(stub);
        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", response.getId());
        assertEquals("My Application", response.getName());

        assertNotNull(response.getVoiceApplicationDetails());
        assertNotNull(response.getVoiceApplicationDetails().getWebHooks());

        WebHook answer = response.getVoiceApplicationDetails().getWebHooks()[0];
        assertEquals("answer_url", answer.getEndpointType());
        assertEquals("https://example.com/answer", answer.getEndpointUrl());
        assertEquals("GET", answer.getHttpMethod());

        WebHook event = response.getVoiceApplicationDetails().getWebHooks()[1];
        assertEquals("event_url", event.getEndpointType());
        assertEquals("https://example.com/event", event.getEndpointUrl());
        assertEquals("POST", event.getHttpMethod());

        assertNotNull(response.getKeys());
        ApplicationKeys keys = response.getKeys();

        assertEquals("PUBLIC_KEY", keys.getPublicKey());
        assertEquals("PRIVATE_KEY", keys.getPrivateKey());
    }

    @Test
    public void testDefaultUri() throws Exception {
        RequestBuilder builder = endpoint.makeRequest("application-id");
        assertEquals("GET", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/applications/application-id", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(new HttpConfig.Builder().baseUri("https://example.com").build());
        GetApplicationEndpoint endpoint = new GetApplicationEndpoint(wrapper);

        RequestBuilder builder = endpoint.makeRequest("application-id");
        assertEquals("GET", builder.getMethod());
        assertEquals("https://example.com/v1/applications/application-id", builder.build().getURI().toString());
    }
}
