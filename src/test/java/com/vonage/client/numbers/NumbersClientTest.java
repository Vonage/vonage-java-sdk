/*
 *   Copyright 2024 Vonage
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

import com.vonage.client.AbstractClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.TestUtils;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.util.LinkedHashMap;
import java.util.Map;

public class NumbersClientTest extends AbstractClientTest<NumbersClient> {

    public NumbersClientTest() {
        client = new NumbersClient(wrapper);
    }

    private void assert401ResponseException(Executable invocation) throws Exception {
        String response = """
                {
                   "error-code": "401",
                   "error-code-label": "authentication failed"
                }""";
        NumbersResponseException ex = assertApiResponseException(
                401, response, NumbersResponseException.class, invocation
        );
        assertEquals("authentication failed", ex.getErrorCodeLabel());
    }

    private void stubBaseSuccessResponse() throws Exception {
        stubResponse(200, """
            {
              "error-code":"200",
              "error-code-label":"success"
            }"""
        );
    }

    @Test
    public void testListNumbers() throws Exception {
        stubResponse(200, "{\n" +
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
                "}");
        ListNumbersResponse response = client.listNumbers();
        TestUtils.testJsonableBaseObject(response);
        assertEquals(1, response.getCount());
        assert401ResponseException(client::listNumbers);
    }

    @Test
    public void testListNumberWithParams() throws Exception {
        stubResponse(200, "{\n" +
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
                "}");

        ListNumbersFilter filter = new ListNumbersFilter();
        filter.setIndex(10);
        filter.setSize(20);
        filter.setPattern("234");
        filter.setSearchPattern(SearchPattern.ENDS_WITH);
        ListNumbersResponse response = client.listNumbers(filter);
        assertEquals(1, response.getCount());
        assert401ResponseException(() -> client.listNumbers(filter));
    }

    @Test
    public void testSearchNumbers() throws Exception {
        stubResponse(200, "{\n" +
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
                "}");
        SearchNumbersResponse response = client.searchNumbers("YY");
        assertEquals(4, response.getCount());
        assert401ResponseException(() -> client.searchNumbers(new SearchNumbersFilter("FR")));
    }

    @Test
    public void testCancelNumber() throws Exception {
        stubBaseSuccessResponse();
        client.cancelNumber("AA", "447700900000");
        assert401ResponseException(() -> client.cancelNumber("UK", "447700900000"));
    }

    @Test
    public void testBuyNumber() throws Exception {
        stubBaseSuccessResponse();
        client.buyNumber("AA", "447700900000");
        assert401ResponseException(() -> client.buyNumber("UK", "447700900000"));
    }

    @Test
    public void testUpdateNumber() throws Exception {
        stubBaseSuccessResponse();
        UpdateNumberRequest request = new UpdateNumberRequest("447700900328", "UK");
        client.updateNumber(request);
        assert401ResponseException(() -> client.updateNumber(request));
    }

    @Test
    public void testLinkNumber() throws Exception {
        stubBaseSuccessResponse();
        client.linkNumber("447700900328", "UK", "my-app-id");
    }

    // ENDPOINTS

    @Test
    public void testListNumbersEndpoint() throws Exception {
        new NumbersEndpointTestSpec<ListNumbersFilter, ListNumbersResponse>() {

            @Override
            protected RestEndpoint<ListNumbersFilter, ListNumbersResponse> endpoint() {
                return client.listNumbers;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected String expectedEndpointUri(ListNumbersFilter request) {
                return "/account/numbers";
            }

            @Override
            protected ListNumbersFilter sampleRequest() {
                ListNumbersFilter filter = new ListNumbersFilter();
                filter.setIndex(10);
                filter.setSize(20);
                filter.setPattern("234");
                filter.setSearchPattern(SearchPattern.STARTS_WITH);
                return filter;
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                ListNumbersFilter request = sampleRequest();
                Map<String, String> params = new LinkedHashMap<>(8);
                params.put("pattern", request.getPattern());
                params.put("search_pattern", String.valueOf(request.getSearchPattern().getValue()));
                params.put("index", String.valueOf(request.getIndex()));
                params.put("size", String.valueOf(request.getSize()));
                return params;
            }
        }
        .runTests();
    }

    @Test
    public void testSearchNumbersEndpoint() throws Exception {
        new NumbersEndpointTestSpec<SearchNumbersFilter, SearchNumbersResponse>() {

            @Override
            protected RestEndpoint<SearchNumbersFilter, SearchNumbersResponse> endpoint() {
                return client.searchNumbers;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected String expectedEndpointUri(SearchNumbersFilter request) {
                return "/number/search";
            }

            @Override
            protected SearchNumbersFilter sampleRequest() {
                SearchNumbersFilter filter = new SearchNumbersFilter("BB");
                filter.setIndex(11);
                filter.setSize(25);
                filter.setPattern("234");
                filter.setFeatures(new String[]{"SMS", "VOICE"});
                filter.setSearchPattern(SearchPattern.STARTS_WITH);
                filter.setType(Type.LANDLINE_TOLL_FREE);
                return filter;
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                SearchNumbersFilter filter = sampleRequest();
                Map<String, String> params = new LinkedHashMap<>(8);
                params.put("country", filter.getCountry());
                params.put("features", String.join(",", filter.getFeatures()));
                params.put("pattern", filter.getPattern());
                params.put("search_pattern", String.valueOf(filter.getSearchPattern().getValue()));
                params.put("index", String.valueOf(filter.getIndex()));
                params.put("size", String.valueOf(filter.getSize()));
                params.put("type", filter.getType().getType());
                return params;
            }
        }
        .runTests();
    }

    @Test
    public void testBuyNumberEndpoint() throws Exception {
        new BuyCancelNumberRequestEndpointTestSpec() {

            @Override
            String endpointName() {
                return "buy";
            }

            @Override
            protected RestEndpoint<BuyCancelNumberRequest, Void> endpoint() {
                return client.buyNumber;
            }
        }
        .runTests();
    }

    @Test
    public void testCancelNumberEndpoint() throws Exception {
        new BuyCancelNumberRequestEndpointTestSpec() {

            @Override
            String endpointName() {
                return "cancel";
            }

            @Override
            protected RestEndpoint<BuyCancelNumberRequest, Void> endpoint() {
                return client.cancelNumber;
            }
        }
        .runTests();
    }

    @Test
    public void testUpdateNumberEndpoint() throws Exception {
        new BaseNumberRequestEndpointTestSpec<UpdateNumberRequest>() {

            @Override
            String endpointName() {
                return "update";
            }

            @Override
            protected RestEndpoint<UpdateNumberRequest, Void> endpoint() {
                return client.updateNumber;
            }

            @Override
            protected UpdateNumberRequest sampleRequest() {
                UpdateNumberRequest request = new UpdateNumberRequest("447700900013", "UK");
                request.setMoHttpUrl("https://api.example.com/mo");
                request.setMoSmppSysType("inbound");
                request.setVoiceCallbackValue("1234-5678-9123-4567");
                request.setVoiceCallbackType(UpdateNumberRequest.CallbackType.APP);
                request.setVoiceStatusCallback("https://api.example.com/callback");
                request.setMessagesCallbackValue("MESSAGES-APPLICATION-ID");
                return request;
            }

            @Override
            protected void populateSampleQueryParams(UpdateNumberRequest request) {
                super.populateSampleQueryParams(request);
                params.put("moHttpUrl", request.getMoHttpUrl());
                params.put("moSmppSysType", request.getMoSmppSysType());
                params.put("voiceCallbackValue", request.getVoiceCallbackValue());
                params.put("voiceCallbackType", request.getVoiceCallbackType().toString());
                params.put("voiceStatusCallback", request.getVoiceStatusCallback());
                params.put("messagesCallbackValue", request.getMessagesCallbackValue());
                params.put("messagesCallbackType", "app");
            }
        }
        .runTests();
    }
}
