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

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.TestUtils;
import com.nexmo.client.account.AccountClient;
import com.nexmo.client.account.BalanceEndpoint;
import com.nexmo.client.account.BalanceResponse;
import com.nexmo.client.applications.endpoints.ApplicationsEndpoint;
import com.nexmo.client.auth.AuthCollection;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.voice.Call;
import com.nexmo.client.voice.CallEvent;
import com.nexmo.client.voice.VoiceClient;
import junit.framework.Assert;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ApplicationClientTest {
    private ApplicationClient client;

    private TestUtils testUtils = new TestUtils();

    private static HttpWrapper stubHttpWrapper(int statusCode, String content) throws Exception {
        HttpClient client = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes("UTF-8")));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        AuthCollection authCollection = new AuthCollection();
        authCollection.add(new TokenAuthMethod("api-key", "api-secret"));

        HttpWrapper wrapper = new HttpWrapper(authCollection);
        wrapper.setHttpClient(client);

        return wrapper;
    }

    @Before
    public void setUp() throws Exception {
        client = new ApplicationClient(null);
    }



    @Test
    public void testCreateApplication() throws Exception {
        ApplicationClient client = new ApplicationClient(stubHttpWrapper(200, "{\n" +
                "  \"id\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
                "  \"name\": \"My Application\",\n" +
                "  \"voice\": {\n" +
                "    \"webhooks\": [\n" +
                "      {\n" +
                "        \"endpoint_type\": \"answer_url\",\n" +
                "        \"endpoint\": \"https://example.com/answer\",\n" +
                "        \"http_method\": \"GET\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"endpoint_type\": \"event_url\",\n" +
                "        \"endpoint\": \"https://example.com/event\",\n" +
                "        \"http_method\": \"POST\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"keys\": {\n" +
                "    \"public_key\": \"PUBLIC_KEY\",\n" +
                "    \"private_key\": \"PRIVATE_KEY\"\n" +
                "  },\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"/v1/applications/aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" +
                "    }\n" +
                "  }\n" +
                "}"));
        ApplicationDetails response = client.createApplication(
                new CreateApplicationRequest(
                        "My Application",
                        "https://example.com/answer",
                        "https://example.com/event"));
        assertEquals("My Application", response.getName());
    }


    @Test
    public void testUpdateApplication() throws Exception {
        ApplicationClient client = new ApplicationClient(stubHttpWrapper(200, "{\n" +
                "  \"id\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
                "  \"name\": \"My Application\",\n" +
                "  \"voice\": {\n" +
                "    \"webhooks\": [\n" +
                "      {\n" +
                "        \"endpoint_type\": \"answer_url\",\n" +
                "        \"endpoint\": \"https://example.com/answer\",\n" +
                "        \"http_method\": \"GET\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"endpoint_type\": \"event_url\",\n" +
                "        \"endpoint\": \"https://example.com/event\",\n" +
                "        \"http_method\": \"POST\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"keys\": {\n" +
                "    \"public_key\": \"PUBLIC_KEY\",\n" +
                "    \"private_key\": \"PRIVATE_KEY\"\n" +
                "  },\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"/v1/applications/aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" +
                "    }\n" +
                "  }\n" +
                "}"));
        ApplicationDetails response = client.updateApplication(
                new UpdateApplicationRequest(
                        "app-id",
                        "My Application",
                        "https://example.com/answer",
                        "https://example.com/event"));
        assertEquals("My Application", response.getName());
    }

    @Test
    public void testGetApplication() throws Exception {
        ApplicationClient client = new ApplicationClient(stubHttpWrapper(200, "{\n" +
                "  \"id\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
                "  \"name\": \"My Application\",\n" +
                "  \"voice\": {\n" +
                "    \"webhooks\": [\n" +
                "      {\n" +
                "        \"endpoint_type\": \"answer_url\",\n" +
                "        \"endpoint\": \"https://example.com/answer\",\n" +
                "        \"http_method\": \"GET\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"endpoint_type\": \"event_url\",\n" +
                "        \"endpoint\": \"https://example.com/event\",\n" +
                "        \"http_method\": \"POST\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"keys\": {\n" +
                "    \"public_key\": \"PUBLIC_KEY\",\n" +
                "    \"private_key\": \"PRIVATE_KEY\"\n" +
                "  },\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"/v1/applications/aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" +
                "    }\n" +
                "  }\n" +
                "}"));
        ApplicationDetails response = client.getApplication("app-id");
        assertEquals("My Application", response.getName());
    }

    @Test
    public void testListApplications() throws Exception {
        ApplicationClient client = new ApplicationClient(stubHttpWrapper(200, "{\n" +
                "  \"count\": 1,\n" +
                "  \"page_size\": 10,\n" +
                "  \"page_index\": 3,\n" +
                "  \"_embedded\": {\n" +
                "    \"applications\": [\n" +
                "      {\n" +
                "        \"id\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
                "        \"name\": \"My Application\",\n" +
                "        \"voice\": {\n" +
                "          \"webhooks\": [\n" +
                "            {\n" +
                "              \"endpoint_type\": \"event_url\",\n" +
                "              \"endpoint\": \"https://example.com/event\",\n" +
                "              \"http_method\": \"POST\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"endpoint_type\": \"answer_url\",\n" +
                "              \"endpoint\": \"https://example.com/answer\",\n" +
                "              \"http_method\": \"GET\"\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        \"keys\": {\n" +
                "          \"public_key\": \"PUBLIC_KEY\"\n" +
                "        },\n" +
                "        \"_links\": {\n" +
                "          \"self\": {\n" +
                "            \"href\": \"/v1/applications/aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"_links\": {\n" +
                "    \"self\": {\n" +
                "      \"href\": \"/v1/applications?page_size=10&page_index=1\"\n" +
                "    },\n" +
                "    \"first\": {\n" +
                "      \"href\": \"/v1/applications?page_size=10\"\n" +
                "    },\n" +
                "    \"last\": {\n" +
                "      \"href\": \"/v1/applications?page_size=10&page_index=5\"\n" +
                "    },\n" +
                "    \"next\": {\n" +
                "      \"href\": \"/v1/applications?page_size=10&page_index=2\"\n" +
                "    }\n" +
                "  }\n" +
                "}"));
        ListApplicationsResponse response = client.listApplications(new ListApplicationsRequest());
        assertEquals("My Application", response.iterator().next().getName());
    }

    @Test
    public void testDeleteApplication() throws Exception {
        ApplicationClient client = new ApplicationClient(stubHttpWrapper(204, ""));
        client.deleteApplication("app-id");
    }
}
