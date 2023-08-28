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

import com.vonage.client.ClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.common.HttpMethod;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.LinkedHashMap;
import java.util.Map;

public class NumbersClientTest extends ClientTest<NumbersClient> {

    public NumbersClientTest() {
        client = new NumbersClient(wrapper);
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
        assertEquals(1, response.getCount());
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

    }

    @Test
    public void testCancelNumber() throws Exception {
        stubResponse(200, "{\n" +
                "  \"error-code\":\"200\",\n" +
                "  \"error-code-label\":\"success\"\n" +
                "}");
        client.cancelNumber("AA", "447700900000");
    }

    @Test
    public void testBuyNumber() throws Exception {
        stubResponse(200, "{\n" +
                "  \"error-code\":\"200\",\n" +
                "  \"error-code-label\":\"success\"\n" +
                "}");
        client.buyNumber("AA", "447700900000");
    }

    @Test
    public void testUpdateNumber() throws Exception {
        stubResponse(200, "{\n" +
                "  \"error-code\":\"200\",\n" +
                "  \"error-code-label\":\"success\"\n" +
                "}");
        client.updateNumber(new UpdateNumberRequest("447700900328", "UK"));
    }

    @Test
    public void testLinkNumber() throws Exception {
        stubResponse(200, "{\n" +
                "  \"error-code\":\"200\",\n" +
                "  \"error-code-label\":\"success\"\n" +
                "}");
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
                Map<String, String> params = new LinkedHashMap<>(8);
                params.put("pattern", "234");
                params.put("search_pattern", "0");
                params.put("index", "10");
                params.put("size", "20");
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
                filter.setIndex(10);
                filter.setSize(20);
                filter.setPattern("234");
                filter.setFeatures(new String[]{"SMS", "VOICE"});
                filter.setSearchPattern(SearchPattern.STARTS_WITH);
                filter.setType(Type.LANDLINE_TOLL_FREE);
                return filter;
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                Map<String, String> params = new LinkedHashMap<>(8);
                params.put("country", "BB");
                params.put("features", "SMS,VOICE");
                params.put("pattern", "234");
                params.put("search_pattern", "0");
                params.put("index", "10");
                params.put("size", "20");
                params.put("type", "landline-toll-free");
                return params;
            }
        }
        .runTests();
    }

    @Test
    public void testBuyNumberEndpoint() throws Exception {
        new NumbersEndpointTestSpec<BuyNumberRequest, Void>() {

            @Override
            protected String expectedContentTypeHeader(BuyNumberRequest request) {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected RestEndpoint<BuyNumberRequest, Void> endpoint() {
                return client.buyNumber;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected String expectedEndpointUri(BuyNumberRequest request) {
                return "/number/buy";
            }

            @Override
            protected BuyNumberRequest sampleRequest() {
                return new BuyNumberRequest("DE", "4930901820");
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                Map<String, String> params = new LinkedHashMap<>(4);
                params.put("country", "DE");
                params.put("msisdn", "4930901820");
                return params;
            }
        }
        .runTests();
    }

    @Test
    public void testCancelNumberEndpoint() throws Exception {
        new NumbersEndpointTestSpec<CancelNumberRequest, Void>() {

            @Override
            protected String expectedContentTypeHeader(CancelNumberRequest request) {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected RestEndpoint<CancelNumberRequest, Void> endpoint() {
                return client.cancelNumber;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected String expectedEndpointUri(CancelNumberRequest request) {
                return "/number/cancel";
            }

            @Override
            protected CancelNumberRequest sampleRequest() {
                return new CancelNumberRequest("DE", "4930901820");
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                Map<String, String> params = new LinkedHashMap<>(4);
                params.put("country", "DE");
                params.put("msisdn", "4930901820");
                return params;
            }
        }
        .runTests();
    }

    @Test
    public void testUpdateNumberEndpoint() throws Exception {
        new NumbersEndpointTestSpec<UpdateNumberRequest, Void>() {

            @Override
            protected String expectedContentTypeHeader(UpdateNumberRequest request) {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected RestEndpoint<UpdateNumberRequest, Void> endpoint() {
                return client.updateNumber;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.POST;
            }

            @Override
            protected String expectedEndpointUri(UpdateNumberRequest request) {
                return "/number/update";
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
            protected Map<String, String> sampleQueryParams() {
                Map<String, String> params = new LinkedHashMap<>(4);
                params.put("country", "UK");
                params.put("msisdn", "447700900013");
                params.put("moHttpUrl", "https://api.example.com/mo");
                params.put("moSmppSysType", "inbound");
                params.put("voiceCallbackValue", "1234-5678-9123-4567");
                params.put("voiceCallbackType", "app");
                params.put("voiceStatusCallback", "https://api.example.com/callback");
                params.put("messagesCallbackValue", "MESSAGES-APPLICATION-ID");
                params.put("messagesCallbackType", "app");
                return params;
            }
        }
        .runTests();
    }
}
