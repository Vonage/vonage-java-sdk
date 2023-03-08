/*
 *   Copyright 2023 Vonage
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

import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.AuthCollection;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.logging.LoggingUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NumbersClientTest {
    private final TestUtils testUtils = new TestUtils();

    private HttpWrapper stubHttpWrapper(int statusCode, String content) throws Exception {
        HttpClient client = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(LoggingUtils.logResponse(any(HttpResponse.class))).thenReturn("response logged");
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
        AuthCollection authCollection = new AuthCollection();
        authCollection.add(new TokenAuthMethod(
                "dummyKey",
                "dummySecret"
        ));

        HttpWrapper wrapper = new HttpWrapper(authCollection);
        wrapper.setHttpClient(client);

        return wrapper;
    }

    @Test
    public void testListNumbers() throws Exception {
        NumbersClient client = new NumbersClient(stubHttpWrapper(200, "{\n" +
                "  \"count\": 1,\n" +
                "  \"numbers\": [\n" +
                "    {\n" +
                "      \"country\": \"GB\",\n" +
                "      \"msisdn\": \"447700900000\",\n" +
                "      \"moHttpUrl\": \"https://example.com/mo\",\n" +
                "      \"type\": \"mobile-lvn\",\n" +
                "      \"features\": [\n" +
                "        \"VOICE\",\n" +
                "        \"SMS\"\n" +
                "      ],\n" +
                "      \"voiceCallbackType\": \"app\",\n" +
                "      \"voiceCallbackValue\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"));
        ListNumbersResponse response = client.listNumbers();
        assertEquals(1, response.getCount());
    }

    @Test
    public void testListNumberWithParams() throws Exception {
        NumbersClient client = new NumbersClient(stubHttpWrapper(200, "{\n" +
                "  \"count\": 1,\n" +
                "  \"numbers\": [\n" +
                "    {\n" +
                "      \"country\": \"GB\",\n" +
                "      \"msisdn\": \"447700900000\",\n" +
                "      \"moHttpUrl\": \"https://example.com/mo\",\n" +
                "      \"type\": \"mobile-lvn\",\n" +
                "      \"features\": [\n" +
                "        \"VOICE\",\n" +
                "        \"SMS\"\n" +
                "      ],\n" +
                "      \"voiceCallbackType\": \"app\",\n" +
                "      \"voiceCallbackValue\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"));

        ListNumbersFilter filter = new ListNumbersFilter();
        filter.setIndex(10);
        filter.setSize(20);
        filter.setPattern("234");
        filter.setSearchPattern(SearchPattern.ENDS_WITH);
        ListNumbersResponse response = client.listNumbers(filter);
        assertEquals(1, response.getCount());
    }

    @Test
    public void testSearchNumbers() throws Exception {
        NumbersClient client = new NumbersClient(stubHttpWrapper(200, "{\n" +
                "  \"count\": 4,\n" +
                "  \"numbers\": [\n" +
                "    {\n" +
                "      \"country\": \"GB\",\n" +
                "      \"msisdn\": \"447700900000\",\n" +
                "      \"cost\": \"0.50\",\n" +
                "      \"type\": \"mobile\",\n" +
                "      \"features\": [\n" +
                "        \"VOICE\",\n" +
                "        \"SMS\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}"));
        SearchNumbersResponse response = client.searchNumbers("YY");
        assertEquals(4, response.getCount());

    }

    @Test
    public void testCancelNumber() throws Exception {
        NumbersClient client = new NumbersClient(stubHttpWrapper(200, "{\n" +
                "  \"error-code\":\"200\",\n" +
                "  \"error-code-label\":\"success\"\n" +
                "}"));

        client.cancelNumber("AA", "447700900000");
    }

    @Test
    public void testBuyNumber() throws Exception {
        NumbersClient client = new NumbersClient(stubHttpWrapper(200, "{\n" +
                "  \"error-code\":\"200\",\n" +
                "  \"error-code-label\":\"success\"\n" +
                "}"));

        client.buyNumber("AA", "447700900000");
    }

    @Test
    public void testUpdateNumber() throws Exception {
        try {
            NumbersClient client = new NumbersClient(stubHttpWrapper(200, "{\n" +
                    "  \"error-code\":\"200\",\n" +
                    "  \"error-code-label\":\"success\"\n" +
                    "}"));
            client.updateNumber(new UpdateNumberRequest(
                    "447700900328", "UK"
            ));
        } catch (Exception e) {
            fail("Parsing a valid response should not raise an exception");
        }
    }

    @Test
    public void testLinkNumber() throws Exception {
        try {
            NumbersClient client = new NumbersClient(stubHttpWrapper(200, "{\n" +
                    "  \"error-code\":\"200\",\n" +
                    "  \"error-code-label\":\"success\"\n" +
                    "}"));
            client.linkNumber("447700900328", "UK", "my-app-id");
        } catch (Exception e) {
            fail("Parsing a valid response should not raise an exception");
        }

    }
}
