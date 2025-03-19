/*
 *   Copyright 2025 Vonage
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
import com.vonage.client.Jsonable;
import com.vonage.client.RestEndpoint;
import static com.vonage.client.TestUtils.*;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class NumbersClientTest extends AbstractClientTest<NumbersClient> {
    private static final String MSISDN = "447700900000", COUNTRY = "GB";

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

    private void assertEqualsSampleListNumbers(Supplier<List<OwnedNumber>> invocation) throws Exception {
        final int count = 127;
        final UUID appId = UUID.randomUUID();
        String moHttpUrl = "https://example.com/mo", voiceCallbackValue = "sip:nexmo@example.com ",
                json = "{\n" +
                        "  \"count\": "+count+",\n" +
                        "  \"numbers\": [{\"features\": null},\n" +
                        "    {\n" +
                        "      \"country\": \""+COUNTRY+"\",\n" +
                        "      \"msisdn\": \""+MSISDN+"\",\n" +
                        "      \"moHttpUrl\": \""+moHttpUrl+"\",\n" +
                        "      \"type\": \"mobile-lvn\",\n" +
                        "      \"features\": [\n" +
                        "        \"SMS\",\n" +
                        "        \"MMS\"\n" +
                        "      ],\n" +
                        "      \"messagesCallbackType\": \"app\",\n" +
                        "      \"messagesCallbackValue\": \""+ APPLICATION_ID_STR+"\",\n" +
                        "      \"app_id\": \""+appId+"\",\n" +
                        "      \"voiceCallbackType\": \"sip\",\n" +
                        "      \"voiceCallbackValue\": \""+voiceCallbackValue+"\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "       \"type\": \"landline-toll-free\",\n" +
                        "       \"features\": []\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";

        stubResponse(200, json);

        var numbers = invocation.get();
        assertNotNull(numbers);
        assertEquals(3, numbers.size());
        assertNotNull(numbers.getFirst());
        assertNull(numbers.getFirst().getFeatures());
        var main = numbers.get(1);
        testJsonableBaseObject(main);
        assertEquals(COUNTRY, main.getCountry());
        assertEquals(MSISDN, main.getMsisdn());
        assertEquals(URI.create(moHttpUrl).toString(), main.getMoHttpUrl());
        assertEquals(Type.MOBILE_LVN, main.getType());
        var features = main.getFeatures();
        assertNotNull(features);
        assertEquals(2, features.length);
        assertEquals(Feature.SMS, features[0]);
        assertEquals(Feature.MMS, features[1]);
        assertEquals(CallbackType.SIP, main.getVoiceCallbackType());
        assertEquals(voiceCallbackValue, main.getVoiceCallbackValue());
        assertEquals(APPLICATION_ID, main.getMessagesCallbackValue());
        assertEquals(appId, main.getAppId());
        var last = numbers.get(2);
        testJsonableBaseObject(last);
        assertEquals(Type.LANDLINE_TOLL_FREE, last.getType());
        assertNotNull(last.getFeatures());
        assertEquals(0, last.getFeatures().length);
    }

    private void assertEqualsSampleSearchNumbers(Supplier<SearchNumbersResponse> invocation) throws Exception {
        int count = 1234;
        String json = "{\n" +
                "  \"count\": "+count+",\n" +
                "  \"numbers\": [{},{},\n" +
                "    {\n" +
                "      \"country\": \""+COUNTRY+"\",\n" +
                "      \"msisdn\": \""+MSISDN+"\",\n" +
                "      \"cost\": \"0.80\",\n" +
                "      \"type\": \"landline\",\n" +
                "      \"features\": [\n" +
                "        \"VOICE\"\n" +
                "      ]\n" +
                "    },{},{}\n" +
                "  ]\n" +
                "}";
        stubResponse(200, json);

        var parsed = invocation.get();
        testJsonableBaseObject(parsed);
        assertEquals(parsed, Jsonable.fromJson(json, SearchNumbersResponse.class));

        assertEquals(1234, parsed.getCount());
        var numbers = parsed.getNumbers();
        assertNotNull(numbers);
        assertEquals(5, numbers.length);
        var main = numbers[2];
        testJsonableBaseObject(main);
        assertEquals(COUNTRY, main.getCountry());
        assertEquals(MSISDN, main.getMsisdn());
        assertEquals("0.80", main.getCost());
        assertEquals(Type.LANDLINE, main.getType());
        var features = main.getFeatures();
        assertNotNull(features);
        assertEquals(1, features.length);
        assertEquals(Feature.VOICE, features[0]);
    }

    @Test
    public void testListNumbers() throws Exception {
        assertEqualsSampleListNumbers(client::listNumbers);
        assert401ResponseException(client::listNumbers);

        var filter = ListNumbersFilter.builder()
                .pattern(SearchPattern.ENDS_WITH, "2345")
                .applicationId(APPLICATION_ID_STR)
                .hasApplication(false).country("us")
                .index(9).size(24).build();

        assertEquals("US", filter.getCountry());
        assertEquals("ENDS_WITH", filter.getSearchPattern().toString());
        assertEquals("2345", filter.getPattern());
        assertEquals(APPLICATION_ID, filter.getApplicationId());
        assertEquals(false, filter.getHasApplication());
        assertEquals(9, filter.getIndex());
        assertEquals(24, filter.getSize());

        assertEqualsSampleListNumbers(() -> client.listNumbers(filter));
        assert401ResponseException(() -> client.listNumbers(filter));

        stubResponse("{}");
        assertNull(client.listNumbers());
    }

    @Test
    public void testSearchNumbers() throws Exception {
        var filter = SearchNumbersFilter.builder().build();
        assertEqualsSampleSearchNumbers(() -> client.searchNumbers("in"));
        assertEqualsSampleSearchNumbers(() -> client.searchNumbers(filter));
        assert401ResponseException(() -> client.searchNumbers(filter));

        stubResponse("{}");
        var response = client.searchNumbers(filter);
        assertNotNull(response);
        assertEquals(0, response.getCount());
        var numbers = response.getNumbers();
        assertNotNull(numbers);
        assertEquals(0, numbers.length);
    }

    @Test
    public void testCancelNumber() throws Exception {
        stubBaseSuccessResponse();
        client.cancelNumber(COUNTRY, MSISDN);
        assert401ResponseException(() -> client.cancelNumber(COUNTRY, MSISDN));
    }

    @Test
    public void testBuyNumber() throws Exception {
        stubBaseSuccessResponse();
        client.buyNumber(COUNTRY, MSISDN);
        assert401ResponseException(() -> client.buyNumber(COUNTRY, MSISDN));
    }

    @Test
    public void testUpdateNumber() throws Exception {
        stubBaseSuccessResponse();
        UpdateNumberRequest request = UpdateNumberRequest.builder("447700900328", "us").build();
        assertNotNull(request.getCountry());
        assertNotNull(request.getMsisdn());
        assertNull(request.getMoSmppSysType());
        assertNull(request.getApplicationId());
        assertNull(request.getMoHttpUrl());
        assertNull(request.getVoiceCallbackType());
        assertNull(request.getVoiceCallbackValue());
        assertNull(request.getVoiceStatusCallback());
        client.updateNumber(request);
        assert401ResponseException(() -> client.updateNumber(request));
    }

    @Test
    public void testLinkNumber() throws Exception {
        stubBaseSuccessResponse();
        client.linkNumber("447700900328", "UK", APPLICATION_ID_STR);
        stubBaseSuccessResponse();
        assertThrows(IllegalArgumentException.class, () ->
                client.linkNumber(MSISDN, COUNTRY, "not-an-application_ID")
        );
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
                        .applicationId(APPLICATION_ID).hasApplication(true)
                        .pattern(SearchPattern.STARTS_WITH, "*1337*")
                        .index(10).size(20).country("DE").build();
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                ListNumbersFilter request = sampleRequest();
                Map<String, String> params = new LinkedHashMap<>();
                params.put("pattern", request.getPattern());
                params.put("search_pattern", String.valueOf(request.getSearchPattern().getValue()));
                params.put("index", String.valueOf(request.getIndex()));
                params.put("size", String.valueOf(request.getSize()));
                params.put("country", request.getCountry());
                params.put("application_id", request.getApplicationId().toString());
                params.put("has_application", request.getHasApplication().toString());
                return params;
            }

            @Override
            public void runTests() throws Exception {
                super.runTests();
                testValidation();
                testBlank();
            }

            private void testValidation() {
                assertThrows(IllegalArgumentException.class, () -> ListNumbersFilter.builder().country("ABC").build());
                assertEquals(1, ListNumbersFilter.builder().index(1).build().getIndex());
                assertThrows(IllegalArgumentException.class, () -> ListNumbersFilter.builder().index(0).build());
                assertEquals(100, ListNumbersFilter.builder().size(100).build().getSize());
                assertThrows(IllegalArgumentException.class, () -> ListNumbersFilter.builder().size(101).build());
                assertThrows(IllegalArgumentException.class, () -> ListNumbersFilter.builder().size(0).build());
            }

            private void testBlank() throws Exception {
                var blank = ListNumbersFilter.builder().build();
                assertNull(blank.getHasApplication());
                assertNull(blank.getApplicationId());
                assertNull(blank.getSearchPattern());
                assertNull(blank.getPattern());
                assertNull(blank.getIndex());
                assertNull(blank.getSize());
                assertNull(blank.getCountry());
                assertRequestUriAndBody(blank, Map.of());
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
                Map<String, String> params = new LinkedHashMap<>();
                params.put("country", filter.getCountry());
                params.put("features", String.join(",", Feature.getToString(filter.getFeatures())));
                params.put("pattern", filter.getPattern());
                params.put("search_pattern", String.valueOf(filter.getSearchPattern().getValue()));
                params.put("index", String.valueOf(filter.getIndex()));
                params.put("size", String.valueOf(filter.getSize()));
                params.put("type", filter.getType().name().toLowerCase().replace('_', '-'));
                return params;
            }

            @Override
            public void runTests() throws Exception {
                super.runTests();
                testEmptyFeatures();
            }

            private void testEmptyFeatures() throws Exception {
                var filter = SearchNumbersFilter.builder().features().build();
                assertEquals(0, filter.getFeatures().length);
                assertRequestUriAndBody(filter, Map.of());
                filter = SearchNumbersFilter.builder().features((Feature[]) null).build();
                assertNull(filter.getFeatures());
                assertNull(Feature.getToString(filter.getFeatures()));
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
                        .applicationId(APPLICATION_ID_STR)
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
        }
        .runTests();
    }
}
