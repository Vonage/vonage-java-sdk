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
package com.nexmo.client.applications;

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.TestUtils;
import com.nexmo.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ListApplicationsEndpointTest {
    private ListApplicationsEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new ListApplicationsEndpoint(new HttpWrapper());
    }

    @Test
    public void testGetAcceptableAuthMethods() throws Exception {
        Class[] auths = this.endpoint.getAcceptableAuthMethods();
        assertArrayEquals(new Class[]{TokenAuthMethod.class}, auths);
    }

    @Test
    public void testMakeRequest() throws Exception {
        RequestBuilder builder = this.endpoint.makeRequest(new ListApplicationsRequest());
        assertEquals("GET", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/applications", builder.build().getURI().toString());

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals(0, params.size());
    }

    @Test
    public void testMakeRequestWithParams() throws Exception {
        ListApplicationsRequest request = new ListApplicationsRequest();
        request.setPageIndex(32);
        request.setPageSize(40);

        RequestBuilder builder = this.endpoint.makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://api.nexmo.com/v1/applications?page_size=40&page_index=32",
                builder.build().getURI().toString()
        );

        Map<String, String> params = TestUtils.makeParameterMap(builder.getParameters());
        assertEquals("32", params.get("page_index"));
        assertEquals("40", params.get("page_size"));
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stub = TestUtils.makeJsonHttpResponse(
                200,
                "{\n" + "  \"count\": 1,\n" + "  \"page_size\": 10,\n" + "  \"page_index\": 3,\n"
                        + "  \"_embedded\": {\n" + "    \"applications\": [\n" + "      {\n"
                        + "        \"id\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n"
                        + "        \"name\": \"My Application\",\n" + "        \"voice\": {\n"
                        + "          \"webhooks\": [\n" + "            {\n"
                        + "              \"endpoint_type\": \"event_url\",\n"
                        + "              \"endpoint\": \"https://example.com/event\",\n"
                        + "              \"http_method\": \"POST\"\n" + "            },\n" + "            {\n"
                        + "              \"endpoint_type\": \"answer_url\",\n"
                        + "              \"endpoint\": \"https://example.com/answer\",\n"
                        + "              \"http_method\": \"GET\"\n" + "            }\n" + "          ]\n"
                        + "        },\n" + "        \"keys\": {\n" + "          \"public_key\": \"PUBLIC_KEY\"\n"
                        + "        },\n" + "        \"_links\": {\n" + "          \"self\": {\n"
                        + "            \"href\": \"/v1/applications/aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n"
                        + "          }\n" + "        }\n" + "      }\n" + "    ]\n" + "  },\n" + "  \"_links\": {\n"
                        + "    \"self\": {\n" + "      \"href\": \"/v1/applications?page_size=10&page_index=1\"\n"
                        + "    },\n" + "    \"first\": {\n" + "      \"href\": \"/v1/applications?page_size=10\"\n"
                        + "    },\n" + "    \"last\": {\n"
                        + "      \"href\": \"/v1/applications?page_size=10&page_index=5\"\n" + "    },\n"
                        + "    \"next\": {\n" + "      \"href\": \"/v1/applications?page_size=10&page_index=2\"\n"
                        + "    }\n" + "  }\n" + "}"
        );
        ListApplicationsResponse response = this.endpoint.parseResponse(stub);
        assertEquals(1, response.getCount());
        assertEquals(10, response.getPageSize());
        assertEquals(3, response.getPageIndex());

        ApplicationDetails app = response.iterator().next();
        assertNotNull(app);

        assertEquals("aaaaaaaa-bbbb-cccc-dddd-0123456789ab", app.getId());
        assertEquals("My Application", app.getName());

        assertNotNull(app.getVoiceApplicationDetails());
        assertNotNull(app.getVoiceApplicationDetails().getWebHooks());

        WebHook event = app.getVoiceApplicationDetails().getWebHooks()[0];
        assertEquals("event_url", event.getEndpointType());
        assertEquals("https://example.com/event", event.getEndpointUrl());
        assertEquals("POST", event.getHttpMethod());

        WebHook answer = app.getVoiceApplicationDetails().getWebHooks()[1];
        assertEquals("answer_url", answer.getEndpointType());
        assertEquals("https://example.com/answer", answer.getEndpointUrl());
        assertEquals("GET", answer.getHttpMethod());

        assertNotNull(app.getKeys());
        ApplicationKeys keys = app.getKeys();

        assertEquals("PUBLIC_KEY", keys.getPublicKey());
        assertNull(keys.getPrivateKey());

        assertEquals(response.getEmbedded().getApplicationDetails()[0], app);
    }

    @Test
    public void testDefaultUri() throws Exception {
        ListApplicationsRequest request = new ListApplicationsRequest();

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/applications",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(new HttpConfig.Builder().baseUri("https://example.com").build());
        ListApplicationsEndpoint endpoint = new ListApplicationsEndpoint(wrapper);
        ListApplicationsRequest request = new ListApplicationsRequest();

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://example.com/v1/applications",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testDefaultUriWithPage() throws Exception {
        ListApplicationsRequest request = new ListApplicationsRequest();
        request.setPageIndex(32);
        request.setPageSize(40);

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/applications?page_size=40&page_index=32",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUriWithPage() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(new HttpConfig.Builder().baseUri("https://example.com").build());
        ListApplicationsEndpoint endpoint = new ListApplicationsEndpoint(wrapper);
        ListApplicationsRequest request = new ListApplicationsRequest();
        request.setPageIndex(32);
        request.setPageSize(40);

        RequestBuilder builder = endpoint.makeRequest(request);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://example.com/v1/applications?page_size=40&page_index=32",
                builder.build().getURI().toString()
        );
    }
}
