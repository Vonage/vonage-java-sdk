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
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.numbers.UpdateNumberRequest.CallbackType;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

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
              "error-code": "200",
              "error-code-label": "success"
            }"""
        );
    }

    private void assertEqualsSampleListNumbers(Supplier<ListNumbersResponse> invocation) throws Exception {
        int count = 127;
        String country = "GB", msisdn = "447700900000", moHttpUrl = "https://example.com/mo",
                voiceCallbackValue = "sip:nexmo@example.com ";

        stubResponse(200, "{\n" +
                "  \"count\": 1,\n" +
                "  \"numbers\": [{\"features\": null},\n" +
                "    {\n" +
                "      \"country\": \""+country+"\",\n" +
                "      \"msisdn\": \""+msisdn+"\",\n" +
                "      \"moHttpUrl\": \""+moHttpUrl+"\",\n" +
                "      \"type\": \"mobile-lvn\",\n" +
                "      \"features\": [\n" +
                "        \"VOICE\",\n" +
                "        \"SMS\"\n" +
                "      ],\n" +
                "      \"messagesCallbackType\": \"app\",\n" +
                "      \"messagesCallbackValue\": \""+TestUtils.APPLICATION_ID_STR+"\"\n" +
                "      \"voiceCallbackType\": \"sip\",\n" +
                "      \"voiceCallbackValue\": \""+voiceCallbackValue+"\"\n" +
                "    },\n" +
                "    {\n" +
                "       \"type\": \"landline-toll-free\",\n" +
                "       \"features\": []\n" +
                "    }\n" +
                "  ]\n" +
                "}");

        var parsed = invocation.get();
        testJsonableBaseObject(parsed);
        assertEquals(count, parsed.getCount());
        var numbers = parsed.getNumbers();
        assertNotNull(numbers);
        assertEquals(3, numbers.length);
        assertNotNull(numbers[0]);
        assertNull(numbers[0].getFeatures());
        var main = numbers[1];
        testJsonableBaseObject(main);
        assertEquals(country, main.getCountry());
        assertEquals(msisdn, main.getMsisdn());
        // TODO change to URI
        assertEquals(URI.create(moHttpUrl).toString(), main.getMoHttpUrl());
        // TODO change to enum
        assertEquals(Type.MOBILE_LVN, Type.valueOf(main.getType()));
        var features = main.getFeatures();
        assertNotNull(features);
        assertEquals(2, features.length);
        // TODO change to enum
        assertEquals(Feature.VOICE, Feature.fromString(features[0]));
        assertEquals(Feature.SMS, Feature.fromString(features[1]));
        // TODO change to enum
        assertEquals(CallbackType.SIP, CallbackType.valueOf(main.getVoiceCallbackType()));
        assertEquals(voiceCallbackValue, main.getVoiceCallbackValue());
        assertEquals(TestUtils.APPLICATION_ID, main.getMessagesCallbackValue());
        var last = numbers[3];
        testJsonableBaseObject(last);
        // TODO change to enum
        assertEquals(Type.LANDLINE_TOLL_FREE, Type.fromString(last.getType()));
        assertNotNull(last.getFeatures());
        assertEquals(0, last.getFeatures().length);
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
        testJsonableBaseObject(response);
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
                "        \"MMS\"\n" +
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
                return ListNumbersFilter.builder()
                    .pattern(SearchPattern.STARTS_WITH, "*234")
                    .index(10).size(20).country("DE").build();
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                ListNumbersFilter request = sampleRequest();
                Map<String, String> params = new LinkedHashMap<>(8);
                params.put("pattern", request.getPattern());
                params.put("search_pattern", String.valueOf(request.getSearchPattern().getValue()));
                params.put("index", String.valueOf(request.getIndex()));
                params.put("size", String.valueOf(request.getSize()));
                params.put("country", request.getCountry());
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
                return SearchNumbersFilter.builder()
                        .country("BB").index(11).size(25)
                        .pattern(SearchPattern.ENDS_WITH, "789")
                        .features(Feature.SMS, Feature.VOICE)
                        .type(Type.LANDLINE_TOLL_FREE).build();
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
                params.put("type", filter.getType().name().toLowerCase().replace('_', '-'));
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
                return UpdateNumberRequest.builder("447700900013", "UK")
                        .applicationId(TestUtils.APPLICATION_ID_STR)
                        .voiceStatusCallback("https://api.example.com/callback")
                        .voiceCallback(CallbackType.TEL, "1234-5678-9123-4567")
                        .moHttpUrl("https://api.example.com/mo").moSmppSysType("inbound").build();
            }

            @Override
            protected void populateSampleQueryParams(UpdateNumberRequest request) {
                super.populateSampleQueryParams(request);
                params.put("country", request.getCountry());
                params.put("msisdn", request.getMsisdn());
                params.put("app_id", request.getApplicationId().toString());
                params.put("moHttpUrl", request.getMoHttpUrl());
                params.put("moSmppSysType", request.getMoSmppSysType());
                params.put("voiceCallbackValue", request.getVoiceCallbackValue());
                params.put("voiceCallbackType", request.getVoiceCallbackType().toString());
                params.put("voiceStatusCallback", request.getVoiceStatusCallback());
            }

            @Override
            public void runTests() throws Exception {
                super.runTests();
                testDeprecated();
            }

            private void testDeprecated() throws Exception {
                var sample = sampleRequest();
                var setterReq = new UpdateNumberRequest(sample.getMsisdn(), sample.getCountry());
                setterReq.setVoiceStatusCallback(sample.getVoiceStatusCallback());
                setterReq.setVoiceCallbackValue(sample.getVoiceCallbackValue());
                setterReq.setVoiceCallbackType(sample.getVoiceCallbackType());
                setterReq.setMoHttpUrl(sample.getMoHttpUrl());
                setterReq.setMoSmppSysType(sample.getMoSmppSysType());
                setterReq.setMessagesCallbackValue("not-an-Application-ID");

                params.remove("app_id");
                params.put("messagesCallbackValue", setterReq.getMessagesCallbackValue());
                params.put("messagesCallbackType", "app");

                assertRequestUriAndBody(setterReq, params);
            }
        }
        .runTests();
    }
}
